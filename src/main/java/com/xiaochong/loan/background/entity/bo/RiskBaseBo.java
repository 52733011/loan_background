package com.xiaochong.loan.background.entity.bo;

/**
 * Created by ray.liu on 2017/4/19.
 */
public class RiskBaseBo {


    private String code;
    private String message;
    private String data;
    private long dataStamp;
    private String traceId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getDataStamp() {
        return dataStamp;
    }

    public void setDataStamp(long dataStamp) {
        this.dataStamp = dataStamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "TongdunEducationResultBo{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", dataStamp=" + dataStamp +
                ", traceId=" + traceId +
                '}';
    }
}
