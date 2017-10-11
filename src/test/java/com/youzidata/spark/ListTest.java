package com.youzidata.spark;

import com.youzidata.spark.model.RowModel;
import com.youzidata.spark.util.TimeUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by MT-T450 on 2017/8/29.
 */
public class ListTest {

    @Test
    public void testSort() {
        List<RowModel> list = new ArrayList<RowModel>();
        RowModel rowModel1 = new RowModel();
        rowModel1.setFlightId("1");
        rowModel1.setTime(TimeUtil.parseDate("12:09:08", "HH:mm:ss"));
        rowModel1.setDate(TimeUtil.parseDate("20170906", TimeUtil.DATE_TYPE));
        list.add(rowModel1);

        RowModel rowModel2 = new RowModel();
        rowModel2.setFlightId("2");
        rowModel2.setTime(TimeUtil.parseDate("14:09:08", "HH:mm:ss"));
        rowModel2.setDate(TimeUtil.parseDate("20170806", TimeUtil.DATE_TYPE));
        list.add(rowModel2);

        RowModel rowModel3 = new RowModel();
        rowModel3.setFlightId("3");
        rowModel3.setTime(TimeUtil.parseDate("15:09:08", "HH:mm:ss"));
        rowModel3.setDate(TimeUtil.parseDate("20170806", TimeUtil.DATE_TYPE));
        list.add(rowModel3);

        Collections.sort(list, new Comparator<RowModel>() {

            public int compare(RowModel o1, RowModel o2) {
                // TODO Auto-generated method stub
                int v1 = o1.getDate().compareTo(o2.getDate());
                if (v1 != 0) {
                    return v1;
                }
                return o1.getTime().compareTo(o2.getTime());
            }
        });


        for (RowModel rowModel : list) {
            System.out.println(rowModel);
        }

    }


}
