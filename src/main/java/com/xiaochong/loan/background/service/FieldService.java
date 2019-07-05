package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.dao.FieldDao;
import com.xiaochong.loan.background.entity.po.Field;
import com.xiaochong.loan.background.entity.po.Merchantinfo;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.FieldVo;
import com.xiaochong.loan.background.entity.vo.MerchantinfoVo;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinxin on 2017/8/11.
 */
@Service("fieldService")
public class FieldService extends BaseService{

    private Logger logger = LoggerFactory.getLogger(FieldService.class);


    @Resource(name = "fieldDao")
    private FieldDao fieldDao;


    /**
     * 添加用户自定义表单
     * @param field
     */
    public BusinessVo<String> insertField(Field field) {
        BusinessVo<String> businessVo= new BusinessVo<>();
        fieldDao.insertLoanField(field);
        businessVo.setData("添加表单成功");
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public BusinessVo<FieldVo> findFieldById(String id) {
        BusinessVo<FieldVo> businessVo= new BusinessVo<>();
        Field field = fieldDao.findLoanFieldById(id);
        FieldVo fieldVo = new FieldVo();
        fieldConvert(fieldVo,field);
        businessVo.setData(fieldVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }


    /**
     * field 转换类
     * @param fieldVo
     * @param field
     */
    private void fieldConvert(FieldVo fieldVo ,Field field){
        fieldVo.setId(field.getId());
        fieldVo.setFieldDefaultValue(field.getFieldDefaultValue());
        fieldVo.setFieldName(field.getFieldName());
        fieldVo.setFieldText(field.getFieldText());
        fieldVo.setFieldType(field.getFieldType());
        fieldVo.setMerchId(field.getMerchId());
        fieldVo.setType(field.getType());
    }

    /**
     * 分页查询
     * @param field
     * @param pageNum
     * @param pageSize
     * @return
     */
    public BusinessVo<BasePageInfoVo<FieldVo>> selectField(Field field, Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<FieldVo>> businessVo = new BusinessVo<>();
        PageHelper.startPage(pageNum, pageSize, true);
        Page<Field> page = (Page<Field>) fieldDao.selectLoandField(field);
        PageInfo<Field> fieldPageInfo = page.toPageInfo();
        BasePageInfoVo<FieldVo> basePageInfoVo = assemblyBasePageInfo(fieldPageInfo);
        List<FieldVo> fieldVoList = new ArrayList<>();
        List<Field> result = page.getResult();
        fieldListConvert(fieldVoList,result);
        basePageInfoVo.setResultList(fieldVoList);
        businessVo.setData(basePageInfoVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * field集合转换
     * @param fieldVoList
     * @param fieldList
     */
    private void fieldListConvert(List<FieldVo> fieldVoList,List<Field> fieldList){
            fieldList.forEach(field -> {
                FieldVo fieldVo = new FieldVo();
                fieldConvert(fieldVo,field);
                fieldVoList.add(fieldVo);
            });
    }

    /**
     * 根据主键修改
     * @param field
     * @return
     */
    public BusinessVo<String> updateField(Field field) {
        BusinessVo<String> businessVo= new BusinessVo<>();
        fieldDao.updateLoanField(field);
        businessVo.setData("更新成功");
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;

    }

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    public BusinessVo<String> deleteField(String id) {
        BusinessVo<String> businessVo= new BusinessVo<>();
        fieldDao.deleteLoanFieldByToid(id);
        businessVo.setData("删除成功");
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }
}
