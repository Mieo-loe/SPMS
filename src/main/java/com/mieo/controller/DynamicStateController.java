package com.mieo.controller;

import com.mieo.model.DynamicState;
import com.mieo.service.DynamicStateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("dynamicState")
public class DynamicStateController {
    @Autowired
    DynamicStateService dynamicStateService;

    /**
     * 添加动态信息
     *
     * @param dynamicState 动态信息
     */
    @RequestMapping("addDynamicState")
    @ResponseBody
     public Map<String,String> addDynamicState(DynamicState dynamicState) {
        dynamicStateService.addDynamicState(dynamicState);
        Map<String,String> map=new HashMap<>();
        map.put("msg", "动态添加成功");
        return  map;
    }


    @RequestMapping("queryDynamicStateByMemberId")
    @ResponseBody
    public List<DynamicState> queryDynamicStateByMemberId(int memberId) {
        return dynamicStateService.queryDynamicStateByMemberId(memberId);
    }

    @RequestMapping("queryDynamicStateByTypeAndTypeId")
    @ResponseBody
    public List<DynamicState> queryDynamicStateByTypeId(@RequestBody Map<String, Integer> map) {
        Integer type = MapUtils.getInteger(map, "type");
        Integer typeId = MapUtils.getInteger(map, "typeId");
        return dynamicStateService.queryDynamicStateByTypeAndTypeId(type, typeId);
    }
}
