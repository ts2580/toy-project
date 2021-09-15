package com.kh.toy.common.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandleableException;

public class HttpConnector {
	
	Gson gson = new Gson();
	
	public String get(String url) {
		
		String responseBody = "";		
		
		try {
			HttpURLConnection conn = getConnection(url, "GET");
			responseBody = getResponseBody(conn);
		}catch (IOException e) {
			throw new HandleableException(ErrorCode.HTTP_CONNECT_ERROR, e);
		}
		return responseBody;
	}
	
	public String get(String url, Map<String, String> headers) {
		
		String responseBody = "";		
		
		try {
			HttpURLConnection conn = getConnection(url, "GET");
			setHeaders(headers, conn); 
			responseBody = getResponseBody(conn);
		}catch (IOException e) {
			throw new HandleableException(ErrorCode.HTTP_CONNECT_ERROR, e);
		}
		return responseBody;
	}
	
	public JsonElement getAsJson(String url, Map<String, String> headers) {
		
		JsonElement responseBody = null;	
		
		
		try {
			HttpURLConnection conn = getConnection(url, "GET");
			setHeaders(headers, conn); 
			responseBody = gson.fromJson(getResponseBody(conn), JsonElement.class);
		}catch (IOException e) {
			throw new HandleableException(ErrorCode.HTTP_CONNECT_ERROR, e);
		}
		return responseBody;
	}
	
	public JsonElement postAsJson(String url, Map<String, String> headers, String body) {
		
		JsonElement responseBody = null;
		
		try {
			HttpURLConnection conn = getConnection(url, "POST");
			setHeaders(headers, conn);
			setBody(body, conn);
			responseBody = gson.fromJson(getResponseBody(conn), JsonElement.class);
		} catch (Exception e) {
			throw new HandleableException(ErrorCode.HTTP_CONNECT_ERROR, e);
		}
		return responseBody;
		
	}
	
	private HttpURLConnection getConnection(String url, String method) throws IOException {
		
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)u.openConnection();
		conn.setRequestMethod(method);
		
		
		// 연결시 최대 지연시간
		conn.setConnectTimeout(10000);
		// 응답시 최대 지연시간
		conn.setReadTimeout(10000);
		return conn;
	}
	
	private String getResponseBody(HttpURLConnection conn) throws IOException {
		
		StringBuffer responseBody = new StringBuffer();
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))){
			String line = null;
			while ((line = br.readLine()) != null ) {
				responseBody.append(line);
			}
		}catch (Exception e) {
			throw new HandleableException(ErrorCode.HTTP_CONNECT_ERROR, e);
		}
		
		return responseBody.toString();
		
	}
	
	public String post(String url, Map<String, String> headers, String body) {
		
		String responseBody = "";
		try {
			HttpURLConnection conn = getConnection(url, "POST");
			setHeaders(headers, conn);
			setBody(body, conn);
			
			// HttpURLConnection을 사용해 OutputStream을 사용하려고 할때는
			// HttpURLConnection의 DoOutput(출력스트림 사용여부)이 true여야함. 
			
			responseBody = getResponseBody(conn);
		} catch (IOException e) {
			throw new HandleableException(ErrorCode.HTTP_CONNECT_ERROR, e);
		}
		return responseBody;
		
	}
	
	
	private void setHeaders(Map<String, String> headers, HttpURLConnection conn) {
		
		for(String key : headers.keySet()) {
			conn.addRequestProperty(key, headers.get(key));
		}
		// 카카오 API받아올꺼니까 헤더 만들기
	}
	
	private void setBody(String body, HttpURLConnection conn) {
		// HttpURLConnection의 사용해 OotputStream을 사용하려고 할때는
		// HttpURLConnection의 DoOutput(풀력스트림 사용여부)이 true여야함. 
		
		conn.setDoOutput(true);
		
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))){
			bw.write(body);
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String urlEncodingForm(RequestParams params) {
		
		// URL safe하게 바꾸고 json으로 넘어오는것두 예쁘게 parse해주공...
		String res = "";
		 Map<String, String> paramsMap = params.getParams();
		
		// name=value&name=value&name=value&...
		
			try {
				for (String key : paramsMap.keySet()) {
				res += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(paramsMap.get(key), "UTF-8");
				}
				
				if(res.length() > 0) {
					res = res.substring(1);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return res;
	}

}
