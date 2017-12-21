package cn.fzz.common;

import cn.fzz.bean.common.AttributeBean;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by fanzezhen on 2017/12/19.
 * Desc:
 */
public class Common {
    public static Field[] getAttributeFields(Object bean){
        return bean.getClass().getDeclaredFields();
    }

    public static AttributeBean getAttribute(Field field, Object bean){
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

    public static Process createProcess(String exePath, String confPath){
        ArrayList<String> commands = new ArrayList<>();
        commands.add(exePath);
        commands.add(confPath);
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(commands);
        Process pr = null;
        try {
            pr = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pr;
    }
}
