package com.youzidata.spark.util;

import com.youzidata.spark.config.IConst;

/**
 * Created by MT-T450 on 2017/6/8.
 */
public class MidUtil {

    public static Integer mulWxdFactor(Float f) {
        return f == null ? null : (int)(f * IConst.WXD_FACTOR);
    }

    public static Float devWxdFactor(Integer i) {
        return i == null ? null : i * 1.0F / IConst.WXD_FACTOR;
    }


}
