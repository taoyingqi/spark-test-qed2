package com.youzidata.spark.service;

import com.youzidata.spark.config.IConst;
import com.youzidata.spark.model.Mid;
import com.youzidata.spark.model.RowModel;
import com.youzidata.spark.model.Sample;
import com.youzidata.spark.model.Ultimate;
import com.youzidata.spark.util.AlgorithmUtil;
import com.youzidata.spark.util.MidUtil;
import com.youzidata.spark.util.TimeUtil;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by lonel on 2017/6/10.
 */
public class UltimateService {
    private final static Logger Log = LoggerFactory.getLogger(UltimateService.class);

    public static Ultimate dataProcess(List<RowModel> list, FSDataOutputStream out) throws Exception {
        // mid calc
        List<Mid> midList = MidService.dataProcess(list, out);
        Log.info("=====midList.size=={}==", midList.size());

        // filter data
        Integer topIndex = AlgorithmUtil.wxdLast1DownOn(list, 800 * IConst.WXD_FACTOR);
        Log.info("=====topIndex=={}======time=={}=====", topIndex, TimeUtil.formatDate(list.get(topIndex).getTime(), TimeUtil.TIME_MILLIS_TYPE));

        List<RowModel> newList = list.subList(topIndex, list.size());
//                HiveDataSplit.fliterData(list, lowerTime, topTime);
        Log.info("=====过滤后数据量=={}=====", newList.size());
//        out.write(("=====获取800以后==" + (new Date()).getTime() + "===\n").getBytes("UTF-8"));

        // ultimate calc
        Ultimate ultimate = calc(newList, midList);

        write(ultimate, out);
//        out.write(("=====写入结果表==" + (new Date()).getTime() + "===\n").getBytes("UTF-8"));

        return ultimate;
    }

    /**
     * @param list    数据区间800以下
     * @param midList
     * @return
     * @throws Exception
     */
    public static Ultimate calc(List<RowModel> list, List<Mid> midList) throws Exception {
//        List<Origin> originList = OriginDao.getAll();

        Ultimate ultimate = new Ultimate();
        ultimate.setFlightId(list.get(0).getFlightId());
        // 下穿标志
        boolean gt500Flag = false, gt0Flag = false;
        for (int i = 1; i < list.size(); i++) {
            RowModel rowModel = list.get(i);
            if (rowModel.getWxd() > 500 * IConst.RALTC_FACTOR && !gt500Flag) {
                gt500Flag = true;
            }
            if (rowModel.getWxd() < 500 * IConst.RALTC_FACTOR && gt500Flag) {
                // 设置下穿500英尺次数
                if (ultimate.getDown500n() == null) {
                    ultimate.setDown500n(1);
                } else {
                    ultimate.setDown500n(ultimate.getDown500n() + 1);
                }
                // 设置最后一次下穿500英尺时刻
                ultimate.setLast1Down500Time(rowModel.getTime());
                // 恢复下穿500英尺-标志
                gt500Flag = false;
            }
            if (rowModel.getWxd() > 0 && !gt0Flag) {
                gt0Flag = true;
            }
            if (rowModel.getWxd() < 0 && gt0Flag) {
                // 设置下穿0英尺次数
                if (ultimate.getDown0n() == null) {
                    ultimate.setDown0n(1);
                } else {
                    ultimate.setDown0n(ultimate.getDown0n() + 1);
                }
                // 设置首次下穿0英尺时刻
                if (ultimate.getFirst1Down0Time() == null) {
                    ultimate.setFirst1Down0Time(rowModel.getTime());
                }
                // 恢复下穿0英尺-标志
                gt0Flag = false;
            }
        }
        // 设置持续时间
        if (ultimate.getFirst1Down0Time() != null && ultimate.getLast1Down500Time() != null) {
            ultimate.setDurationTime(new Date(ultimate.getFirst1Down0Time().getTime() - ultimate.getLast1Down500Time().getTime()));
        } else {
            Log.warn("[航班{}，数据不全。{}]", ultimate.getFlightId(), ultimate);
        }
        // 三个高度的最大下降率
        int wxdi = 0, qnhi = 0, heighti = 0;
        int wxdDownRate = AlgorithmUtil.abs(midList.get(0).getWxdFh().getSample2().getDownRate())
                , qnhDownRate = AlgorithmUtil.abs(midList.get(0).getQnhFh().getSample2().getDownRate())
                , heightDownRate = AlgorithmUtil.abs(midList.get(0).getHeightFh().getSample2().getDownRate());
        for (int i = 1; i < midList.size(); i++) {
            Mid mid = midList.get(i);
            if (wxdDownRate < AlgorithmUtil.abs(mid.getWxdFh().getSample2().getDownRate())) {
                wxdi = i;
                wxdDownRate = AlgorithmUtil.abs(mid.getWxdFh().getSample2().getDownRate());
            }
            if (qnhDownRate < AlgorithmUtil.abs(mid.getQnhFh().getSample2().getDownRate())) {
                qnhi = i;
                qnhDownRate = AlgorithmUtil.abs(mid.getQnhFh().getSample2().getDownRate());
            }
            if (heightDownRate < AlgorithmUtil.abs(mid.getHeightFh().getSample2().getDownRate())) {
                heighti = i;
                heightDownRate = AlgorithmUtil.abs(mid.getHeightFh().getSample2().getDownRate());
            }
        }
        ultimate.setWxdMdc(midList.get(wxdi).getWxdFh().getSample2());
        ultimate.setQnhMdc(midList.get(qnhi).getQnhFh().getSample2());
        ultimate.setHeightMdc(midList.get(heighti).getHeightFh().getSample2());
        // 下降率超过500英尺次数
        Sample downRateGt500LdSample = new Sample();
        for (int i = 0; i < midList.size(); i++) {
            Mid mid = midList.get(i);
            // 跳过空
            if (mid.getDurationSec() == null) {
                continue;
            }
            if (mid.getDurationSec().equals(1)) {
                downRateGt500LdSample.setStartTime(mid.getHeightFh().getTime());
            }
            if (mid.getDurationSec() > 1) {
                if (ultimate.getDownRateGt500n() == null) {
                    ultimate.setDownRateGt500n(1);
                } else {
                    ultimate.setDownRateGt500n(ultimate.getDownRateGt500n() + 1);
                }
                downRateGt500LdSample.setEndTime(mid.getHeightFh().getTime());
                downRateGt500LdSample.setDurationSec(mid.getDurationSec());
                //
                if (ultimate.getDownRateGt500Ld() == null) {
                    ultimate.setDownRateGt500Ld(new Sample(
                            downRateGt500LdSample.getStartTime(),
                            new Date(downRateGt500LdSample.getEndTime().getTime() + 250),
                            null,
                            downRateGt500LdSample.getDurationSec()
                    ));
                } else {
                    if (ultimate.getDownRateGt500Ld().getDurationSec() < downRateGt500LdSample.getDurationSec()) {
                        ultimate.setDownRateGt500Ld(new Sample(
                                downRateGt500LdSample.getStartTime(),
                                new Date(downRateGt500LdSample.getEndTime().getTime() + 250),
                                null,
                                downRateGt500LdSample.getDurationSec()
                        ));
                    }
                }
            }
        }
        return ultimate;
    }

