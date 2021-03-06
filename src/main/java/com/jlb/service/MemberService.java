package com.jlb.service;

import com.jlb.model.Member;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MemberService {

    /**
     * 添加成员信息
     *
     * @param member 需要添加的成员信息
     * @return 状态码
     */
    void addMember(Member member);

    /**
     * 通过成员id删除信息
     *
     * @param id 成员id
     * @return 状态码
     */
    int deleteMemberById(Integer id);

    /**
     * 通过成员id批量删除成员信息
     *
     * @param ids 成员id链表
     * @return 状态码
     */
    Map<String,String> deleteMemberByIds(List<Integer> ids);


    /**
     * 修改对应id的成员信息
     *
     * @param member 成员信息
     */
    void updateMemberById(Member member);

    /**
     * 修改成员的账号密码
     * @param member 成员信息
     */
    void updateMemberPasswordByPhone(Member member);

    /**
     * 通过电话号码查询成员信息
     * @param phone
     * @return
     */
    Member queryMemberByPhone(String phone);

    /**
     * 查询所有的成员信息
     *
     * @return 成员信息
     */
    List<Member> queryAllMemberInfo();

    /**
     * 查询用户密码
     *
     * @return 用户密码
     */
    String queryMemberPassword(String memberAccount);

    /**
     * 通过成员账号查询成员信息
     *
     * @param memberAccount 成员账号
     * @return 成员信息
     */
    Member queryMemberInfoByAccount(String memberAccount);

    /**
     * 通过用户id查找用户信息
     *
     * @param id 被查找的用户id
     * @return 用户信息
     */
    Member queryMemberById(Integer id);

    /**
     * 通过用户id查询用户名称
     * @return
     */
     String queryMemberNameByMemberId(Integer id);

    /**
     * 查询团队下的成员是否为空
     * @param teamId
     * @return
     */
    @Select("SELECT count(*) FROM  member where member_team_id=#{teamId}")
    int queryMemberCountByTeamId(int teamId);

    /**
     * 查询所有的成员数量
     * @return
     */
    int queryMemberCountAll();

    /**
     * 查询所有的成员手机号
     * @return
     */
    List<String> queryAllMemberPhone();

    /**
     * 查询所有的成员账号
     * @return
     */
    List<String> queryAllMemberAccount();

    /**
     * 查询一个团队下的所有成员
     *
     * @param teamId 团队id
     * @return 团队下所有成员
     */
    List<Member> queryMemberByTeamId(int teamId);
}
