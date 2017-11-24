package com.egojit.easyweb.upm.service;

import com.egojit.easyweb.common.base.BaseService;
import com.egojit.easyweb.upms.dao.mapper.SysUserMapper;
import com.egojit.easyweb.upms.model.SysUser;
import org.springframework.stereotype.Service;

/**
 * Created by egojit on 2017/11/23.
 */
@Service
public class SysUserService extends BaseService<SysUserMapper,SysUser>{


    /**
     * 用户用户名获取用户
     * @param loginName
     * @return
     */
    public SysUser getByLoginName(String loginName){
        SysUser user=new SysUser();
        user.setLoginName(loginName);
        return mapper.getByLoginName(user);
    }
}
