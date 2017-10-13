package com.youzidata.spark.model;

import java.util.Date;

public class UltimateResult {

    private String flightId;
    private Integer down500n;
    private String last1Down500Time;
    private Integer down0n;
    private String first1Down0Time;
    private String durationTime;
    // wxdMdc; //无线电高度最大下降率
    private String wxdMdcStartTime; //起始时刻
    private String wxdMdcEndTime; //截止时刻
    private Float wxdMdcDownRate; //下降率

    //qnhMdc; //QNH高度最大下降率
    private String qnhMdcStartTime; //起始时刻
    private String qnhMdcEndTime; //截止时刻
    private Integer qnhMdcDownRate; //下降率

    //heightMdc; //Height高度最大下降率
    private String heightMdcStartTime; //起始时刻
    private String heightMdcEndTime; //截止时刻
    private Integer heightMdcDownRate; //下降率

    private Integer downRateGt500n; //下降率超过500英尺每分钟-次数
    //downRateGt500Ld; //下降率超过500英尺每分钟-最长一次持续毫秒
    private String downRateGt500LdStartTime; //起始时刻
    private String downRateGt500LdEndTime; //截止时刻
    private Integer downRateGt500LdDurationSec; //持续毫秒

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

    public String getLast1Down500Time() {
        return last1Down500Time;
    }

    public void setLast1Down500Time(String last1Down500Time) {
        this.last1Down500Time = last1Down500Time;
    }

    public Integer getDown0n() {
        return down0n;
    }

    public void setDown0n(Integer down0n) {
        this.down0n = down0n;
    }

    public String getFirst1Down0Time() {
        return first1Down0Time;
    }

    public void setFirst1Down0Time(String first1Down0Time) {
        this.first1Down0Time = first1Down0Time;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public String getWxdMdcStartTime() {
        return wxdMdcStartTime;
    }

    public void setWxdMdcStartTime(String wxdMdcStartTime) {
        this.wxdMdcStartTime = wxdMdcStartTime;
    }

    public String getWxdMdcEndTime() {
        return wxdMdcEndTime;
    }

    public void setWxdMdcEndTime(String wxdMdcEndTime) {
        this.wxdMdcEndTime = wxdMdcEndTime;
    }

    public Float getWxdMdcDownRate() {
        return wxdMdcDownRate;
    }

    public void setWxdMdcDownRate(Float wxdMdcDownRate) {
        this.wxdMdcDownRate = wxdMdcDownRate;
    }

    public String getQnhMdcStartTime() {
        return qnhMdcStartTime;
    }

    public void setQnhMdcStartTime(String qnhMdcStartTime) {
        this.qnhMdcStartTime = qnhMdcStartTime;
    }

    public String getQnhMdcEndTime() {
        return qnhMdcEndTime;
    }

    public void setQnhMdcEndTime(String qnhMdcEndTime) {
        this.qnhMdcEndTime = qnhMdcEndTime;
    }

    public Integer getQnhMdcDownRate() {
        return qnhMdcDownRate;
    }

    public void setQnhMdcDownRate(Integer qnhMdcDownRate) {
        this.qnhMdcDownRate = qnhMdcDownRate;
    }

    public String getHeightMdcStartTime() {
        return heightMdcStartTime;
    }

    public void setHeightMdcStartTime(String heightMdcStartTime) {
        this.heightMdcStartTime = heightMdcStartTime;
    }

    public String getHeightMdcEndTime() {
        return heightMdcEndTime;
    }

    public void setHeightMdcEndTime(String heightMdcEndTime) {
        this.heightMdcEndTime = heightMdcEndTime;
    }

    public Integer getHeightMdcDownRate() {
        return heightMdcDownRate;
    }

    public void setHeightMdcDownRate(Integer heightMdcDownRate) {
        this.heightMdcDownRate = heightMdcDownRate;
    }

    public Integer getDownRateGt500n() {
        return downRateGt500n;
    }

    public void setDownRateGt500n(Integer downRateGt500n) {
        this.downRateGt500n = downRateGt500n;
    }

    public String getDownRateGt500LdStartTime() {
        return downRateGt500LdStartTime;
    }

    public void setDownRateGt500LdStartTime(String downRateGt500LdStartTime) {
        this.downRateGt500LdStartTime = downRateGt500LdStartTime;
    }

    public String getDownRateGt500LdEndTime() {
        return downRateGt500LdEndTime;
    }

    public void setDownRateGt500LdEndTime(String downRateGt500LdEndTime) {
        this.downRateGt500LdEndTime = downRateGt500LdEndTime;
    }

    public Integer getDownRateGt500LdDurationSec() {
        return downRateGt500LdDurationSec;
    }

    public void setDownRateGt500LdDurationSec(Integer downRateGt500LdDurationSec) {
        this.downRateGt500LdDurationSec = downRateGt500LdDurationSec;
    }
}
