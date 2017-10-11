package com.youzidata.spark.model;

import java.util.Date;

/**
 * Created by MT-T450 on 2017/6/7.
 * 采样
 */
public class Sample {
    private Date startTime; //起始时刻
    private Date endTime; //截止时刻
    private Integer downRate; //下降率
    private Integer durationSec; //持续毫秒

    public Sample() {
    }

    public Sample(Date startTime, Date endTime, Integer downRate, Integer durationSec) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.downRate = downRate;
        this.durationSec = durationSec;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getDownRate() {
        return downRate;
    }

    public void setDownRate(Integer downRate) {
        this.downRate = downRate;
    }

    public Integer getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(Integer durationSec) {
        this.durationSec = durationSec;
    }

    @Override
    public String toString() {
        return "Sample{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", downRate=" + downRate +
                ", durationSec=" + durationSec +
                '}';
    }
}
