
/*
 * Copyright(c) 2017 千里授渔 All rights reserved.
 * 关注微信公众号【千里授渔】
 */
package com.egojit.easyweb.upms.dao.mapper;

import com.egojit.easyweb.upms.model.SysCompanyDic;
import io.swagger.models.auth.In;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Description：
 * Auther：高露
 * Q Q:408365330
 * Company: 鼎斗信息技术有限公司
 * Time:2018-4-25
 */
public interface SysCompanyDicMapper extends Mapper<SysCompanyDic> {

    /**
     * 获取企业字典
     * @param parm
     * @return
     */
    List<Map> selectByCompany(Map parm);

    /**
     * 获取企业数量
     * @param parm
     * @return
     */
    Integer selectCountByCompany(Map parm);

    /**
     * 获取公司可选择的字典
     * @param companyId
     * @return
     */
    List<Map>  getCanSelectDic(String companyId);

    /**
     * 获取企业所有字典
     * @param companyId
     * @return
     */
    List<Map> selectAllByCompany(String companyId);
}