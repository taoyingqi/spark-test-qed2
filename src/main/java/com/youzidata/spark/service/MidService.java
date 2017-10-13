package com.youzidata.spark.service;

import com.youzidata.spark.config.IConst;
import com.youzidata.spark.handler.HiveDataSplit;
import com.youzidata.spark.model.Mid;
import com.youzidata.spark.model.RowModel;
import com.youzidata.spark.model.Sample;
import com.youzidata.spark.util.AlgorithmUtil;
import com.youzidata.spark.util.MidUtil;
import com.youzidata.spark.util.TimeUtil;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MT-T450 on 2017/6/8.
 */
public class MidService {
    private static final Logger Log = LoggerFactory.getLogger(MidService.class);

    public static List<Mid> dataProcess(List<RowModel> list, FSDataOutputStream out) throws Exception {
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
        Integer topIndex = AlgorithmUtil.wxdLast1DownOn(list, 500 * IConst.WXD_FACTOR);
        Date topTime = list.get(topIndex).getTime();
        Integer lowerIndex = AlgorithmUtil.wxdFirst1DownOn(list, 0 * IConst.WXD_FACTOR, topIndex);
        Date lowerTime = list.get(lowerIndex).getTime();

        Log.info("=====topIndex=={}======time=={}=====", topIndex, TimeUtil.formatDate(topTime, TimeUtil.TIME_MILLIS_TYPE));
        Log.info("=====lowerIndex=={}======time=={}=====", lowerIndex, TimeUtil.formatDate(lowerTime, TimeUtil.TIME_MILLIS_TYPE));

/*        if (topIndex < lowerIndex) {
            Integer temp = topIndex;
            topIndex = lowerIndex;
            lowerIndex = temp;
        }*/

        List<RowModel> newList = list.subList(topIndex, lowerIndex);
//        List<RowModel> newList = HiveDataSplit.fliterData(list, topTime, lowerTime);
        Log.info("====0=={}", newList.get(0));
        Log.info("====1=={}", newList.get(1));
        Log.info("====2=={}", newList.get(2));
        Log.info("====3=={}", newList.get(3));
        Log.info("====4=={}", newList.get(4));
        Log.info("====5=={}", newList.get(5));

        Log.info("=====过滤后数据量=={}=====", newList.size());
        out.write(("=====获取500-0=="+(new Date()).getTime()+"===\n").getBytes("UTF-8"));

        // mid calc
        List<Mid> midList = calc(newList);
        Log.info("=====处理后数据量=={}=====", midList.size());

        write(midList, out);
        out.write(("=====写入中间表=="+(new Date()).getTime()+"===\n").getBytes("UTF-8"));

        return midList;
    }