    public static void write(Ultimate ultimate, FSDataOutputStream out) throws IOException {
        String row = "\n\n" + ultimate.getFlightId()
                + "," + ultimate.getDown500n()
                + "," + TimeUtil.formatDate(ultimate.getLast1Down500Time(), TimeUtil.TIME_MILLIS_TYPE)
                + "," + ultimate.getDown0n()
                + "," + TimeUtil.formatDate(ultimate.getFirst1Down0Time(), TimeUtil.TIME_MILLIS_TYPE)
                + "," + TimeUtil.formatDate(ultimate.getDurationTime(), TimeUtil.TIME_MILLIS_TYPE)

                + "," + TimeUtil.formatDate(ultimate.getWxdMdc().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                + "," + TimeUtil.formatDate(ultimate.getWxdMdc().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                + "," + MidUtil.devWxdFactor(ultimate.getWxdMdc().getDownRate())
                + "," + TimeUtil.formatDate(ultimate.getQnhMdc().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                + "," + TimeUtil.formatDate(ultimate.getQnhMdc().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                + "," + ultimate.getQnhMdc().getDownRate()
                + "," + TimeUtil.formatDate(ultimate.getHeightMdc().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                + "," + TimeUtil.formatDate(ultimate.getHeightMdc().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                + "," + ultimate.getHeightMdc().getDownRate()

                + "," + ultimate.getDownRateGt500n()
                + "," + TimeUtil.formatDate(ultimate.getDownRateGt500Ld().getStartTime(), TimeUtil.TIME_MILLIS_TYPE)
                + "," + TimeUtil.formatDate(ultimate.getDownRateGt500Ld().getEndTime(), TimeUtil.TIME_MILLIS_TYPE)
                + "," + ultimate.getDownRateGt500Ld().getDurationSec();
//		System.out.println(row);
//        out.write(("=====最终结果=====\n").getBytes("UTF-8"));
        out.write((row + "\n").getBytes("UTF-8"));
    }


}
