package net.eclipse.why.editeur.actions;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.PO;

/**
 * The goal of this class is to make and run prover's commands and interpret the
 * message returned.
 */
public class ProverExecutor {

	/**
	 * Execute commands to prove a particular goal using a particular prover. if
	 * the prover is an assistant, we are going to execute the last command
	 * separately
	 * 
	 * @param goalNumber
	 *            the goal number
	 * @param proverNumber
	 *            the prover number
	 * @return 2 for an unproved result, 1 for a proved one, -1 for an error
	 *         case
	 */
	public int prove(int goalNumber, int proverNumber) {

		int result = 0;
		boolean assistant = (FileInfos.status[proverNumber].equals("assistant")) ? true
				: false;
		String why = FileInfos.commands[proverNumber];
		why = replaceBy(why, "%s", FileInfos.getName());
		why = replaceBy(why, "%r", FileInfos.getRoot());
		why = replaceBy(why, "%g", FileInfos.goals.get(goalNumber - 1).getWhyFile());
		why = replaceBy(why, "%e", FileInfos.goals.get(goalNumber - 1).getErgoFile());
		String[] cmd = why.split("\n");

		Executor executor = new Executor();

		if (!executor.run(new File(FileInfos.getRoot()), cmd, true))
			return -1;

		if (assistant) {

			try {
				String file = FileInfos.getRoot() + "coq/" + FileInfos.getName() + "_why.v";
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IFile ifile = root.getFileForLocation(new Path(file));
				ifile.refreshLocal(IResource.DEPTH_ZERO, null);
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				IDE.openEditor(page, ifile, true);
			} catch (Exception e) {
				result = -1;
			}
			result = 0;
		} else {
			result = interpret(executor.getMessage());
		}

		if (result > 0) {
			PO op = FileInfos.goals.get(goalNumber - 1);
			op.setState(proverNumber, result);
		}

		return result;
	}

	/**
	 * Returns the String o once all occurrences of string p in o have been
	 * replaced by string q.
	 * 
	 * @param o
	 * @param p
	 * @param q
	 * @return the String o once all occurrences of string p in o have been
	 *         replaced by string q
	 */
	private String replaceBy(String o, String p, String q) {
		String r = "";
		String[] strtmp = o.split(p);
		if (strtmp.length > 1) {
			r = strtmp[0];
			for (int w = 1; w < strtmp.length; w++) {
				r += q;
				r += strtmp[w];
			}
		} else {
			r = o;
		}
		return r;
	}

	/**
	 * Function of interpretation of messages returned by the
	 * <code>why-dp</code> command
	 * 
	 * @param retourDP
	 *            the message
	 * @return the result of prover work
	 */
	private int interpret(String retourDP) {

		try {
			String[] retour = retourDP.split("\n");

			int index = retour[1].indexOf(":");

			String res = retour[1].substring(index + 2, index + 3);

			if (res.equals(".")) { // valid case
				return 1;
			} else if (res.equals("*")) { // invalid case
				return 2;
			} else if (res.equals("?")) { // unknown case
				return 3;
			} else if (res.equals("#")) { // timeout case
				return 4;
			} else if (res.equals("!")) { // failure case
				return 5;
			} else { // an error occurred
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}
	}
}
