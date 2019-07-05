package com.xiaochong.loan.background.mapper;


import com.github.pagehelper.Page;
import com.xiaochong.loan.background.entity.po.Field;
import com.xiaochong.loan.background.entity.vo.FieldVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FieldMapper {
	
	/**
	 * 插入用户自定义表单
	 * @param field
	 */
	void insertLoanField(Field field);
	
	/**
	 * 根据商户id查询
	 * @param merid
	 * @return
	 */
	List<Field> findLoanFieldByMerid(String merid);

	/**
	 * 根据条件查询
	 * @return
	 */
	List<Field> selectLoandField(Field field);
	
	/**
	 * 根据toid查询
	 * @return
	 */
	Field findLoanFieldById(@Param("id") String id);

	/**
	 * 修改
	 * @param field
	 */
	void updateLoanField(Field field);
	
	/**
	 * 根据toid删除
	 * @param toid
	 */
	void deleteLoanFieldByToid(String toid);
	
	/**
	 * 根据商户id删除
	 * @param merid
	 */
	void deleteLoanFieldByMerid(String merid);


}
