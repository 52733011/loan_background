package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/9.
 * 商户状态
 */
public enum FileTypeEnum {

    PICTURE("图片资料","1"),DOC("文件资料","2"),VIDEO("视频资料","3"),CONTENT("文字输入","4");

    private String name;

    private String type;


    FileTypeEnum(String name, String type) {
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
