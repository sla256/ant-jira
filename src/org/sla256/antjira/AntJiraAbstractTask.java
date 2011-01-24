package org.sla256.antjira;

import org.apache.tools.ant.Task;

/**
 * Abstract parent class for all main ant-jira tasks and sub-tasks. A few reusable methods and common properties.
 */
public abstract class AntJiraAbstractTask extends Task {
	/**
	 * Jira SOAP web service endpoint URL. 
	 */
	protected String webServiceEndpointUrl;
	
	/**
	 * Represents an authentication token returned by Jira on login. Passed on from the initial login
	 * performed by the main Jira task.
	 * This is an internally set property (not through the property of the task in the build file
	 * but by the parent main task AntJira. 
	 */
	protected String jiraLoginResponse;
	
	protected Boolean isVerbose = false;
	
	/**
	 * Controls verbose attribute. If enabled, all ant-jira tasks will produced extra console output
	 * helpful for tracing, troubleshooting, etc.  
	 * @param isVerbose
	 */
	public void setVerbose(Boolean isVerbose) {
		this.isVerbose = isVerbose;
	}
	
	/**
	 * Note this setter can only be called from this package. I.e. it is intentionally not available as an ant
	 * task attribute. 
	 * @param jiraLoginResponse
	 */
	void setJiraLoginResponse(String jiraLoginResponse) {
		this.jiraLoginResponse = jiraLoginResponse;
	}
	
	/**
	 * Internal reusable method for tracing, only prints given message in case of enabled verbose method.
	 * @param msg
	 */
	protected void trace(String msg) {
		if( isVerbose ) {
			log(msg);
		}
	}
}
