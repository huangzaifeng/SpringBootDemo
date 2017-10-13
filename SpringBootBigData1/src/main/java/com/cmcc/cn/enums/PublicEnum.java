package com.cmcc.cn.enums;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by le on 2017/9/27.
 */
public enum PublicEnum {
    STORM("zookeeper","");

    /**
     * 枚举值
     */
    private final String code;

    /**
     * 枚举描述
     */
    private final String message;

    /**
     * 构造一个<code>LocalCacheEnum</code>枚举对象
     *
     * @param code 枚举值
     * @param message 枚举描述
     */
    private PublicEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * @return Returns the code.
     */
    public String code() {
        return code;
    }

    /**
     * @return Returns the message.
     */
    public String message() {
        return message;
    }

    /**
     * 获取全部枚举
     *
     * @return List<PermitTypeEnum>
     */
    public static List<PublicEnum> getAllEnum() {
        List<PublicEnum> list = new ArrayList<PublicEnum>();
        for (PublicEnum each : values()) {
            list.add(each);
        }
        return list;
    }

    /**
     * 获取全部枚举值
     *
     * @return List<String>
     */
    public static List<String> getAllEnumCode() {
        List<String> list = new ArrayList<String>();
        for (PublicEnum each : values()) {
            list.add(each.code());
        }
        return list;
    }

    public static PublicEnum getByCode(String code) {
        for (PublicEnum _enum : values()) {
            if (StringUtils.equals(_enum.getCode(), code)) {
                return _enum;
            }
        }
        return null;
    }
}
