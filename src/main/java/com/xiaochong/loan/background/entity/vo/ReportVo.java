package com.xiaochong.loan.background.entity.vo;

public class ReportVo<T> {

    private String optionType;

    private Boolean isTable;

    private Integer tableType;

    private T optionData;

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public Boolean getTable() {
        return isTable;
    }

    public void setTable(Boolean table) {
        isTable = table;
    }

    public Integer getTableType() {
        return tableType;
    }

    public void setTableType(Integer tableType) {
        this.tableType = tableType;
    }

    public T getOptionData() {
        return optionData;
    }

    public void setOptionData(T optionData) {
        this.optionData = optionData;
    }

    @Override
    public String toString() {
        return "ReportVo{" +
                "optionType='" + optionType + '\'' +
                ", isTable=" + isTable +
                ", tableType=" + tableType +
                ", optionData=" + optionData +
                '}';
    }
}
