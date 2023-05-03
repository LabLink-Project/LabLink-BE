//package com.example.lablink.user.service;
//
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.connection.Message;
//
//import java.util.HashMap;
//
//public class MessageService {
//
//	@Value("${coolsms.devHee.apikey}")
//	private String apiKey;
//
//	@Value("${coolsms.devHee.apisecret}")
//	private String apiSecret;
//
//	@Value("${coolsms.devHee.fromnumber}")
//	private String fromNumber;
//
//	public void sendMessage(String toNumber, String randomNumber) {
//
//		Message coolsms = new Message(apiKey, apiSecret);
//
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("to", toNumber);
//		params.put("from", fromNumber);
//		params.put("type", "SMS");
//		params.put("text", "[grabMe] 인증번호 "+randomNumber+" 를 입력하세요.");
//		params.put("app_version", "test app 1.2"); // application name and version
//
//		try {
//			JSONObject obj = (JSONObject) coolsms.send(params);
//			System.out.println(obj.toString());
//		} catch (CoolsmsException e) {
//			System.out.println(e.getMessage());
//			System.out.println(e.getCode());
//		}
//	}
//
//}