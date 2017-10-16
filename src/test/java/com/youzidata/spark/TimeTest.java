package com.youzidata.spark;

import com.youzidata.spark.util.TimeUtil;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class TimeTest {


    @Test
    public void testParse() {
        Date date = TimeUtil.parseDate("3:02:23:000", "HH:mm:ss:SSS");
        Date date2 = TimeUtil.parseDate("10:10:10:230", "HH:mm:ss:SSS");
        System.out.println(TimeUtil.formatDate(date, "yyyy-MM-dd HH:mm:ss:SSS"));
        System.out.println(date.equals(date2));
    }

    @Test
    public void testMillis() {
        Date date = TimeUtil.parseDate("20170908121200250", TimeUtil.DATE_TIME_MILLIS_TYPE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        System.out.println(c.get(Calendar.MILLISECOND));
    }

}
