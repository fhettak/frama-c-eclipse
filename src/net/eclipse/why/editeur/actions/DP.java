package net.eclipse.why.editeur.actions;

import java.io.File;

/**
 * The 'dp' command container
 * 
 * @author A. Oudot
 */
public class DP {

	/**
	 * Command getter : check if the 'why-dp' command exists
	 * in the system. If it doesn't exist, 'dp' is returned.
	 * This function action is due to changes in Why platform.
	 * 
	 * @return String
	 */
	public static String get() {
		try {
			CommandExecutor executor = new CommandExecutor();
			File f = new File("/");
			if(executor.execute(f, "which why-dp", true)) {
				return "why-dp";
			} else {
//				if(!executor.execute(f, "which dp", true)) {
//					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//					MessageDialog.openWarning(window.getShell(), "", "Why commands might fail. You should verify that your WHY installation is complete.");
//				}
				return "dp";
			}
		} catch(Exception e) {
		}
		return "dp";
	}
}
