package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.MerchantinfoFlow;
import com.xiaochong.loan.background.mapper.MerchantinfoFlowMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("merchantinfoFlowDao")
public class MerchantinfoFlowDao {

	@Autowired
	private MerchantinfoFlowMapper merchantinfoFlowMapper;
	
	/**
	 * 查询商户认证流程
	 * @return
	 */
	public MerchantinfoFlow getLoanMerchantinfoFlow(String id) {
		return merchantinfoFlowMapper.getLoanMerchantinfoFlow(id);
	}

	public List<MerchantinfoFlow> getByMerchId(Integer merchId) {
		return merchantinfoFlowMapper.getByMerchId(merchId.toString());
	}
	
	
	/**
	 * 插入商户认证流程
	 * @param merchantinfoFlow
	 */
    public void insertLoanMerchantinfoFlow(MerchantinfoFlow merchantinfoFlow){
		merchantinfoFlowMapper.insertLoanMerchantinfoFlow(merchantinfoFlow);
	}
	
	/**
	 * 修改商户认证流程
	 * @param merchantinfoFlow
	 */
    public void updateLoanMerchantinfoFlow(MerchantinfoFlow merchantinfoFlow){
		merchantinfoFlowMapper.updateLoanMerchantinfoFlow(merchantinfoFlow);
	}

		/**
	 * 修改商户认证流程
	 * @param merchantinfoFlow
	 */
        public void updateLoanMerchantinfoFlowByMerchId(MerchantinfoFlow merchantinfoFlow){
		merchantinfoFlowMapper.updateLoanMerchantinfoFlowByMerchId(merchantinfoFlow);
	}


    public int deleteLoanMerchantinfoFlow(Integer id){
		return merchantinfoFlowMapper.deleteMerchantinfoFlowByMerchId(id);
	}
}
