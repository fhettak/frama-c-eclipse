package net.eclipse.why.editeur.actions;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

/**
 * This class contains functions which execute concretely given commands.
 * 
 * @author A. Oudot
 */
public class Executor {

	private String message;
	private String error;

	/**
	 * Execute given commands
	 * 
	 * @param dir
	 *            the directory where commands must be executed
	 * @param o
	 *            the commands set
	 * @param waitfor
	 *            waiting for the end of command execution
	 * @return the result, the message and the error
	 */
	public boolean run(File dir, Object o, boolean waitfor) {

		TraceView.init();
		
		String[] objects = null;

		// we get commands, which can be given in a String object,
		// in a String[] object or in an ArrayList<String>, in a
		// String[] object.
		if (o instanceof String[]) {
			objects = (String[]) o;
		} else if (o instanceof String) {
			objects = new String[] { (String) o };
		} else if (o instanceof ArrayList) {
			ArrayList<String> arrayList = (ArrayList<String>) o;
			objects = new String[arrayList.size()];
			for (int e = 0; e < arrayList.size(); e++) {
				objects[e] = (String) arrayList.get(e);
			}
		} else {
			return false;
		}

		Runtime runtime = Runtime.getRuntime();
		boolean success = true;
		int z = 0;

		try {
			// while there are commands to execute and no error occurs
			while (success && z < objects.length) {

				TraceView.print(MessageType.MESSAGE, objects[z]); // print
																		// command
				Process princess = runtime.exec(objects[z], null, dir); // and
																		// execute it
				StreamReader istream = new StreamReader(princess.getInputStream());
				StreamReader estream = new StreamReader(princess.getErrorStream());
				istream.start();
				estream.start();

				if (waitfor) {

					// if an interrupted exception has occurred, the waitfor()
					// function will throw the corresponding exception
					int wwf = princess.waitFor();
					// we wait the end of execution
					while (istream.isAlive())
						;
					while (estream.isAlive())
						;
					// we get messages
					message = istream.getResult();
					error = estream.getResult();

					boolean result = !(wwf == 0); // wwf!=0 => error during
													// execution

					if (result) { // an error number was returned
						success = false;
						if (message.length() > 0) { // here is a message
							// if the error message exists too
							if (error.length() > 0) { 
								// the first message is probably a warning
								TraceView.print(MessageType.WARNING, message); 
							} else { // else it's likely an error
								TraceView.print(MessageType.ERROR, message
										+ "\n");
							}
						}
						if (error.length() > 0) { // in all cases, the error
													// message is always
													// considered as an error
							TraceView.print(MessageType.ERROR, error + "\n");
						}
					} else { // if the returned value is normal
						if (error.length() > 0) { // an error message would be,
													// in all likelihood, a
													// warning
							TraceView.print(MessageType.WARNING, error);
						}
						if (message.length() > 0) {
							TraceView.print(MessageType.MESSAGE, message
									+ "\n"); // an the main message, a
												// congratulation !
						}
					}
				}
				z++;
			}
		} catch (InterruptedException i) {
			TraceView.print(MessageType.WARNING, "VCG Thread stopped...");
			success = false;
		} catch (IOException e) {
			TraceView.print(MessageType.ERROR, "Executor.run() : " + e);
			success = false;
		}
		
		return success;
	}

	/**
	 * Execute a delete command in the why/ folder
	 * 
	 * @return the result of the execution of this command
	 */
	public static boolean clean() {

		TraceView.init();

		boolean success;
		success = true; // this variable is tactless enough to be true,
							   // all over the time!

		File why = new File(FileInfos.getRoot() + "why");

		String s = "deleting why" + File.separator + FileInfos.getName()
				+ "_po*.why " + "why" + File.separator + FileInfos.getName()
				+ "_po*.xpl " + "why" + File.separator + FileInfos.getName()
				+ "_ctx.why " + "why" + File.separator + FileInfos.getName()
				+ ".why" + " files...";
		TraceView.print(MessageType.MESSAGE, s);

		// DELETE FILES :
		// - why/filename_po*.why
		// - why/filename_po*.xpl
		// - why/filename_ctx.why
		// - why/filename.why

		File[] files = why.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				String name = pathname.getName();
				return (name.startsWith(FileInfos.getName() + "_po") && name
						.endsWith(".why"))
						|| (name.startsWith(FileInfos.getName() + "_po") && name
								.endsWith(".xpl"))
						|| name.equals(FileInfos.getName() + "_ctx.why")
						|| name.equals(FileInfos.getName() + ".why");
			}
		});

		if (files != null) {
			for (int f = 0; f < files.length; f++) {
				if (!files[f].delete() /* delete this file */) {
					TraceView.print(MessageType.ERROR, "File "
							+ files[f].getName() + " cannot be deleted!");
					success = false;
				}
			}
		}

		return success;
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
