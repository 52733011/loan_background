package com.xiaochong.loan.background.mapper;

import com.xiaochong.loan.background.entity.po.AccountRecord;

import java.util.List;

public interface AccountRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_account_record
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_account_record
     *
     * @mbggenerated
     */
    int insert(AccountRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_account_record
     *
     * @mbggenerated
     */
    int insertSelective(AccountRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_account_record
     *
     * @mbggenerated
     */
    AccountRecord selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_account_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(AccountRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_account_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(AccountRecord record);

    List<AccountRecord> selectAccountRecord(AccountRecord accountRecord);

    int getCountForAccountRecord(AccountRecord accountRecord);

    void updateAccountRecordByOrderNo(AccountRecord accountRecord);
}