package net.eclipse.why.editeur.views;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.ConsoleOutputStream;
import org.eclipse.cdt.core.resources.IConsole;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class TraceView {

	public enum MessageType {
		MESSAGE,
		WARNING,
		ERROR,
	}

	private static IConsole console;
	static public void init () {
		try {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("test");
			console = CCorePlugin.getDefault().getConsole();
			console.start(project);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Print function
	 * @param Type of the message
	 * @param The actual message
	 */
	public static void print(MessageType type, String message) {
		ConsoleOutputStream stream =null;
		try {
			if(type == MessageType.MESSAGE) {
				stream = console.getOutputStream();
			} else if(type == MessageType.WARNING ) {
				stream = console.getInfoStream();
			} else if(type == MessageType.ERROR) {
				stream = console.getErrorStream();
			}
			stream.write(message.getBytes());
			stream.write('\n');
			stream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
