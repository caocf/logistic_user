package com.cn.xtouch.fodelforuser.constant;

/**
 * 全局参数类
 * 
 * @author tfl
 * 
 */
public class GlobalParameters {

	/**
	 * 请求状态 账号在另一台机器上登录
	 */
	public static final int STATUS_CODE_401 = 401;
	/**
	 * 请求状态码 成功
	 */
	public static final String STATUS_1001 = "1001";
	/**
	 * 请求状态码 失败
	 */
	public static final String STATUS_1002 = "1002";

	/**
	 * 服务器地址 测试地址 http://192.168.0.104 正式地址 http://customer.mshede.com
	 */
	public final static String BASE_URL = "http://customer.mshede.com";

	/**
	 * 获取我的订单接口地址
	 */
	public final static String GET_MY_ORDERS = BASE_URL
			+ "/waybill/unFinishedList";

	/**
	 * 登录接口地址
	 */
	public final static String LOGIN = BASE_URL + "/user/login?p=";

	/**
	 * 修改派送时间接口地址
	 */
	public final static String MODIFY_DELIVERY_TIME = BASE_URL
			+ "/waybill/modifyDate?p=";

	/**
	 * 获取配送地址接口地址
	 */
	public final static String GET_DELIVERY_ADDRESS = BASE_URL
			+ "/waybill/getAddress";
	/**
	 * 修改配送地址接口
	 */
	public final static String MODIFY_DELIVERY_ADDRESS = BASE_URL
			+ "/waybill/modifyAddress?p=";

	/**
	 * 获取验证码
	 */
	public final static String GET_VERIFICATION_CODE = BASE_URL
			+ "/user/sendVCode?p=";

	/**
	 * 校验验证码
	 */
	public final static String CHECK_VERIFICATION_CODE = BASE_URL
			+ "/user/checkVCode";

	/**
	 * 注册
	 */
	public final static String SIGN_UP = BASE_URL + "/user/signUp?p=";

	/**
	 * 获取订单路由信息接口地址
	 */
	public final static String GET_ROUTE_INFO = BASE_URL
			+ "/waybill/getRouteInfo";
	/**
	 * 找回密码
	 */
	public final static String FIND_PASSWORD = BASE_URL
			+ "/user/forgetPassword";
	/**
	 * 获取历史记录订单
	 */
	public final static String GET_HISTORY_ORDER = BASE_URL
			+ "/waybill/finishList";

	/**
	 * 修改头像
	 */
	public final static String CHANGE_PORTRAIT = BASE_URL
			+ "/user/editPortrait";

	/**
	 * 修改昵称
	 */
	public final static String CHANGE_NAME = BASE_URL + "/user/editInfo";

	/**
	 * 提交评论
	 */
	public final static String POST_COMMENT = BASE_URL + "/comment/post";

	/**
	 * 获取评论
	 */
	public final static String GET_COMMENT_INFO = BASE_URL + "/waybill/detail";
}
