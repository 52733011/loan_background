package com.xiaochong.loan.background.service;

import com.xiaochong.loan.background.dao.AttributionInfoDao;
import com.xiaochong.loan.background.entity.po.AttributionInfo;
import com.xiaochong.loan.background.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


@Service("attributionInfoService")
public class AttributionInfoService extends BaseService{
	private Logger logger = LoggerFactory.getLogger(AttributionInfoService.class);

	@Resource(name = "attributionInfoDao")
	private AttributionInfoDao attributionInfoDao;

	@Transactional(rollbackFor = Exception.class)
	public void insertByOSS(String path) throws IOException {
		URL url = new URL(path);
		URLConnection urlConnection = url.openConnection();
		InputStream inputStream = urlConnection.getInputStream();
		List<AttributionInfo> attributionInfos = new ArrayList<>();
		InputStreamReader read = new InputStreamReader( inputStream,"GBK");
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt ;
		int num = 0;
		while((lineTxt = bufferedReader.readLine()) != null){
			num++;
			String[] split = lineTxt.split(",");
			for (int i = 0; i < split.length; i++) {
				split[i] = split[i].replaceAll("\"","");
			}
			AttributionInfo attributionInfo = new AttributionInfo();
			attributionInfo.setId(Integer.parseInt(split[0]));
			attributionInfo.setMobileNum(split[1]);
			attributionInfo.setCity(split[2]);
			attributionInfo.setArea(split[3]);
			attributionInfo.setCarrier(split[4]);
			attributionInfo.setAreaCode(split[5]);
			attributionInfo.setPostCode(split[6]);

			attributionInfos.add(attributionInfo);
			logger.info(lineTxt);
			if(num>=5000){
				logger.info("insert"+attributionInfos.size());
				attributionInfoDao.insertByBatch(attributionInfos);
				num=0;
				attributionInfos = new ArrayList<>();
			}
		}

		read.close();
	}

	public AttributionInfo attributionQuery(String num) {
		AttributionInfo attributionInfo = null;
		if(num.length()==11){
			attributionInfo = new AttributionInfo();
			attributionInfo.setAreaCode(num.substring(0,3));
			List<AttributionInfo> attributionInfos = attributionInfoDao.listByAttributionInfo(attributionInfo);
			if(attributionInfos!=null && attributionInfos.size()>0){
				attributionInfo = attributionInfos.get(0);
			}else{
				attributionInfo = new AttributionInfo();
				attributionInfo.setMobileNum(num.substring(0,7));
				attributionInfo = attributionInfoDao.getByAttributionInfo(attributionInfo);
			}
		}else if(num.length()==12){
			attributionInfo = new AttributionInfo();
			attributionInfo.setAreaCode(num.substring(0,4));
			List<AttributionInfo> attributionInfos = attributionInfoDao.listByAttributionInfo(attributionInfo);
			if(attributionInfos!=null && attributionInfos.size()>0){
				attributionInfo = attributionInfos.get(0);
			}
		}
		return attributionInfo;
	}
}
