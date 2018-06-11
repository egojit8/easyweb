package com.egojit.easyweb.upms.sso.controller;

import com.egojit.easyweb.common.Global;
import com.egojit.easyweb.common.base.BaseApiController;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.utils.CookieUtils;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upm.service.SysUserService;
import com.egojit.easyweb.upms.sso.UserUtils;
import com.egojit.easyweb.upms.sso.security.UsernamePasswordToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by egojit on 2017/10/25.
 */
@Controller
@RequestMapping("/oss")
@Api(value = "登录管理", description = "登录管理")
public class OssController extends BaseApiController {


    @Autowired
    SysUserService userService;
    /**
     * 登录
     * @return
     */
    @GetMapping("/login")
    @ApiOperation(value = "登录界面")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String principal = UserUtils.getPrincipal();

//		// 默认页签模式
//		String tabmode = CookieUtils.getCookie(request, "tabmode");
//		if (tabmode == null){
//			CookieUtils.setCookie(response, "tabmode", "1");
//		}
//
//        if (logger.isDebugEnabled()){
//            logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
//        }

        // 如果已登录，再次访问主页，则退出原账号。
        if (Global.TRUE.equals(Global.getInstance().getConfig("notAllowRefreshIndex"))){
            CookieUtils.setCookie(response, "LOGINED", "false");
        }

        // 如果已经登录，则跳转到管理首页
        if(StringUtils.isNotEmpty(principal)){
            return "redirect:/admin/index";
        }
        return "login";
    }

    /**
     * 登录接口
     * @param username
     * @param password
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    @ApiOperation(value = "登录接口")
    public BaseResult login(@RequestParam(required = true) String username,
                            @RequestParam(required = true) String password) {
        Subject subject = SecurityUtils.getSubject();

        try {
            // 登录，即身份验证
            UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(),false,null,"");
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
//            SysUser user = userService.getByLoginName(token.getUsername());
////            // 在session中存放用户信息
//            subject.getSession().setAttribute("userLogin", user);
        }
        catch (UnknownAccountException e) {
            return new BaseResult(BaseResultCode.UNAUTH, "账号不存在");
        }
        catch (IncorrectCredentialsException e) {
            return new BaseResult(BaseResultCode.UNAUTH, "用户名或者密码错误");
//            throw new JsonException("用户名或密码错误", 405);
        } catch (LockedAccountException e) {
            return new BaseResult(BaseResultCode.UNAUTH, "登录失败，该用户已被冻结");
//            throw new JsonException("登录失败，该用户已被冻结", 405);
        } catch (AuthenticationException e) {
            return new BaseResult(BaseResultCode.UNAUTH, "登录失败，该用户已被冻结");
//            throw new JsonException("用户名或密码错误", 405);
        }
        return new BaseResult(BaseResultCode.SUCCESS, "/admin/index");

    }
}
