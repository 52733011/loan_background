package com.xiaochong.loan.background.entity.vo;

import com.xiaochong.loan.background.entity.ExtendInfoBo;

import java.util.List;

public class WatchDetailsVo {


    private String bizCode;
    private String code;
    private String level;
    private String refreshTime;
    private String settlement;
    private String status;
    private String type;
    private String statement;
    private List<ExtendInfoBo> extendInfo;

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public List<ExtendInfoBo> getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(List<ExtendInfoBo> extendInfo) {
        this.extendInfo = extendInfo;
    }
}
