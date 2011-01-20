package org.sla256.antjira; 

import javax.xml.rpc.ServiceException;

import org.apache.tools.ant.Task;

import org.sla256.antjira.jirasoapservice.*;

public class AntJiraTask extends Task {
	
	private String username;
	
	private String password;
	
	private String webServiceEndpointUrl;
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setWebServiceEndpointUrl(String webServiceEndpointUrl) {
		this.webServiceEndpointUrl = webServiceEndpointUrl;
	}
	
	public void execute() {
	    // use of the reference to Project-instance
        String message = getProject().getProperty("ant.project.name");

        // Task's log method
        log("Here is project '" + message + "'.");

        // where this task is used?
        log("I am used in: " +  getLocation() );
        
        try
        {
	        JiraSoapServiceServiceLocator jssLocator = new JiraSoapServiceServiceLocator();
	        
	        jssLocator.setJirasoapserviceV2EndpointAddress(webServiceEndpointUrl);
	        JiraSoapService jss = jssLocator.getJirasoapserviceV2();
	        
	        log(username);
	        log(password);
	        
	        String loginResponse = jss.login(username, password);
/*	        
	        if( loginResponse == null ) {
	        	log("Login failed.", 1);
	        	throw new RuntimeException("Login failed");
	        }
	*/        	
	        //long issueCount = jss.getIssueCountForFilter(loginResponse, "10461");
	        
	        //log("Issue count " + issueCount);
        }
        catch(ServiceException e)
        {
        	log(e.getMessage());
        	//throw new RuntimeException(e.getMessage(), e);
        }
        catch(java.rmi.RemoteException e)
        {
        	log(e.getMessage());
        	//throw new RuntimeException(e.getMessage(), e);
        }
        catch(NullPointerException e)
        {
        	log(e.getStackTrace()[0].toString());
        }
	}
}
