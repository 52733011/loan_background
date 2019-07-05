package com.xiaochong.loan.background.utils;

import com.xiaochong.loan.background.entity.po.RepaymentSerialRecord;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.service.ManageRepaymentSerialService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by jinxin on 2017/10/20.
 */
public class ExecelUtils {

    private static Logger logger = LoggerFactory.getLogger(ExecelUtils.class);


    /**
     * csv解析
     * @param businessVo
     * @param file
     * @param repaymentSerialRecordList
     * @return
     * @throws Exception
     */
    public static boolean csv(BusinessVo<String> businessVo, MultipartFile file, List<RepaymentSerialRecord> repaymentSerialRecordList) throws Exception {
        CSVFileUtil csvFileUtil = new CSVFileUtil(file.getInputStream());
        String line = csvFileUtil.readLine();
        boolean flag =false;
        while (StringUtils.isNotBlank(line)){
            List<String> cellList = CSVFileUtil.fromCSVLinetoArray(line);
            String value = cellList.get(0);
            if(StringUtils.isNotBlank(value)&&value.contains("账务明细列表结束")){
                flag=false;
                break;
            }
            if(flag){
                //录入开始
                try {
                    RepaymentSerialRecord repaymentSerialRecord = new RepaymentSerialRecord();
                    repaymentSerialRecord.setAccountingSerialNo(cellList.get(0));
                    repaymentSerialRecord.setDealSerialNo(cellList.get(1));
                    Date date = DateUtils.stringToDate(cellList.get(4), DateUtils.ymdhms_formatWithBias);
                    if(date==null){
                        date=DateUtils.stringToDate(cellList.get(4), DateUtils.ymdhms_formatNoSs);
                    }
                    repaymentSerialRecord.setDealTime(date);
                    repaymentSerialRecord.setTransferAccountName(cellList.get(5));
                    repaymentSerialRecord.setTransferMoney(new BigDecimal(cellList.get(6)));
                    repaymentSerialRecord.setTransferMark(cellList.get(11));
                    repaymentSerialRecordList.add(repaymentSerialRecord);
                }catch (Exception e){
                    logger.error("数据出错：{}",e);
                    businessVo.setCode(BusinessConstantsUtils.DATA_FORMATE_ERROR_CODE);
                    businessVo.setMessage(BusinessConstantsUtils.DATA_FORMATE_ERROR_DESC);
                    return false;
                }
            }
            if(cellList.get(0).equals("账务流水号")){
                if("业务流水号".equals(cellList.get(1))&&
                        "发生时间".equals(cellList.get(4))&&
                        "对方账号".equals(cellList.get(5))&&
                        "收入金额（+元）".equals(cellList.get(6))&&
                        "备注".equals(cellList.get(11))
                        ){
                    flag=true;
                }else {
                    businessVo.setCode(BusinessConstantsUtils.FILE_FORMATE_ERROR_CODE);
                    businessVo.setCode(BusinessConstantsUtils.FILE_FORMATE_ERROR_DESC);
                    return false;
                }
            }
            line=csvFileUtil.readLine();
        }
        return true;
    }

    /**
     * excel 解析
     * @param businessVo
     * @param file
     * @param repaymentSerialRecordList
     * @return
     * @throws IOException
     */
    public static boolean xlsx(BusinessVo<String> businessVo,MultipartFile file,List<RepaymentSerialRecord> repaymentSerialRecordList ) throws IOException {
        InputStream inputStream =null;
        Workbook workbook = null;
        try {
            inputStream=file.getInputStream();
            workbook = new HSSFWorkbook(inputStream);
        } catch (Exception ex) {
            // 解决read error异常
            inputStream=file.getInputStream();
            workbook = new XSSFWorkbook(inputStream);
        }

        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet sheetAt = workbook.getSheetAt(numSheet);
            if(sheetAt==null){
                continue;
            }
            //处理当前页，循环读取每一行
            boolean flag = false;
            for (int rowNum = 0; rowNum <sheetAt.getLastRowNum() ; rowNum++) {
                Row row = sheetAt.getRow(rowNum);
                int firstCellNum = row.getFirstCellNum();
                Cell cell = row.getCell(firstCellNum);
                if(cell!=null){
                    String stringCellValue = cell.getStringCellValue();
                    if(stringCellValue.contains("账务明细列表结束")){
                        flag=false;
                        break;
                    }
                    if(flag){
                        //录入开始
                        try {
                            RepaymentSerialRecord repaymentSerialRecord = new RepaymentSerialRecord();
                            repaymentSerialRecord.setAccountingSerialNo(row.getCell(0).getStringCellValue());
                            repaymentSerialRecord.setDealSerialNo(row.getCell(1).getStringCellValue());
                            repaymentSerialRecord.setDealTime(row.getCell(4).getDateCellValue());
                            repaymentSerialRecord.setTransferAccountName(row.getCell(5).getStringCellValue());
                            repaymentSerialRecord.setTransferMoney(new BigDecimal(row.getCell(6).getNumericCellValue()));
                            repaymentSerialRecord.setTransferMark(row.getCell(11).getStringCellValue());
                            repaymentSerialRecordList.add(repaymentSerialRecord);
                        }catch (Exception e){
                            logger.error("数据出错：{}",e);
                            businessVo.setCode(BusinessConstantsUtils.DATA_FORMATE_ERROR_CODE);
                            businessVo.setMessage(BusinessConstantsUtils.DATA_FORMATE_ERROR_DESC);
                            return false;
                        }
                    }
                    if(stringCellValue.equals("账务流水号")){
                        if("业务流水号".equals(row.getCell(1).getStringCellValue())&&
                                "发生时间".equals(row.getCell(4).getStringCellValue())&&
                                "对方账号".equals(row.getCell(5).getStringCellValue())&&
                                "收入金额（+元）".equals(row.getCell(6).getStringCellValue())&&
                                "备注".equals(row.getCell(11).getStringCellValue())
                                ){
                            flag=true;
                        }else {
                            businessVo.setCode(BusinessConstantsUtils.FILE_FORMATE_ERROR_CODE);
                            businessVo.setCode(BusinessConstantsUtils.FILE_FORMATE_ERROR_DESC);
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}
