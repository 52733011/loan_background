package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ContactInfo;
import com.xiaochong.loan.background.mapper.ContactInfoMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("contactInfoDao")
public class ContactInfoDao {

    @Resource
    private ContactInfoMapper contactInfoMapper;

    public int insertOrUpdate(ContactInfo contactInfo) {
        ContactInfo info = contactInfo.getId()!=null
                ?contactInfoMapper.selectByPrimaryKey(contactInfo.getId()):null;
        if(info==null){
            return this.insert(contactInfo);
        }else{
            return this.update(contactInfo);
        }
    }

    private int update(ContactInfo contactInfo) {
        if(contactInfo.getUpdatetime()==null){
            contactInfo.setUpdatetime(new Date());
        }
        return contactInfoMapper.updateByPrimaryKeySelective(contactInfo);
    }

    public int insert(ContactInfo contactInfo) {
        if(contactInfo.getCreatetime()==null){
            contactInfo.setCreatetime(new Date());
        }
        return contactInfoMapper.insertSelective(contactInfo);
    }

    public List<ContactInfo> listByContactInfo(ContactInfo contactInfo) {
        return contactInfoMapper.listByContactInfo(contactInfo);
    }

    public ContactInfo getByContactInfo(ContactInfo contactInfo) {
        return contactInfoMapper.getByContactInfo(contactInfo);
    }
}
