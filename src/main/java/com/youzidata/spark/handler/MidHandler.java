package com.youzidata.spark.handler;

import com.youzidata.spark.model.Mid;
import com.youzidata.spark.model.RowModel;
import com.youzidata.spark.service.MidService;
import org.apache.commons.collections.IteratorUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.MapGroupsFunction;
import org.apache.spark.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MidHandler {
    private final static Logger Log = LoggerFactory.getLogger(MidHandler.class);


    public static void main(String[] args) {
        // mid calc

        Log.info("================开始执行========="+new Date()+"=============");

        SparkSession sparkSession = SparkSession.builder().appName("dataSplitQed_mid").enableHiveSupport().getOrCreate();

        SQLContext sqlCtx = new SQLContext(sparkSession);

//		Dataset<Row> df = sqlCtx.sql("SELECT TIME0,ALT_QNH,HEIGHT,RALTC,ID from china_east_airlines_99990");
//		Dataset<Row> df = sqlCtx.sql("SELECT TIME0,ALT_QNH,HEIGHT,RALTC,ID from china_east_airlines_bdx");
        Dataset<Row> df = sqlCtx.sql("select START_TIME,RALTC,ALT_QNH,HEIGHT,ID from dh_data");

        Log.info("================数据查询时间========="+new Date()+"=============");

        Dataset<RowModel> splitData = HiveDataSplit.rowMapping(df);

        Log.info("================splitData.count=={}=======", splitData.count());

        Log.info("================数据拆分时间========="+new Date()+"=============");

        Dataset<String> result = splitData.groupByKey(new MapFunction<RowModel,String>() {

            public String call(RowModel value) throws Exception {
                return value.getFlightId();
            }
        }, Encoders.STRING()).mapGroups(new MapGroupsFunction<String, RowModel, String>() {
            public String call(String key, Iterator<RowModel> values) throws Exception {
                Log.info("=========begin========"+new Date()+"===========");
//                List<RowModel> list = IteratorUtils.toList(values);
                List<RowModel> list = new LinkedList<RowModel>();
                int i = 0;
                while(values.hasNext()) {
                    i++;
                    RowModel rowModel = values.next();
                    if (i > 20500 && i < 21000) {
                        Log.info("========{}", rowModel);
                    }
                }
                Configuration conf = new Configuration();
                FileSystem fs = FileSystem.get(conf);
                Path path = new Path("/flight_decline/flight_"+key+"_qed.csv");
                final FSDataOutputStream out = fs.create(path);
                MidService.dataProcess(list,out);
                fs.close();

                Log.info("============="+key+"===finish===========");
                Log.info("=========end========"+new Date()+"===========");
                return key;
            }
        }, Encoders.STRING());
//        result.show();
        long num = result.count();
        Log.info("======共计=======" + num + "=====个航班=====");

        Log.info("=========完成执行==="+new Date()+"========");

        sparkSession.close();

    }

}
