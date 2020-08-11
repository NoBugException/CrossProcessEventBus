package com.nobugexception.hermes.hermes;

/**
 * 返回码汇总
 */
public class ResultCode {

    public static final int SUCCESS_CODE = 0;

    public static final int ERROR_CODE_1 = -1;
    public static final String ERROR_MSG_1 = "服务未连接";

    public static final int ERROR_CODE_2 = -2;
    public static final String ERROR_MSG_2 = "服务无响应";

    public static final int ERROR_CODE_3 = -3;
    public static final String ERROR_MSG_3 = "请求为空";

    public static final int ERROR_CODE_4 = -4;
    public static final String ERROR_MSG_4 = "参数错误";

    public static final int ERROR_CODE_5 = -5;
    public static final String ERROR_MSG_5 = "服务找不到该请求";
}
