package com.xiaochong.loan.background.mapper;


import com.xiaochong.loan.background.entity.po.Checkflow;

import java.util.List;

public interface CheckflowMapper {

	/** 查询所有认证流程 */
	List<Checkflow> selectLoanCheckflow();

    List<Checkflow> listByCheckflow(Checkflow checkflow);

	Checkflow getByCheckflow(Checkflow checkflow);
}
