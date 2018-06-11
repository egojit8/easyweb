package com.egojit.easyweb.upms.micro.controller.v1;

import com.egojit.easyweb.common.base.BaseApiController;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upm.service.SysCompanyDicService;
import com.egojit.easyweb.upm.service.SysDictService;
import com.egojit.easyweb.upms.model.SysDict;
import com.egojit.easyweb.upms.model.vo.SysDicSearchVo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Description：字典接口
 * Auther：高露
 * Q Q:408365330
 * Company: 鼎斗信息技术有限公司
 * Time:2018-4-25
 */
@RestController
@RequestMapping("/api/upms/v1/dic")
public class ApiDictionaryController extends BaseApiController {

    @Autowired
    private SysDictService service;

    @Autowired
    private SysCompanyDicService sysCompanyDicService;

    /**
     * 字典接口
     *
     * @return
     */
    @GetMapping("/list")
    public BaseResult list(SysDicSearchVo model) {
        BaseResult result = new BaseResult(BaseResultCode.EXCEPTION, "服务器错误！注册失败！");
//        Example example = new Example(SysDict.class);
//        Example.Criteria criteria = example.createCriteria();
//        if (StringUtils.isNotEmpty(model.getType())) {
//            criteria.andEqualTo("type", model.getType());
//        }
//        List<SysDict> dicts = service.selectByExample(example);
        if(StringUtils.isEmpty(model.getCompanyId())){
            result = new BaseResult(BaseResultCode.ARGUMENT, "companyId不能为空！");
            return result;
        }
        List<Map> dicts= sysCompanyDicService.selectAllByCompany(model.getCompanyId());
        result = new BaseResult(BaseResultCode.SUCCESS, dicts);
        return result;
    }


}
