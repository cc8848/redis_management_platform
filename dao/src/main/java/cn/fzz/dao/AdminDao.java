package cn.fzz.dao;
/**
 * Project Name:dao
 * File Name:AdminDao.java
 * Package Name:cn.fzz.dao
 * Date:2017年12月10日下午2:36:08
 * Copyright (c) 2017, fanzezhen@outlook.com All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.fzz.dao.mapper.UserMapper;
import cn.fzz.bean.UserBean;

import java.util.List;


/**
 * ClassName:DemoDao <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017年12月10日 下午2:36:08 <br/>
 * @author zhen
 * @version
 * @since JDK 1.8
 * @see
 */
public class AdminDao {
    private static final Logger logger = LoggerFactory.getLogger(AdminDao.class);

    //注入mapper
    @Autowired
    private UserMapper userMapper;

    /**
     * insert:(这里用一句话描述这个方法的作用). <br/>插入数据
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     *
     * @author zhen
     * @since JDK 1.8
     */
    public int saveUser(UserBean userBean) {
        return userMapper.addUser(userBean);
    }
    public List<UserBean> getAll(){
        return userMapper.getAll();
    }
    public UserBean getUserByEmail(String email){
        return userMapper.getUserByEmail(email);
    }
}

