package com.lucky.service.base.util;

import java.util.UUID;

/**
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/15 09:43
 *
 */
public class UUIDUtils {

    public static String generateUUID32(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static String generateUUID36(){
        return UUID.randomUUID().toString();
    }
}
