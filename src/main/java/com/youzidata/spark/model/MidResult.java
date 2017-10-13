package com.youzidata.spark.model;


/**
 * Created by MT-T450 on 2017/6/7.
 * 航班中间表 结果返回
 */
public class MidResult {
    private String flightId;

    //无线电高度
    private String wxdFhStartTime; //起始时刻
    private String wxdFhEndTime; //截止时刻
    private Integer wxdFhDownRate; //下降率
    private String wxdFhAvgStartTime; //起始时刻
    private String wxdFhAvgEndTime; //截止时刻
    private Integer wxdFhAvgDownRate; //下降率（平均）
    //QNH高度
    private String qnhFhStartTime; //起始时刻
    private String qnhFhEndTime; //截止时刻
    private Integer qnhFhDownRate; //下降率
    private String qnhFhAvgStartTime; //起始时刻
    private String qnhFhAvgEndTime; //截止时刻
    private Integer qnhFhAvgDownRate; //下降率（平均）
    //Height高度
    private String heightFhStartTime; //起始时刻
    private String heightFhEndTime; //截止时刻
    private Integer heightFhDownRate; //下降率
    private String heightFhAvgStartTime; //起始时刻
    private String heightFhAvgEndTime; //截止时刻
    private Integer heightFhAvgDownRate; //下降率（平均）

    private Boolean wxdCond;
    private Boolean qnhCond;
    private Boolean heightCond;
    private Boolean multiCond;
    private Integer durationSec;

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getWxdFhStartTime() {
        return wxdFhStartTime;
    }

    public void setWxdFhStartTime(String wxdFhStartTime) {
        this.wxdFhStartTime = wxdFhStartTime;
    }

    public String getWxdFhEndTime() {
        return wxdFhEndTime;
    }

    public void setWxdFhEndTime(String wxdFhEndTime) {
        this.wxdFhEndTime = wxdFhEndTime;
    }

    public Integer getWxdFhDownRate() {
        return wxdFhDownRate;
    }

    public void setWxdFhDownRate(Integer wxdFhDownRate) {
        this.wxdFhDownRate = wxdFhDownRate;
    }

    public String getWxdFhAvgStartTime() {
        return wxdFhAvgStartTime;
    }

    public void setWxdFhAvgStartTime(String wxdFhAvgStartTime) {
        this.wxdFhAvgStartTime = wxdFhAvgStartTime;
    }

    public String getWxdFhAvgEndTime() {
        return wxdFhAvgEndTime;
    }

    public void setWxdFhAvgEndTime(String wxdFhAvgEndTime) {
        this.wxdFhAvgEndTime = wxdFhAvgEndTime;
    }

    public Integer getWxdFhAvgDownRate() {
        return wxdFhAvgDownRate;
    }

    public void setWxdFhAvgDownRate(Integer wxdFhAvgDownRate) {
        this.wxdFhAvgDownRate = wxdFhAvgDownRate;
    }

    public String getQnhFhStartTime() {
        return qnhFhStartTime;
    }

    public void setQnhFhStartTime(String qnhFhStartTime) {
        this.qnhFhStartTime = qnhFhStartTime;
    }

    public String getQnhFhEndTime() {
        return qnhFhEndTime;
    }

    public void setQnhFhEndTime(String qnhFhEndTime) {
        this.qnhFhEndTime = qnhFhEndTime;
    }

    public Integer getQnhFhDownRate() {
        return qnhFhDownRate;
    }

    public void setQnhFhDownRate(Integer qnhFhDownRate) {
        this.qnhFhDownRate = qnhFhDownRate;
    }

    public String getQnhFhAvgStartTime() {
        return qnhFhAvgStartTime;
    }

    public void setQnhFhAvgStartTime(String qnhFhAvgStartTime) {
        this.qnhFhAvgStartTime = qnhFhAvgStartTime;
    }

    public String getQnhFhAvgEndTime() {
        return qnhFhAvgEndTime;
    }

    public void setQnhFhAvgEndTime(String qnhFhAvgEndTime) {
        this.qnhFhAvgEndTime = qnhFhAvgEndTime;
    }

    public Integer getQnhFhAvgDownRate() {
        return qnhFhAvgDownRate;
    }

    public void setQnhFhAvgDownRate(Integer qnhFhAvgDownRate) {
        this.qnhFhAvgDownRate = qnhFhAvgDownRate;
    }

    public String getHeightFhStartTime() {
        return heightFhStartTime;
    }

    public void setHeightFhStartTime(String heightFhStartTime) {
        this.heightFhStartTime = heightFhStartTime;
    }

    public String getHeightFhEndTime() {
        return heightFhEndTime;
    }

    public void setHeightFhEndTime(String heightFhEndTime) {
        this.heightFhEndTime = heightFhEndTime;
    }

    public Integer getHeightFhDownRate() {
        return heightFhDownRate;
    }

    public void setHeightFhDownRate(Integer heightFhDownRate) {
        this.heightFhDownRate = heightFhDownRate;
    }

    public String getHeightFhAvgStartTime() {
        return heightFhAvgStartTime;
    }

    public void setHeightFhAvgStartTime(String heightFhAvgStartTime) {
        this.heightFhAvgStartTime = heightFhAvgStartTime;
    }

    public String getHeightFhAvgEndTime() {
        return heightFhAvgEndTime;
    }

    public void setHeightFhAvgEndTime(String heightFhAvgEndTime) {
        this.heightFhAvgEndTime = heightFhAvgEndTime;
    }

    public Integer getHeightFhAvgDownRate() {
        return heightFhAvgDownRate;
    }

    public void setHeightFhAvgDownRate(Integer heightFhAvgDownRate) {
        this.heightFhAvgDownRate = heightFhAvgDownRate;
    }

    public Boolean getWxdCond() {
        return wxdCond;
    }

    public void setWxdCond(Boolean wxdCond) {
        this.wxdCond = wxdCond;
    }

    public Boolean getQnhCond() {
        return qnhCond;
    }

    public void setQnhCond(Boolean qnhCond) {
        this.qnhCond = qnhCond;
    }

    public Boolean getHeightCond() {
        return heightCond;
    }

    public void setHeightCond(Boolean heightCond) {
        this.heightCond = heightCond;
    }

    public Boolean getMultiCond() {
        return multiCond;
    }

    public void setMultiCond(Boolean multiCond) {
        this.multiCond = multiCond;
    }

    public Integer getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(Integer durationSec) {
        this.durationSec = durationSec;
    }
}
