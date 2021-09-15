package test.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.http.RequestParams;

@WebServlet("/test/http/*")
public class HttpRequestTest extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
       
    public HttpRequestTest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		String[] uriArr = request.getRequestURI().split("/");
		
		switch(uriArr[uriArr.length-1]) {
		case "connect":
			testHttpUrlConnection(request, response);
			break;
		case "kakao-test":
			testKakao(request, response);
			break;
		case "naver-test":
			testNaver(request, response);
			break;
		default:
			break;	
		}
		
	}

	private void testNaver(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		Gson gson = new Gson();
		HttpConnector conn = new HttpConnector();
		String url = "https://openapi.naver.com/v1/datalab/search";
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("X-Naver-Client-Id", "BDZJpEv6LvBtiOAPS8PY");
		headers.put("X-Naver-Client-Secret", "PnEq6ry2SY");
		headers.put("Content-Type", "application/json");
		
		/*
		String requestBody = "{\r\n" + 
				"  \"startDate\": \"2016-01-01\",\r\n" + 
				"  \"endDate\": \"2021-01-01\",\r\n" + 
				"  \"timeUnit\": \"month\",\r\n" + 
				"  \"keywordGroups\": [\r\n" + 
				"    {\r\n" + 
				"      \"groupName\": \"자바\",\r\n" + 
				"      \"keywords\": [\r\n" + 
				"        \"스프링\",\r\n" + 
				"        \"스프링부트\"\r\n" + 
				"      ]\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"groupName\": \"파이썬\",\r\n" + 
				"      \"keywords\": [\r\n" + 
				"        \"장고\",\r\n" + 
				"        \"플래터\"\r\n" + 
				"      ]\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"ages\": [\r\n" + 
				"    \"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"10\"\r\n" + 
				"  ]\r\n" + 
				"}";
		// body 그대로 때려박으니 개 더러웡. Json으로 깔끔하게 나오도록 맹글어주자.
		 */
		
		List<String> javaKeyWords = new ArrayList<String>();
		javaKeyWords.add("스프링");
		javaKeyWords.add("스프링부트");
		
		List<String> pythonKeyWords = new ArrayList<String>();
		pythonKeyWords.add("장고");
		pythonKeyWords.add("플래터");
		
		Map<String, Object> javaGroup = new LinkedHashMap<String, Object>();
		javaGroup.put("groupName", "자바");
		javaGroup.put("keywords", javaKeyWords);
		
		Map<String, Object> pythonGroup = new LinkedHashMap<String, Object>();
		pythonGroup.put("groupName", "파이썬");
		pythonGroup.put("keywords", pythonKeyWords);
		
		List<Map<String, Object>> keywordGroups = new ArrayList<Map<String,Object>>();
		keywordGroups.add(javaGroup);
		keywordGroups.add(pythonGroup);
		
		String[] ages = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",};
		
		Map<String, Object> datas = new LinkedHashMap<String, Object>();
		datas.put("startDate", "2016-01-01");
		datas.put("endDate", "2021-01-01");
		datas.put("timeUnit", "month");
		datas.put("keywordGroups", keywordGroups);
		datas.put("ages", ages);
		
		String requestBody = gson.toJson(datas);
		System.out.println("requestBody : " + requestBody);
		// JSON이 그대로!
		
		String responseBody = conn.post(url, headers, requestBody);
		System.out.println(responseBody);
		
		
	}

	private void testKakao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		HttpConnector conn = new HttpConnector();
		//Map<String, String> params = new HashMap<String, String>();
		//params.put("query", "자바");
		//params.put("sort", "latest");
		
		RequestParams params = RequestParams.builder().param("query", "자바").param("sort", "latest").build();
		
		
		String queryString = conn.urlEncodingForm(params);
		System.out.println(queryString);
		
		
		String url = "https://dapi.kakao.com/v3/search/book?"+queryString;
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "KakaoAK 060604da6e42267573842a35f2c94da5");
		
		// gson - default
		// json object => Map
		// json array => List
		// json String => String
		// json number => number, double
		// json null => null
		// json boolean => boolean
		
		JsonObject datas = conn.getAsJson(url, headers).getAsJsonObject();
		JsonArray documents = datas.getAsJsonArray("documents");
		
		for (JsonElement jsonElement : documents) {
			JsonObject e = jsonElement.getAsJsonObject();
			System.out.println("authors : " + e.getAsJsonArray("authors"));
			// 공동저자가 있을수도 잇으니. 리스트 ㅋㅋ
			System.out.println("title : " + e.getAsJsonPrimitive("title").getAsString());
		}
		 
	}

	private void testHttpUrlConnection(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			URL url = new URL("http://localhost:9090/mail/?mailTemplate=join-auth-mail");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			StringBuffer responseBody = new StringBuffer();
			
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))){
				String line = null;
				while ((line = br.readLine()) != null ) {
					responseBody.append(line);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(responseBody);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
