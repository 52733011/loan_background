package com.xiaochong.loan.background.controller.manager;

import com.xiaochong.loan.background.entity.po.Field;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.FieldVo;
import com.xiaochong.loan.background.service.FieldService;
import com.xiaochong.loan.background.utils.ConstansUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "后台商户自定义表单")
@RestController
@RequestMapping("/back/field")
public class FieldController {

    private Logger logger = LoggerFactory.getLogger(FieldController.class);

    @Resource(name = "fieldService")
    private FieldService fieldService;


    @ApiOperation(value = "新增商户自定义表单",notes = "新增商户自定义表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fieldText", value = "表单中文title",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "fieldName", value = "名称",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "类型",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "fieldType", value = "表单类型（0-text  1-radio  2-checkbox 3-select  4-textarea ）",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "fieldDefaultValue", value = "默认值（对于下拉列表，可以用json来表达）",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "merchId", value = "商户id",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
    @PostMapping("/insert")
    public BaseResultVo<String> insertLoanField(@RequestParam("fieldText")String fieldText,
                                                @RequestParam("fieldName")String fieldName,
                                                @RequestParam("type")String type,
                                                @RequestParam("fieldType")String fieldType,
                                                @RequestParam("fieldDefaultValue")String fieldDefaultValue,
                                                @RequestParam("merchId")Integer merchId) {
        BaseResultVo<String> result = new BaseResultVo<String>();
        Field field = new Field();
        try {
            field.setFieldText(fieldText);
            field.setFieldName(fieldName);
            field.setType(type);
            field.setFieldType(fieldType);
            field.setFieldDefaultValue(fieldDefaultValue);
            field.setMerchId(merchId);
            BusinessVo<String> businessVo =fieldService.insertField(field);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("新增商户自定义表单失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        return result;
    }

    @ApiOperation(value = "查询商户自定义表单详情",notes = "查询商户自定义表单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id",required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
    @PostMapping("/selectById")
    public BaseResultVo<FieldVo> findLoanFieldByToid(@RequestParam("id")String id){
        BaseResultVo<FieldVo> result = new BaseResultVo<>();
        try {
            BusinessVo<FieldVo> businessVo =fieldService.findFieldById(id);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询商户自定义表单详情失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        return result;
    }

    @ApiOperation(value = "分页查询商户自定义表单",notes = "分页查询商户自定义表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchId", value = "商户id",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "当前页",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
    @PostMapping("/select")
    public BaseResultVo<BasePageInfoVo<FieldVo>> showLoanFieldByPage(@RequestParam("merchId")Integer merchId,
                                                                     @RequestParam("pageNum")Integer pageNum,
                                                                     @RequestParam("pageSize")Integer pageSize){
        BaseResultVo<BasePageInfoVo<FieldVo>> result = new BaseResultVo<>();
        Field field = new Field();
        field.setMerchId(merchId);
        try {
            BusinessVo<BasePageInfoVo<FieldVo>> businessVo=fieldService.selectField(field,pageNum,pageSize);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("分页查询商户自定义表单失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        return result;
    }

    @ApiOperation(value = "根据主键id修改商户自定义表单",notes = "根据主键id修改商户自定义表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fieldText", value = "表单中文title",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "fieldName", value = "名称",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "类型",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "fieldType", value = "表单类型（0-text  1-radio  2-checkbox 3-select  4-textarea ）",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "fieldDefaultValue", value = "默认值（对于下拉列表，可以用json来表达）",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "id", value = "主键id",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
    @PostMapping("/update")
    public BaseResultVo<String> updateLoanField(@RequestParam(name="fieldText",required = false)String fieldText,
                                                @RequestParam(name="fieldName",required = false)String fieldName,
                                                @RequestParam(name="type",required = false)String type,
                                                @RequestParam(name="fieldType",required = false)String fieldType,
                                                @RequestParam(name="fieldDefaultValue",required = false)String fieldDefaultValue,
                                                @RequestParam("id")Integer id){
        BaseResultVo<String> result = new BaseResultVo<String>();
        Field field = new Field();
        field.setId(id);
        field.setFieldText(fieldText);
        field.setFieldName(fieldName);
        field.setType(type);
        field.setFieldType(fieldType);
        field.setFieldDefaultValue(fieldDefaultValue);
        try {
            BusinessVo<String> businessVo=fieldService.updateField(field);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("根据主键id修改商户自定义表单失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        return result;
    }

    @ApiOperation(value = "根据主键id删除商户自定义表单",notes = "根据主键id删除商户自定义表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
    @PostMapping("/delete")
    public BaseResultVo<String> deleteLoanFieldByToid(@RequestParam("id")String id){
        BaseResultVo<String> result = new BaseResultVo<String>();
        try {
            BusinessVo<String> businessVo=fieldService.deleteField(id);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);

        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("根据主键id删除商户自定义表单失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        return result;
    }
}
