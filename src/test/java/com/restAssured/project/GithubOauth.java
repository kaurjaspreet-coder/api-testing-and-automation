package com.restAssured.project;

import java.util.Map;
import static org.hamcrest.Matchers.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.restAssured.resources.Data;
import com.restAssured.resources.Utility;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class GithubOauth extends Data {
	
	RequestSpecification gitHubReq;
	String accessTokenResp;
	Utility utility= new Utility();
	
	@Test(description="sending authorization code to get access token", 
			groups={"regression", "gitHubSmoke"})
	public void getAccessToken() {
	
		Map<String,String> accessToken=utility.dataToGetAcessToken();
		String endPoint= "https://github.com/login/oauth/access_token";
		Response response=RestAssured.given().log().all()
		.formParams(accessToken)
		.when().post(endPoint)
		.then().log().all()
		.assertThat().statusCode(200)
		.extract().response();
		accessTokenResp=	response.asString().split("&")[0].split("=")[1];
	       gitHubReq= utility.createGitHubRequest(accessTokenResp);
		if ((response.getStatusCode())!=200) {
            throw new RuntimeException("Failed to extract access token from the response.");
        }
	}
	
	
	@Test(description="Using Access Token to get the user information", 
		  dependsOnMethods="getAccessToken", groups={"regression", "gitHubSmoke"} )
	public void getUserInfo() {	
	 Assert.assertNotNull(accessTokenResp, "Access token is not initialized properly.");
	 gitHubReq.when().get("/user/repos")
	.then().log().all()
	.assertThat().statusCode(200).body("login", notNullValue())
	.body("id", notNullValue());
		
	}
	
	@Test(description="Using Access Token to get the user  repository information",
		  dependsOnMethods="getAccessToken" , groups={"regression"})
	public void getUserRespo() {
	gitHubReq.when().get("/user")
	.then().log().all()
	.assertThat().statusCode(200)
	.body("size()", greaterThan(0));
	}
	
	@Test(description="invalid Access token",
	      dependsOnMethods="getAccessToken", groups={"regression"})
	public void invalidToken() {
		String invalidToken="28f96fdf8051dd0b9269";
		gitHubReq.header("Authorization","Bearer " +  invalidToken )
		.when().get("/user")
		.then().log().all()
		.assertThat().statusCode(401)
		.body("message", equalTo("Bad credentials"));
	}
	}
	


