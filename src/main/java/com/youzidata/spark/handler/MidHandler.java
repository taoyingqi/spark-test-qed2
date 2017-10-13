package com.youzidata.spark.handler;

import com.youzidata.spark.model.Mid;
import com.youzidata.spark.model.MidResult;
import com.youzidata.spark.model.RowModel;
import com.youzidata.spark.service.MidService;
import com.youzidata.spark.util.TimeUtil;
import org.apache.commons.collections.IteratorUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.function.FlatMapGroupsFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.MapGroupsFunction;
import org.apache.spark.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MidHandler {
    private final static Logger Log = LoggerFactory.getLogger(MidHandler.class);


    public static void main(String[] args) {
        // mid calc

        Log.info("================开始执行========="+new Date()+"=============");

        SparkSession sparkSession = SparkSession.builder().appName("dataSplitQed_mid").enableHiveSupport().getOrCreate();

        SQLContext sqlCtx = new SQLContext(sparkSession);

//		Dataset<Row> df = sqlCtx.sql("SELECT TIME0,ALT_QNH,HEIGHT,RALTC,ID from china_east_airlines_99990");
//		Dataset<Row> df = sqlCtx.sql("SELECT TIME0,ALT_QNH,HEIGHT,RALTC,ID from china_east_airlines_bdx");
        Dataset<Row> df = sqlCtx.sql("select START_TIME,RALTC,ALT_QNH,HEIGHT,ID from dh_data order by START_TIME");

        Log.info("================数据查询时间========="+new Date()+"=============");

        Dataset<RowModel> splitData = HiveDataSplit.rowMapping(df);

        Log.info("================splitData.count=={}=======", splitData.count());

        Log.info("================数据拆分时间========="+new Date()+"=============");



        Dataset<MidResult> result = splitData.groupByKey(new MapFunction<RowModel,String>() {

            public String call(RowModel value) throws Exception {
                return value.getFlightId();
            }
        }, Encoders.STRING()).flatMapGroups(new FlatMapGroupsFunction<String, RowModel, MidResult>() {
            public Iterator<MidResult> call(String key, Iterator<RowModel> values) throws Exception {
                Log.info("=========begin========"+new Date()+"===========");
                List<RowModel> list = IteratorUtils.toList(values);
                Configuration conf = new Configuration();
                FileSystem fs = FileSystem.get(conf);
                Path path = new Path("/flight_decline/flight_"+key+"_qed.csv");
                final FSDataOutputStream out = fs.create(path);
                List<Mid> midList = MidService.dataProcess(list,out);
                fs.close();

                Log.info("============="+key+"===finish===========");
                Log.info("=========end========"+new Date()+"===========");

                List<MidResult> midResultList = toResult(midList);
                return midResultList.iterator();
            }
        }, Encoders.bean(MidResult.class));
//        result.show();
        long num = result.count();
        Log.info("======共计=======" + num + "=====个航班=====");
        result.show();
        Log.info("=========完成执行==="+new Date()+"========");

        sparkSession.close();

    }

    public static List<MidResult> toResult(List<Mid> midList) {
        List<MidResult> list = new ArrayList<MidResult>();
        for (Mid mid : midList) {
            MidResult midResult = new MidResult();
            midResult.setFlightId(mid.getFlightId());

            midResult.setWxdFhStartTime(TimeUtil.formatDate(mid.getWxdFh().getSample1().getStartTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setWxdFhEndTime(TimeUtil.formatDate(mid.getWxdFh().getSample1().getEndTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setWxdFhDownRate(mid.getWxdFh().getSample1().getDownRate());
            midResult.setWxdFhAvgStartTime(TimeUtil.formatDate(mid.getWxdFh().getSample2().getStartTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setWxdFhAvgEndTime(TimeUtil.formatDate(mid.getWxdFh().getSample2().getEndTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setWxdFhAvgDownRate(mid.getWxdFh().getSample2().getDownRate());

            midResult.setQnhFhStartTime(TimeUtil.formatDate(mid.getQnhFh().getSample1().getStartTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setQnhFhEndTime(TimeUtil.formatDate(mid.getQnhFh().getSample1().getEndTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setQnhFhDownRate(mid.getQnhFh().getSample1().getDownRate());
            midResult.setQnhFhAvgStartTime(TimeUtil.formatDate(mid.getQnhFh().getSample2().getStartTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setQnhFhAvgEndTime(TimeUtil.formatDate(mid.getQnhFh().getSample2().getEndTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setQnhFhAvgDownRate(mid.getQnhFh().getSample2().getDownRate());

            midResult.setHeightFhStartTime(TimeUtil.formatDate(mid.getHeightFh().getSample1().getStartTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setHeightFhEndTime(TimeUtil.formatDate(mid.getHeightFh().getSample1().getEndTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setHeightFhDownRate(mid.getHeightFh().getSample1().getDownRate());
            midResult.setHeightFhAvgStartTime(TimeUtil.formatDate(mid.getHeightFh().getSample2().getStartTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setHeightFhAvgEndTime(TimeUtil.formatDate(mid.getHeightFh().getSample2().getEndTime(), TimeUtil.TIME_MILLIS_TYPE));
            midResult.setHeightFhAvgDownRate(mid.getHeightFh().getSample2().getDownRate());

            midResult.setWxdCond(mid.getWxdCond());
            midResult.setQnhCond(mid.getQnhCond());
            midResult.setHeightCond(mid.getHeightCond());
            midResult.setMultiCond(mid.getMultiCond());
            midResult.setDurationSec(mid.getDurationSec());

            list.add(midResult);
        }
        return list;
    }

}
