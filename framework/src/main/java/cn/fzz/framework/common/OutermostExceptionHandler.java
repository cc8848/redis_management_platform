package cn.fzz.framework.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class OutermostExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView error(Exception e){
        ModelAndView modelAndView = new ModelAndView();

        // 封装异常信息，用于页面的渲染
        modelAndView.addObject("exception", e.getClass().getName());
        // 转发到error.jsp 页面，进行错误提示
        modelAndView.setViewName("/exception/500");

        return modelAndView;
    }

}
