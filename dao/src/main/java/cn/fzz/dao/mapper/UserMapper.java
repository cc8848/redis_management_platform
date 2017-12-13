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

    @Insert("INSERT INTO user(id, name) VALUES(#{id}, #{name})")
    int addUser(UserBean user);
}
