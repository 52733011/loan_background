package com.xiaochong.loan.background.mapper;

import com.xiaochong.loan.background.entity.po.Report;
import com.xiaochong.loan.background.entity.po.ReportEducationInfo;
import com.xiaochong.loan.background.entity.po.ReportUserInfo;
import org.apache.ibatis.annotations.Param;

public interface ReportEducationInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report_education_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report_education_info
     *
     * @mbggenerated
     */
    int insert(ReportEducationInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report_education_info
     *
     * @mbggenerated
     */
    int insertSelective(ReportEducationInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report_education_info
     *
     * @mbggenerated
     */
    ReportEducationInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report_education_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(ReportEducationInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report_education_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(ReportEducationInfo record);


    /**
     * 查询字段值
     * @param orderNum 订单编号
     * @param filedName 字段名称
     * @return 报告详情
     */
    ReportEducationInfo selectReportUserByOrderNumAndFiledName(@Param("orderNum")String orderNum, @Param("filedName")String filedName);


    /**
     *
     * @param orderNum
     * @return
     */
    ReportEducationInfo selectReportUserByOrderNum(@Param("orderNum")String orderNum);
}