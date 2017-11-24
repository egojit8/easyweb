package com.egojit.easyweb.upms.sso;

import com.egojit.easyweb.common.utils.CacheUtils;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by egojit on 2017/10/24.
 */
public class LoginUtil {
    /**
     * 是否是验证码登录
     * @param useruame 用户名
     * @param isFail 计数加1
     * @param clean 计数清零
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
        Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
        if (loginFailMap==null){
            loginFailMap = Maps.newHashMap();
            CacheUtils.put("loginFailMap", loginFailMap);
        }
        Integer loginFailNum = loginFailMap.get(useruame);
        if (loginFailNum==null){
            loginFailNum = 0;
        }
        if (isFail){
            loginFailNum++;
            loginFailMap.put(useruame, loginFailNum);
        }
        if (clean){
            loginFailMap.remove(useruame);
        }
        return loginFailNum >= 3;
    }
}
