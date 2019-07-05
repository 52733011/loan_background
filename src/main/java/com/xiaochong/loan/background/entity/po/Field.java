package com.xiaochong.loan.background.entity.po;

public class Field {

	private Integer id;
	
	private String fieldText;
	
	private String fieldName;
	
	private String type;
	
	private String fieldType;
	
	private String fieldDefaultValue;
	
	private Integer merchId;


    public String getFieldText() {
        return fieldText;
    }

    public void setFieldText(String fieldText) {
        this.fieldText = fieldText;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldDefaultValue() {
        return fieldDefaultValue;
    }

    public void setFieldDefaultValue(String fieldDefaultValue) {
        this.fieldDefaultValue = fieldDefaultValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    @Override
    public String toString() {
        return "LoanField{" +
                "id='" + id + '\'' +
                ", fieldText='" + fieldText + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", type='" + type + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", fieldDefaultValue='" + fieldDefaultValue + '\'' +
                ", merchId='" + merchId + '\'' +
                '}';
    }
}
