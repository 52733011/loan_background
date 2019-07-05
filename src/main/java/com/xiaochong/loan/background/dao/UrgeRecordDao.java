package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.UrgeRecord;
import com.xiaochong.loan.background.mapper.UrgeRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jinxin on 2017/10/24.
 */
@Repository
public class UrgeRecordDao {

    @Autowired
    private UrgeRecordMapper urgeRecordMapper;

    public List<UrgeRecord> selectByUrgeOverDueId(Integer urgeOverDueId){
        return urgeRecordMapper.selectByUrgeOverDueId(urgeOverDueId);
    }

    public void  insertUrgeRecord(UrgeRecord urgeRecord){
        urgeRecordMapper.insertSelective(urgeRecord);
    }

    public int selectCountByUrgeOverDueId(Integer urgeOverDueId) {
        return urgeRecordMapper.selectCountByUrgeOverDueId(urgeOverDueId);
    }
}
