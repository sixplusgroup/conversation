package finley.gmair.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class PayUtil {

	/**
	 ** 生成6位或10位随机数 param codeLength(多少位)
	 ** @return     
	 */
	public static String createCode(int codeLength) {
		String code = "";
		for (int i = 0; i < codeLength; i++) {
			code += (int) (Math.random() * 9);
		}
		return code;
	}

	/**
	 * 生成sign
	 * @param sArray
	 * @param key
	 * @return
	 */
	public static String generateSignature(Map<String,String> sArray,String key) {
		if (sArray == null || sArray.size() <= 0) {
			return null;
		}
		StringBuilder sign=new StringBuilder();
		Set<String> params = sArray.keySet();
		String[] sa = {};
		sa = params.toArray(sa);
		Arrays.sort(sa);

		for (String paramKey : sa) {
			String value = sArray.get(paramKey);
			if (StringUtils.isBlank(value)|| paramKey.equals("sign")) {
				continue;
			}
			sign.append(paramKey+"="+value+"&");
		}
		sign.append("key="+key);

		return MD5Util.MD5Encryption(sign.toString(),"UTF-8");
	}

	/**
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方法     
	 * @param outputStr 参数     
	 */
	public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
		// 创建SSLContext 
		StringBuffer buffer = null;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(requestMethod);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			//往服务器端写内容 
			if (null != outputStr) {
				OutputStream os = conn.getOutputStream();
				os.write(outputStr.getBytes("utf-8"));
				os.close();
			}
			// 读取服务器端返回的内容 
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			buffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public static String urlEncodeUTF8(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String mapToXml(Map<String, String> paraMap) {
		StringBuilder paramBuffer=new StringBuilder();
		paramBuffer.append("<xml>");
		if (paraMap.containsKey("appid")) {
			paramBuffer.append("<appid>"+paraMap.get("appid")+"</appid>");
		}
		if (paraMap.containsKey("mch_id")) {
			paramBuffer.append("<mch_id>"+paraMap.get("mch_id")+"</mch_id>");
		}
		if (paraMap.containsKey("nonce_str")) {
			paramBuffer.append("<nonce_str>"+paraMap.get("nonce_str")+"</nonce_str>");
		}
		if (paraMap.containsKey("sign")) {
			paramBuffer.append("<sign>"+paraMap.get("sign")+"</sign>");
		}
		if (paraMap.containsKey("body")) {
			paramBuffer.append("<body>"+paraMap.get("body")+"</body>");
		}
		if (paraMap.containsKey("out_trade_no")) {
			paramBuffer.append("<out_trade_no>"+paraMap.get("out_trade_no")+"</out_trade_no>");
		}
		if (paraMap.containsKey("total_fee")) {
			paramBuffer.append("<total_fee>"+paraMap.get("total_fee")+"</total_fee>");
		}
		if (paraMap.containsKey("spbill_create_ip")) {
			paramBuffer.append("<spbill_create_ip>"+paraMap.get("spbill_create_ip")+"</spbill_create_ip>");
		}
		if (paraMap.containsKey("notify_url")) {
			paramBuffer.append("<notify_url>"+paraMap.get("notify_url")+"</notify_url>");
		}
		if (paraMap.containsKey("trade_type")) {
			paramBuffer.append("<trade_type>"+paraMap.get("trade_type")+"</trade_type>");
		}
		if (paraMap.containsKey("openid")) {
			paramBuffer.append("<openid>"+paraMap.get("openid")+"</openid>");
		}
		if (paraMap.containsKey("return_code")) {
			paramBuffer.append("<return_code>"+paraMap.get("return_code")+"</return_code>");
		}
		if (paraMap.containsKey("return_msg")) {
			paramBuffer.append("<return_msg>"+paraMap.get("return_msg")+"</return_msg>");
		}
		if (paraMap.containsKey("sign_type")) {
			paramBuffer.append("<sign_type>"+paraMap.get("sign_type")+"</sign_type>");
		}
		paramBuffer.append("</xml>");
		return paramBuffer.toString();
	}

	public static String getIpAddress(HttpServletRequest request) {
		// 避免反向代理不能获取真实地址, 取X-Forwarded-For中第一个非unknown的有效IP字符串
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String generateId() {
		String today = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String code=createCode(8);
		return today+code;
	}
}
