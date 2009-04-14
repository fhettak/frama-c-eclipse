package net.eclipse.why.editeur.actions;

import java.io.File;

import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.PO;


/**
 * The goal of this class is to
 * make and run prover's commands and
 * interpret the message returned.
 * 
 * @author oudot
 */
public class ExecProver {
	
	
	/**
	 * Class constructor.
	 */
	public ExecProver() {
		/*do nothing*/
	}
	
	
	/**
	 *  Execute commands to prove a particular goal using a particular prover.
	 * 
	 * @param goalNumber the goal number
	 * @param subGoalNumber the subgoal number
	 * @param proverNumber the prover number
	 * @return 2 for an unproved result, 1 for a proved one, -1 for an error case
	 */
	public int prove(int goalNumber, int subGoalNumber, int proverNumber) {
		
		int result = 0;
		boolean assistant = (FileInfos.status[proverNumber].equals("assistant")) ? true : false;
		String num = "" + goalNumber;
		if(subGoalNumber > 0) num += ("-"+subGoalNumber);
 		
		//Making commands
		String why = FileInfos.commands[proverNumber];
		why = replaceBy(why, "%s", FileInfos.getName());
		why = replaceBy(why, "%r", FileInfos.getRoot());
		why = replaceBy(why, "%n", num);
		String[] cmd = why.split("\n");
		
		// EXECUTION PART :
		CommandExecutor executor = new CommandExecutor();
		
		String c = null;
		//if the prover is an assistant, we are going to execute the last
		//command separately
		if(assistant) {
			if(cmd!=null && cmd.length>0) {
				String[] cmds = new String[cmd.length -1];
				c = cmd[cmd.length-1]; //the last command
				for(int h=0; h<(cmd.length-1); h++) {
					cmds[h] = cmd[h];
				}
				cmd = cmds; //the others
			}
		}
		
		//execution of commands
		if(!executor.execute(new File(FileInfos.getRoot()), cmd, true))
			return -1;
		
		//if assistant, execution of the last command
		if(assistant) {
			if(!executor.execute(new File(FileInfos.getRoot()), c, false))
				return -1;
			result = 0;
		} else { //else, we interpret the message returned by prover
			result = interpret(executor.getMessage());
		}
		
		//modification of goal state
		if(result>0) {
			PO op = (PO)FileInfos.goals.get(goalNumber-1);
			if(subGoalNumber > 0) op = op.getSubGoal(subGoalNumber);
			op.setState(proverNumber, result);
		}
		
		return result;
	}
	
