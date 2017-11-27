package com.egojit.easyweb.common.base;

import com.egojit.easyweb.common.Global;
import com.egojit.easyweb.common.utils.IdGen;
import com.egojit.easyweb.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by egojit on 2017/11/23.
 */
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public BaseEntity(){

    }
    public BaseEntity(String id) {
        this();
        this.id = id;
    }
    /**
     * id
     */
    @Id
    private String id;
    /**
     * 是否是新记录（默认：true），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     */
    @Transient
    protected boolean isNewRecord = true;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;







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



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
     * 插入之前执行方法，需要手动调用
     */
    public  void preInsert(){
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        if (this.isNewRecord){
            setId(IdGen.uuid());
        }
        this.createDate = new Date();
    }
    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     *
     * @return
     */
    public boolean getIsNewRecord() {
        return isNewRecord || StringUtils.isBlank(getId());
    }

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     */
    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
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
