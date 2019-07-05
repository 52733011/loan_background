package com.xiaochong.loan.background.dao;


import com.github.pagehelper.Page;
import com.xiaochong.loan.background.entity.po.Field;
import com.xiaochong.loan.background.entity.vo.FieldVo;
import com.xiaochong.loan.background.mapper.FieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("fieldDao")
public class FieldDao {

	@Autowired
	private FieldMapper fieldMapper;

	/**
	 * 插入用户自定义表单
	 * @param field
	 */
	public void insertLoanField(Field field){
		fieldMapper.insertLoanField(field);
	};
	
	/**
	 * 根据商户id查询
	 * @param merid
	 * @return
	 */
    public List<Field> findLoanFieldByMerid(String merid){
		return  fieldMapper.findLoanFieldByMerid(merid);
	}

	/**
	 * 根据条件查询
	 * @return
	 */
    public List<Field> selectLoandField(Field loanField){
		return fieldMapper.selectLoandField(loanField);
	}
	
	/**
	 * 根据toid查询
	 * @return
	 */
    public Field findLoanFieldById(String id){
		return  fieldMapper.findLoanFieldById(id);
	}
	
	/**
	 * 修改
	 * @param loanField
	 */
    public void updateLoanField(Field loanField){
		fieldMapper.updateLoanField(loanField);
	}
	
	/**
	 * 根据toid删除
	 */
    public void deleteLoanFieldByToid(String id){
		fieldMapper.deleteLoanFieldByToid(id);
	}
	
	/**
	 * 根据商户id删除
	 * @param merid
	 */
    public void deleteLoanFieldByMerid(String merid){
		fieldMapper.deleteLoanFieldByMerid(merid);
	}


}
