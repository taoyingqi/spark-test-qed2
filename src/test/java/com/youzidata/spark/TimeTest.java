package com.youzidata.spark;

import com.youzidata.spark.util.TimeUtil;
import org.junit.Test;

import java.util.Date;

public class TimeTest {


    @Test
    public void testParse() {
        Date date = TimeUtil.parseDate("3:02:23:000", "HH:mm:ss:SSS");
        Date date2 = TimeUtil.parseDate("10:10:10:230", "HH:mm:ss:SSS");
        System.out.println(TimeUtil.formatDate(date, "yyyy-MM-dd HH:mm:ss:SSS"));
        System.out.println(date.equals(date2));
    }


}
