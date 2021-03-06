package org.sla256.antjira; 

import java.util.*;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.sla256.antjira.jirasoapservice.JiraSoapService;
import org.sla256.antjira.jirasoapservice.JiraSoapServiceServiceLocator;

/**
 * Main / parent task to implement Ant-Jira integration in the build file.
 * Defines and handles common properties such as Jira username, password, web service end point.
 * Upon execution, performs initial Jira login and then calls nested specialized tasks to perform a particular
 * function, such as get Jira filter count, etc.
 * Transparently passes needed info (login token, WS endpoint URL, etc) to the nested tasks. 
 * 
 * Example of usage:
 * <pre>
 * {@code
 * <antjira username="ant-jira-test" password="111111" webServiceEndpointUrl="http://sandbox.onjira.com/rpc/soap/jirasoapservice-v2">
 *     <filtercount filterID="10021" filterCountProperty="jira.test.filter.count" />
 * </antjira>
 * }
 * </pre>
 */
public class AntJiraTask extends AntJiraAbstractTask {
	
	/**
	 * Internal collection of filter count tasks.
	 */
	private List<AntJiraFilterCountTask> filterCountTasks = new ArrayList<AntJiraFilterCountTask>();
	
	/**
	 * Internal collection of create issue tasks.
	 */
	private List<AntJiraCreateIssueTask> createIssueTasks = new ArrayList<AntJiraCreateIssueTask>();
	
	/**
	 * Jira username.
	 */
	private String username;
	
	/**
	 * Jira password.
	 */
	private String password;
	

	/**
	 * Sets Jira username.
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Sets Jira password.
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Jira SOAP web service endpoint URL.
	 * @param webServiceEndpointUrl
	 */
	public void setWebServiceEndpointUrl(String webServiceEndpointUrl) {
		this.webServiceEndpointUrl = webServiceEndpointUrl;
	}
	
	/**
	 * Performs initial login / authentication, and then calls nested tasks (if any) passing the 
	 * login token and WS endpoint URL.
	 */
	public void execute() {
        try
        {
        	trace("Running AntJiraTask.execute()");
        	
	        JiraSoapServiceServiceLocator jssLocator = new JiraSoapServiceServiceLocator();
	        
	        jssLocator.setJirasoapserviceV2EndpointAddress(webServiceEndpointUrl);
	        JiraSoapService jss = jssLocator.getJirasoapserviceV2();

	        trace("Obtained Jira SOAP WS handle, calling login");

	        jiraLoginResponse = jss.login(username, password);

	        if( jiraLoginResponse == null ) {
	        	log("Login failed.", 1);
	        	throw new BuildException("Login failed");
	        }

	        trace("Logged in, calling " + filterCountTasks.size() + " filter count task(s)");

	        for(AntJiraFilterCountTask fcTask : filterCountTasks) {
	        	fcTask.setJiraLoginResponse(jiraLoginResponse);
	        	fcTask.setWebServiceEndpointUrl(webServiceEndpointUrl);
	        	fcTask.perform();
	        }

	        trace("Logged in, calling " + createIssueTasks.size() + " create issue task(s)");

	        for(AntJiraCreateIssueTask ciTask : createIssueTasks) {
	        	ciTask.setJiraLoginResponse(jiraLoginResponse);
	        	ciTask.setWebServiceEndpointUrl(webServiceEndpointUrl);
	        	ciTask.perform();
	        }
	        
	        trace("Finished AntJiraTask.execute()");
        }
        catch(Exception e)
        {
        	log(e.getMessage());
        	throw new BuildException(e.getMessage(), e);
        }
	}
	
	/**
	 * Allows nested ant tasks of AntJiraFilterCountTask type.
	 * @param filterCountTask
	 */
	public void addFilterCount(AntJiraFilterCountTask filterCountTask) {
		filterCountTasks.add(filterCountTask);
	}
	
	/**
	 * Allows nested ant tasks of AntJiraCreateIssueTask type.
	 * @param createIssueTask
	 */
	public void addCreateIssue(AntJiraCreateIssueTask createIssueTask) {
		createIssueTasks.add(createIssueTask);
	}
}
