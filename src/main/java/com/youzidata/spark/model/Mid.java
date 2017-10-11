package com.youzidata.spark.model;

import java.util.Date;

/**
 * Created by MT-T450 on 2017/6/7.
 * 航班中间表
 */
public class Mid {
    private String flightId;
    private FH wxdFh;
    private FH qnhFh;
    private FH heightFh;
    private Boolean wxdCond;
    private Boolean qnhCond;
    private Boolean heightCond;
    private Boolean multiCond;
    private Integer durationSec;

    //航班高度
    public static class FH {
        private Date time;
        private Integer height;
        private Sample sample1;
        private Sample sample2;

        public FH() {
            this.sample1 = new Sample();
            this.sample2 = new Sample();
        }

        public FH(Date time, Integer height, Sample sample1, Sample sample2) {
            this.time = time;
            this.height = height;
            this.sample1 = sample1;
            this.sample2 = sample2;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Sample getSample1() {
            return sample1;
        }

        public void setSample1(Sample sample1) {
            this.sample1 = sample1;
        }

        public Sample getSample2() {
            return sample2;
        }

        public void setSample2(Sample sample2) {
            this.sample2 = sample2;
        }
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public FH getWxdFh() {
        return wxdFh;
    }

    public void setWxdFh(FH wxdFh) {
        this.wxdFh = wxdFh;
    }

    public FH getQnhFh() {
        return qnhFh;
    }

    public void setQnhFh(FH qnhFh) {
        this.qnhFh = qnhFh;
    }

    public FH getHeightFh() {
        return heightFh;
    }

    public void setHeightFh(FH heightFh) {
        this.heightFh = heightFh;
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
