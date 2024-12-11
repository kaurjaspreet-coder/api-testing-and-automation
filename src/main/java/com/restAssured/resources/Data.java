package com.restAssured.resources;

import static io.restassured.RestAssured.given;

import io.restassured.matcher.RestAssuredMatchers;
 

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

public class Data  {
	 
public String createIssueData() {
	return "{ \"fields\": {"
            + "    \"project\": {"
            + "        \"key\": \"JASI\"  "
            + "    },"
            + "    \"summary\": \"jira project\","
            + "    \"description\": \"descriptionDetails\","
            + "    \"issuetype\": {"
            + "        \"name\": \"Bug\""
            + "    }"
            + "} }";
}

public String addCommentData() {
	return "{\r\n"
			+ "    \"body\": \"testing comment\",\r\n"
			+ "    \"visibility\": {\r\n"
			+ "        \"type\": \"role\",\r\n"
			+ "        \"value\": \"Administrators\"\r\n"
			+ "    }\r\n"
			+ "}\r\n"
			+ "";	
}

@DataProvider(name="commentData")
public Object[][] getCommentData(){
	return new Object[][] {
		{"10800","{\r\n"
				+ "    \"body\": \"updated on dec-02-2024\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}"},
		{"10001","{\r\n"
				+ "    \"body\": \"updated comment dec-03-2024\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}"},
		{"10203","{\r\n"
				+ "    \"body\": \"updated comment dec-03-2024\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}"}
		
	};
}
}
