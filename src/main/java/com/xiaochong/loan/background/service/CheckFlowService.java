package com.xiaochong.loan.background.service;

import com.xiaochong.loan.background.dao.CheckflowDao;
import com.xiaochong.loan.background.dao.FlowOptionDao;
import com.xiaochong.loan.background.dao.MerchantFlowOptionDao;
import com.xiaochong.loan.background.dao.MerchantinfoFlowDao;
import com.xiaochong.loan.background.entity.po.Checkflow;
import com.xiaochong.loan.background.entity.po.FlowOption;
import com.xiaochong.loan.background.entity.po.MerchantFlowOption;
import com.xiaochong.loan.background.entity.po.MerchantinfoFlow;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.CheckflowVo;
import com.xiaochong.loan.background.entity.vo.MerchantinfoFlowVo;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import com.xiaochong.loan.background.utils.enums.MerchFlowNoEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("checkFlowService")
public class CheckFlowService {

	private Logger logger = LoggerFactory.getLogger(CheckFlowService.class);

	@Resource(name = "checkflowDao")
	private CheckflowDao checkflowDao;

	@Resource(name = "merchantinfoFlowDao")
	private MerchantinfoFlowDao merchantinfoFlowDao;


    @Autowired
    private MerchantFlowOptionDao merchantFlowOptionDao;

    @Autowired
    private FlowOptionDao flowOptionDao;

    /**
     * 查询所有流程
     * @return
     */
	public BusinessVo<List<Checkflow>> selectLoanCheckflow() {
	    BusinessVo<List<Checkflow>> businessVo = new BusinessVo<>();
	    businessVo.setData(checkflowDao.selectLoanCheckflow());
	    businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
	    businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		return businessVo;
	}

    /**
     * 查询认证流程
     * @param id
     * @return
     */
    public BusinessVo<MerchantinfoFlowVo> getMerchantinfoFlow(Integer id) {
	    BusinessVo<MerchantinfoFlowVo> businessVo = new BusinessVo<>();
        List<MerchantinfoFlow> byMerchIdList = merchantinfoFlowDao.getByMerchId(id);
        MerchantinfoFlowVo merchantinfoFlowVo = new MerchantinfoFlowVo();
        StrBuilder strBuilder = new StrBuilder();
        if(byMerchIdList!=null && byMerchIdList.size()!=0){
            byMerchIdList.forEach(merchantinfoFlow -> {
                String flowNo = merchantinfoFlow.getFlowNo();
                if(!MerchFlowNoEnum.BASE_INFO.getFlowNo().equals(flowNo)){
                    strBuilder.append(",").append(flowNo);
                }
            });
            if(StringUtils.isNotBlank(strBuilder.toString())){
                merchantinfoFlowVo.setFlow(strBuilder.substring(1));
            }
            merchantinfoFlowVo.setMerchId(byMerchIdList.get(0).getMerchId());
        }
        businessVo.setData(merchantinfoFlowVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    private void checkFlowConvert(CheckflowVo checkflowVo ,Checkflow checkflow){
	    checkflowVo.setId(checkflow.getId());
	    checkflowVo.setFlowName(checkflow.getFlowName());
	    checkflowVo.setStatus(checkflow.getStatus());
	    checkflowVo.setUrl(checkflow.getUrl());
	    checkflowVo.setType(checkflow.getType());
    }

    /**
     * 根据数据库是否有数据判断更新或者修改
     * @return
     */
    @Transactional
    public BusinessVo<String> insertOrUpdateLoanMerchantinfoFlow(Integer merchId,String flow) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        if(StringUtils.isNotBlank(flow)){
            int count = 0;
            if(flow.indexOf(MerchFlowNoEnum.EDUCATION.getFlowNo())>=0){
                count++;
            }
            if(flow.indexOf(MerchFlowNoEnum.EDUCATION_NEW.getFlowNo())>=0){
                count++;
            }
            if(flow.indexOf(MerchFlowNoEnum.EDUCATION_TONGDUN.getFlowNo())>=0){
                count++;
            }
            if(count>1){
                businessVo.setMessage("学信流程只能选择一个");
                businessVo.setCode(BusinessConstantsUtils.EDUCATION_FLOW_REPETITION_ERROR_CODE);
                businessVo.setMessage(BusinessConstantsUtils.EDUCATION_FLOW_REPETITION_ERROR_DESC);
                return businessVo;
            }
        }
        int i = merchantinfoFlowDao.deleteLoanMerchantinfoFlow(merchId);
        merchantFlowOptionDao.deleteByMerchId(merchId);
        String[] flows={};
        if(StringUtils.isNotBlank(flow)){
            StringBuilder stringBuilder= new StringBuilder(flow);
            stringBuilder.append(",").append(MerchFlowNoEnum.BASE_INFO.getFlowNo());
            flows = stringBuilder.toString().split(",");
        }else {
            flows=new String[1];
            flows[0]=MerchFlowNoEnum.BASE_INFO.getFlowNo();
        }
        for (String f: flows) {
            MerchantinfoFlow m= new MerchantinfoFlow();
            m.setMerchId(merchId);
            m.setFlowNo(f);
            Checkflow checkflow = new Checkflow();
            checkflow.setFlowNo(f);
            Checkflow byCheckflow = checkflowDao.getByCheckflow(checkflow);
            m.setFlowStep(byCheckflow.getStep());
            merchantinfoFlowDao.insertLoanMerchantinfoFlow(m);
            //添加流程字段
            FlowOption flowOption = new FlowOption();
            flowOption.setFlowNo(f);
            List<FlowOption> flowOptionList = flowOptionDao.selectFlowOption(flowOption);
            flowOptionList.forEach(ft->{
                MerchantFlowOption merchantFlowOption = new MerchantFlowOption();
                merchantFlowOption.setMerchId(merchId);
                merchantFlowOption.setFlowNo(f);
                merchantFlowOption.setFlowOptionNo(ft.getFlowOptionNo());
                merchantFlowOptionDao.insertMerchantFlowOption(merchantFlowOption);
            });
        }
        if(i==0){
            businessVo.setData("商户流程添加成功");
        }else {
            businessVo.setData("商户流程修改成功");
        }
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }
}
