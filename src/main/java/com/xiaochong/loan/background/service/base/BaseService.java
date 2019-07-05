package com.xiaochong.loan.background.service.base;

import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;

/**
 * Created by ray.liu on 2017/5/10.
 */
public class BaseService {

    protected <T> BasePageInfoVo<T> assemblyBasePageInfo(PageInfo pageInfo){
        //封装为分页结果
        BasePageInfoVo<T> basePageInfoVo = new BasePageInfoVo<>();
        basePageInfoVo.setCurrentPage(pageInfo.getPageNum());
        basePageInfoVo.setHasNextPage(pageInfo.isHasNextPage());
        basePageInfoVo.setHasPrePage(pageInfo.isHasPreviousPage());
        basePageInfoVo.setTotalPage(pageInfo.getPages());
        basePageInfoVo.setFirstPage(pageInfo.getFirstPage());
        basePageInfoVo.setIsFirstPage(pageInfo.isIsFirstPage());
        basePageInfoVo.setLastPage(pageInfo.getLastPage());
        basePageInfoVo.setIsLastPage(pageInfo.isIsLastPage());
        basePageInfoVo.setNextPage(pageInfo.getNextPage());
        basePageInfoVo.setPrePage(pageInfo.getPrePage());
        basePageInfoVo.setPageSize(pageInfo.getPageSize());
        basePageInfoVo.setTotalRecord(pageInfo.getTotal());
        return basePageInfoVo;
    }


}
