package cn.fzz.web;

import cn.fzz.bean.UserBean;
import cn.fzz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fanzezhen on 2017/12/13.
 * Desc:
 */
@RestController
public class TestUserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/user/addUser", method = RequestMethod.GET)
    public @ResponseBody int saveUser(HttpServletRequest request,
                                      @RequestParam(value = "id", required = false) String id,
                                      @RequestParam(value = "name", required = false) String name){
        UserBean personBean = new UserBean(Integer.parseInt(id), name);

        return userService.saveUser(personBean);
    }
}
