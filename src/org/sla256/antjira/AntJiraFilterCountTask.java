package org.sla256.antjira; 

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.sla256.antjira.jirasoapservice.JiraSoapService;
import org.sla256.antjira.jirasoapservice.JiraSoapServiceServiceLocator;

/**
 * Specialized AntJira task which calls Jira web service to count number of Jira items in the 
 * given filter (by ID). Writes the count in the given property name. 
 * Required Ant properties:
 * filterID: numeric Jira filter ID to be queries for a count
 * filterCountProperty: string, a name of Ant property to store the result
 * 
 * Example of usage: 
 * <pre>
 * {@code
 * <filtercount filterID="10021" filterCountProperty="jira.test.filter.count" />
 * }
 * </pre>
 */
public class AntJiraFilterCountTask extends Task {

	/**
	 * Represents an authentication token returned by Jira on login. Passed on from the initial login
	 * performed by the main Jira task.
	 * This is an internally set property (not through the property of the task in the build file
	 * but by the parent main task AntJira. 
	 */
	private String jiraLoginResponse;
	
	/**
	 * This is an internally set property (not through the property of the task in the build file
	 * but by the parent main task AntJira. 
	 */
	private String webServiceEndpointUrl;
	
	/**
	 * Represents Jira filter ID which this task will call and set the returned count in the defined property.
	 * This is a traditional ant task property which we want to be set in the build file.
	 */
	private String filterID;

	/**
	 * Defines the name of Ant property where the filter count will be stored. 
	 */
	private String filterCountProperty;
	
	/**
	 * Note this setter can only be called from this package, i.e. we don't want it to be set from the 
	 * build file. This ensures the endpoint is defined only once at the parent task level.
	 * @param webServiceEndpointUrl
	 */
	void setWebServiceEndpointUrl(String webServiceEndpointUrl) {
		this.webServiceEndpointUrl = webServiceEndpointUrl;
	}
	
	/**
	 * Note this setter can only be called from this package, 
	 * @param jiraLoginResponse
	 */
	void setJiraLoginResponse(String jiraLoginResponse) {
		this.jiraLoginResponse = jiraLoginResponse;
	}
	
	/**
	 * Represents Jira filter ID which this task will call and set the returned count in the defined property.
	 * @param filterID
	 */
	public void setFilterID(String filterID) {
		this.filterID = filterID;
	}
	
	/**
	 * Defines the name of Ant property where the filter count will be stored.
	 * @param filterCountProperty
	 */
	public void setFilterCountProperty(String filterCountProperty) {
		this.filterCountProperty = filterCountProperty;
	}
	
	/**
	 * Calls Jira getIssueCountForFilter WS. Assumes authentication was performed and a correct / valid
	 * Jira login token is available.
	 */
	public void execute() {
        try
        {
	        JiraSoapServiceServiceLocator jssLocator = new JiraSoapServiceServiceLocator();
	        
	        jssLocator.setJirasoapserviceV2EndpointAddress(webServiceEndpointUrl);
	        JiraSoapService jss = jssLocator.getJirasoapserviceV2();
	        
	        long issueCount = jss.getIssueCountForFilter(jiraLoginResponse, filterID);

	        getProject().setProperty(filterCountProperty, String.valueOf(issueCount));
        }
        catch(Exception e)
        {
        	log(e.getMessage());
        	throw new BuildException(e.getMessage(), e);
        }
	}
}
