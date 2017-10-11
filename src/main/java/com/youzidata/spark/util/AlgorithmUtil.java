package com.youzidata.spark.util;

import com.youzidata.spark.model.RowModel;

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
    public static Integer wxdFirst1DownOn(List<RowModel> list, int wxd) {
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getWxd() < wxd){
                return i;
            }
        }
        return null;
    }

}
