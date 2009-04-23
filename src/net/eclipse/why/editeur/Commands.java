package net.eclipse.why.editeur;

import java.util.ArrayList;

import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.QualifiedName;

/**
 * Class to make and store commands which have to be executed to run
 * verification tools.
 * 
 * @author A. Oudot
 */
public class Commands {

	private ArrayList<String> commands; // commands
	private IResource resource;

	public Commands(IResource r) {
		commands = new ArrayList<String>();
		resource = r;
	}

	/**
	 * Make commands
	 * 
	 * @return boolean
	 */
	public boolean make() {

		boolean boo = true;

		boo = make1();

		return boo;
	}

	/**
	 * Make commands from properties of the selected file
	 * 
	 * @return boolean
	 */
	private boolean make1() {

		try {
			String opt = resource.getPersistentProperty(new QualifiedName("",
					IConstants.PROP_WHYOPT));

			if (opt == null)
				opt = IConstants.PROP_WHYOPT_DEFAULT;
			boolean separation = opt.substring(0, 1).equals("1");
			boolean split = opt.substring(1, 2).equals("1");
			boolean overflow = opt.substring(2, 3).equals("1");
			boolean fastwp = opt.substring(3).equals("1");

			String type = resource.getFileExtension();
			String w = "";

			if (type.equals("c")) { // ##### C FILE #####
				w = "frama-c -jessie-analysis -jessie-gen-goals ";
				if (separation) {
					w += "-jc-opt -separation ";
				}
				if (split || fastwp) {
					w += "-why-opt ";
					if (split) {
						w += "-split-user-conj ";
					}
					if (fastwp) {
						w += "-fast-wp ";
					}
				}
				if (overflow) {
					w += "-jessie-int-model bounded ";
				}
				w += "../" + FileInfos.getName() + ".c";
				commands.add(w);

			} else if (type.equals("jc")) { // ##### JESSIE FILE #####
				w = "jessie ";
				if (split || fastwp) {
					w += "-why-opt ";
					if (split) {
						w += "-split-user-conj ";
					}
					if (fastwp) {
						w += "-fast-wp ";
					}
				}
				w += "-locs " + FileInfos.getName() + ".jloc ";
				w += FileInfos.getName() + ".jc";

				commands.add(w);
				commands.add("make -f " + FileInfos.getName()
						+ ".makefile goals");
			}

		} catch (Exception e) {
			TraceView.print(MessageType.ERROR, "Commands.make1() : " + e);
			return false;
		}

		return true;
	}

	/**
	 * Gets commands
	 * 
	 * @return ArrayList
	 */
	public ArrayList<String> getCommands() {

		if (commands.size() == 0) {
			return null;
		}

		return commands;
	}

}
