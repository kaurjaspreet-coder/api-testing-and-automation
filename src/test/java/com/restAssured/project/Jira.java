package com.restAssured.project;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.restAssured.resources.Data;
import com.restAssured.resources.Utility;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.io.IOException;


public class Jira extends Data{
	 private String sessionId;
	SessionFilter session= new SessionFilter();
	Utility utility= new Utility();
	RequestSpecification req =utility.jiraBaseUrl();
	

@Test(groups={"jiraSmoke","jiraRegression"})
public void loggingInJira() throws IOException {
	JsonPath response=req.body(utility.jiraLoginCredentials())
	.filter(session)
	.when().post("rest/auth/1/session")
	.then().log().all().assertThat().statusCode(200)
	.extract().response().jsonPath();
	String resp=response.getString("session.name");	
	this.sessionId = response.getString("session.value");  // Store session ID for reuse
    System.out.println("Session ID: " + sessionId);
}

@Test(dependsOnMethods="loggingInJira", groups={"jiraSmoke","jiraRegression"})
public void creatingIssueOnJira() {
 JsonPath response=	req.body(createIssueData())
    .cookie("JSESSIONID", sessionId).when().post("rest/api/2/issue")
	.then().log().all().assertThat().statusCode(200).extract().response().jsonPath();
 System.out.println(response.getString("message"));
}

public boolean commentExists(String commentID) {
	int statusCode=req
			.cookie("JSESSIONID", sessionId)
			.pathParam("issueKey","10000")
			.pathParam("commentId",commentID)
			.when().get("/rest/api/2/issue/{issueKey}/comment/{commentId}")
		    .getStatusCode();
	     if (statusCode == 200) {
	         return true; 
	     } else {
	         System.out.println("Failed to find comment." + commentID);
	         return false;  
	     }
}

//adding a comment
@Test(dependsOnMethods="loggingInJira", groups={"jiraRegression"})
public void addComment() {
JsonPath response=req.body(addCommentData())
        .cookie("JSESSIONID", sessionId)
		.pathParam("issueKey", "10000")
		.when().post("rest/api/2/issue/{issueKey}/comment").
	    then().log().all().extract().response().jsonPath();
        System.out.println(response.getString("body"));
}

//deleting comment
@Test(dependsOnMethods="loggingInJira", dataProvider="commentData", groups="jiraRegression" )
public void deleteComment(String commentID, String commentBody) {
	if(commentExists(commentID)) {
	req.cookie("JSESSIONID", sessionId)
	.pathParam("issueKey","10000")
	.pathParam("commentId",commentID)
	.when().delete("/rest/api/2/issue/{issueKey}/comment/{commentId}")
	.then().log().all().assertThat().statusCode(204);
}
	else {
        Assert.fail("Comment with ID" + commentID + "does not exist.");
    }
}

//updating comment
@Test(dependsOnMethods="loggingInJira" ,dataProvider="commentData", groups="jiraRegression")
	public void  updateComment(String commentID, String commentData) {
	if(commentExists(commentID)) {
	req.body(commentData)
	.pathParam("issueKey", "10000")
	.pathParam("commentId", commentID)
	.when().put("/rest/api/2/issue/{issueKey}/comment/{commentId}")
	.then().log().all().assertThat().statusCode(200);	
	}
	else {
		 System.out.println("Comment with ID " + commentID + " does not exist for issue " );
	        Assert.fail("Comment does not exist, cannot update");
	    }
	}

//attaching log file
@Test(dependsOnMethods="loggingInJira",groups="jiraRegression")
public void attachmentInComments() {
String	logFilePath=System.getProperty("user.dir")+ "\\resources\\jira.txt";
req.log().all()
.cookie("JSESSIONID", sessionId)
.header("X-Atlassian-Token","no-check")
.pathParam("issueKey", "10000")
.header("content-type","multipart/form-data")
.multiPart("file",new File(logFilePath))
.when().post("rest/api/2/issue/{issueKey}/attachments")
.then().log().all().assertThat().statusCode(200);
}
}
