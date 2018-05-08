package cn.fzz.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by fanzezhen on 2017/12/19.
 * Desc:
 */
@org.springframework.stereotype.Controller
public class Controller {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/404")
    public String page404(){
        return "exception/404";
    }
}