    public static List<Mid> calc(List<RowModel> list) {
        Log.info("=========list.size=={}=====", list.size());
        List<Mid> midList = new ArrayList<Mid>();
        for (int i = 0; i < list.size(); i++) {
            RowModel rowModel = list.get(i);
            Mid mid = new Mid();
            mid.setFlightId(rowModel.getFlightId());
            // Wxd
            Mid.FH wxdFH = new Mid.FH();
            wxdFH.setTime(rowModel.getTime());
            wxdFH.setHeight(rowModel.getWxd());
            // 1.计算无线电高度每0.5秒下降率：从第3个高度开始，用当下的高度减去后移2个单位的高度，乘以120
            if (i > 1) {
                wxdFH.setSample1(new Sample(
                        midList.get(i - 2).getWxdFh().getTime(),
                        wxdFH.getTime(),
                        (rowModel.getWxd() - list.get(i - 2).getWxd()) * 120,
                        null));
            }
            // 2.计算无线电高度的每2秒平均下降率：从第11个高度开始，计算前9个无线电高度下降率的平均值
            if (i > 9) {
                Integer sumSample1DownRate = wxdFH.getSample1().getDownRate();
                for (int j = i - 8; j < i; j++) {
                    sumSample1DownRate += midList.get(j).getWxdFh().getSample1().getDownRate();
                }
                wxdFH.setSample2(new Sample(
                        midList.get(i - 8).getWxdFh().getSample1().getEndTime(),
                        wxdFH.getTime(),
                        sumSample1DownRate / 9,
                        null
                ));
            }
            // 设置 Wxd
            mid.setWxdFh(wxdFH);

            // Qnh
            Mid.FH qnhFh = new Mid.FH();
            qnhFh.setTime(rowModel.getTime());
            if (i % 4 == 0) {
                qnhFh.setHeight(rowModel.getQnh());
                // 4.计算气压/Height高度每秒下降率：从第2个整秒高度开始，用当下的高度减去后移1个单位的高度，乘以60
                if (i > 3) {
                    qnhFh.setSample1(new Sample(
                            midList.get(i - 4).getQnhFh().getTime(),
                            qnhFh.getTime(),
                            (qnhFh.getHeight() - midList.get(i - 4).getQnhFh().getHeight()) * 60,
                            null
                    ));
                }
                // 5.计算气压/Height高度每2秒平均下降率：从第4个整秒高度开始，计算前3个下降率的平均值
                if (i > 11) {
                    qnhFh.setSample2(new Sample(
                            midList.get(i - 8).getQnhFh().getTime(),
                            qnhFh.getTime(),
                            (qnhFh.getSample1().getDownRate()
                                    + midList.get(i - 4).getQnhFh().getSample1().getDownRate()
                                    + midList.get(i - 8).getQnhFh().getSample1().getDownRate()) / 3,
                            null
                    ));
                }
            }
            // 设置 Qnh
            mid.setQnhFh(qnhFh);
            // Height
            Mid.FH heightFH = new Mid.FH();
            heightFH.setTime(rowModel.getTime());
            if (i % 4 == 0) {
                heightFH.setHeight(rowModel.getHeight());
                // 4.计算气压/Height高度每秒下降率：从第2个整秒高度开始，用当下的高度减去后移1个单位的高度，乘以60
                if (i > 3) {
                    heightFH.setSample1(new Sample(
                            midList.get(i - 4).getHeightFh().getTime(),
                            heightFH.getTime(),
                            (heightFH.getHeight() - midList.get(i - 4).getHeightFh().getHeight()) * 60,
                            null
                    ));
                }
                // 5.计算气压/Height高度每2秒平均下降率：从第4个整秒高度开始，计算前3个下降率的平均值
                if (i > 11) {
                    heightFH.setSample2(new Sample(
                            midList.get(i - 8).getHeightFh().getTime(),
                            heightFH.getTime(),
                            (heightFH.getSample1().getDownRate()
                                    + midList.get(i - 4).getHeightFh().getSample1().getDownRate()
                                    + midList.get(i - 8).getHeightFh().getSample1().getDownRate()) / 3,
                            null
                    ));
                }
            }
            // 设置 Height
            mid.setHeightFh(heightFH);

            if ((i - 3) % 4 == 0) {
                //秒内四个时刻的平均下降率超过500
                if (wxdFH.getSample2().getDownRate() != null && wxdFH.getSample2().getDownRate() < -500 * IConst.WXD_FACTOR
                        && midList.get(i - 1).getWxdFh().getSample2().getDownRate() != null && midList.get(i - 1).getWxdFh().getSample2().getDownRate() < -500 * IConst.WXD_FACTOR
                        && midList.get(i - 2).getWxdFh().getSample2().getDownRate() != null && midList.get(i - 2).getWxdFh().getSample2().getDownRate() < -500 * IConst.WXD_FACTOR
                        && midList.get(i - 3).getWxdFh().getSample2().getDownRate() != null && midList.get(i - 3).getWxdFh().getSample2().getDownRate() < -500 * IConst.WXD_FACTOR
                        ) {
                    midList.get(i - 3).setWxdCond(true);
                } else {
                    midList.get(i - 3).setWxdCond(false);
                }
                int n = 0;
                if (midList.get(i - 3).getWxdCond()) n++;
                if (midList.get(i - 3).getQnhCond()) n++;
                if (midList.get(i - 3).getHeightCond()) n++;
                midList.get(i - 3).setMultiCond(n > 1); // 至少两个平均下降率均超过500英尺
            }
            if (i % 4 == 0) {
                mid.setQnhCond(qnhFh.getSample2().getDownRate() != null && qnhFh.getSample2().getDownRate() < -500);
                mid.setHeightCond(heightFH.getSample2().getDownRate() != null && heightFH.getSample2().getDownRate() < -500);
            }
            midList.add(mid);
        }
        int start = 0, end = 0;
        for (int i = 0; i < midList.size(); i+=4) {
            Mid mid = midList.get(i);
            if (mid.getMultiCond() != null && mid.getMultiCond()) {
                if (start == 0) {
                    start = i;
                }
            }
            if (mid.getMultiCond() != null && !mid.getMultiCond()) {
                end = i;
            }
            if (start != 0 && start < end) {
                // 计算持续时间
                midList.get(start).setDurationSec(1);
                midList.get(end -1).setDurationSec((int) (midList.get(end - 1).getHeightFh().getTime().getTime() - midList.get(start).getHeightFh().getTime().getTime() + 250));
                start = 0; end = 0;
            }
        }
        // 末尾 MultiCond 一直为 true
        if (start > end) {
            // 计算持续时间
            midList.get(start).setDurationSec(1);
            end = midList.size();
            midList.get(end -1).setDurationSec((int) (midList.get(end - 1).getHeightFh().getTime().getTime() - midList.get(start).getHeightFh().getTime().getTime() + 250));
        }
        return midList;
    }


