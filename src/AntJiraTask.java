import org.apache.tools.ant.Task;

public class AntJiraTask extends Task {
	
	public void execute() {
	    // use of the reference to Project-instance
        String message = getProject().getProperty("ant.project.name");

        // Task's log method
        log("Here is project '" + message + "'.");

        // where this task is used?
        log("I am used in: " +  getLocation() );
	}
}
