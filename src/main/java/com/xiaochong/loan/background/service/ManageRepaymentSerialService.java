package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.RepaymentSerialSourceTypeEnum;
import com.xiaochong.loan.background.utils.enums.RepaymentSerialStatusEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by jinxin on 2017/10/18.
 */
@Service
public class ManageRepaymentSerialService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(ManageRepaymentSerialService.class);

    @Autowired
    private RepaymentSerialRecordDao repaymentSerialRecordDao;

    @Autowired
    private BorrowerDao borrowerDao;

    @Autowired
    private MerchantDao merchantDao;

    @Value("${repayment.demo.path}")
    private String repayment_demo_path;

    /**
     * 还款流水查询
     * @param repaymentSerialRecord
     * @param borrower
     * @param status
     * @param pageNum
     * @param pageSize
     * @param token    @return
     * */
    public BusinessVo<RepaymentSerialVo> selectByRepaymentSerialRecord(RepaymentSerialRecord repaymentSerialRecord, Borrower borrower, String status, Integer pageNum, Integer pageSize, String token) {
        BusinessVo<RepaymentSerialVo> businessVo = new BusinessVo<>();
        RepaymentSerialVo repaymentSerialVo = new RepaymentSerialVo();
        List<Integer> borrowerList = new ArrayList<>();
        if(borrower!=null){
            borrowerList = borrowerDao.selectBorrowerId(borrower);
            if(CollectionUtils.isEmpty(borrowerList)){
                borrowerList.add(-1);
            }
            repaymentSerialRecord.setBorrowerIds(borrowerList);
        }
        List<Map<String, Object>> statusCountList = repaymentSerialRecordDao.selectCountByRepaymentSerialRecord(repaymentSerialRecord);
        Integer allCount =0;
        repaymentSerialVo.setWaitRechargeCount(0);
        repaymentSerialVo.setWaitMatchCount(0);
        repaymentSerialVo.setRechargeEdCount(0);
        if(!CollectionUtils.isEmpty(statusCountList)){
            for (Map<String, Object> map : statusCountList) {
                String statusGet = (String) map.get("status");
                Integer count = Integer.valueOf(map.get("count").toString()) ;
                if(RepaymentSerialStatusEnum.RECHARGED.getType().equals(statusGet)){
                    repaymentSerialVo.setRechargeEdCount(count);
                }else   if(RepaymentSerialStatusEnum.WAITING_MATCH.getType().equals(statusGet)){
                    repaymentSerialVo.setWaitMatchCount(count);
                } else if(RepaymentSerialStatusEnum.WAITING_RECHARGE.getType().equals(statusGet)){
                    repaymentSerialVo.setWaitRechargeCount(count);
                }
                allCount=allCount+count;
            }
        }
        repaymentSerialVo.setAllCount(allCount);
        repaymentSerialRecord.setStatus(status);
        PageHelper.startPage(pageNum, pageSize, true);
        Page<RepaymentSerialRecord> page = (Page<RepaymentSerialRecord>) repaymentSerialRecordDao.selectByRepaymentSerialRecord(repaymentSerialRecord);
        PageInfo<RepaymentSerialRecord> loanMerchantinfoVoPageInfo = page.toPageInfo();
        BasePageInfoVo<RepaymentSerialRecordVo> basePageInfoVo = assemblyBasePageInfo(loanMerchantinfoVoPageInfo);
        List<RepaymentSerialRecordVo> repaymentSerialRecordVos = new ArrayList<>();
        List<RepaymentSerialRecord> result = page.getResult();
        if(CollectionUtils.isNotEmpty(result)){
            RepaymentSerialRecordListConvert(repaymentSerialRecordVos,result);
        }
        basePageInfoVo.setResultList(repaymentSerialRecordVos);
        repaymentSerialVo.setBasePageInfoVo(basePageInfoVo);
        businessVo.setData(repaymentSerialVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 集合转换
     * @param repaymentSerialRecordVos
     * @param result
     */
    private void RepaymentSerialRecordListConvert(List<RepaymentSerialRecordVo> repaymentSerialRecordVos, List<RepaymentSerialRecord> result) {
        result.forEach(repaymentSerialRecord -> {
            RepaymentSerialRecordVo repaymentSerialRecordVo = new RepaymentSerialRecordVo();
            repaymentSerialRecordConvert(repaymentSerialRecordVo,repaymentSerialRecord);
            repaymentSerialRecordVos.add(repaymentSerialRecordVo);
        });
    }

    /**
     * 类转换
     * @param repaymentSerialRecordVo
     * @param repaymentSerialRecord
     */
    private void repaymentSerialRecordConvert(RepaymentSerialRecordVo repaymentSerialRecordVo, RepaymentSerialRecord repaymentSerialRecord) {
        repaymentSerialRecordVo.setId(repaymentSerialRecord.getId());
        repaymentSerialRecordVo.setAccountingSerialNo(repaymentSerialRecord.getAccountingSerialNo());
        Integer borrowerId = repaymentSerialRecord.getBorrowerId();
        if(borrowerId!=null){
            Borrower borrower = borrowerDao.selectBorrowerById(borrowerId);
            repaymentSerialRecordVo.setBorrowerId(borrowerId);
            repaymentSerialRecordVo.setBorrowerName(borrower.getName());
            repaymentSerialRecordVo.setBorrowerPhone(borrower.getPhone());
        }
        repaymentSerialRecordVo.setDealSerialNo(repaymentSerialRecord.getDealSerialNo());
        repaymentSerialRecordVo.setDealTime(DateUtils.format(repaymentSerialRecord.getDealTime(),DateUtils.yyyyMMdd_format));
        Integer merchId = repaymentSerialRecord.getMerchId();
        if(merchId!=null){
            repaymentSerialRecordVo.setMerchId(merchId);
            Merchantinfo merchantinfoByToid = merchantDao.findMerchantinfoByToid(merchId);
            repaymentSerialRecordVo.setMerchName(merchantinfoByToid.getMerchantName());
        }
        repaymentSerialRecordVo.setTransferAccountName(repaymentSerialRecord.getTransferAccountName());
        repaymentSerialRecordVo.setTransferMoney(repaymentSerialRecord.getTransferMoney());
        repaymentSerialRecordVo.setTransferMark(repaymentSerialRecord.getTransferMark());
        repaymentSerialRecordVo.setStatus(repaymentSerialRecord.getStatus());
    }




//    /**
//     * 充值全部待充值
//     * @param token
//     * @return
//     */
//    @Transactional
//    public BusinessVo<String> rechargeAll(String token) {
//        BusinessVo<String> businessVo = new BusinessVo<>();
//        ManageAdmin manageAdmin = sessionComponent.getAttributeManageAdmin(token);
//        Integer manageUserByTokenId = manageAdmin.getId();
//        RepaymentSerialRecord repaymentSerialRecordForSearch = new RepaymentSerialRecord();
//        repaymentSerialRecordForSearch.setStatus(RepaymentSerialStatusEnum.WAITING_RECHARGE.getType());
//        List<RepaymentSerialRecord> repaymentSerialRecordList = repaymentSerialRecordDao.selectByRepaymentSerialRecord(repaymentSerialRecordForSearch);
//        if(CollectionUtils.isNotEmpty(repaymentSerialRecordList)){
//            for (RepaymentSerialRecord repaymentSerialRecord : repaymentSerialRecordList) {
//                try {
//                    recharge(repaymentSerialRecord,manageUserByTokenId);
//                } catch (RechargeException e) {
//                    logger.error("管理后台充值全部充值失败",e);
//                    businessVo.setCode(BusinessConstantsUtils.RECHARGE_ERROR_CODE);
//                    businessVo.setMessage(BusinessConstantsUtils.RECHARGE_ERROR_DESC);
//                    return businessVo;
//                }
//            }
//        }
//        businessVo.setCode("操作成功！");
//        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
//        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
//        return businessVo;
//    }

//    /**
//     * 充值并记录
//     * @param repaymentSerialRecord
//     * @param userId
//     */
//    private void recharge(RepaymentSerialRecord repaymentSerialRecord,Integer userId) throws RechargeException {
//        if(RepaymentSerialStatusEnum.RECHARGED.getType().equals(repaymentSerialRecord.getStatus())){
//            return;
//        }
//        Integer borrowerId = repaymentSerialRecord.getBorrowerId();
//        BigDecimal transferMoney = repaymentSerialRecord.getTransferMoney();
//        Date now = new Date();
//        Map<String, Object> resultMap = borrowerBanlanceComponent.updateBalance(transferMoney, userId, borrowerId);
//        BorrowerAccountRecord borrowerAccountRecord = new BorrowerAccountRecord();
//        RepaymentSerialRecord repaymentSerialRecordForUpdate = new RepaymentSerialRecord();
//        if("success".equals(resultMap.get("result"))){
//            borrowerAccountRecord.setResult(RepaymentResultTypeEnum.SUCCESS.getType());
//            borrowerAccountRecord.setMark("充值成功");
//            repaymentSerialRecordForUpdate.setStatus(RepaymentSerialStatusEnum.RECHARGED.getType());
//            repaymentSerialRecordForUpdate.setUpdateTime(now);
//            repaymentSerialRecordForUpdate.setUpdateUser(userId);
//            repaymentSerialRecordForUpdate.setId(repaymentSerialRecord.getId());
//            repaymentSerialRecordForUpdate.setUpdateSource(RepaymentSerialSourceTypeEnum.XIAOCHONG.getType());
//            repaymentSerialRecordDao.update(repaymentSerialRecordForUpdate);
//        }else {
//            borrowerAccountRecord.setResult(RepaymentResultTypeEnum.FAIL.getType());
//            borrowerAccountRecord.setMark("充值失败");
//            logger.error("充值失败：{}",repaymentSerialRecord);
//            throw new RechargeException("充值失败！");
//        }
//        BorrowerBalance borrowerBalance = borrowerBalanceDao.selectBorrowerBalanceByBorrowerId(borrowerId);
//        borrowerAccountRecord.setBorrowerBanlanceId(borrowerBalance.getId());
//        borrowerAccountRecord.setCapitalMoney(transferMoney);
//        borrowerAccountRecord.setCapitalBeforeMoney((BigDecimal) resultMap.get("before"));
//        borrowerAccountRecord.setCapitalAfterMoney((BigDecimal) resultMap.get("after"));
//        borrowerAccountRecord.setOperateSerialNo(UUID.randomUUID().toString());
//        borrowerAccountRecord.setCapitalType(CapitalTypeEnum.RECHARGE.getType());
//        borrowerAccountRecord.setCreateUser(userId);
//        borrowerAccountRecord.setCreateTime(now);
//        borrowerAccountRecord.setSource(BorrowerAccountRecordSourceTypeEnum.XIAOCHONG.getType());
//        borrowerAccountRecordDao.insert(borrowerAccountRecord);
//    }

//    /**
//     * 根据id进行充值
//     * @param token
//     * @param ids
//     * @return
//     */
//    public BusinessVo<String> rechargeByIds(String token, String ids) {
//        BusinessVo<String> businessVo = new BusinessVo<>();
//        if(StringUtils.isNotBlank(ids)){
//            String[] idArray = ids.split(",");
//            ManageAdmin manageAdmin = sessionComponent.getAttributeManageAdmin(token);
//            Integer manageAdminId = manageAdmin.getId();
//            for (int i = 0; i <idArray.length ; i++) {
//                RepaymentSerialRecord repaymentSerialRecord = repaymentSerialRecordDao.selectById(Integer.valueOf(idArray[i]));
//                try {
//                    recharge(repaymentSerialRecord,manageAdminId);
//                } catch (RechargeException e) {
//                    logger.error("管理后台根据ids充值失败",e);
//                    businessVo.setCode(BusinessConstantsUtils.RECHARGE_ERROR_CODE);
//                    businessVo.setMessage(BusinessConstantsUtils.RECHARGE_ERROR_DESC);
//                    return businessVo;
//                }
//            }
//        }
//        businessVo.setData("充值成功！");
//        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
//        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
//        return businessVo;
//    }

//    /**
//     * 根据条件查询借款人
//     * @param token
//     * @param searchStatus
//     * @param searchContent
//     * @param merchId
//     * @return
//     */
//    public BusinessVo<BorrowerVo> searchBorrower(String token, String searchStatus, String searchContent, Integer merchId) throws Exception {
//        BusinessVo<BorrowerVo> businessVo = new BusinessVo<>();
//        Borrower borrower = new Borrower();
//        if("1".equals(searchStatus)){
//            borrower.setPhone(searchContent);
//        }else if("2".equals(searchStatus)){
//            borrower.setIdCard(searchStatus);
//        }
//        borrower.setMerchId(merchId);
//        List<Borrower> borrowerList = borrowerDao.selectBorrower(borrower);
//        if(CollectionUtils.isNotEmpty(borrowerList)){
//            if(borrowerList.size()>1){
//                logger.error("借款人数据出错：{}",borrowerList);
//                throw new Exception("借款人数据出错!");
//            }
//            Borrower borrowerReturn = borrowerList.get(0);
//            BorrowerVo borrowerVo = new BorrowerVo();
//            borrowerVo.setId(borrowerReturn.getId());
//            borrowerVo.setMerchId(borrowerReturn.getMerchId());
//            borrowerVo.setName(borrowerReturn.getName());
//            borrowerVo.setPhone(borrowerReturn.getPhone());
//            borrowerVo.setIdCard(borrowerReturn.getIdCard());
//            businessVo.setData(borrowerVo);
//        }
//        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
//        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
//        return businessVo;
//    }

//    /**
//     * 匹配或取消匹配
//     * @param token
//     * @param serialId
//     * @param borrowerId
//     * @param status
//     * @return
//     */
//    public BusinessVo<String> matchOrCancelBorrower(String token, Integer serialId, Integer borrowerId, String status) {
//        BusinessVo<String> businessVo = new BusinessVo<>();
//        RepaymentSerialRecord repaymentSerialRecordForUpdate = new RepaymentSerialRecord();
//        repaymentSerialRecordForUpdate.setId(serialId);
//        if("1".equals(status)){
//            if(borrowerId!=null){
//                repaymentSerialRecordForUpdate.setBorrowerId(borrowerId);
//            }
//        }
//        ManageAdmin manageAdmin = sessionComponent.getAttributeManageAdmin(token);
//        repaymentSerialRecordForUpdate.setUpdateUser(manageAdmin.getId());
//        repaymentSerialRecordForUpdate.setUpdateTime(new Date());
//        repaymentSerialRecordForUpdate.setUpdateSource(RepaymentSerialSourceTypeEnum.XIAOCHONG.getType());
//        repaymentSerialRecordDao.updateBorrowerIdById(repaymentSerialRecordForUpdate);
//        businessVo.setData("操作成功");
//        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
//        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
//        return businessVo;
//    }

    /**
     * 添加还款流水
     * @param token
     * @param repaymentSerialRecord
     * @param phone
     * @return
     */
    public BusinessVo<String> addRepaymentSerial(String token, RepaymentSerialRecord repaymentSerialRecord, String phone) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        Integer merchId = repaymentSerialRecord.getMerchId();
        Integer userByTokenId = UserTokenUtils.getManagerAdminIdByToken(token);
        Borrower borrowerForSearch= new Borrower();
        borrowerForSearch.setMerchId(merchId);
        borrowerForSearch.setPhone(phone);
        List<Borrower> borrowerList = borrowerDao.selectBorrower(borrowerForSearch);
        if(CollectionUtils.isNotEmpty(borrowerList)){
            Borrower borrower = borrowerList.get(0);
            repaymentSerialRecord.setCreateUser(userByTokenId);
            repaymentSerialRecord.setCreateTime(new Date());
            repaymentSerialRecord.setBorrowerId(borrower.getId());
            repaymentSerialRecord.setStatus(RepaymentSerialStatusEnum.WAITING_RECHARGE.getType());
            repaymentSerialRecord.setDealSerialNo(UUID.randomUUID().toString());
            repaymentSerialRecord.setSource(RepaymentSerialSourceTypeEnum.XIAOCHONG.getType());
            repaymentSerialRecordDao.insert(repaymentSerialRecord);
        }else {
            businessVo.setData("账户不存在！");
            businessVo.setCode(BusinessConstantsUtils.PHONE_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.PHONE_ERROR_DESC);
            return businessVo;
        }
        businessVo.setData("添加成功！");
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 下载模板
     * @return
     * @throws IOException
     */
    public byte[] downLoadDemo() throws IOException {
        byte[] bytes = FileCopyUtils.copyToByteArray(new File(repayment_demo_path));
        return bytes;
    }

    /**
     * 流水上传
     * @param token
     * @param file
     * @return
     * @throws IOException
     */
    @Transactional
    public BusinessVo<String> leadInbatchSerial(Integer merchId,String token, MultipartFile file) throws Exception {
        BusinessVo<String> businessVo = new BusinessVo<>();
        Integer userByTokenId = UserTokenUtils.getManagerAdminIdByToken(token);
        List<RepaymentSerialRecord> repaymentSerialRecordList = new ArrayList<>();
       if( file.getOriginalFilename().endsWith(".csv")||file.getOriginalFilename().endsWith(".CSV")){
            if(!ExecelUtils.csv(businessVo,file,repaymentSerialRecordList)){
                return businessVo;
            }
       }else{
          if(!ExecelUtils.xlsx(businessVo,file,repaymentSerialRecordList)){
              return businessVo;
          }
       }
        //数据入库
        repaymentSerialRecordList.forEach(repaymentSerialRecord -> {
            String transferMark = repaymentSerialRecord.getTransferMark();
            //取数组第二个手机号
            String[] infoArray;
            if(transferMark.contains("+")){
                infoArray= transferMark.split("\\+");
            }else if(transferMark.contains(",")){
                infoArray= transferMark.split(",");
            }else if(transferMark.contains("，")){
                infoArray= transferMark.split("，");
            }else {
                infoArray= transferMark.split(" ");
            }
            String phone= infoArray[1];
            if(StringUtils.isNotBlank(phone)){
                Borrower borrowerForSearch = new Borrower();
                borrowerForSearch.setMerchId(merchId);
                borrowerForSearch.setPhone(phone);
                List<Borrower> borrowerList = borrowerDao.selectBorrower(borrowerForSearch);
                if(CollectionUtils.isNotEmpty(borrowerList)){
                    Borrower borrower = borrowerList.get(0);
                    String name = borrower.getName();
                    if(StringUtils.isNotBlank(name)){
                        if(name.equals(infoArray[0])){
                            repaymentSerialRecord.setStatus(RepaymentSerialStatusEnum.WAITING_RECHARGE.getType());
                        }else {
                            repaymentSerialRecord.setStatus(RepaymentSerialStatusEnum.LIKE_MATCH.getType());
                        }
                    }
                    repaymentSerialRecord.setBorrowerId(borrower.getId());

                }else {
                    logger.info("匹配失败：{}",transferMark);
                    repaymentSerialRecord.setStatus(RepaymentSerialStatusEnum.WAITING_MATCH.getType());
                }
            }else {
                logger.info("匹配失败：{}",transferMark);
                repaymentSerialRecord.setStatus(RepaymentSerialStatusEnum.WAITING_MATCH.getType());
            }
            repaymentSerialRecord.setCreateUser(userByTokenId);
            repaymentSerialRecord.setCreateTime(new Date());
            repaymentSerialRecord.setMerchId(merchId);
            repaymentSerialRecord.setSource(RepaymentSerialSourceTypeEnum.XIAOCHONG.getType());
            repaymentSerialRecordDao.insert(repaymentSerialRecord);
        });
        businessVo.setData("上传完成");
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

}
