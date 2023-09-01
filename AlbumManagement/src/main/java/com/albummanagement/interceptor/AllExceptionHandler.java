package com.albummanagement.interceptor;

import com.albummanagement.view.ErrorCodeENUM;
import com.albummanagement.view.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 *
 * @Author:gj
 * @DateTime:2023/4/13 21:17
 **/
//该注解就是个AOP代理，对所有加了@Controller的类进行代理
@ControllerAdvice
public class AllExceptionHandler {
    //进行异常处理，处理Exception异常(所有的异常)
    @ExceptionHandler(Exception.class)
    //返回json数据，不加该注解则返回页面
    @ResponseBody
    public Result doException(Exception ex) {
        String exception = ex.getMessage();
        //暂时不处理，这里打印一下堆栈表示处理
        if (exception.equals("NOT_LOGIN")) {
            return Result.fail(ErrorCodeENUM.NOT_LOGIN.getCode(), ErrorCodeENUM.NOT_LOGIN.getMsg());
        } else if (exception.equals("LOGIN_EXPIRED")) {
            return Result.fail(ErrorCodeENUM.LOGIN_EXPIRED.getCode(), ErrorCodeENUM.LOGIN_EXPIRED.getMsg());
        }
        ex.printStackTrace();
        return Result.fail(2000, "未处理错误");
    }
}
