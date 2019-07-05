package com.xiaochong.loan.background.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.MerchDataTemplateDao;
import com.xiaochong.loan.background.dao.MerchDataTemplateProjectDao;
import com.xiaochong.loan.background.dao.ProxyUserDao;
import com.xiaochong.loan.background.entity.po.MerchDataTemplate;
import com.xiaochong.loan.background.entity.po.MerchDataTemplateProject;
import com.xiaochong.loan.background.entity.po.ProxyUser;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.MerchDataTemplateVo;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.enums.MerchDataTemplateStatusEnum;
import com.xiaochong.loan.background.utils.enums.UserLoginTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by jinxin on 2017/9/1.
 */
@Service
public class MerchDataTemplateService extends BaseService{

    @Autowired
    private MerchDataTemplateDao merchDataTemplateDao;

    @Autowired
    private ProxyUserDao proxyUserDao;

    @Autowired
    private SessionComponent sessionComponent;

    @Autowired
    private MerchDataTemplateProjectDao merchDataTemplateProjectDao;

    /**
     * 分页查询数据模板
     * @param pageNum
     * @param pageSize
     * @return
     */
    public BusinessVo<BasePageInfoVo<MerchDataTemplateVo>> merchDataTemplateListSearch(String token, Integer pageSize, Integer pageNum) {
        BusinessVo<BasePageInfoVo<MerchDataTemplateVo>> businessVo = new BusinessVo<>();
        MerchDataTemplate merchDataTemplate = new MerchDataTemplate();
        merchDataTemplate.setMerchId(getProxyUserByToken(token).getMerchId());
        PageHelper.startPage(pageNum, pageSize, true);
        Page<MerchDataTemplate> page = (Page<MerchDataTemplate>) merchDataTemplateDao.searchMerchDataTemplate(merchDataTemplate);
        PageInfo<MerchDataTemplate> loanMerchantinfoVoPageInfo = page.toPageInfo();
        BasePageInfoVo<MerchDataTemplateVo> basePageInfoVo = assemblyBasePageInfo(loanMerchantinfoVoPageInfo);
        List<MerchDataTemplateVo> merchDataTemplateVoList = new ArrayList<>();
        List<MerchDataTemplate> result = page.getResult();
        if(CollectionUtils.isNotEmpty(result)){
            merchDataTemplateListConvert(merchDataTemplateVoList,result);
        }
        basePageInfoVo.setResultList(merchDataTemplateVoList);
        businessVo.setData(basePageInfoVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 集合类转换
     * @param merchDataTemplateVoList
     * @param result
     */
    private void merchDataTemplateListConvert(List<MerchDataTemplateVo> merchDataTemplateVoList, List<MerchDataTemplate> result) {
            result.forEach(merchDataTemplate -> {
                MerchDataTemplateVo merchDataTemplateVo = new MerchDataTemplateVo();
                merchDataTemplateConvert(merchDataTemplateVo,merchDataTemplate);
                merchDataTemplateVoList.add(merchDataTemplateVo);
            });
    }

    /**
     * 模板类转换
     * @param merchDataTemplateVo
     * @param merchDataTemplate
     */
    private void merchDataTemplateConvert(MerchDataTemplateVo merchDataTemplateVo, MerchDataTemplate merchDataTemplate) {
        merchDataTemplateVo.setId(merchDataTemplate.getId());
        Integer createBy = merchDataTemplate.getCreateBy();
        ProxyUser byId = proxyUserDao.getById(createBy);
        merchDataTemplateVo.setCreateBy(createBy);
        merchDataTemplateVo.setTemplateName(merchDataTemplate.getTemplateName());
        merchDataTemplateVo.setMerchId(merchDataTemplate.getMerchId());
        if(byId!=null){
            merchDataTemplateVo.setCreateByName(byId.getUsername());
        }
        merchDataTemplateVo.setStatus(merchDataTemplate.getStatus());
        merchDataTemplateVo.setCreateTime(DateUtils.format(merchDataTemplate.getCreateTime(),DateUtils.yyyyMMddHHmmss_hanziformat));
        merchDataTemplateVo.setTemplateDesc(merchDataTemplate.getTemplateDesc());
    }

    /**
     * 添加商户模板
     * @param token
     * @param merchDataTemplate
     * @param projectList
     * @return
     */
    @Transactional
    public BusinessVo<String> insertMerchDataTemplate(String token, MerchDataTemplate merchDataTemplate, String projectList){
        BusinessVo<String> businessVo = new BusinessVo<>();
        //判断模板名是否重复
        ProxyUser proxyUser = getProxyUserByToken(token);
        Integer merchId = proxyUser.getMerchId();
        merchDataTemplate.setMerchId(merchId);
        List<MerchDataTemplate> merchDataTemplateList = merchDataTemplateDao.searchMerchDataTemplate(merchDataTemplate);
        if(CollectionUtils.isNotEmpty(merchDataTemplateList)){
            businessVo.setCode(BusinessConstantsUtils.DATA_TEMPLATE_NAME_REPETITION_CODE);
            businessVo.setMessage(BusinessConstantsUtils.DATA_TEMPLATE_NAME_REPETITION_DESC);
            return businessVo;
        }
        JSONArray jsonArray = JSON.parseArray(projectList);
        String[] pros = projectList.split("},\\{");
        Set<String> projectSet= new HashSet<>();
        List<JSONObject> jsonObjectList = new ArrayList<>();
        //判断项目名是否重复
        jsonArray.forEach(json->{
            JSONObject jsonObject = JSON.parseObject(json.toString());
            projectSet.add(jsonObject.getString("projectName"));
            jsonObjectList.add(jsonObject);
        });
        if(projectSet.size()!=pros.length){
            businessVo.setCode(BusinessConstantsUtils.PROJECT_NAME_REPETITION_CODE);
            businessVo.setMessage(BusinessConstantsUtils.PROJECT_NAME_REPETITION_DESC);
            return businessVo;
        }
        Date now = new Date();
        merchDataTemplate.setCreateBy(proxyUser.getId());
        merchDataTemplate.setCreateTime(now);
        merchDataTemplate.setStatus(MerchDataTemplateStatusEnum.USING.getType());
        merchDataTemplateDao.insertTemplate(merchDataTemplate);
        //插入项目
        Integer templateId = merchDataTemplate.getId();
        int no=0;
        //[{projectName:value,fileType:value,mark:value},{}]
        for (int i = 0; i <jsonObjectList.size() ; i++) {
            MerchDataTemplateProject merchDataTemplateProject= new MerchDataTemplateProject();
            merchDataTemplateProject.setDataTempId(templateId);
            merchDataTemplateProject.setCreateTime(now);
            JSONObject jsonObject = jsonObjectList.get(i);
            String projectName = jsonObject.getString("projectName");
            merchDataTemplateProject.setProjectName(projectName);
            merchDataTemplateProject.setFileType(jsonObject.getString("fileType"));
            merchDataTemplateProject.setMark(jsonObject.getString("mark"));
            merchDataTemplateProject.setSerialNo(i);
            merchDataTemplateProjectDao.insertProject(merchDataTemplateProject);
        }
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return  businessVo;
    }

    private ProxyUser getProxyUserByToken(String token){
        String idString = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        return proxyUserDao.getById(Integer.parseInt(idString));
    }

    /**
     * 根据id更新状态
     * @return
     */
    @Transactional
    public BusinessVo<String> updateStatus(MerchDataTemplate merchDataTemplate){
        BusinessVo<String> businessVo = new BusinessVo<>();
        merchDataTemplateDao.update(merchDataTemplate);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

}
