/**
 *
 */
package com.jlb.controller;

import com.jlb.common.util.MessageUtil;
import com.jlb.model.Member;
import com.jlb.model.util.TypeToken;
import com.jlb.service.MemberService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@Slf4j
@RequestMapping("login")
public class LoginController {
    @Autowired
    MemberService memberService;
    @Autowired
    Member member;
    @Autowired
    MessageUtil messageUtil;

    @RequestMapping("verifyAccount")
    @ResponseBody
    public Map<String, String> verifyAccount(@RequestBody Map<String, String> map1) {
        String username = MapUtils.getString(map1, "username");
        String password = MapUtils.getString(map1, "password");
        Map<String, String> map = new HashMap<>();
        map.put("temp", "1");
        //1.获取Shiro的subject
        Subject subject = SecurityUtils.getSubject();
        //2.封装用户数据
        TypeToken   typeToken=  new TypeToken(username, password);
        //3.执行登录校验
        try {
            //使用realm中的认证逻辑进行认证
//            subject.login(token);
            subject.login(typeToken);
            //认证成功后的操作
            map.put("msg", "登录成功，正在跳转页面");
            //页面信息初始化
            webInit(username);
        } catch (UnknownAccountException e) {
            //用户名不存在
            log.debug("用户名不存在");
            map.put("temp", "0");
            map.put("msg", "账号或密码错误，请重新输入");
        } catch (IncorrectCredentialsException e) {
            //密码错误
            log.debug("用户名存在但密码错误");
            map.put("temp", "0");
            map.put("msg", "账号或密码错误，请重新输入");
        }
        return map;
    }


    /**
     * 页面的准备
     */
    private void webInit(String username) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        session.setTimeout(10000000);
        System.out.println(session.getClass());
        session.setAttribute("user", memberService.queryMemberInfoByAccount(username));
    }

    @RequestMapping("toLogin")
    public String toLogin() {
        log.debug("跳转到登录页面,移除登录信息");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }

    @RequestMapping("toLoginPhone")
    public String toLoginPhone() {
        log.debug("跳转到短信验证登录页面");
        return "login_phone";
    }
    @RequestMapping("phoneLogin")
    @ResponseBody
    public Map<String,String> phoneLogin(@RequestBody Map<String, String> map) {
        String phone=map.get("phone");
        Map<String,String> msg=new HashMap<>();
        msg.put("msg", "正在请求发送验证码...");
        msg.put("temp", "success");
        log.debug("短信登录");
        if(memberService.queryMemberByPhone(phone)==null){
            msg.put("msg", "电话号码不存在");
            msg.put("temp", "danger");
            return msg;
        }
        return msg;
    }


    @RequestMapping("getVerifyCode")
    @ResponseBody
    public Map<String,String> getVerifyCode(String phoneNumber,String type){
        Map<String,String> map=new HashMap<>();
        String verifyCode = String.format("%04d", new Random().nextInt(9999));
        String msg = messageUtil.messageSend(verifyCode, phoneNumber, type);
        map.put("verifyCode",verifyCode);
       map.put("msg", msg);
        return map;
    }

    @RequestMapping("toPhoneIndex")
    public String toPhoneIndex(String phone) {
        Member member = memberService.queryMemberByPhone(phone);
        //1.获取Shiro的subject
        Subject subject = SecurityUtils.getSubject();
        TypeToken typeToken=new TypeToken(member.getMemberAccount());
        //2.封装用户数据
        //3.执行登录校验
        try {
            //使用realm中的认证逻辑进行认证
//            subject.login(token);
            subject.login(typeToken);
            //认证成功后的操作
            //页面信息初始化
            webInit(member.getMemberAccount());
        } catch (UnknownAccountException e) {
            //用户名不存在
        } catch (IncorrectCredentialsException e) {
            //密码错误
        }
        return "index";
    }

}
