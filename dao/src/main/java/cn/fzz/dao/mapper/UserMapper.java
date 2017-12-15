package cn.fzz.dao.mapper;

import cn.fzz.bean.UserBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fanzezhen on 2017/12/13.
 * Desc:
 */
@Mapper
@Component
public interface UserMapper {
//    public int saveUser (UserBean paramter) ;
    @Select("SELECT * FROM user")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "name", column = "name")
    })
    List<UserBean> getAll();

    @Insert("INSERT INTO user(id, username, password, email, date_joined, date_update) VALUES(#{id}, #{username}, " +
            "#{password}, #{email}, #{dateJoined}, #{dateUpdate})")
    int addUser(UserBean user);

    @Select("SELECT * FROM USER WHERE EMAIL = #{EMAIL}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "email", column = "email"),
            @Result(property = "dateJoined", column = "date_joined"),
            @Result(property = "dateUpdate", column = "date_update")
    })
    UserBean getUserByEmail(String email);
}
