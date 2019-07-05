package com.xiaochong.loan.background.entity.vo;

import java.util.List;

public class ReportTableRemarkVo {

    private String name;

    private String title;

    private String value;

    private List<String> remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getRemark() {
        return remark;
    }

    public void setRemark(List<String> remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ReportTableRemarkVo{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", value='" + value + '\'' +
                ", remark=" + remark +
                '}';
    }
}
