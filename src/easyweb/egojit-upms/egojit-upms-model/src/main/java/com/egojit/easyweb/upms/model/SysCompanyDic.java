/*
 * Copyright(c) 2017 千里授渔 All rights reserved.
 * 关注微信公众号【千里授渔】
 */
package com.egojit.easyweb.upms.model;

import com.egojit.easyweb.common.base.Entity.BaseDomainEntity;

import javax.persistence.*;
import java.util.Date;
/**
 * Description：
 * Auther：高露
 * Q Q:408365330
 * Company: 鼎斗信息技术有限公司
 * Time:2018-4-25
 */
@Table(name = "sys_company_dic")
public class SysCompanyDic extends BaseDomainEntity{


// 编号
// 企业id
    private String companyId;
// 字典id
    private String dicId;

    public void setCompanyId(String value) {
        this.companyId = value;
    }

    public String getCompanyId() {
        return this.companyId;
    }
    public void setDicId(String value) {
        this.dicId = value;
    }

    public String getDicId() {
        return this.dicId;
    }
}
