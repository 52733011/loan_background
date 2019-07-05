package com.xiaochong.loan.background.mapper;


import com.github.pagehelper.Page;
import com.xiaochong.loan.background.entity.po.Merchantinfo;
import com.xiaochong.loan.background.entity.vo.MerchantNameVo;
import com.xiaochong.loan.background.entity.vo.MerchantinfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantMapper {
	
	/**
	 * 根据toid查询
	 * @return
	 */
	Merchantinfo findMerchantinfoByToid(@Param("id") Integer id);

	/**
	 * 条件
	 * @return
	 */
	List<Merchantinfo>  selectMerchantinfo (Merchantinfo merchantinfo);
	/**
	 * 新建商户
	 */
	void insertMerchant(Merchantinfo merchantinfo);
	
	/**
	 * 分页查询
	 * @return
	 */
	List<MerchantinfoVo> selectMerchantByPage(Merchantinfo merchantinfo);
	
	/**
	 * 查询总数
	 * @param page
	 * @return
	 */
	int getCountByPage(Page<Merchantinfo> page);
	
	/**
	 * 删除商户
	 * @param toid
	 */
	void deleteMerchant(String toid);
	
	/**
	 * 修改商户
	 * @param info
	 */
	void updateMerchant(Merchantinfo info);

	int updateByPrimaryKeySelective(Merchantinfo info);

    List<MerchantNameVo> getAllMerchantName(@Param("merchantName") String merchantName);

    int insertSelective(Merchantinfo loanMerchantinfo);
}
