package com.egojit.easyweb;

import com.alibaba.fastjson.JSON;
import com.egojit.easyweb.upms.model.SysUser;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 备注：OtherTest
 * 作者：egojit
 * 日期：2017/11/27
 */
public class OtherTest {

    @Test
    public void testFastJson() {
        SysUser user = JSON.parseObject("",SysUser.class);
        Assert.assertNull(user);
    }


}
