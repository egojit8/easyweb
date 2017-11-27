package com.egojit.easyweb.common.base;

import com.egojit.easyweb.common.utils.IdGen;

import java.util.Date;

/**
 * 备注：CurdEndity  支持增删改查的对象
 * 作者：egojit
 * 日期：2017/11/27
 */
public class CurdEndity extends BaseEntity{
    public CurdEndity(){
        super();
        this.delFlag=DEL_FLAG_NORMAL;
    }
    public CurdEndity(String id) {
        super(id);
        this.delFlag=DEL_FLAG_NORMAL;
    }


    private String delFlag;
    protected String updateBy;	// 更新者Id
    protected Date updateDate;	// 更新日期

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    public void preUpdate(){
        this.updateDate = new Date();
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    public  void preInsert(){
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        super.preInsert();
        this.updateDate = new Date();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
