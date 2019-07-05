package com.xiaochong.loan.background.entity.vo;

/**
 * Created by jinxin on 2017/8/16.
 * 学信网个人信息返回
 */
public class EducationDataVo {
    private String name;      //姓名
    private String sex;        //性别
    private String birth;      //出生年月日
    private String nation;      //民族
    private String school;      //毕业院校
    private String degree;      //学历
    private String profession;    //所学专业
    private String department;    //系（所、函授站）
    private String inSchool;    //入学时间
    private String outSchool;    //离校时间
    private String studyStyl;    //学习形式
    private String lengthStudy;    //学制
    private String departCourt;    //分院
    private String grade;      //班级
    private String status;      //学籍状态
    private String cardId;      //证件号码
    private String degreeType;    //学历类别
    private String captcha;      //返回验证码Base64字符串表示

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInSchool() {
        return inSchool;
    }

    public void setInSchool(String inSchool) {
        this.inSchool = inSchool;
    }

    public String getOutSchool() {
        return outSchool;
    }

    public void setOutSchool(String outSchool) {
        this.outSchool = outSchool;
    }

    public String getStudyStyl() {
        return studyStyl;
    }

    public void setStudyStyl(String studyStyl) {
        this.studyStyl = studyStyl;
    }

    public String getLengthStudy() {
        return lengthStudy;
    }

    public void setLengthStudy(String lengthStudy) {
        this.lengthStudy = lengthStudy;
    }

    public String getDepartCourt() {
        return departCourt;
    }

    public void setDepartCourt(String departCourt) {
        this.departCourt = departCourt;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(String degreeType) {
        this.degreeType = degreeType;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Override
    public String toString() {
        return "EducationData{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birth='" + birth + '\'' +
                ", nation='" + nation + '\'' +
                ", school='" + school + '\'' +
                ", degree='" + degree + '\'' +
                ", profession='" + profession + '\'' +
                ", department='" + department + '\'' +
                ", inSchool='" + inSchool + '\'' +
                ", outSchool='" + outSchool + '\'' +
                ", studyStyl='" + studyStyl + '\'' +
                ", lengthStudy='" + lengthStudy + '\'' +
                ", departCourt='" + departCourt + '\'' +
                ", grade='" + grade + '\'' +
                ", status='" + status + '\'' +
                ", cardId='" + cardId + '\'' +
                ", degreeType='" + degreeType + '\'' +
                ", captcha='" + captcha + '\'' +
                '}';
    }
}
