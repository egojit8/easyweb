package com.egojit.easyweb.upm.service;

import com.egojit.easyweb.common.base.BaseService;
import com.egojit.easyweb.common.base.CurdService;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.common.models.Company;
import com.egojit.easyweb.common.utils.MD5Util;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.tire.dao.mapper.TireCarTeamMapper;
import com.egojit.easyweb.tire.model.TireCarTeam;
import com.egojit.easyweb.upms.dao.mapper.SysOfficeMapper;
import com.egojit.easyweb.upms.dao.mapper.SysUserMapper;
import com.egojit.easyweb.upms.model.SysOffice;
import com.egojit.easyweb.upms.model.SysUser;
import com.egojit.easyweb.upms.model.SysUserExt;
import com.github.pagehelper.PageHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.Iterator;
import java.util.List;

/**
 * Created by egojit on 2017/11/23.
 */
@Service
public class SysUserService extends CurdService<SysUserMapper,SysUser> {

    @Autowired
    private SysOfficeMapper officeMapper;

    @Autowired
    private TireCarTeamMapper tireCarTeamMapper;

    @Override
    public SysUser selectByPrimaryKey(Object o) {
        SysUser sysUser= super.selectByPrimaryKey(o);
        SysOffice cp=officeMapper.selectByPrimaryKey(sysUser.getCompanyId());
        if(cp!=null){
            sysUser.setCompanyName(cp.getName());
        }
        TireCarTeam  carTeam=tireCarTeamMapper.selectByPrimaryKey(sysUser.getCarTeamId());
        if(carTeam!=null){
            sysUser.setCarTeamName(carTeam.getName());
        }
        SysOffice dp=officeMapper.selectByPrimaryKey(sysUser.getOfficeId());
        if(dp!=null){
            sysUser.setDepartmentName(dp.getName());
        }
        SysUser user=this.mapper.selectByPrimaryKey(sysUser.getCreateBy());
        if(user!=null){
            sysUser.setCreateByName(user.getName());
        }
        return sysUser;
    }

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

    /**
     * 修改密码
     * @param ids
     * @return
     */
    public boolean changePwds(String[] ids,String pwd){
        int count=0;
        if(ids!=null){
            for (String id:ids){
                if(StringUtils.isNotEmpty(id)) {
                    SysUser user =   mapper.selectByPrimaryKey(id);
                    String ecPwd= MD5Util.shiroPwd(pwd,user.getLoginName());
                    user.setPassword(ecPwd);
                    count += mapper.updateByPrimaryKeySelective(user);
                }
            }
        }
        return count>0?true:false;
    }


    /**
     * 查询
     * @return
     */
    public List<SysUserExt> select(Example example){
        return mapper.selectExtByExample(example);
    }

    @Override
    public Page<SysUser> selectPageByExample(Example example, Page<SysUser> page) {
        page= super.selectPageByExample(example, page);
        if(page.getRows()!=null)
        {
            //Iterator迭代器
            //1、获取迭代器
            Iterator iter = page.getRows().iterator();
            //2、通过循环迭代
            //hasNext():判断是否存在下一个元素
            while(iter.hasNext()){
                //如果存在，则调用next实现迭代
                //Object-->Integer-->int
                SysUser u=(SysUser) iter.next();  //把Object型强转成int型
                SysOffice cp=officeMapper.selectByPrimaryKey(u.getCompanyId());
                if(cp!=null){
                    u.setCompanyName(cp.getName());
                }
                TireCarTeam  carTeam=tireCarTeamMapper.selectByPrimaryKey(u.getCarTeamId());
                if(carTeam!=null){
                    u.setCarTeamName(carTeam.getName());
                }
                SysOffice dp=officeMapper.selectByPrimaryKey(u.getOfficeId());
                if(dp!=null){
                    u.setDepartmentName(dp.getName());
                }
            }
        }
        return page;
    }


    //    /**
//     * 根据对象条件查询分页数据-返回page对象
//     * @param example 对象
//     * @return 返回page对象
//     */
//
//    public Page<SysUserExt> selectExtPageByExample(Example example, Page<SysUserExt> page) {
//        if(!page.isDisabled()) {
//            long count= this.selectCountByExample(example);
//            page.setRecords(count);
//            PageHelper.startPage(page.getPage(), page.getPageSize());
//        }
//
//        List<SysUserExt> list=mapper.selectExtByExample(example);
//        return page.setList(list);
//
//    }

}
