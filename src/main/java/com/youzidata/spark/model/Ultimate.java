package com.youzidata.spark.model;

import java.util.Date;

/**
 * Created by MT-T450 on 2017/6/7.
 * 航班最终表
 */
public class Ultimate {
    private String flightId;
    private Integer down500n;
    private Date last1Down500Time;
    private Integer down0n;
    private Date first1Down0Time;
    private Date durationTime;
    private Sample wxdMdc; //无线电高度最大下降率
    private Sample qnhMdc; //QNH高度最大下降率
    private Sample heightMdc; //Height高度最大下降率
    private Integer downRateGt500n; //下降率超过500英尺每分钟-次数
    private Sample downRateGt500Ld; //下降率超过500英尺每分钟-最长一次持续毫秒

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public Integer getDown500n() {
        return down500n;
    }

    public void setDown500n(Integer down500n) {
        this.down500n = down500n;
    }

    public Date getLast1Down500Time() {
        return last1Down500Time;
    }

    public void setLast1Down500Time(Date last1Down500Time) {
        this.last1Down500Time = last1Down500Time;
    }

    public Integer getDown0n() {
        return down0n;
    }

    public void setDown0n(Integer down0n) {
        this.down0n = down0n;
    }

    public Date getFirst1Down0Time() {
        return first1Down0Time;
    }

    public void setFirst1Down0Time(Date first1Down0Time) {
        this.first1Down0Time = first1Down0Time;
    }

    public Date getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Date durationTime) {
        this.durationTime = durationTime;
    }

    public Sample getWxdMdc() {
        return wxdMdc;
    }

    public void setWxdMdc(Sample wxdMdc) {
        this.wxdMdc = wxdMdc;
    }

    public Sample getQnhMdc() {
        return qnhMdc;
    }

    public void setQnhMdc(Sample qnhMdc) {
        this.qnhMdc = qnhMdc;
    }

    public Sample getHeightMdc() {
        return heightMdc;
    }

    public void setHeightMdc(Sample heightMdc) {
        this.heightMdc = heightMdc;
    }

    public Integer getDownRateGt500n() {
        return downRateGt500n;
    }

    public void setDownRateGt500n(Integer downRateGt500n) {
        this.downRateGt500n = downRateGt500n;
    }

    public Sample getDownRateGt500Ld() {
        return downRateGt500Ld;
    }

    public void setDownRateGt500Ld(Sample downRateGt500Ld) {
        this.downRateGt500Ld = downRateGt500Ld;
    }

    @Override
    public String toString() {
        return "Ultimate{" +
                "flightId=" + flightId +
                ", down500n=" + down500n +
                ", last1Down500Time=" + last1Down500Time +
                ", down0n=" + down0n +
                ", first1Down0Time=" + first1Down0Time +
                ", durationTime=" + durationTime +
                ", wxdMdc=" + wxdMdc +
                ", qnhMdc=" + qnhMdc +
                ", heightMdc=" + heightMdc +
                ", downRateGt500n=" + downRateGt500n +
                ", downRateGt500Ld=" + downRateGt500Ld +
                '}';
    }
}