package com.xiaochong.loan.background.entity.bo;

/**
 * Created by ray.liu on 2017/4/19.
 */
public class TongdunEducationDataBo {

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 姓名
     */
    private String name;

    /**
     * 毕业院校
     */
    private String graduate;

    /**
     * 学历
     */
    private String educationDegree;

    /**
     * 入学年份
     */
    private String enrolDate;

    /**
     * 专业
     */
    private String specialityName;

    /**
     * 毕业时间
     */
    private String graduateTime;

    /**
     * 毕业结论
     */
    private String studyResult;

    /**
     * 学历类型
     */
    private String studyStyle;

    /**
     * 照片
     */
    private String photo;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGraduate() {
        return graduate;
    }

    public void setGraduate(String graduate) {
        this.graduate = graduate;
    }

    public String getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(String educationDegree) {
        this.educationDegree = educationDegree;
    }

    public String getEnrolDate() {
        return enrolDate;
    }

    public void setEnrolDate(String enrolDate) {
        this.enrolDate = enrolDate;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public String getGraduateTime() {
        return graduateTime;
    }

    public void setGraduateTime(String graduateTime) {
        this.graduateTime = graduateTime;
    }

    public String getStudyResult() {
        return studyResult;
    }

    public void setStudyResult(String studyResult) {
        this.studyResult = studyResult;
    }

    public String getStudyStyle() {
        return studyStyle;
    }

    public void setStudyStyle(String studyStyle) {
        this.studyStyle = studyStyle;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "TongdunEducationDataBo{" +
                "idCard='" + idCard + '\'' +
                ", name='" + name + '\'' +
                ", graduate='" + graduate + '\'' +
                ", educationDegree='" + educationDegree + '\'' +
                ", enrolDate='" + enrolDate + '\'' +
                ", specialityName='" + specialityName + '\'' +
                ", graduateTime='" + graduateTime + '\'' +
                ", studyResult='" + studyResult + '\'' +
                ", studyStyle='" + studyStyle + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
