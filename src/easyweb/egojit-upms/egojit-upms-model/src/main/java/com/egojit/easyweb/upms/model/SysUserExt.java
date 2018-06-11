package com.egojit.easyweb.upms.model;

import com.egojit.easyweb.common.base.Entity.BaseDomainEntity;
import com.egojit.easyweb.common.utils.StringUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;

@Table(name = "sys_user")
public class SysUserExt extends BaseDomainEntity {

    public SysUserExt(){
        super();
    }
    public SysUserExt(String id) {
        super(id);
    }

    /**
     * 归属公司
     */
    @Column(name = "company_id")
    private String companyId;
    /**
     * 所属车队
     */
    private String carTeamId;
    /**
     * 归属部门
     */
    @Column(name = "office_id")
    private String officeId;

    /**
     * 登录名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 工号
     */
    private String no;

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 用户类型
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * 用户头像
     */
    private String photo;

    /**
     * 最后登陆IP
     */
    @Column(name = "login_ip")
    private String loginIp;

    /**
     * 最后登陆时间
     */
    @Column(name = "login_date")
    private Date loginDate;

    /**
     * 是否可登录
     */
    @Column(name = "login_flag")
    private String loginFlag;




    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 用户公司
     */
    private Map company;

    /**
     * 用户部门
     */
    private Map department;


    /**
     * 创建人
     */
    private Map createUser;



//    private List<SysRole> roleList= new ArrayList<SysRole>();

    public  boolean isAdmin(){
        if(StringUtils.isEmpty(userType)||userType=="0"){
            return false;
        }else
            return true;
    }

//    public List<SysRole> getRoleList() {
//        return roleList;
//    }
//
//    public void setRoleList(List<SysRole> roleList) {
//        this.roleList = roleList;
//    }

    /**
     * 获取归属公司
     *
     * @return company_id - 归属公司
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * 设置归属公司
     *
     * @param companyId 归属公司
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取归属部门
     *
     * @return office_id - 归属部门
     */
    public String getOfficeId() {
        return officeId;
    }

    /**
     * 设置归属部门
     *
     * @param officeId 归属部门
     */
    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    /**
     * 获取登录名
     *
     * @return login_name - 登录名
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置登录名
     *
     * @param loginName 登录名
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取工号
     *
     * @return no - 工号
     */
    public String getNo() {
        return no;
    }

    /**
     * 设置工号
     *
     * @param no 工号
     */
    public void setNo(String no) {
        this.no = no;
    }

    /**
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取电话
     *
     * @return phone - 电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话
     *
     * @param phone 电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取手机
     *
     * @return mobile - 手机
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机
     *
     * @param mobile 手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取用户类型
     *
     * @return user_type - 用户类型
     */
    public String getUserType() {
        return userType;
    }

    /**
     * 设置用户类型
     *
     * @param userType 用户类型
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * 获取用户头像
     *
     * @return photo - 用户头像
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * 设置用户头像
     *
     * @param photo 用户头像
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * 获取最后登陆IP
     *
     * @return login_ip - 最后登陆IP
     */
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * 设置最后登陆IP
     *
     * @param loginIp 最后登陆IP
     */
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    /**
     * 获取最后登陆时间
     *
     * @return login_date - 最后登陆时间
     */
    public Date getLoginDate() {
        return loginDate;
    }

    /**
     * 设置最后登陆时间
     *
     * @param loginDate 最后登陆时间
     */
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    /**
     * 获取是否可登录
     *
     * @return login_flag - 是否可登录
     */
    public String getLoginFlag() {
        return loginFlag;
    }

    /**
     * 设置是否可登录
     *
     * @param loginFlag 是否可登录
     */
    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }





    /**
     * 设置更新时间
     *
     * @param updateDate 更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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

    public String getCarTeamId() {
        return carTeamId;
    }

    public void setCarTeamId(String carTeamId) {
        this.carTeamId = carTeamId;
    }

    public Map getCompany() {
        return company;
    }

    public void setCompany(Map company) {
        this.company = company;
    }

    public Map getDepartment() {
        return department;
    }

    public void setDepartment(Map department) {
        this.department = department;
    }

    public Map getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Map createUser) {
        this.createUser = createUser;
    }
}