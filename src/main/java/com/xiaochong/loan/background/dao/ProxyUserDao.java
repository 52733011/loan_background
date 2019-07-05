package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.ProxyUser;
import com.xiaochong.loan.background.entity.vo.ProxyUserVo;
import com.xiaochong.loan.background.entity.vo.SubUserWebappVo;
import com.xiaochong.loan.background.mapper.ProxyUserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("proxyUserDao")
public class ProxyUserDao {

    @Resource
    private ProxyUserMapper proxyUserMapper;

	public ProxyUser getById(int id) {
	    return proxyUserMapper.selectByPrimaryKey(id);
	}

    public ProxyUser getByProxyUser(ProxyUser proxyUser) {
        return proxyUserMapper.getByProxyUser(proxyUser);
    }

    public List<ProxyUser> selectProxyUser(ProxyUser proxyUser) {
        return proxyUserMapper.selectProxyUser(proxyUser);
    }

    public int updateProxyUser(ProxyUser proxyUser) {
	    return proxyUserMapper.updateByPrimaryKeySelective(proxyUser);
    }

    public int insertProxyUser(ProxyUser proxyUser) {
	    if(proxyUser.getCreatetime()==null){
            proxyUser.setCreatetime(new Date());
        }
	    return proxyUserMapper.insertSelective(proxyUser);
    }

    public void updatePorxyUserStatus(Integer merchid, String type, String status) {
	    proxyUserMapper.updatePorxyUserStatus(merchid,type,status);
    }

    public List<SubUserWebappVo> pageProxyUserVo(Integer merchId, String isMaster) {
        return proxyUserMapper.pageProxyUserVo(merchId,isMaster);
    }

    public List<ProxyUser> listByProxyUser(ProxyUser proxyUser) {
        return proxyUserMapper.listByProxyUser(proxyUser);
    }

    public List<ProxyUserVo> proxyUserPage(Integer merchId, String searchStatus, String condition, String startTime, String endTime) {
        return proxyUserMapper.proxyUserPage(merchId,searchStatus,condition,startTime,endTime);
    }
}
