package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FieldVo {

    @ApiModelProperty(value = "主键")
	private Integer id;
    @ApiModelProperty(value = "表单中文title")
	private String fieldText;
    @ApiModelProperty(value = "表单名称")
	private String fieldName;
    @ApiModelProperty(value = "类型")
	private String type;
    @ApiModelProperty(value = "表单类型：0-text  1-radio  2-checkbox 3-select  4-textarea ")
	private String fieldType;
    @ApiModelProperty(value = "默认值，对于下拉列表，可以用json来表达")
	private String fieldDefaultValue;
    @ApiModelProperty(value = "商户id")
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

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
