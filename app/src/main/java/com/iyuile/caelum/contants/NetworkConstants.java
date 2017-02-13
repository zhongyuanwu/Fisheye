package com.iyuile.caelum.contants;

/**
 * 
 * @Description 网络常量
 * @author WangYao
 * @version 1
 * @date 2015-10-15 15:47:48
 */
public interface NetworkConstants {

	int RESPONSE_CODE_NOT_NETWORKAVAILABLE_0 = 0;// 没有网络可用

	/**
	 * 1XX信息化
	 * */
	int RESPONSE_CODE_CONTINUE_100 = 100;// 继续
	int RESPONSE_CODE_SWITCHING_PROTOCOLS_101 = 101;// 交换协议
	int RESPONSE_CODE_PROCESSING_101 = 101;// 处理控制（WebDAV）

	/**
	 * 2XX成功
	 * */
	int RESPONSE_CODE_OK_200 = 200;// 成功
	int RESPONSE_CODE_CREATED_201 = 201;// 创建成功
	int RESPONSE_CODE_ACCEPTED_202 = 202;// 公认
	int RESPONSE_CODE_NO_CONTENT_204 = 204;// 没有内容(没有创建)

	/**
	 * 3XX重定向
	 * */
	int RESPONSE_CODE_MULTIPLE_CHOICES_300 = 300;// 多重选择
	int RESPONSE_CODE_MOVED_PERMANENTLY_301 = 301;// 永久移动
	int RESPONSE_CODE_FOUND_302 = 302;// 发现
	int RESPONSE_CODE_SEE_OTHER_303 = 303;// 查看其它
	int RESPONSE_CODE_NOT_MODIFIED_304 = 304;// 未修改
	int RESPONSE_CODE_USE_PROXY_305 = 305;// 使用代理服务器
	int RESPONSE_CODE_TEMPORARY_REDIRECT_307 = 307;// 临时重定向
	int RESPONSE_CODE_PERMANENT_REDIRECT_308 = 308;// 永久重定向（了实验性）

	/**
	 * 4XX客户端错误
	 * */
	int RESPONSE_CODE_BAD_REQUEST_400 = 400;// 坏请求(短信验证码发送失败;)
	int RESPONSE_CODE_UNAUTHORIZED_401 = 401;// 未授权
	int RESPONSE_CODE_FORBIDDEN_403 = 403;// 被禁止
	int RESPONSE_CODE__NOT_FOUND_404 = 404;// 未发现
	int RESPONSE_CODE_METHOD_NOT_ALLOWED_405 = 405;// 不允许的方法
	int RESPONSE_CODE_UNPROCESSABLE_ENTITY_422 = 422;// 无法处理的实体

	/**
	 * 5XX服务器错误
	 * */
	int RESPONSE_CODE_INTERNAL_SERVER_ERROR_500 = 500;// 内部服务器错误
	int RESPONSE_CODE_SERVICE_UNAVAILABLE_503 = 503;// 暂停服务(没有网络也报这个)
	int RESPONSE_CODE_GATEWAY_TIMEOUT_504 = 504;// 网关超时(没有网络也报这个)

	int RESPONSE_CODE_SQL_STATE_1045 = 1045;// 内部服务器错误(数据库问题)

	String API_HTTP = "http://";
	String API_LOCATION = "api.caelum.iyuile.com";
	String API_URL = API_HTTP + API_LOCATION;
	String API_IP = "115.28.138.71";

//	// 访问不到服务器
//	String RESPONSE_CODE_NOT_NETWORKAVAILABLE_MESSAGE_1 = "UnknownHostException exception: Unable to resolve host \""+API_LOCATION+"\": No address associated with hostname";
//	String RESPONSE_CODE_NOT_NETWORKAVAILABLE_MESSAGE_2 = "org.apache.http.conn.HttpHostConnectException: Connection to "+API_URL+" refused";
//	String RESPONSE_CODE_NOT_NETWORKAVAILABLE_MESSAGE_3 = "org.apache.http.conn.ConnectTimeoutException: Connect to /"+API_IP+" timed out";// http 请求使用
////	String RESPONSE_CODE_NOT_NETWORKAVAILABLE_MESSAGE_3 = "org.apache.http.conn.ConnectTimeoutException: Connect to /"+API_IP+":443 timed out";// https 请求使用
//	String RESPONSE_CODE_NOT_NETWORKAVAILABLE_MESSAGE_4 = "failed to connect to api.apus.iyuile.com/"+API_IP+" (port 80) after 5000ms: isConnected failed: ENETUNREACH (Network is unreachable)";// http 请求使用
////	String RESPONSE_CODE_NOT_NETWORKAVAILABLE_MESSAGE_4 = "failed to connect to api.apus.iyuile.com/"+API_IP+" (port 443) after 5000ms: isConnected failed: ENETUNREACH (Network is unreachable)";// https 请求使用

	/**
	 * 令牌
	 */
	String _PARAM_AUTHORIZATION = "Authorization";
	String _VALUE_AUTHORIZATION = "Bearer %s";// param=access_token

	String _PARAM_ACCEPT = "Accept";
	String _VALUE_ACCEPT = "application/vnd.Caelum.v1+json";

}
