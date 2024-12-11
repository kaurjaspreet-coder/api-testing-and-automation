package com.restAssured.resources;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class Utility {
	private PrintStream logFile;
	 
	private static String getConfigValue(String key) {
		Properties prop = new Properties();
	    String projectDir = System.getProperty("user.dir");
	    String filePath = projectDir + "\\src\\main\\java\\com\\restAssured\\resources\\config.properties";
	    try (FileInputStream inputStream = new FileInputStream(filePath)) {
	        prop.load(inputStream);
	    }
	    catch(Exception e) {
	    	   System.out.println("An error occurred: " + e.getMessage());
	    }
	 return prop.getProperty(key);
	    }
    
    //methods  related to gitHubOauth
	public Map<String, String> dataToGetAcessToken() {
		Map<String, String> accessToken= new HashMap<>();
		accessToken.put("client_id",getConfigValue("github.client.id"));
		accessToken.put("client_secret",getConfigValue("github.clientsecret.id"));
		accessToken.put("code", getConfigValue("github.authorization.code"));
		accessToken.put("redirect_uri", getConfigValue("github.redirect.url"));
		return accessToken;
	}
	
	public RequestSpecification createGitHubRequest(String accessTokenResp) {
		return	RestAssured.given().baseUri(getConfigValue("github.base.uri"))
			.header("Accept","application/vnd.github+json")
			.header("Authorization","Bearer " + accessTokenResp );
		}

	
//methods related to JIRA	
	public PrintStream createFile() throws FileNotFoundException {
		String	path=System.getProperty("user.dir")+ "\\resources\\jira.txt";
	File file = new File(path);
	PrintStream logFile= new PrintStream(new FileOutputStream(file));
	return logFile;
	}
	
	
    public RequestSpecification jiraBaseUrl() {
		try {
			logFile = createFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Log file creation failed.");
		}
    	RequestSpecification req= given().baseUri(getConfigValue("jira.base.uri"))
       .header("content-type","application/json")
       .filter(new RequestLoggingFilter(logFile))       
       .filter(new ResponseLoggingFilter(logFile));
    	return req;
    }
	
	public Map<String, String> jiraLoginCredentials() {
	    Map<String, String> loginInfo= new HashMap<>();
		loginInfo.put("username",getConfigValue("jira.username"));
		loginInfo.put("password",getConfigValue("jira.password") );
		return loginInfo;
	    }
}