	/**
	 * Returns the String o once all occurences of string p in o have been
	 * replaced by string q.
	 * 
	 * @param o
	 * @param p
	 * @param q
	 * @return the String o once all occurences of string p in o have been replaced by string q
	 */
	private String replaceBy(String o, String p, String q) {
		String r = "";
		String[] strtmp = o.split(p);
		if(strtmp.length > 1) {
			r = strtmp[0];
			for(int w=1; w<strtmp.length; w++) {
				r += q;
				r += strtmp[w];
			}
		} else {
			r = o;
		}
		return r;
	}
	
	
	/*
	 *  Exécution des commandes pour prouver un but donné à l'aide d'un prouveur donné.<BR><BR>
	 * 
	 * @param int numéro de but
	 * @param int numéro de sous-but
	 * @param int numéro de prouveur
	 * @return 2 si le but n'a pas été prouvé, 1 si le but est prouvé, -1 si erreur.
	 *
	public int prove(int goalNumber, int subGoalNumber, int proverNumber) {
		
		
		int resultat = 0;
		String name;
		
		name = FileInfos.getName() + "_po" + goalNumber;
		if(subGoalNumber > 0) name += "-" + subGoalNumber;
		goalFileName = name + ".why";
		contextFileName = FileInfos.getName() + "_ctx.why";
		
		boolean coq = false;
		boolean assistant = (FileInfos.status[proverNumber].equals("assistant")) ? true : false;
		
		
		String proverName = FileInfos.provers[proverNumber].toLowerCase();
		String whyOption = FileInfos.proversWhy[proverNumber];
		String extension = FileInfos.extensions[proverNumber];
		String exe = FileInfos.exeProverFiles[proverNumber];
		
		if(!assistant && extension.equals("v")) coq = true;
		if(assistant && proverName.equals("coqide")) {
			coq = true;
			proverName = "coq";
		}
		
		String newFile = FileInfos.getName() + "_po" + goalNumber;
		if(subGoalNumber > 0) newFile += "-" + subGoalNumber;
		newFile += "_why." + extension;
		
		if(exe.equals("")) {
			exe = "dp";
		}
		
//		String[] whyCmd;
//		if(coq) {
//			File coqD = FileInfos.getDir(null, "coq");
//			whyCmd = new String[2];
//			whyCmd[0] = "make -f " + FileInfos.getName() + ".makefile " + FileInfos.getFile(coqD, FileInfos.getName()+"_spec_why.vo");
//			whyCmd[1] = "make -f " + FileInfos.getName() + ".makefile " + FileInfos.getFile(coqD, newFile); 
//		} else {
//			File whyD = FileInfos.getDir(null, "why");
//			whyCmd = new String[1];
//			whyCmd[0] = "why --" + whyOption + " -dir " + proverName + " -no-prelude " + FileInfos.getFile(whyD, contextFileName) + " " + FileInfos.getFile(whyD, goalFileName);
//		}
//		
//		File pD = FileInfos.getDir(null, proverName);
//		String proverCmd = exe + " " + FileInfos.getFile(pD, newFile);
		
		
		String[] whyCmd;
		if(coq) {
			whyCmd = new String[2];
			whyCmd[0] = "make -f " + FileInfos.getName() + ".makefile " + FileInfos.getRoot() + "coq" + File.separator + FileInfos.getName() + "_spec_why.vo";
			whyCmd[1] = "make -f " + FileInfos.getName() + ".makefile " + FileInfos.getRoot() + "coq" + File.separator + newFile; 
		} else {
			whyCmd = new String[1];
			whyCmd[0] = "why --" + whyOption + " -dir " + proverName + " -no-prelude why" + File.separator + contextFileName + " why" + File.separator + goalFileName;
		}
		
		String proverCmd = exe + " " + proverName + File.separator + newFile;
		
		
		
		//PARTIE EXECUTION :
		CommandExecutor executor = new CommandExecutor();
		
		if(!executor.execute(new File(FileInfos.getRoot()), whyCmd, true))
			return -1;
			
		if(!executor.execute(new File(FileInfos.getRoot()), proverCmd, !assistant))
			return -1;
		
		
		if(coq) {
			resultat = interpretCoq(executor.getMessage(), executor.getError());
		} else {
			resultat = interpret(executor.getMessage());
		}
		
		if(assistant) {
			resultat = 0;
		}
			
		if(resultat>0) {
			PO op = (PO)FileInfos.goals.get(goalNumber-1);
			if(subGoalNumber > 0) op = op.getSubGoal(subGoalNumber);
			op.setState(proverNumber, resultat);
		}
		
		return resultat;
	}/**/
	
	
	/**
	 * Function of interpretation of messages returned by the <code>why-dp</code> command
	 * 
	 * @param retourDP the message
	 * @return the result of prover work
	 */
	private int interpret(String retourDP) {
		
		try {
			String[] retour = retourDP.split("\n");
			
			int index = retour[1].indexOf(":");
			
			String res = retour[1].substring(index+2,index+3);
			
			if(res.equals(".")) { //valid case
				return 1;
			} else
			if(res.equals("*")) { //invalid case
				return 2;
			} else
			if(res.equals("?")) { //unknown case
				return 3;
			} else
			if(res.equals("#")) { //timeout case
				return 4;
			} else
			if(res.equals("!")) { //failure case
				return 5;
			} else {			  //an error occured
				return -1;
			}
		} catch(Exception e) {
			//TraceDisplay.printErr("Impossible to analyse the 'dp' command returned message");
			return -1;
		}
	}
	
	/*
	 * 
	 *
	private int interpretCoq(String msg, String err) {
		
		if(err.trim().length() > 0) {
			if(msg.trim().length() > 0) {
				if(msg.toLowerCase().contains(MessageType.ERROR) || msg.toLowerCase().contains("erreur")) {
					return 5;
				} else {
					return 1;
				}
			} else {
				return 5;
			}
		} else {
			return 1;
		}
	}/**/
}
