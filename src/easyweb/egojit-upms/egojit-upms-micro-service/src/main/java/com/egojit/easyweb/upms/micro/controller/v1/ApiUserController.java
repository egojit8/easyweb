package com.egojit.easyweb.upms.micro.controller.v1;

import com.egojit.easyweb.common.base.BaseApiController;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.models.Company;
import com.egojit.easyweb.common.models.User;
import com.egojit.easyweb.common.utils.MD5Util;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.common.utils.UserUtil;
import com.egojit.easyweb.upm.service.SysOfficeService;
import com.egojit.easyweb.upm.service.SysUserService;
import com.egojit.easyweb.upms.model.SysDict;
import com.egojit.easyweb.upms.model.SysOffice;
import com.egojit.easyweb.upms.model.SysUser;
import com.egojit.easyweb.upms.model.SysUserExt;
import com.egojit.easyweb.upms.model.vo.SysUserEditPwdVo;
import com.egojit.easyweb.upms.model.vo.SysUserLoginVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Description：字典接口
 * Auther：高露
 * Q Q:408365330
 * Company: 鼎斗信息技术有限公司
 * Time:2018-4-25
 */
@RestController
@RequestMapping("/api/upms/v1/user")
public class ApiUserController extends BaseApiController {

    @Autowired
    private SysUserService service;
    @Autowired
    private SysOfficeService officeService;



    /**
     * 字典接口
     *
     * @return
     */
    @PostMapping("/login")
    public BaseResult login(@RequestBody SysUserLoginVo parm) {
//        Object obj = new SimpleHash(MD5Util.HASH_ALGORITHM, credentials, credentialsSalt, MD5Util.HASH_INTERATIONS);
        BaseResult result = new BaseResult(BaseResultCode.EXCEPTION, "服务器错误！注册失败！");
        if (StringUtils.isEmpty(parm.getLoginName())) {
            result = new BaseResult(BaseResultCode.ARGUMENT, "账号不能为空！");
            return result;
        }
        if (StringUtils.isEmpty(parm.getPassword())) {
            result = new BaseResult(BaseResultCode.ARGUMENT, "密码不能为空！");
            return result;
        }
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loginName", parm.getLoginName());
        List<SysUser> users = service.selectByExample(example);
        String pwd = MD5Util.shiroPwd(parm.getPassword(), parm.getLoginName());
        if (users == null || users.size() == 0) {
            result = new BaseResult(BaseResultCode.EXCEPTION, "用户不存在！");
        } else if (!pwd.equals(users.get(0).getPassword())) {
            result = new BaseResult(BaseResultCode.EXCEPTION, "密码不正确！");
        } else {
            SysUser userExt= users.get(0);
            User user=new User();
            user.setEmail(userExt.getEmail());
            user.setLoginDate(userExt.getLoginDate());
            user.setLoginName(userExt.getLoginName());
            user.setMobile(userExt.getMobile());
            user.setName(userExt.getName());

            user.setPhone(userExt.getPhone());
            user.setPhoto(userExt.getPhoto());
            user.setCarTeamId(userExt.getCarTeamId());
            user.setId(userExt.getId());

            try {

                SysOffice cp=officeService.selectByPrimaryKey(userExt.getCompanyId());
                if(cp!=null){
                    Company company=new Company();
                    BeanUtils.copyProperties(company,cp);
                    user.setCompany(company);
                }

                SysOffice dp=officeService.selectByPrimaryKey(userExt.getOfficeId());
                if(dp!=null){
                    Company department=new Company();
                    BeanUtils.copyProperties(department,dp);
                    user.setDepartment(department);
                }

                UserUtil.saveUser(user);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            result = new BaseResult(BaseResultCode.SUCCESS,user);
        }
        return result;
    }

    /**
     * 修改资料
     *
     * @return
     */
    @PostMapping("/edit")
    public BaseResult edit(@RequestBody SysUser parm) {
        BaseResult result = new BaseResult(BaseResultCode.SUCCESS, "修改成功！");
        if (StringUtils.isEmpty(parm.getId())) {
            result = new BaseResult(BaseResultCode.ARGUMENT, "用户id不能为空！");
            return result;
        }
        if (StringUtils.isNotEmpty(parm.getPassword())) {
            if (StringUtils.isNotEmpty(parm.getLoginName())) {
                String pwd = MD5Util.shiroPwd(parm.getPassword(), parm.getLoginName());
                parm.setPassword(pwd);
            } else {
                result = new BaseResult(BaseResultCode.EXCEPTION, "登录名不能为空！");
            }
        }
        service.updateByPrimaryKeySelective(parm);
        return result;
    }

    /**
     * 修改密码
     *
     * @return
     */
    @PostMapping("/editPwd")
    public BaseResult editPwd(@RequestBody SysUserEditPwdVo parm) {
        BaseResult result = new BaseResult(BaseResultCode.SUCCESS, "修改成功！");
        SysUser editUser=new SysUser();
        if (StringUtils.isEmpty(parm.getLoginName())) {
            result = new BaseResult(BaseResultCode.ARGUMENT, "账户不能为空！");
            return result;
        }
        if (StringUtils.isEmpty(parm.getOldPwd())) {
            result = new BaseResult(BaseResultCode.ARGUMENT, "原密码不能为空！");
            return result;
        }
        if (StringUtils.isEmpty(parm.getNewPwd())) {
            result = new BaseResult(BaseResultCode.ARGUMENT, "新密码不能为空！");
            return result;
        }
        SysUser user = service.getByLoginName(parm.getLoginName());
        if(user==null){
            result = new BaseResult(BaseResultCode.ARGUMENT, "登录名不存在！");
        }else {
            editUser.setId(user.getId());
            String pwd = MD5Util.shiroPwd(parm.getOldPwd(), parm.getLoginName());
            if(!pwd.equals(user.getPassword())){
                result = new BaseResult(BaseResultCode.ARGUMENT, "原密码错误,请重试！");
            }else {
                String newPwd = MD5Util.shiroPwd(parm.getNewPwd(), parm.getLoginName());
                editUser.setPassword(newPwd);
                service.updateByPrimaryKeySelective(editUser);
            }
        }
        return result;
    }
}
