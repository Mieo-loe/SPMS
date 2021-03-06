package com.jlb.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class LocalTime {

        public static boolean  isPostpone(String time){
            Date t=new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
            Date endTime=new Date();
            try {
                endTime= df.parse(time.toString());
            } catch (ParseException e) {
                log.debug("LocalTime 解析时间错误");
                e.printStackTrace();
            }
            return endTime.before(t);
        }


}
