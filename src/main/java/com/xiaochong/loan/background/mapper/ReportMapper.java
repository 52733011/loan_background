package com.xiaochong.loan.background.mapper;

import com.xiaochong.loan.background.entity.po.Report;
import org.apache.ibatis.annotations.Param;

public interface ReportMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report
     *
     * @mbggenerated
     */
    int insert(Report record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report
     *
     * @mbggenerated
     */
    int insertSelective(Report record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report
     *
     * @mbggenerated
     */
    Report selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Report record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(Report record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_report
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Report record);


    Report selectByOrderNo(@Param("orderNo") String orderNo);
}