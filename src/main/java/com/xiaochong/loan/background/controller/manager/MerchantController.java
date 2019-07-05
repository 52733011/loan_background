package com.xiaochong.loan.background.controller.manager;


import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.component.OssComponent;
import com.xiaochong.loan.background.entity.po.Merchantinfo;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.exception.FileException;
import com.xiaochong.loan.background.service.MerchantService;
import com.xiaochong.loan.background.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 商户管理
 * @author lhx
 *
 */
@Api(value = "后台商户管理")
@RestController
@RequestMapping("/back/merchant")
public class MerchantController {

	@Resource(name = "merchantService")
	private MerchantService merchantService;


    @Resource(name = "component.OssComponent")
    private OssComponent ossComponent;

	private Logger logger = LoggerFactory.getLogger(MerchantController.class);

	@ApiOperation(value = "新增商户信息",notes = "新增商户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantName", value = "商户名称",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "shortName", value = "商户简称",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "enName", value = "商户英文简称",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "linkMan", value = "联系人",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "linkMobile", value = "联系电话",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "linkEmail", value = "联系邮箱",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "address", value = "地址",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "scale", value = "公司规模",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "industry", value = "行业",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "cooperantBeginTime", value = "合作开始时间",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "cooperantStopTime", value = "合作结束时间",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "auditType", value = "审核方式  1 商户审核   0  小虫审核  3不审核",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pwd", value = "默认初始密码",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "certificate", value = "上传的文件",required=true, dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "agreement", value = "上传的文件",required=true, dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "menuIds", value = "权限id,用逗号隔开", required = false, dataType = "String", paramType = "form"),
    })
	@PostMapping("/addMerchantInfo")
	public BaseResultVo addMerchantInfo(
            @RequestParam("token") String token,
            @RequestParam("merchantName") String merchantName,
            @RequestParam("shortName") String shortName,
            @RequestParam("enName") String enName,
            @RequestParam("linkMan") String linkMan,
            @RequestParam("linkMobile") String linkMobile,
            @RequestParam("linkEmail") String linkEmail,
            @RequestParam(value = "address",required = false) String address,
            @RequestParam(value = "scale",required = false) String scale,
            @RequestParam(value = "industry",required = false) String industry,
			@RequestParam("cooperantBeginTime") String cooperantBeginTime,
			@RequestParam("cooperantStopTime") String cooperantStopTime,
			@RequestParam("pwd") String pwd,
			@RequestParam("auditType") String auditType,
			@RequestParam(value = "certificate",required = false) MultipartFile certificate,
			@RequestParam(value = "agreement",required = false) MultipartFile agreement,
            @RequestParam(value = "menuIds",required = false)String menuIds
			){
             BaseResultVo<String> result = new BaseResultVo<>();
        logger.info("新增商户信息,token:{},merchantName:{},shortName:{},enName:{},linkMan:{},linkMobile:{},linkEmail:{}" +
                ",address:{},scale:{},industry:{},auditType{},cooperantBeginTime:{},cooperantStopTime:{},pwd:{},certificate:{},agreement:{},resourcesIds:{}",token,merchantName,shortName
        ,enName,linkMan,linkMobile,linkEmail,address,scale,industry,auditType,cooperantBeginTime,cooperantStopTime,pwd,certificate,agreement,menuIds);
        try {
             BusinessVo<String> businessVo = merchantService.insertMerchant(token,
                     merchantName, shortName, enName, linkMan, linkMobile, linkEmail,
                     address, scale, industry, cooperantBeginTime, cooperantStopTime, pwd,
					 auditType, certificate, agreement, menuIds);
             result.setResult(businessVo);
             result.setCode(ResultConstansUtil.SUCCESS_CODE);
             result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
             result.setCode(ResultConstansUtil.MERCH_REGISTERED_ERROR_CODE);
             result.setMessage(ResultConstansUtil.MERCH_REGISTERED_ERROR_DESC);
             logger.error("商户名称:{},新增商户失败:{}",merchantName,e);
             e.printStackTrace();
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
		 return result;
	}


	@ApiOperation(value = "通过商户名称模糊分页查询",notes = "通过商户名称和简称模糊分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantName", value = "商户名称",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "shortName", value = "商户简称",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "查询 开始时间",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "查询 结束时间",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "当前页",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
	@PostMapping("/showLoanMerchantinfos")
	public BaseResultVo showLoanMerchantinfos(@RequestParam(name="merchantName",required = false) String merchantName,
                                              @RequestParam(name="shortName",required = false) String shortName,
                                              @RequestParam(name="startTime",required = false) String startTime,
                                              @RequestParam(name="endTime",required = false) String endTime,
                                              @RequestParam(name="pageNum",required = true) Integer pageNum,
                                              @RequestParam(name="pageSize",required = true) Integer pageSize){
		BaseResultVo baseResultVo = new BaseResultVo();
		try {
			Merchantinfo merchantinfo = new Merchantinfo();
            merchantinfo.setMerchantName(merchantName);
            merchantinfo.setShortName(shortName);
            merchantinfo.setStartTime(DateUtils.stringToDate(startTime,DateUtils.yyyyMMdd_format));
            merchantinfo.setEndTime(DateUtils.stringToDate(endTime,DateUtils.yyyyMMdd_format));
            BusinessVo<BasePageInfoVo<MerchantinfoVo>> basePageInfoVoBusinessVo = merchantService.selectMerchantByPage(merchantinfo, pageNum, pageSize);
			baseResultVo.setResult(basePageInfoVoBusinessVo);
			baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
			baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
			baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
			baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
			logger.error("分页查询失败！",e);
		}
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
		return baseResultVo;
	}

	@ApiOperation(value = "根据id查询商户信息",notes = "根据id查询商户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商户id",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "Integer", paramType = "form")
    })
	@PostMapping("/getLoanMerchantinfo")
	public BaseResultVo<MerchantinfoVo> getLoanMerchantinfo(@RequestParam("id") Integer id) {
		BaseResultVo<MerchantinfoVo> result = new BaseResultVo<>();
        logger.info("根据id查询商户信息,id:{}",id);
        try {
			BusinessVo<MerchantinfoVo> merchantinfoByToid = merchantService.findMerchantinfoByToid(id);
			result.setResult(merchantinfoByToid);
			result.setCode(ResultConstansUtil.SUCCESS_CODE);
			result.setMessage(ResultConstansUtil.SUCCESS_DESC);
		} catch (Exception e) {
			result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
			result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
			logger.error("查询详情失败！",e);
		}
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
		return result;
	}

	@ApiOperation(value = "根据id删除商户信息",notes = "根据id删除商户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商户id",required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
	@PostMapping("/deleteMerchantinfo")
	public BaseResultVo deleteMerchantinfo(@RequestParam("id") String id){
		BaseResultVo result = new BaseResultVo();
        logger.info("新增商户信息,id:{}",id);
		try {
            merchantService.deleteMerchant(id);
			result.setCode(ResultConstansUtil.SUCCESS_CODE);
			result.setMessage(ResultConstansUtil.SUCCESS_DESC);
		} catch (Exception e) {
			result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
			result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
			logger.error("删除商户失败！",e);
		}
		result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
		return result;
	}

	@ApiOperation(value = "更改商户状态接口",notes = "根据商户id与status修改商户状态,直接修改为传过来的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商户id",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "商户状态，0-启用 1-停用",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
	@PostMapping("/updateMerchantstatus")
	public BaseResultVo updateMerchantstatus(@RequestParam("id") Integer id,
                                                 @RequestParam("status") String status) {
		BaseResultVo result = new BaseResultVo();
        logger.info("新增商户信息,id:{},status:{}",id,status);
		try {
            BusinessVo<String> businessVo=merchantService.updateStatus(id,status);
            result.setResult(businessVo);
			result.setCode(ResultConstansUtil.SUCCESS_CODE);
			result.setMessage(ResultConstansUtil.SUCCESS_DESC);
		} catch (Exception e) {
			result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
			result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
			logger.error("删除商户失败！",e);
		}
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
		return result;
	}

    @ApiOperation(value = "修改商户信息",notes = "修改商户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "merchantName", value = "商户名称",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "shortName", value = "商户简称",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "enName", value = "商户英文简称",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "linkMan", value = "联系人",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "linkMobile", value = "联系电话",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "linkEmail", value = "联系邮箱",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "address", value = "地址",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "scale", value = "公司规模",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "industry", value = "行业",required=false, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "auditType", value = "审核方式  1 商户审核   0  小虫审核  3不审核",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "cooperantBeginTime", value = "合作开始时间",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "cooperantStopTime", value = "合作结束时间",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "certificate", value = "上传的文件",required=false, dataType = "file", paramType = "formData"),
            @ApiImplicitParam(name = "agreement", value = "上传的文件",required=false, dataType = "file", paramType = "formData"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "menuIds", value = "权限id,用逗号隔开", required = false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/updateMerchantInfo")
    public BaseResultVo updateMerchantInfo(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "id") Integer id,
            @RequestParam(required = false,value = "merchantName") String merchantName,
            @RequestParam(required = false,value = "shortName") String shortName,
            @RequestParam(required = false,value = "enName") String enName,
            @RequestParam(required = false,value = "linkMan") String linkMan,
            @RequestParam(required = false,value = "linkMobile") String linkMobile,
            @RequestParam(required = false,value = "linkEmail") String linkEmail,
            @RequestParam(required = false,value = "address") String address,
            @RequestParam(required = false,value = "scale") String scale,
            @RequestParam(required = false,value = "industry") String industry,
			@RequestParam(required = false,value = "auditType") String auditType,
            @RequestParam(required = false,value = "cooperantBeginTime") String cooperantBeginTime,
            @RequestParam(required = false,value = "cooperantStopTime") String cooperantStopTime,
            @RequestParam(value = "certificate",required = false) MultipartFile certificate,
            @RequestParam(value = "agreement",required = false) MultipartFile agreement,
            @RequestParam(value = "menuIds",required = false)String menuIds){
		BaseResultVo<String> result = new BaseResultVo<>();
        logger.info("修改商户信息,merchantName:{},shortName:{},enName:{},linkMan:{},linkMobile:{},linkEmail:{}" +
                        ",address:{},scale:{},industry:{},auditType{},cooperantBeginTime:{},cooperantStopTime:{},pwd:{}," +
						"certificate:{},agreement:{},resourcesIds:{}",
                merchantName,shortName,enName,linkMan,linkMobile,linkEmail,address,scale,industry,auditType,cooperantBeginTime,
                cooperantStopTime,certificate,agreement,menuIds);
        try {

            BusinessVo<String> businessVo = merchantService.updateMerchant(token,id,merchantName,shortName,
                    enName, linkMan, linkMobile, linkEmail, address,
                    scale, industry, auditType, cooperantBeginTime, cooperantStopTime,
                     certificate, agreement, menuIds);

            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
			result.setMessage(ResultConstansUtil.SUCCESS_DESC);
		} catch (Exception e) {
			result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
			result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
			logger.error("修改商户信息失败:{}",e);
		}
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
		return result;
	}


    @ApiOperation(value = "根据商户名称模糊查询获取所有商户名称与id",notes = "根据商户名称模糊查询获取所有商户名称与id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchName", value = "商户名称",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
    @PostMapping("/getAllMerchantName")
    public BaseResultVo<List<MerchantNameVo>> getAllMerchantName(@RequestParam(value = "merchName",required = false) String merchName) {
        BaseResultVo<List<MerchantNameVo>> result = new BaseResultVo<>();
        logger.info("根据商户名称模糊查询获取所有商户名称与id,merchName:{}",merchName);
        try {
            BusinessVo<List<MerchantNameVo>> businessVo=merchantService.getAllMerchantName(merchName);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("根据商户名称模糊查询获取所有商户名称与id失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }

    private String uploadFile(MultipartFile multipartFile,String filename){
        try {
            return ossComponent.uploadFile(filename,multipartFile);
        } catch (FileException e) {
            logger.error("文件上传失败:"+multipartFile.getOriginalFilename());
            return null;
        }
    }


	/*@ApiOperation(value = "下载图片",notes = "下载营业执照图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "toid", value = "商户toid",required=true, dataType = "String", paramType = "query")
    })
	@GetMapping("/downPicture")
	public void downPicture(@RequestParam("toid") String toid,HttpServletResponse response){
	    BufferedInputStream bis = null;
	    InputStream is = null;
		try {
			LoanMerchantinfo merchantinfo = loanMerchantService.findMerchantinfoByToid(toid);
			String certificate_name = merchantinfo.getCertificate_name();
			is = OssUtils.downFile(certificate_name);
			response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(certificate_name.getBytes(), "iso-8859-1"));
            byte[] buffer = new byte[1024];
			bis = new BufferedInputStream(is);
			OutputStream os = response.getOutputStream();
			int i = bis.read(buffer);
			while (i != -1) {
			    os.write(buffer, 0, i);
			    i = bis.read(buffer);
			}
			logger.info("下载图片成功，toid:"+toid);
		} catch (Exception e) {
			logger.error("下载图片失败，toid:"+toid,e.getMessage());
			e.printStackTrace();
		}finally {
			if(bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			 if (is != null) {
				 try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			 }

		}
	}*/

	/*@ApiOperation(value = "下载图片",notes = "下载营业执照图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "toid", value = "商户toid",required=true, dataType = "String", paramType = "query")
    })
	@GetMapping("/downPicture")
	public void downPicture(@RequestParam("toid") String toid,HttpServletResponse response){
		FileInputStream fis = null;
	    BufferedInputStream bis = null;
		try {
			LoanMerchantinfo merchantinfo = loanMerchantService.findMerchantinfoByToid(toid);
			String certificate_pic = merchantinfo.getCertificate_pic();
			Map<String, Object> map = OssUtils.downFile(certificate_pic);
			if((boolean) map.get("flag")) {
				File file = (File) map.get("file");
				response.reset();
	            response.setContentType("application/force-download");
	            response.setHeader("Content-Disposition", "attachment;filename=" + new String(certificate_pic.substring(certificate_pic.lastIndexOf("_")+1).getBytes(), "iso-8859-1"));
	            byte[] buffer = new byte[1024];
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream os = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
				    os.write(buffer, 0, i);
				    i = bis.read(buffer);
				}
				logger.info("下载图片成功，toid:"+toid);
			}else {
				logger.error("下载图片失败，toid:"+toid);
			}
		} catch (Exception e) {
			logger.error("下载图片失败，toid:"+toid,e.getMessage());
			e.printStackTrace();
		}finally {
			if(bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			 if (fis != null) {
				 try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			 }
			
		}
	}*/
}
