package org.sla256.antjira; 

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.sla256.antjira.jirasoapservice.JiraSoapService;
import org.sla256.antjira.jirasoapservice.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.soap.beans.*;

/**
 * Specialized AntJira task which calls Jira web service to create a new Jira issue. 
 * Required Ant properties:
 * project: Jira project ket/shortname
 * summary: summary/title of the issue
 * type: issue type (corresponding number, e.g. 1)
 * 
 * Example of usage: 
 * <pre>
 * {@code
 * <createissue verbose="true" summary="ant-jira-test1" type="1" project="TST" />
 * }
 * </pre>
 */
public class AntJiraCreateIssueTask extends AntJiraAbstractTask {

	/**
	 * Represents Jira issue summary / title.
	 */
	private String summary;

	/**
	 * Represents Jira issue project key/shortname.
	 */
	private String project;

	/**
	 * Represents Jira issue type.
	 */
	private String type;
	
	/**
	 * Note this setter can only be called from this package, i.e. we don't want it to be set from the 
	 * build file. This ensures the endpoint is defined only once at the parent task level.
	 * @param webServiceEndpointUrl
	 */
	void setWebServiceEndpointUrl(String webServiceEndpointUrl) {
		trace("Setting ws end point URL to " + webServiceEndpointUrl);
		this.webServiceEndpointUrl = webServiceEndpointUrl;
	}
	
	/**
	 * Represents Jira issue summary / title.
	 * @param summary
	 */
	public void setSummary(String summary) {
		trace("Setting summary to " + summary);
		this.summary = summary;
	}

	/**
	 * Represents Jira issue project.
	 * @param project
	 */
	public void setProject(String project) {
		trace("Setting project to " + project);
		this.project = project;
	}
	
	/**
	 * Represents Jira issue type.
	 * @param type
	 */
	public void setType(String type) {
		trace("Setting type to " + type);
		this.type = type;
	}
	
	/**
	 * Calls Jira createIssue WS. Assumes authentication was performed and a correct / valid
	 * Jira login token is available.
	 */
	public void execute() {
        try
        {
        	trace("Running AntJiraCreateIssueTask.execute()");
        	
	        JiraSoapServiceServiceLocator jssLocator = new JiraSoapServiceServiceLocator();
	        
	        jssLocator.setJirasoapserviceV2EndpointAddress(webServiceEndpointUrl);
	        JiraSoapService jss = jssLocator.getJirasoapserviceV2();
	        
	        trace("Obtained Jira SOAP WS handle, calling createIssue");
	        trace(project);
	        trace(type);
	        trace(summary);
	        
	        RemoteIssue newIssue = new RemoteIssue();
	        newIssue.setSummary(summary);
	        newIssue.setProject(project);
	        newIssue.setType(type);
	        
	        RemoteIssue createdIssue = jss.createIssue(jiraLoginResponse, newIssue);

	        trace("Got new issue with " + createdIssue.getKey() + "");

	        // getProject().setProperty(filterCountProperty, String.valueOf(issueCount));
	        
	        trace("Finished AntJiraCreateIssueTask.execute()");
        }
        catch(Exception e)
        {
        	trace("Error!");
        	trace(e.toString());
        	log(e.getMessage());
        	trace(e.getMessage());
        	throw new BuildException(e.getMessage(), e);
        }
	}
}
