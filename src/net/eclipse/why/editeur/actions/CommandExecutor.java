package net.eclipse.why.editeur.actions;

import java.io.File;

/**
 * An interface for command execution
 * 
 * @author oudot
 */
public class CommandExecutor {

	
	private String message; //the returned message
	private String error; //the error message returned
	
	/**
	 * Class constructor
	 */
	public CommandExecutor() {
		message = "";
		error = "";
	}
	
	/**
	 * Execution function
	 * 
	 * @param dir the execution directory
	 * @param o the set of commands to execute
	 * @param waitfor shall we wait the end of command execution to continue
	 * @return the result of the execution
	 */
	public boolean execute(File dir, Object o, boolean waitfor) {
		
		TraceDisplay.init();
		
		String[] results = Executor.run(dir, o, waitfor);
		
		message = results[1];
		error = results[2];
		
		if(results[0].equals("X")) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the returned message
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Gets the returned error message
	 * 
	 * @return the error message
	 */
	public String getError() {
		return error;
	}
}
