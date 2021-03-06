package com.jlb.mapper;

import com.jlb.model.DynamicState;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
public interface DynamicStateMapper {

    /**
     * 添加动态信息
     *
     * @param dynamicState 动态信息
     */
    @Insert("insert into dynamic_state (dynamic_state_action, dynamic_state_content, dynamic_state_create_time, " +
            "                           dynamic_state_create_id, dynamic_state_type, dynamic_state_type_id) " +
            "VALUES (#{dynamicStateAction},#{dynamicStateContent},#{dynamicStateCreateTime},#{dynamicStateCreateId},#{dynamicStateType},#{dynamicStateTypeId})")
    void addDynamicState(DynamicState dynamicState);

    /**
     * 删除动态信息
     *
     * @param type   动态类型
     * @param typeId 动态类型对应id
     */
    @Delete("delete from dynamic_state where dynamic_state_type=#{type} and dynamic_state_type_id=#{typeId}")
    void deleteDynamicStateByTypeAndTypeId(int type, int typeId);

    /**
     * 删除动态信息
     * @param dynamicState
     */
    @Delete("delete from dynamic_state where dynamic_state_type=#{dynamicStateType} and dynamic_state_type_id=#{dynamicStateTypeId}")
    void deleteDynamicStateByDynamicState(DynamicState dynamicState);

    /**
     * 删除该成员下的所有动态信息
     * @param memberId
     */
    @Delete("delete from dynamic_state where dynamic_state_create_id=#{memberId}")
    void deleteDynamicStateByMemberId(int memberId);

    /**
     * 通过用户的id查询用户的信息
     *
     * @param id 用户的id
     * @return 用户的动态
     */
    @Select("SELECT dynamic_state.* FROM  dynamic_state LEFT JOIN member   ON member_id=dynamic_state_create_id WHERE member_id=#{id}")
    @ResultMap("dynamic_state_creator")
    List<DynamicState> queryDynamicStateByMemberId(int id);

    /**
     * 查询所有的动态信息
     *
     * @return 所有动态信息
     */
    @Select("select * from dynamic_state")
    @ResultMap("dynamic_state_creator")
    List<DynamicState> queryDynamicStateAll();

    /**
     * 查询最新的200条动态信息
     */
    @Select("SELECT * FROM dynamic_state ORDER BY dynamic_state_create_time DESC LIMIT 0,100")
    @ResultMap("dynamic_state_creator")
    List<DynamicState> queryDynamicStateLimit();

    /**
     * 通过类型id查找对应类型的所有动态信息
     *
     * @param type   动态类型
     * @param typeId 类型对应id
     * @return 动态信息
     */
    @Select("select * from dynamic_state where dynamic_state_type=#{type} and dynamic_state_type_id=#{typeId}")
    @Results(id = "dynamic_state_creator", value = {
            @Result(property = "dynamicStateCreator", column = "dynamic_state_create_id", one = @One(select = "com.jlb.mapper.MemberMapper.queryMemberById"))
    })
    List<DynamicState> queryDynamicStateByTypeAndTypeId(int type, int typeId);

}
