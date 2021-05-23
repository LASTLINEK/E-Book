package edu.bjtu.sei.simplecrud.config;

import com.alibaba.fastjson.JSON;
import edu.bjtu.sei.simplecrud.domain.User;
import edu.bjtu.sei.simplecrud.service.UserService;
import edu.bjtu.sei.simplecrud.util.JsonResult;
import edu.bjtu.sei.simplecrud.util.ResultTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @Author: Hutengfei
 * @Description: 登录成功处理逻辑
 * @Date Create in 2019/9/3 15:52
 */
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserService sysUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException, IOException {
        //更新用户表上次登录时间、更新人、更新时间等字段

        //此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，
        //进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展

        //返回json数据
        JsonResult result = ResultTool.success();
        //处理编码方式，防止中文乱码的情况
        httpServletResponse.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}