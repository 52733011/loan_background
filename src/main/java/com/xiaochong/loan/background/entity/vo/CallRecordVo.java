package com.xiaochong.loan.background.entity.vo;

public class CallRecordVo {

    private String phone;

    private String tag;

    private String location;

    private String frequency;

    private String duration;

    private String dialingCount;

    private String calledCount;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDialingCount() {
        return dialingCount;
    }

    public void setDialingCount(String dialingCount) {
        this.dialingCount = dialingCount;
    }

    public String getCalledCount() {
        return calledCount;
    }

    public void setCalledCount(String calledCount) {
        this.calledCount = calledCount;
    }

    @Override
    public String toString() {
        return "CallRecordVo{" +
                "phone='" + phone + '\'' +
                ", tag='" + tag + '\'' +
                ", location='" + location + '\'' +
                ", frequency='" + frequency + '\'' +
                ", duration='" + duration + '\'' +
                ", dialingCount='" + dialingCount + '\'' +
                ", calledCount='" + calledCount + '\'' +
                '}';
    }
}
