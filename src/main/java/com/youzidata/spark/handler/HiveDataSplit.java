package com.youzidata.spark.handler;

import com.youzidata.spark.config.IConst;
import com.youzidata.spark.model.Mid;
import com.youzidata.spark.model.RowModel;
import com.youzidata.spark.util.AlgorithmUtil;
import com.youzidata.spark.util.TimeUtil;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by MT-T450 on 2017/8/29.
 */
public class HiveDataSplit {
    private final static Logger Log = LoggerFactory.getLogger(HiveDataSplit.class);

    public static void main(String[] args) throws Exception {
    }

    //拆分数据
    public static Dataset<RowModel> rowMapping(Dataset<Row> df) {
        Dataset<RowModel> newdata = df.flatMap(new FlatMapFunction() {
            public Iterator<RowModel> call(Object t) throws Exception {
                Row r = (Row) t;
                List<RowModel> list = new ArrayList<RowModel>();
                try {
                    Date time = TimeUtil.parseDate(r.getString(0), TimeUtil.TIME_MILLIS_TYPE);
                    String[] RALTC = r.getString(3).trim().split("\\s+");
                    Integer ALT_QNH = Integer.parseInt(r.getString(1));
                    Integer HEIGHT = Integer.parseInt(r.getString(2));
                    String ID = r.getString(4);
//                Date date_time = TimeUtil.parseDate(r.getString(5), "yyyyMMdd");

                    for (String s : RALTC) {
                        RowModel row = new RowModel();
                        row.setTime(time);
                        row.setWxd((int) (Float.parseFloat(s) * IConst.WXD_FACTOR));
                        row.setQnh(ALT_QNH);
                        row.setHeight(HEIGHT);
                        row.setFlightId(ID);
//                    row.setDate(date_time);
                        list.add(row);
                    }
                } catch (Exception e) {
                }
                return list.iterator();
            }
        }, Encoders.kryo(RowModel.class));
        return newdata;
    }

    public static void dataProcess(List<RowModel> list, FSDataOutputStream out) throws Exception {
        out.write(("=====开始计算==" + TimeUtil.formatDate(new Date(), TimeUtil.DATE_TIME_MILLIS_TYPE) + "===\n").getBytes("UTF-8"));
        // order by date, time
        /*Collections.sort(list, new Comparator<RowModel>() {

            public int compare(RowModel o1, RowModel o2) {
                // TODO Auto-generated method stub
                int v1 = o1.getDate().compareTo(o2.getDate());
                if (v1 != 0) {
                    return v1;
                }
                return o1.getTime().compareTo(o2.getTime());
            }
        });*/
        out.write(("=====排序结束==" + TimeUtil.formatDate(new Date(), TimeUtil.DATE_TIME_MILLIS_TYPE) + "===\n").getBytes("UTF-8"));

        // filter data
        Integer topIndex = AlgorithmUtil.wxdLast1DownOn(list, 500);
        Date topTime = list.get(topIndex).getTime();

        Integer lowerIndex = AlgorithmUtil.wxdFirst1DownOn(list, 0, topIndex);
        Date lowerTime = list.get(lowerIndex).getTime();

        List<RowModel> newList = fliterData(list, topTime, lowerTime);

        // mid calc

        // ultimate calc

    }

    public static List<RowModel> fliterData(List<RowModel> list, Date minTime, Date maxTime){
        List<RowModel> flightList = new ArrayList<RowModel>();
        // 开始标志
        boolean startflag = false;
        for(int i = 0; i < list.size(); i++){
            RowModel row = list.get(i);
            if(row.getTime().equals(minTime)){
                startflag = true;
            }
            if(startflag) {
                flightList.add(row);
            }
            //结束标志
            if (list.get(i).getTime().equals(maxTime)) {
                break;
            }
        }
        return flightList;
    }


}
