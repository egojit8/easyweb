package com.egojit.easyweb.upms.model;

import com.egojit.easyweb.common.base.BaseEntity;
import com.egojit.easyweb.common.base.CurdEndity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "sys_role")
public class SysRole extends CurdEndity {


    /**
     * 归属机构
     */
    @Column(name = "office_id")
    private String officeId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 英文名称
     */
    private String enname;

    /**
     * 角色类型
     */
    @Column(name = "role_type")
    private String roleType;

    /**
     * 数据范围
     */
    @Column(name = "data_scope")
    private String dataScope;

    /**
     * 是否系统数据
     */
    @Column(name = "is_sys")
    private String isSys;

    /**
     * 是否可用
     */
    private String useable;






    /**
     * 备注信息
     */
    private String remarks;



    /**
     * 获取归属机构
     *
     * @return office_id - 归属机构
     */
    public String getOfficeId() {
        return officeId;
    }

    /**
     * 设置归属机构
     *
     * @param officeId 归属机构
     */
    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    /**
     * 获取角色名称
     *
     * @return name - 角色名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置角色名称
     *
     * @param name 角色名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取英文名称
     *
     * @return enname - 英文名称
     */
    public String getEnname() {
        return enname;
    }

    /**
     * 设置英文名称
     *
     * @param enname 英文名称
     */
    public void setEnname(String enname) {
        this.enname = enname;
    }

    /**
     * 获取角色类型
     *
     * @return role_type - 角色类型
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * 设置角色类型
     *
     * @param roleType 角色类型
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    /**
     * 获取数据范围
     *
     * @return data_scope - 数据范围
     */
    public String getDataScope() {
        return dataScope;
    }

    /**
     * 设置数据范围
     *
     * @param dataScope 数据范围
     */
    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    /**
     * 获取是否系统数据
     *
     * @return is_sys - 是否系统数据
     */
    public String getIsSys() {
        return isSys;
    }

    /**
     * 设置是否系统数据
     *
     * @param isSys 是否系统数据
     */
    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }

    /**
     * 获取是否可用
     *
     * @return useable - 是否可用
     */
    public String getUseable() {
        return useable;
    }

    /**
     * 设置是否可用
     *
     * @param useable 是否可用
     */
    public void setUseable(String useable) {
        this.useable = useable;
    }



    /**
     * 获取备注信息
     *
     * @return remarks - 备注信息
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注信息
     *
     * @param remarks 备注信息
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}