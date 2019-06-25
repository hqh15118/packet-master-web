package com.zjucsc.application.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 10:49
 */
@Component
public class GlobalExceptionHandler implements HandlerExceptionResolver {

    @Override
    @ResponseBody
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(ex.getMessage());
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }

}
