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

		String mode = EditeurWHY.getDefault().getPreferenceStore().getString(
				IConstants.PREF_RUN_OPTIONS);

		if (mode.equals(IConstants.PREF_RUN_OPTIONS_NORMAL_MODE)) {
			boo = make1();
		} else {
			boo = make2();
		}

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
	 * Make commands from the Verification Tools Preferences Page.
	 * 
	 * @return boolean
	 */
	private boolean make2() {

		// We get commands
		String[] d2r2 = new String[0];
		String command = "";
		d2r2 = EditeurWHY.getDefault().getPreferenceStore().getString(
				IConstants.PREF_LIST_OF_COMMANDS).split(
				IConstants.LINE_SEPARATOR);
		if (d2r2 == null) {
			TraceView.print(MessageType.WARNING, "No command to execute!");
			return false;
		}

		// we search the first command which corresponds with the selected file
		// type
		int b = 0;
		while (b < d2r2.length && !d2r2[b].equals(resource.getFileExtension())) {
			b++;
		}
		b++;

		while (b < d2r2.length
				&& d2r2[b].endsWith(IConstants.ELEMENT_SEPARATOR)) {

			String[] doggy = d2r2[b].split(IConstants.ELEMENT_SEPARATOR)[0]
					.split(IConstants.DIR_DEMONSTRATOR);
			command = doggy[0];
			for (int d = 1; d < doggy.length; d++) {

				command += FileInfos.getRoot() + doggy[d];
			}
			doggy = command.split(IConstants.FILE_DEMONSTRATOR);
			command = doggy[0];
			for (int d = 1; d < doggy.length; d++) {
				command += FileInfos.getName() + doggy[d];
			}
			command = command.trim();
			commands.add(command);
			b++;
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
