package com.youzidata.spark.model;


import com.youzidata.spark.util.TimeUtil;

import java.util.Date;

/**
 * Created by MT-T450 on 2017/6/7.
 * 加工过源数据完整
 */
public class RowModel {
    private Date time;
    private Integer wxd;
    private Integer qnh;
    private Integer height;
    private String flightId;
    private Date date;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getWxd() {
        return wxd;
    }

    public void setWxd(Integer wxd) {
        this.wxd = wxd;
    }

    public Integer getQnh() {
        return qnh;
    }

    public void setQnh(Integer qnh) {
        this.qnh = qnh;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RowModel{" +
                "time=" + TimeUtil.formatDate(time, TimeUtil.TIME_MILLIS_TYPE) +
                ", wxd=" + wxd +
                ", qnh=" + qnh +
                ", height=" + height +
                ", flightId=" + flightId +
                ", date=" + date +
                '}';
    }
}
