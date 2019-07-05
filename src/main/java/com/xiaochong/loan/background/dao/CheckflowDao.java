package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.Checkflow;
import com.xiaochong.loan.background.mapper.CheckflowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("checkflowDao")
public class CheckflowDao {

	@Autowired
	private CheckflowMapper checkflowMapper;

	/** 查询所有认证流程 */
	public List<Checkflow> selectLoanCheckflow(){
		return  checkflowMapper.selectLoanCheckflow();
	}

    public List<Checkflow> listByCheckflow(Checkflow checkflow) {
		return checkflowMapper.listByCheckflow(checkflow);
    }

	public Checkflow getByCheckflow(Checkflow checkflow) {
		return checkflowMapper.getByCheckflow(checkflow);
	}
}
