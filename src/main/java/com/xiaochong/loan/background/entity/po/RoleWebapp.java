package com.xiaochong.loan.background.entity.po;

import java.util.Date;

public class RoleWebapp {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column loan_role_webapp.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column loan_role_webapp.merch_id
     *
     * @mbggenerated
     */
    private Integer merchId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column loan_role_webapp.role_name
     *
     * @mbggenerated
     */
    private String roleName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column loan_role_webapp.role_remark
     *
     * @mbggenerated
     */
    private String roleRemark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column loan_role_webapp.status
     *
     * @mbggenerated
     */
    private String status = "1";

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column loan_role_webapp.updatetime
     *
     * @mbggenerated
     */
    private Date updatetime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column loan_role_webapp.createtime
     *
     * @mbggenerated
     */
    private Date createtime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column loan_role_webapp.id
     *
     * @return the value of loan_role_webapp.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column loan_role_webapp.id
     *
     * @param id the value for loan_role_webapp.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column loan_role_webapp.merch_id
     *
     * @return the value of loan_role_webapp.merch_id
     *
     * @mbggenerated
     */
    public Integer getMerchId() {
        return merchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column loan_role_webapp.merch_id
     *
     * @param merchId the value for loan_role_webapp.merch_id
     *
     * @mbggenerated
     */
    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column loan_role_webapp.role_name
     *
     * @return the value of loan_role_webapp.role_name
     *
     * @mbggenerated
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column loan_role_webapp.role_name
     *
     * @param roleName the value for loan_role_webapp.role_name
     *
     * @mbggenerated
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column loan_role_webapp.role_remark
     *
     * @return the value of loan_role_webapp.role_remark
     *
     * @mbggenerated
     */
    public String getRoleRemark() {
        return roleRemark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column loan_role_webapp.role_remark
     *
     * @param roleRemark the value for loan_role_webapp.role_remark
     *
     * @mbggenerated
     */
    public void setRoleRemark(String roleRemark) {
        this.roleRemark = roleRemark == null ? null : roleRemark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column loan_role_webapp.status
     *
     * @return the value of loan_role_webapp.status
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column loan_role_webapp.status
     *
     * @param status the value for loan_role_webapp.status
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column loan_role_webapp.updatetime
     *
     * @return the value of loan_role_webapp.updatetime
     *
     * @mbggenerated
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column loan_role_webapp.updatetime
     *
     * @param updatetime the value for loan_role_webapp.updatetime
     *
     * @mbggenerated
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column loan_role_webapp.createtime
     *
     * @return the value of loan_role_webapp.createtime
     *
     * @mbggenerated
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column loan_role_webapp.createtime
     *
     * @param createtime the value for loan_role_webapp.createtime
     *
     * @mbggenerated
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}