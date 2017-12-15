package cn.fzz.web.controller;

import cn.fzz.bean.UserBean;
import cn.fzz.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by fanzezhen on 2017/12/13.
 * Desc:
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    public @ResponseBody
    int saveUser(HttpServletRequest request,
                 @RequestParam(value = "id", required = true) String id,
                 @RequestParam(value = "name", required = true) String name) {
        UserBean userBean = new UserBean();

        return userService.saveUser(userBean);
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome(ModelMap map) {
        // 加入一个属性，用来在模板中读取
        map.addAttribute("message", "http://blog.didispace.com");
        // return模板文件的名称，对应src/main/resources/templates/index.html
        return "welcome";
    }

    @RequestMapping(value = "/register/")
    public String register(@Valid String email,
                           @Valid String password,
                           @Valid String username,
                           ModelMap modelMap) {

        if (!(StringUtils.isEmpty(email) || StringUtils.isEmpty(password))) {
            UserBean userBean = new UserBean(username, password, email);
            if (userService.saveUser(userBean) == 0) {
                modelMap.addAttribute("email", email);
                modelMap.addAttribute("password", password);
                modelMap.addAttribute("username", username);
            }
        }
        return "register";
    }

    @RequestMapping(value = "/login")
    public String login(@Valid String email,
                        @Valid String password,
                        ModelMap modelMap) {
        if (!(StringUtils.isEmpty(email) || StringUtils.isEmpty(password))) {
            modelMap.addAttribute("email", email);
            modelMap.addAttribute("password", password);
            if (password.equals(userService.getUserByEmail(email).getPassword())){
                modelMap.addAttribute("email", "successful");
                modelMap.addAttribute("password", "successful");
            }
        }
        return "login";
    }
}
