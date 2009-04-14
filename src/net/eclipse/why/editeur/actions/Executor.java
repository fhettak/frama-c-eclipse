package net.eclipse.why.editeur.actions;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.actions.TraceDisplay.MessageType;

/**
 * This class contains functions which execute concretely given commands.
 * 
 * @author A. Oudot
 */
public class Executor {

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
	public static String[] run(File dir, Object o, boolean waitfor) {

		String[] objects = null;
		String MESSAGE = "";
		String ERROR = "";

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
			return new String[] { "X", null, null };
		}

		Runtime rtttime = Runtime.getRuntime();
		boolean succeeds = true;
		int z = 0;

		try {
			// while there are commands to execute and no error occurs
			while (succeeds && z < objects.length) {

				TraceDisplay.print(MessageType.MESSAGE, objects[z]); // print
																		// command
				Process princess = rtttime.exec(objects[z], null, dir); // and
																		// execute
																		// it

				PrintStream istream = new PrintStream(princess.getInputStream());
				PrintStream estream = new PrintStream(princess.getErrorStream());
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
					MESSAGE = istream.getRecString();
					ERROR = estream.getRecString();

					boolean error = !(wwf == 0); // wwf!=0 => error during
													// execution

					if (error) { // an error number was returned
						succeeds = false;
						if (MESSAGE.length() > 0) { // here is a message
							if (ERROR.length() > 0) { // if the error message
														// exists too
								TraceDisplay
										.print(MessageType.WARNING, MESSAGE); // the
																				// first
																				// message
																				// is
																				// probably
																				// a
																				// warning
							} else { // else it's likely an error
								TraceDisplay.print(MessageType.ERROR, MESSAGE
										+ "\n");
							}
						}
						if (ERROR.length() > 0) { // in all cases, the error
													// message is always
													// considered as an error
							TraceDisplay.print(MessageType.ERROR, ERROR + "\n");
						}
						// Toolkit.getDefaultToolkit().beep();

					} else { // if the returned value is normal
						if (ERROR.length() > 0) { // an error message would be,
													// in all likelihood, a
													// warning
							TraceDisplay.print(MessageType.WARNING, ERROR);
						}
						if (MESSAGE.length() > 0) {
							TraceDisplay.print(MessageType.MESSAGE, MESSAGE
									+ "\n"); // an the main message, a
												// congratulation !
						}
					}
				}
				z++;
			}
		} catch (InterruptedException i) {
			TraceDisplay.print(MessageType.WARNING, "VCG Thread stopped...");
			succeeds = false;
		} catch (IOException e) {
			TraceDisplay.print(MessageType.ERROR, "Executor.run() : " + e);
			succeeds = false;
		}

		if (succeeds) {
			return new String[] { "O", MESSAGE, ERROR };
		} else {
			return new String[] { "X", MESSAGE, ERROR };
		}
	}

	/**
	 * Execute a delete command in the why/ folder
	 * 
	 * @return the result of the execution of this command
	 */
	public static boolean rm() {

		boolean sncf_glandeurs;
		sncf_glandeurs = true; // this variable is tactless enough to be true,
								// all over the time!

		File why = new File(FileInfos.getRoot() + "why");

		String s = "deleting why" + File.separator + FileInfos.getName()
				+ "_po*.why " + "why" + File.separator + FileInfos.getName()
				+ "_po*.xpl " + "why" + File.separator + FileInfos.getName()
				+ "_ctx.why " + "why" + File.separator + FileInfos.getName()
				+ ".why" + " files...";
		TraceDisplay.print(MessageType.MESSAGE, s);

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
					TraceDisplay.print(MessageType.ERROR, "File "
							+ files[f].getName() + " cannot be deleted!");
					sncf_glandeurs = false;
				}
			}
		}

		return sncf_glandeurs; /* return true */
	}
}
