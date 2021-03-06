package com.youzidata.spark.util;

import com.youzidata.spark.model.RowModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by MT-T450 on 2017/8/29.
 */
public class AlgorithmUtil {

    //筛选上限
    // 定阶段，按无线电高度（末次）下降到1000/800/600/500及之后（阶段可适应人工调整）
    public static Integer wxdLast1DownOn(List<RowModel> list, int wxd) {
        boolean flag = false;
        for (int i = list.size() - 1; i >= 1; i--) {
            if (list.get(i).getWxd() < wxd && !flag) {
                flag = true;
            }
            if (list.get(i).getWxd() > wxd && flag) {
                return i + 1;
            }
        }
        return null;
    }

    //筛选下限
    // 定阶段，按无线电高度（首次）下降到0
    public static Integer wxdFirst1DownOn(List<RowModel> list, int wxd, int start) {
        for(int i = start; i < list.size(); i++){
            if(list.get(i).getWxd() < wxd){
                return i;
            }
        }
        return null;
    }


    public static int abs(Integer x) {
        if (x == null) {
            return 0;
        }
        return Math.abs(x);
    }

    // 返回不大于x的最大4的倍数
    public static int floor4(int x) {
        return x - x % 4;
    }

    // 返回不小于x的最小4的倍数
    public static int ceil4(int x) {
        return x - x % 4 + 4;
    }

    // 毫秒数/250
    public static int remainder4(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MILLISECOND) / 250;
    }


}
