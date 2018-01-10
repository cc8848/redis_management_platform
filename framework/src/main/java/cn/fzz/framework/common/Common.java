package cn.fzz.framework.common;

import cn.fzz.bean.common.AttributeBean;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanzezhen on 2017/12/19.
 * Desc:
 */
public class Common {
    //记录当前已使用的端口号
    private static List<Integer> ports = new ArrayList<>();
    private static int port = 6380;

    /**
     * 校验端口号是否可用
     *
     * @param object
     * @return
     */
    public static Integer checkPort(Object object) {
        if (StringUtils.isEmpty(object)) {  //如果未传入端口号， 则自动生成
            List<String> readPort;
            do {
                while (ports.indexOf(port) >= 0) {
                    port++;
                }
                readPort = netstat_anoByPort(port);
                ports.add(port);
            }while (readPort != null && readPort.size() != 0);
        } else {
            List<String> readPort = netstat_anoByPort(port);
            if (ports.indexOf(new Integer(object.toString())) >= 0 || readPort!= null && readPort.size() != 0) {    //如果用户传入的端口号已被占用
                return null;
            }
            ports.add(port);
        }

        return port;
    }

    /**
     * @param bean
     * @return
     */
    public static Field[] getAttributeFields(Object bean) {
        return bean.getClass().getDeclaredFields();
    }

    /**
     * @param field
     * @param bean
     * @return
     */
    public static AttributeBean getAttribute(Field field, Object bean) {
        String name = field.getName();
        String type = field.getGenericType().toString();                //获取属性的类型

        Object value = null;
        try {
            //将属性的首字符大写，构造get方法
            Method m = bean.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
            value = m.invoke(bean);     //调用getter方法获取属性值
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new AttributeBean(type, name, value);
    }
/*
    public static void setAttribute(Field field, Object bean, Object value) {
        String name = field.getName();
        String type = field.getGenericType().toString();                //获取属性的类型

        try {
            //将属性的首字符大写，构造get方法
            Method m = bean.getClass().getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1));
            if (type.equals("class [[Ljava.lang.String;")) {  //如果是字符串二维数组
                m.invoke(bean, String.valueOf(value));
            }
            Object invoke = m.invoke(bean, value);//调用setter方法
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    /**
     * 根据软件路径和配置文件路径启动进程
     *
     * @param exePath
     * @param confPath
     * @return
     */
    public static Process createProcess(String exePath, String confPath) {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(exePath);
        commands.add(confPath);
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(commands);
        try {
            return pb.start();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据端口号杀死进程
     *
     * @param port
     */
    public static void killProcessByPort(int port) {
        //查找进程号
        List<String> read = netstat_anoByPort(port);
        if ((read == null || read.size() == 0)) {
            System.out.println("找不到该端口的进程");
        } else {
            for (String string : read) {
                System.out.println(string);
            }
            System.out.println("找到" + read.size() + "个进程，正在准备清理");
            killProcess(read);
        }
    }

    public static List<String> netstat_anoByPort(int port) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("cmd /c netstat -ano | findstr \"" + port + "\"");
            InputStream inputStream = process.getInputStream();
            return read(inputStream, port);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据端口号批量杀死进程
     *
     * @param ports
     */
    public void killProcessByPorts(Set<Integer> ports) {
        for (int port : ports) {
            killProcessByPort(port);
        }
    }

    /**
     * 根据端口号杀死进程--
     *
     * @param in
     * @param port
     * @return
     * @throws IOException
     */
    private static List<String> read(InputStream in, int port) throws IOException {
        List<String> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            boolean validPort = validPort(line, port);
            if (validPort) {
                data.add(line);
            }
        }
        reader.close();
        return data;
    }

    public static String readTxt(InputStream in, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    /**
     * 验证此行是否为指定的端口，因为 findstr命令会是把包含的找出来，例如查找80端口，但是会把8099查找出来
     *
     * @param str
     * @param port
     * @return
     */
    private static boolean validPort(String str, int port) {
        Pattern pattern = Pattern.compile("^ *[a-zA-Z]+ +\\S+");
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            String find = matcher.group();
            int spStart = find.lastIndexOf(":");
            find = find.substring(spStart + 1);

            int linePort;
            try {
                linePort = Integer.parseInt(find);
            } catch (NumberFormatException e) {
                System.out.println("查找到错误的端口:" + find);
                e.printStackTrace();
                return false;
            }
            return linePort == port;
        } else {
            System.out.println("matcher.find()失败！");
            new Exception("matcher.find()失败！").printStackTrace();
            return false;
        }
    }

    /**
     * 根据端口号杀死进程--更换为一个Set，去掉重复的pid值
     *
     * @param data
     */
    public static void killProcess(List<String> data) {
        Set<Integer> pidSet = new HashSet<>();
        for (String line : data) {
            int offset = line.lastIndexOf(" ");
            String pidStr = line.substring(offset);
            pidStr = pidStr.replaceAll(" ", "");
            int pid;
            try {
                pid = Integer.parseInt(pidStr);
                pidSet.add(pid);
            } catch (NumberFormatException e) {
                System.out.println("获取的进程号错误:" + pidStr);
            }
        }
        killWithPid(pidSet);
    }

    /**
     * 批量杀死进程
     *
     * @param pidSet
     */
    public static void killWithPid(Set<Integer> pidSet) {
        for (Integer pid : pidSet) {
            try {
                Process process = Runtime.getRuntime().exec("taskkill /F /pid " + pid + "");
                InputStream inputStream = process.getInputStream();
                String txt = readTxt(inputStream, "GBK");
                System.out.println(txt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
