package com.xiaochong.loan.background.mapper;


import com.xiaochong.loan.background.entity.po.MerchantinfoFlow;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantinfoFlowMapper {
	
	/**
	 * 查询商户认证流程
	 * @return
	 */
	MerchantinfoFlow getLoanMerchantinfoFlow(@Param("id") String id);

	List<MerchantinfoFlow> getByMerchId(@Param("merchId") String merchId);

	
	/**
	 * 插入商户认证流程
	 * @param merchantinfoFlow
	 */
	void insertLoanMerchantinfoFlow(MerchantinfoFlow merchantinfoFlow);
	
	/**
	 * 修改商户认证流程
	 * @param merchantinfoFlow
	 */
	void updateLoanMerchantinfoFlow(MerchantinfoFlow merchantinfoFlow);

	/**
	 * 修改商户认证流程
	 * @param merchantinfoFlow
	 */
	void updateLoanMerchantinfoFlowByMerchId(MerchantinfoFlow merchantinfoFlow);
	
	int deleteMerchantinfoFlowByMerchId(@Param("merchId") Integer merchId);

    List<MerchantinfoFlow> listByMerchantinfoFlow(MerchantinfoFlow merchantinfoFlow);
}
