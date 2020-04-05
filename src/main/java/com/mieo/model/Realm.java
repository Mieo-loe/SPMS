package com.mieo.model;

import com.mieo.mapper.MemberMapper;
import com.mieo.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class Realm extends AuthorizingRealm implements Serializable {
    @Autowired
    MemberService memberService;

    /**
     * 授权过滤器
     * 执行授权逻辑
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.debug("执行授权逻辑");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        //获取当前登录用户
        Member member = (Member) subject.getPrincipal();
        //添加资源认证
        if (member.getMemberRole().equals("1")) {
            info.addStringPermission("member:add");
            info.addStringPermission("member:delete");
            info.addStringPermission("task:add");
            info.addStringPermission("task:delete");
            info.addStringPermission("task:edit");
            info.addStringPermission("team:add");
            info.addStringPermission("team:delete");
            info.addStringPermission("project:add");
            info.addStringPermission("project:delete");
            info.addStringPermission("member:edit");
            info.addStringPermission("setting:edit");
        }
        info.addStringPermission(member.getMemberRole());
        //页面信息的初始化
        return info;
    }

    /**
     * 执行认证逻辑
     *
     * @param authenticationToken 包含用户名密码，主机，remember的包装类
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.debug("执行认证逻辑");
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        List<Member> members = memberService.queryAllMemberInfo();
        for (Member member : members) {
            //判断用户账号
            if (member.getMemberAccount().equals(usernamePasswordToken.getUsername())) {
                //用户名存在
                log.debug("用户名存在");
                //判断用户密码
                String memberSalt = member.getMemberSalt();
                ByteSource salt = ByteSource.Util.bytes(member.getMemberSalt());
                return new SimpleAuthenticationInfo(member, member.getMemberPassword(), salt, this.getName());
            }
        }
        //用户名不存在，默认为这个异常，直接return null;
        log.debug("用户名不存在");
        return null;
    }
}
