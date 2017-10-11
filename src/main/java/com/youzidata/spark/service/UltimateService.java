package com.youzidata.spark.service;

import com.youzidata.spark.model.RowModel;
import com.youzidata.spark.model.Ultimate;
import com.youzidata.spark.util.TimeUtil;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by lonel on 2017/6/10.
 */
public class UltimateService {
    private final static Logger LOG = LoggerFactory.getLogger(UltimateService.class);

    public static Ultimate calc(List<RowModel> list, FSDataOutputStream out) throws Exception {
        out.write(("=====开始计算==" + TimeUtil.formatDate(new Date(), TimeUtil.DATE_TIME_MILLIS_TYPE) + "===\n").getBytes("UTF-8"));

        return null;
    }


}
