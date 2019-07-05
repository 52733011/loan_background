package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.OssComponent;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.exception.FileException;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by jinxin on 2017/9/4.
 */
@Service
public class SubmitDataPageService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(SubmitDataPageService.class);

    @Value("${webapp.zipTempPath}")
    private String webappZipTempPath;

    @Autowired
    private OssComponent ossComponent;

    @Autowired
    private SessionComponent sessionComponent;

    @Autowired
    private ProxyUserDao proxyUserDao;

    @Autowired
    private MerchantSubmitProjectDao merchantSubmitProjectDao;

    @Autowired
    private MerchantSubmitDataDao merchantSubmitDataDao;

    @Autowired
    private MerchDataTemplateProjectDao merchDataTemplateProjectDao;

    @Autowired
    private LoanApplicationDao loanApplicationDao;

    @Autowired
    private BankCardDao bankCardDao;

    @Autowired
    private MerchantAuditDataDao merchantAuditDataDao;

    @Autowired
    private ManageAdminDao manageAdminDao;

    @Autowired
    private  OrderDao orderDao;

    /**
     * 上传或者录入文字
     * @param token
     * @param applicationId
     * @param projectId
     * @param file
     * @param content
     * @return
     * @throws IOException
     * @throws FileException
     */
    public BusinessVo<String> upload(String token, Integer applicationId, Integer projectId, MultipartFile file, String content) throws IOException, FileException {
        BusinessVo<String> businessVo = new BusinessVo<>();
        MerchantSubmitProject merchantSubmitProject = new MerchantSubmitProject();
        merchantSubmitProject.setApplicationId(applicationId);
        merchantSubmitProject.setProjectId(projectId);
        String id = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        merchantSubmitProject.setSubmitBy(Integer.parseInt(id));
        merchantSubmitProject.setSubmitTime(new Date());
        if(content!=null){
            merchantSubmitProject.setContent(content);
            merchantSubmitProject.setInputType(ProjectInputTypeEnum.CONTENT.getType());
            businessVo.setData("录入成功");
        }else {
            String originalFilename = file.getOriginalFilename();
            int indexOfPoint = originalFilename.lastIndexOf(".");
            if(indexOfPoint>20){
                //控制文件名在20个字符
                originalFilename=originalFilename.substring(indexOfPoint-20);
            }
            //视频文件需要压缩 MP4、3gp、avi、mpg、mpeg、rm、rmvb、mov、mkv
            String reg = ConstansUtils.VIDEO_FORMAT;
            Pattern p = Pattern.compile(reg);
            boolean boo = p.matcher(originalFilename).find();
            String url=null;
            String fileName=null;
            if(boo){
                //压缩
                String zipPath = webappZipTempPath+"/zipTemporary/";
                fileName = StringUtils.replace(UUID.randomUUID().toString(),"-","")+originalFilename.substring(0, originalFilename.lastIndexOf("."))+".zip" ;
                File outFile = FileUtils.getOutFile(zipPath, fileName);
                ZipOutputStream zipOutputStream = null;
                FileOutputStream fileOutputStream = null;
                InputStream inputStream=null;
                try {
                    long start = System.currentTimeMillis();
                    fileOutputStream = new FileOutputStream(outFile);
                    zipOutputStream = new ZipOutputStream(fileOutputStream);
                    zipOutputStream.putNextEntry(new ZipEntry(originalFilename));
                    inputStream= file.getInputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }
//                    zipOutputStream.write(file.getBytes());
                    long end = System.currentTimeMillis();
                    logger.info(originalFilename+"压缩耗时：{} ms",end-start);
                } catch (Exception e) {
                    businessVo.setCode(BusinessConstantsUtils.DATA_UPLOAD_FAILED_CODE);
                    businessVo.setMessage(BusinessConstantsUtils.DATA_UPLOAD_FAILED_DESC);
                    return businessVo;
                }finally {
                    if (zipOutputStream != null) {
                        zipOutputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                long start = System.currentTimeMillis();
                FileInputStream fileInputStream = new FileInputStream(outFile);
                url = ossComponent.uploadFile(fileName, fileInputStream);
                fileInputStream.close();
                long end = System.currentTimeMillis();
                logger.info(originalFilename+"上传耗时：{} ms",end-start);
                outFile.delete();
            }else{
                fileName=StringUtils.replace(UUID.randomUUID().toString(),"-","")+originalFilename;
                long start = System.currentTimeMillis();
                url = ossComponent.uploadFile(fileName, file);
                long end = System.currentTimeMillis();
                logger.info(originalFilename+"上传耗时：{} ms",end-start);
            }
            merchantSubmitProject.setFileName(fileName);
            merchantSubmitProject.setFileUrl(url);
            merchantSubmitProject.setInputType(ProjectInputTypeEnum.OTHER.getType());
            businessVo.setData("文件上传成功");
        }
        merchantSubmitProjectDao.insertOrUpdateMerchantSubmitProject(merchantSubmitProject);
        businessVo.setData(merchantSubmitProject.getFileUrl());
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 提交或暂存数据接口
     * @param token
     * @param applicationId
     * @param status
     * @param mark
     * @return
     */
    @Transactional
    public BusinessVo<String> submitOrScratch(String token, Integer applicationId, String status, String mark) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        //判定数据是否提交完成
//        LoanApplication loanApplicationById = loanApplicationDao.selectLoanApplicationById(applicationId);
//        if(loanApplicationById!=null){
//            MerchDataTemplate merchDataTemplate=merchDataTemplateDao.selectById(loanApplicationById.getTemplateId());
//            if(merchDataTemplate!=null){
//                int countSubmitData = merchantSubmitProjectDao.selectCountMerchantSubmitProjectByAppId(applicationId);
//                int countProject = merchDataTemplateProjectDao.selectCountProjectByTemplateId(merchDataTemplate.getId());
//                if(countProject!=countSubmitData){
//                    businessVo.setCode(BusinessConstantsUtils.LACK_OF_SUBMIT_DATA_CODE);
//                    businessVo.setMessage(BusinessConstantsUtils.LACK_OF_SUBMIT_DATA_DESC);
//                    return businessVo;
//                }
//            }
//        }
        MerchantSubmitData merchantSubmitData= new MerchantSubmitData();
        merchantSubmitData.setApplicationId(applicationId);
        merchantSubmitData.setStatus(status);
        merchantSubmitData.setMark(mark);
        merchantSubmitData.setSubmitTime(new Date());
        String id = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        merchantSubmitData.setSubmitBy(Integer.parseInt(id));
        //更新或新增
        merchantSubmitDataDao.insertOrUpdateMerchantSubmitData(merchantSubmitData);
        //更改申请表状态
        if(SubmitDataTypeEnum.SUBMIT_APPLICATION.getType().equals(status)){
            LoanApplication loanApplication = new LoanApplication();
            loanApplication.setId(applicationId);
            loanApplication.setStatus(LoanApplicationStatusEnum.WAITING_AUDIT.getType());
            loanApplication.setUpdateTime(new Date());
            loanApplicationDao.updateById(loanApplication);
        }
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }


    /**
     * 根据接待人 放贷id查询 模板项目
     * @param applicationId
     * @return
     */
    public BusinessVo<SubmitDataPageVo> selectProjects(Integer applicationId) {
        BusinessVo<SubmitDataPageVo> businessVo = new BusinessVo<>();
        //使用 applicationId 得到 temp id
        LoanApplication loanApplication=loanApplicationDao.selectLoanApplicationById(applicationId);
        String orderNo = loanApplication.getOrderNo();
        SubmitDataPageVo submitDataPageVo = new SubmitDataPageVo();
        //个人信息
        submitDataPageVo.setOrderNo(orderNo);
        Order order = orderDao.selectOrderByOrdeNum(orderNo);
        if(order!=null){
            submitDataPageVo.setIsold(order.getIsOld());
        }
        submitDataPageVo.setBorrowerName(loanApplication.getBorrowerName());
        submitDataPageVo.setBorrowerIdCard(loanApplication.getBorrowerIdCard());
        submitDataPageVo.setBorrowerPhone(loanApplication.getBorrowerPhone());
        submitDataPageVo.setLoanMoney(loanApplication.getLoanMoney());
        submitDataPageVo.setRemitMoney(loanApplication.getRemitMoney());
        String stageType = loanApplication.getStageType();
        submitDataPageVo.setStageType(stageType);
        //放款时间
        submitDataPageVo.setLoanTime(DateUtils.format(loanApplication.getLoanTime(),DateUtils.yyyyMMdd_format));
        String suffix;
        if(StringUtils.equals(StagingTypeEnum.DAY.getType().toString(),stageType)){
                suffix="天";
        }else {
                suffix="月";
        }
        submitDataPageVo.setStageLimit(loanApplication.getStageLimit()+suffix);
        //开户行
        BankCard bankCard = new BankCard();
        bankCard.setOrderNo(orderNo);
        BankCard byBankCard = bankCardDao.getByBankCard(bankCard);
        if(byBankCard!=null){
            submitDataPageVo.setBorrowerBankCard(byBankCard.getBankCard());
            submitDataPageVo.setBorrowerBankName(byBankCard.getBankName());
        }
        List<MerchDataTemplateProject> merchDataTemplateProjectList=merchDataTemplateProjectDao.selectProjectByTemplateId(loanApplication.getTemplateId());
        List<MerchDataTemplateProjectVo> merchDataTemplateProjectVoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(merchDataTemplateProjectList)){
            merchDataTemplateProjectListConvert(merchDataTemplateProjectVoList,merchDataTemplateProjectList,applicationId);
        }
        submitDataPageVo.setMerchDataTemplateProjectVoList(merchDataTemplateProjectVoList);
        //备注信息
        List<MerchantSubmitData> merchantSubmitDataList = merchantSubmitDataDao.selectMerchantSubmitDataByApplicationId(applicationId);
        if(!CollectionUtils.isEmpty(merchantSubmitDataList)){
            MerchantSubmitData m = merchantSubmitDataList.get(0);
            submitDataPageVo.setMark(m.getMark());
            Integer submitBy = m.getSubmitBy();
            submitDataPageVo.setSubmitBy(submitBy);
            ProxyUser byId = proxyUserDao.getById(submitBy);
            submitDataPageVo.setSubmitByName(byId.getUsername());
            submitDataPageVo.setSubmitTime(DateUtils.format(m.getSubmitTime(),DateUtils.yyyyMMddHHmmss_hanziformat));
        }
//        审核信息
        String status = loanApplication.getStatus();
        //待审核不展示之前的审核信息
        if(!LoanApplicationStatusEnum.WAITING_AUDIT.getType().equals(status)){
            List<MerchantAuditData> merchantAuditDataList = merchantAuditDataDao.selectMerchantAuditDataByApplicationId(applicationId);
            if(!CollectionUtils.isEmpty(merchantAuditDataList)){
                //根据时间倒序，取第一个
                MerchantAuditData merchantAuditData = merchantAuditDataList.get(0);
                submitDataPageVo.setAuditContent(merchantAuditData.getAuditContent());
                Integer auditBy = merchantAuditData.getAuditBy();
                submitDataPageVo.setAuditBy(auditBy);
                String auditByName;
                if(LoanAuditTypeEnum.XIAOCHONG_AUDIT.getType().equals(loanApplication.getAuditType())){
                    ManageAdmin byId = manageAdminDao.getById(auditBy);
                    auditByName=byId.getUserName();
                }else {
                    ProxyUser byId = proxyUserDao.getById(auditBy);
                    auditByName=byId.getUsername();
                }
                submitDataPageVo.setAuditByName(auditByName);
                submitDataPageVo.setAuditTime(DateUtils.format(merchantAuditData.getAuditTime(),DateUtils.yyyyMMddHHmmss_hanziformat));
            }
        }
        businessVo.setData(submitDataPageVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return  businessVo;
    }



    /**
     * 项目集合类转换
     * @param merchDataTemplateProjectVoList
     * @param merchDataTemplateProjectList
     * @param applicationId
     */
    private void merchDataTemplateProjectListConvert(List<MerchDataTemplateProjectVo> merchDataTemplateProjectVoList, List<MerchDataTemplateProject> merchDataTemplateProjectList, Integer applicationId) {
        merchDataTemplateProjectList.forEach(merchDataTemplateProject -> {
            MerchDataTemplateProjectVo merchDataTemplateProjectVo= new MerchDataTemplateProjectVo();
            merchDataTemplateProjectConvert(merchDataTemplateProjectVo,merchDataTemplateProject,applicationId);
            merchDataTemplateProjectVoList.add(merchDataTemplateProjectVo);
        });
    }

    /**
     * 项目类转换
     * @param merchDataTemplateProjectVo
     * @param merchDataTemplateProject
     * @param applicationId
     */
    private void merchDataTemplateProjectConvert(MerchDataTemplateProjectVo merchDataTemplateProjectVo, MerchDataTemplateProject merchDataTemplateProject, Integer applicationId) {
        Integer projectId = merchDataTemplateProject.getId();
        MerchantSubmitProject merchantSubmitProject= new MerchantSubmitProject();
        merchantSubmitProject.setApplicationId(applicationId);
        merchantSubmitProject.setProjectId(projectId);
        List<MerchantSubmitProject> merchantSubmitProjectList = merchantSubmitProjectDao.selectMerchantSubmitProject(merchantSubmitProject);
        if(!CollectionUtils.isEmpty(merchantSubmitProjectList)){
            MerchantSubmitProject m = merchantSubmitProjectList.get(0);
            if(ProjectInputTypeEnum.CONTENT.getType().equals(m.getInputType())){
                merchDataTemplateProjectVo.setData(m.getContent());
            }else {

                merchDataTemplateProjectVo.setData(m.getFileUrl());
            }
            String fileName = m.getFileName();
            if(fileName!=null&&fileName.length()>36){
                merchDataTemplateProjectVo.setFilename(fileName.substring(32));
            }else {
                merchDataTemplateProjectVo.setFilename(fileName);
            }
            merchDataTemplateProjectVo.setId(m.getId());
        }
        merchDataTemplateProjectVo.setProjectId(projectId);
        merchDataTemplateProjectVo.setDataTempId(merchDataTemplateProject.getDataTempId());
        merchDataTemplateProjectVo.setFileType(merchDataTemplateProject.getFileType());
        merchDataTemplateProjectVo.setMark(merchDataTemplateProject.getMark());
        merchDataTemplateProjectVo.setProjectName(merchDataTemplateProject.getProjectName());

    }

    /**
     * 根据状态查询
     * @param loanApplication
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public BusinessVo<BasePageInfoVo<LoanApplicationVo>> selectLoanApplicationList(LoanApplication loanApplication, Integer pageNum, Integer pageSize, String token) {
        BusinessVo<BasePageInfoVo<LoanApplicationVo>> businessVo = new BusinessVo<>();
        ProxyUser proxyUserByToken = UserTokenUtils.getProxyUserByToken(token);
        loanApplication.setMerchId(proxyUserByToken.getMerchId());
        PageHelper.startPage(pageNum, pageSize, true);
        Page<LoanApplication> page = (Page<LoanApplication>) loanApplicationDao.selectLoanAuditApplicationListByStutus(loanApplication);
        PageInfo<LoanApplication> loanMerchantinfoVoPageInfo = page.toPageInfo();
        BasePageInfoVo<LoanApplicationVo> basePageInfoVo = assemblyBasePageInfo(loanMerchantinfoVoPageInfo);
        List<LoanApplicationVo> loanApplicationVoList = new ArrayList<>();
        List<LoanApplication> result = page.getResult();
        if(!CollectionUtils.isEmpty(result)){
            loanApplicationListConvert(loanApplicationVoList,result);
        }
        basePageInfoVo.setResultList(loanApplicationVoList);
        businessVo.setData(basePageInfoVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 放贷集合类转换
     * @param loanApplicationVoList
     * @param result
     */
    private void loanApplicationListConvert(List<LoanApplicationVo> loanApplicationVoList, List<LoanApplication> result) {
        result.forEach(loanApplication -> {
            LoanApplicationVo loanApplicationVo = new LoanApplicationVo();
            loanApplicationConvert(loanApplicationVo,loanApplication);
            loanApplicationVoList.add(loanApplicationVo);
        });
    }

    /**
     * 放贷类转换
     * @param loanApplicationVo
     * @param loanApplication
     */
    private void loanApplicationConvert(LoanApplicationVo loanApplicationVo, LoanApplication loanApplication) {
        //银行卡
//        BankCard bankCard = new BankCard();
//        bankCard.setOrderNo(loanApplication.getOrderNo());
//        BankCard byBankCard = bankCardDao.getByBankCard(bankCard);
//        if(byBankCard!=null){
//            loanApplicationVo.setBankCard(byBankCard.getBankCard());
//            loanApplicationVo.setDepositBank(byBankCard.getBankName());
//        }
        //创建人信息
        ProxyUser byId = proxyUserDao.getById(loanApplication.getCreateBy());
        if(byId!=null){
            loanApplicationVo.setCreateByName(byId.getUsername());
            loanApplicationVo.setCreateByPhone(byId.getPhone());
        }
        loanApplicationVo.setId(loanApplication.getId());
        loanApplicationVo.setUserName(loanApplication.getBorrowerName());
        loanApplicationVo.setPhone(loanApplication.getBorrowerPhone());
        loanApplicationVo.setIdCard(loanApplication.getBorrowerIdCard());
        loanApplicationVo.setLoanMoney(String.valueOf(loanApplication.getLoanMoney()));
        loanApplicationVo.setRemitMoney(String.valueOf(loanApplication.getRemitMoney()));
        loanApplicationVo.setOrderNo(loanApplication.getOrderNo());
        loanApplicationVo.setSubmitType(loanApplication.getSubmitType());
        String stageType = loanApplication.getStageType();
        String stageUnit;
        if(StagingTypeEnum.MONTH.getType().toString().equals(stageType)){
            stageUnit="月";
        }else {
            stageUnit="天";
        }
        loanApplicationVo.setStageType(stageType);
        loanApplicationVo.setStageLimit(loanApplication.getStageLimit()+stageUnit);
        loanApplicationVo.setCreateTime(DateUtils.format(loanApplication.getCreateTime(),DateUtils.yyyyMMdd_format));
        loanApplicationVo.setStatus(loanApplication.getStatus());
    }

    /**
     * 关闭订单接口
     * @param token
     * @return
     */
    @Transactional
    public BusinessVo<String> closeApplication( String token,Integer applicationId) {
        BusinessVo<String> businessVo= new BusinessVo<>();
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setId(applicationId);
        String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        loanApplication.setUpdateBy(Integer.parseInt(proxyUserId));
        loanApplication.setUpdateTime(new Date());
        loanApplication.setStatus(LoanApplicationStatusEnum.REFUSED.getType());
        loanApplicationDao.updateById(loanApplication);
        businessVo.setData("关闭订单成功");
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 下载所有文件压缩包
     * @param applicationId
     * @return
     * @throws IOException
     */
    public byte[] downloadAllFiles(Integer applicationId) throws IOException {
        MerchantSubmitProject merchantSubmitProject = new MerchantSubmitProject();
        merchantSubmitProject.setApplicationId(applicationId);
        merchantSubmitProject.setInputType(ProjectInputTypeEnum.OTHER.getType());
        List<MerchantSubmitProject> merchantSubmitProjectList = merchantSubmitProjectDao.selectMerchantSubmitProject(merchantSubmitProject);
        if(CollectionUtils.isEmpty(merchantSubmitProjectList)){
            return null;
        }
        String zipPath = webappZipTempPath+"/zipTemporary/";
        String zipFileName = StringUtils.replace(UUID.randomUUID().toString(),"-","")+".zip" ;
        ZipOutputStream zipOutputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(FileUtils.getOutFile(zipPath, zipFileName));
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            for (MerchantSubmitProject m:merchantSubmitProjectList) {
                if(ProjectInputTypeEnum.CONTENT.getType().equals(m.getInputType())){
                    continue;
                }
                MerchDataTemplateProject merchDataTemplateProject = merchDataTemplateProjectDao.selectProjectById(m.getProjectId());
                String fileName = m.getFileName();
                String sufix = fileName.substring(fileName.lastIndexOf("."));
                zipOutputStream.putNextEntry(new ZipEntry(merchDataTemplateProject.getProjectName()+sufix));
                URL url= new URL(m.getFileUrl());
                URLConnection urlConnection = url.openConnection();
                zipOutputStream.write(FileCopyUtils.copyToByteArray(urlConnection.getInputStream()));
            }
        } catch (IOException e) {
            logger.error("下载所有上传资料压缩失败：{}",e);
            return null;
        }finally {
            if (zipOutputStream != null) {
                zipOutputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
        File zipFile = new File(zipPath+zipFileName);
        byte[] bytes=FileCopyUtils.copyToByteArray(zipFile);
        zipFile.delete();
        return  bytes;
    }

    /**
     * 设定放款时间
     * @param applicationId
     * @param loanDate
     * @return
     */
    public BusinessVo<String> setLoanTime(Integer applicationId, Date loanDate) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanTime(loanDate);
        loanApplication.setId(applicationId);
        loanApplicationDao.updateById(loanApplication);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     *删除上传数据关联
     * @param submitProjectId
     * @return
     */
    public BusinessVo<String> deleteSubmitProject(Integer submitProjectId) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        MerchantSubmitProject merchantSubmitProject=merchantSubmitProjectDao.selectMerchantSubmitProjectById(submitProjectId);
        //暂时未做删除oss动作
        if(merchantSubmitProject==null){
            businessVo.setCode(BusinessConstantsUtils.PROJECT_DATA_IS_NOT_EXISTS_CODE);
            businessVo.setMessage(BusinessConstantsUtils.PROJECT_DATA_IS_NOT_EXISTS_DESC);
            return businessVo;
        }
        merchantSubmitProjectDao.deleteById(submitProjectId);
        businessVo.setData("删除成功");
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }
}
