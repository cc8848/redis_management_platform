package cn.fzz.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by fanzezhen on 2017/12/15.
 * Desc:
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @RequestMapping("/login")
    public String login(@PathVariable String password,
                        ModelMap modelMap){
//        map.addAttribute("message", "http://blog.didispace.com");
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid String email,
                           @Valid String password,
                           ModelMap modelMap){
        modelMap.addAttribute("email", email);
        modelMap.addAttribute("password", password);
        return "register";
    }
}
