package com.egojit.easyweb.common.base;

import com.egojit.easyweb.common.Global;
import com.egojit.easyweb.common.utils.IdGen;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by egojit on 2017/11/23.
 */
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public BaseEntity(){
        this.id= IdGen.uuid();
        this.delFlag=DEL_FLAG_NORMAL;
    }
    /**
     * id
     */
    private String id;

    protected Date updateDate;	// 更新日期
    protected String delFlag; 	// 删除标记（0：正常；1：删除；2：审核）
    protected String createBy;	// 创建者id
    protected Date createDate;	// 创建日期
    protected String updateBy;	// 更新者Id

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 全局变量对象
     */
    @JsonIgnore
    public Global getGlobal() {
        return Global.getInstance();
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";
}
