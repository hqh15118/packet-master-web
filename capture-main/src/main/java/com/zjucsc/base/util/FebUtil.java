package com.zjucsc.base.util;

import com.zjucsc.base.domain.BaseConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * #project spring-boot-starter
 *
 * @author hongqianhui
 * #create_time 2019-04-28 - 23:40
 */
@Slf4j
public class FebUtil {
    /**
     * token 加密
     *
     * @param token token
     * @return 加密后的 token
     */
    public static String encryptToken(String token) {
        try {
            EncryptUtil encryptUtil = new EncryptUtil(BaseConstant.TOKEN_CACHE_PREFIX);
            return encryptUtil.encrypt(token);
        } catch (Exception e) {
            log.info("token加密失败：", e);
            return null;
        }
    }

    /**
     * token 解密
     *
     * @param encryptToken 加密后的 token
     * @return 解密后的 token
     */
    public static String decryptToken(String encryptToken) {
        try {
            EncryptUtil encryptUtil = new EncryptUtil(BaseConstant.TOKEN_CACHE_PREFIX);
            return encryptUtil.decrypt(encryptToken);
        } catch (Exception e) {
            log.info("token解密失败：", e);
            return null;
        }
    }
}