    public static void write(List<Mid> list, FSDataOutputStream out) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            Mid mid = list.get(i);
            String row = mid.getFlightId()
                    + "," + TimeUtil.formatDate(mid.getWxdFh().getTime(), TimeUtil.TIME_MILLIS_TYPE)
                    + "," + MidUtil.devWxdFactor(mid.getWxdFh().getHeight());

            if (i < 2) {
                row += ",,,";
            } else {
                row += "," + TimeUtil.formatDate(mid.getWxdFh().getSample1().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                        + "," + TimeUtil.formatDate(mid.getWxdFh().getSample1().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                        + "," + MidUtil.devWxdFactor(mid.getWxdFh().getSample1().getDownRate());
            }

            if (i < 10) {
                row += ",,,";
            } else {
                row += "," + TimeUtil.formatDate(mid.getWxdFh().getSample2().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                        + "," + TimeUtil.formatDate(mid.getWxdFh().getSample2().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                        + "," + MidUtil.devWxdFactor(mid.getWxdFh().getSample2().getDownRate());
            }

            row += "," + TimeUtil.formatDate(mid.getQnhFh().getTime(), TimeUtil.TIME_MILLIS_TYPE);
            if (i % 4 == 0) {
                if (i <= 3) {
                    row += "," + mid.getQnhFh().getHeight() + ",,," + ",,,";
                }
                if (i > 3 && i <= 11) {
                    row += "," + mid.getQnhFh().getHeight()
                            + "," + TimeUtil.formatDate(mid.getQnhFh().getSample1().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + TimeUtil.formatDate(mid.getQnhFh().getSample1().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + mid.getQnhFh().getSample1().getDownRate()
                            + ",,,";

                }
                if (i > 11) {
                    row += "," + mid.getQnhFh().getHeight()
                            + "," + TimeUtil.formatDate(mid.getQnhFh().getSample1().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + TimeUtil.formatDate(mid.getQnhFh().getSample1().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + mid.getQnhFh().getSample1().getDownRate()
                            + "," + TimeUtil.formatDate(mid.getQnhFh().getSample2().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + TimeUtil.formatDate(mid.getQnhFh().getSample2().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + mid.getQnhFh().getSample2().getDownRate();
                }
            } else {
                row += ",,,,"+",,,";
            }

            row += "," + TimeUtil.formatDate(mid.getHeightFh().getTime(), TimeUtil.TIME_MILLIS_TYPE);
            if (i % 4 == 0) {
                if (i <= 3) {
                    row += "," + mid.getHeightFh().getHeight() + ",,," + ",,,";
                }
                if (i > 3 && i <= 11) {
                    row += "," + mid.getHeightFh().getHeight()
                            + "," + TimeUtil.formatDate(mid.getHeightFh().getSample1().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + TimeUtil.formatDate(mid.getHeightFh().getSample1().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + mid.getHeightFh().getSample1().getDownRate()
                            + ",,,";
                }
                if (i > 11) {
                    row += "," + mid.getHeightFh().getHeight()
                            + "," + TimeUtil.formatDate(mid.getHeightFh().getSample1().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + TimeUtil.formatDate(mid.getHeightFh().getSample1().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + mid.getHeightFh().getSample1().getDownRate()
                            + "," + TimeUtil.formatDate(mid.getHeightFh().getSample2().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + TimeUtil.formatDate(mid.getHeightFh().getSample2().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                            + "," + mid.getHeightFh().getSample2().getDownRate();
                }
            } else {
                row += ",,,,"+",,,";
            }

            if (i % 4 == 0) {
                row += "," + mid.getWxdCond()
                        + "," + mid.getQnhCond()
                        + "," + mid.getHeightCond()
                        + "," + mid.getMultiCond();
            } else {
                row += ",,,,";
            }
            if (mid.getDurationSec() != null) {
                row += "," + mid.getDurationSec();
            } else {
                row += ",";
            }
            row += "\n";
            out.write(row.getBytes("UTF-8"));
        }
    }

}
