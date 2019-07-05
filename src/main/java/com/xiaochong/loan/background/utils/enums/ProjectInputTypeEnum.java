package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/9.
 * 商户状态
 */
public enum ProjectInputTypeEnum {

    CONTENT("文字输入","1"),OTHER("其他资料（文件，视频，图片等需要上传的）","2");

    private String name;

    private String type;


    ProjectInputTypeEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
