package net.eclipse.why.editeur.actions;

import java.io.File;

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
	 * @param subGoalNumber
	 *            the subgoal number
	 * @param proverNumber
	 *            the prover number
	 * @return 2 for an unproved result, 1 for a proved one, -1 for an error
	 *         case
	 */
	public int prove(int goalNumber, int subGoalNumber, int proverNumber) {

		int result = 0;
		boolean assistant = (FileInfos.status[proverNumber].equals("assistant")) ? true
				: false;
		String num = "" + goalNumber;
		if (subGoalNumber > 0)
			num += ("-" + subGoalNumber);

		String why = FileInfos.commands[proverNumber];
		why = replaceBy(why, "%s", FileInfos.getName());
		why = replaceBy(why, "%r", FileInfos.getRoot());
		why = replaceBy(why, "%g", FileInfos.goals.get(goalNumber - 1).getFname());
		why = replaceBy(why, "%n", Integer.toString(FileInfos.goals.get(goalNumber - 1).getNum_in_f()));
		String[] cmd = why.split("\n");

		Executor executor = new Executor();

		String c = null;
		if (assistant) {
			if (cmd != null && cmd.length > 0) {
				String[] cmds = new String[cmd.length - 1];
				c = cmd[cmd.length - 1]; // the last command
				for (int h = 0; h < (cmd.length - 1); h++) {
					cmds[h] = cmd[h];
				}
				cmd = cmds; // the others
			}
		}

		if (!executor.run(new File(FileInfos.getRoot()), cmd, true))
			return -1;

		if (assistant) {
			if (!executor.run(new File(FileInfos.getRoot()), c, false))
				return -1;
			result = 0;
		} else {
			result = interpret(executor.getMessage());
		}

		if (result > 0) {
			PO op = (PO) FileInfos.goals.get(goalNumber - 1);
			if (subGoalNumber > 0)
				op = op.getSubGoal(subGoalNumber);
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
