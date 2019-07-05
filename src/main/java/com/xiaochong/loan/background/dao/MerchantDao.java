package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.Merchantinfo;
import com.xiaochong.loan.background.entity.vo.MerchantNameVo;
import com.xiaochong.loan.background.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("merchantDao")
public class MerchantDao {


	@Autowired
	private MerchantMapper merchantMapper;

	/**
	 * 根据toid查询
	 * @return
	 */
	public Merchantinfo findMerchantinfoByToid(Integer id){
		return merchantMapper.findMerchantinfoByToid(id);
	}

	/**
	 * 根据条件查询
	 * @return
	 */
	public List<Merchantinfo> selectMerchantinfo(Merchantinfo merchantinfo){
		return merchantMapper.selectMerchantinfo(merchantinfo);
	}
	/**
	 * 新建商户
	 */
	public int insertMerchant(Merchantinfo loanMerchantinfo){
		return merchantMapper.insertSelective(loanMerchantinfo);
	}
	
	/**
	 * 删除商户
	 */
	public void deleteMerchant(String id){
		merchantMapper.deleteMerchant(id);
	}
	
	/**
	 * 修改商户
	 * @param info
	 */
	public void updateMerchant(Merchantinfo info){
		merchantMapper.updateByPrimaryKeySelective(info);

	}

    public List<MerchantNameVo> getAllMerchantName(String merchName) {
	    return merchantMapper.getAllMerchantName(merchName);
    }
}
