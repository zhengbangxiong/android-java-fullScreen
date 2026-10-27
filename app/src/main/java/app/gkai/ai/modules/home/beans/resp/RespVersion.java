package app.gkai.ai.modules.home.beans.resp;

import lombok.Data;
import lombok.ToString;

/**
 * 响应公共对象
 * 
 * @date: 2016/7/3
 */

@ToString
@Data
public class RespVersion<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * 状态码：200成功
	 */
	private int code = 200;

	/**
	 * 状态编码
	 */
	private String success = "true";


	/**
	 * 国际化提示消息
	 */
	private String message = "成功";


	/**
	 * 业务数据
	 */
	private T result;

}
