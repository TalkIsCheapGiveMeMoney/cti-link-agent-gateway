package com.tinet.ctilink.agentgateway.inc;



import java.util.HashMap;
import java.util.Map;

import com.tinet.ctilink.agentgateway.inc.ErrorMsg;
import com.tinet.ctilink.agentgateway.inc.Variable;
import com.tinet.ctilink.agentgateway.inc.SocketConst;
import org.apache.commons.collections.MapUtils;


/**
 ***********************************************
 * @Title     ActionErrorUtil.java
 * @Pageage   com.tinet.ccic.ami				   
 * @author    罗尧   Email:j2ee.xiao@gmail.com 
 * @since 1.0 创建时间 2012-3-31 下午3:25:47		   
 ***********************************************
 */
public class ActionErrorUtil {
	/**
	 * 根据不同登录类型返回密码错误信息
	 */
	public static Map<String,Object> pwdErrorMsg(Map<String, Object> msg ,String hotline, String cno){
		Map<String,Object> response = createFailResponse(msg, ErrorMsg.ERRORCODE_BAD_USERNAME_PASSWORD,"账号或密码不对");
		response.put(Variable.VARIABLE_HOTLINE, hotline);
		response.put(Variable.VARIABLE_CNO, cno);
		return response;
	}

	/**
	 * 根据不同登录类型返回密码错误信息
	 */
	public static Map<String,Object> paramErrorMsg(Map<String,Object> msg,String resMsg, String hotline,String cno){
			Map<String,Object> response = createFailResponse(msg, ErrorMsg.ERRORCODE_EXCEPTION,resMsg);
			response.put(Variable.VARIABLE_HOTLINE, hotline);
			response.put(Variable.VARIABLE_CNO, cno);
			return response;
	}

	/**
	 * 返回绑定电话错误信息
	 */
	public static String backendBindTypeErrorMsg(Map<String,Object> msg ,String checkTelType,String cno){
		String typeAndCno = checkTelType;
		checkTelType = typeAndCno.substring(0, 1);
		String cnoUse = typeAndCno.substring(1);
		if (ErrorMsg.NUMBER_FORMAT_ERROR.equals(checkTelType)) {
			return "号码格式不正确";
		} else if (ErrorMsg.NUMBER_IN_USE.equals(checkTelType)) {
			return "号码已经被座席" + cnoUse + "使用";
		} else if (ErrorMsg.NUMBER_NOT_EXIST.equals(checkTelType)) {
			return "分机号码不存在";
		}else if (ErrorMsg.NUMBER_NOT_ROUTED.equals(checkTelType)) {
			return "此号码无对应路由";
		}
		return "";
	}

	/**
	 * 返回绑定电话错误信息
	 */
	public static Map<String,Object> frontendBindTypeErrorMsg(Map<String,Object> msg ,String checkTelType,String cno){
		Map<String,Object> response = null; 
		String typeAndCno = checkTelType;
		checkTelType = typeAndCno.substring(0, 1);
		String cnoUse = typeAndCno.substring(1);
		if (ErrorMsg.NUMBER_FORMAT_ERROR.equals(checkTelType)) {
			response = createFailResponse(msg, ErrorMsg.ERRORCODE_BAD_BIND_TEL, "号码格式不正确");
			response.put(Variable.VARIABLE_CNO, cno);
			response.put(SocketConst.ERROR_MSG, "号码格式不正确");
		} else if (ErrorMsg.NUMBER_IN_USE.equals(checkTelType)) {
			response = createFailResponse(msg, ErrorMsg.ERRORCODE_BAD_BIND_TEL, "号码已经被座席" + cnoUse + "使用");
			response.put(Variable.VARIABLE_CNO, cno);
			response.put(SocketConst.ERROR_MSG, "号码已经被座席" + cnoUse + "使用");
		} else if (ErrorMsg.NUMBER_NOT_EXIST.equals(checkTelType)) {
			response = createFailResponse(msg, ErrorMsg.ERRORCODE_BAD_BIND_TEL, "分机号码不存在");
			response.put(Variable.VARIABLE_CNO, cno);
			response.put(SocketConst.ERROR_MSG, "分机号码不存在");
		}else if (ErrorMsg.NUMBER_NOT_ROUTED.equals(checkTelType)) {
			response = createFailResponse(msg, ErrorMsg.ERRORCODE_BAD_BIND_TEL, "此号码无对应路由");
			response.put(Variable.VARIABLE_CNO, cno);
			response.put(SocketConst.ERROR_MSG, "此号码无对应路由");
		}
			
		return response;
	}

	static public Map<String,Object> createFailResponse(Map<String, Object> msg , int code , String resMsg){
		Map<String,Object> response = new HashMap<String,Object>();
		response.put(Variable.VARIABLE_TYPE, "response");
		String resType = MapUtils.getString(msg, Variable.VARIABLE_TYPE);
		String utid = MapUtils.getString(msg, Variable.VARIABLE_UTID);
		response.put(Variable.VARIABLE_CODE, "" + code);
		response.put(Variable.VARIABLE_MSG, resMsg);
		if (resType != null) {
			response.put(Variable.VARIABLE_RES_TYPE, resType);
		}
		if(utid != null){
			response.put(Variable.VARIABLE_UTID, utid);
		}	
		return response;
	}
	
	static public Map<String,Object> createSuccessResponse(Map<String, Object> msg){
		Map<String,Object> response = new HashMap<String,Object>();
		response.put(Variable.VARIABLE_TYPE, "response");
		String resType = MapUtils.getString(msg, Variable.VARIABLE_TYPE);
		String utid = MapUtils.getString(msg, Variable.VARIABLE_UTID);
		response.put(Variable.VARIABLE_CODE, "0");
		response.put(Variable.VARIABLE_MSG, "ok");
		if (resType != null) {
			response.put(Variable.VARIABLE_RES_TYPE, resType);
		}
		if(utid != null){
			response.put(Variable.VARIABLE_UTID, utid);
		}
		return response;
	}
}
