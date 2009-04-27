package net.eclipse.why.editeur.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.Function;
import net.eclipse.why.editeur.PO;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

/**
 * Makes all PO and Function objects in FileInfos class.
 * 
 * @author A. Oudot
 */
public class InfosMaker {

	private String tag;

	/**
	 * Main function to make PO and Function objects into the FileInfos class
	 * reading .xpl or .why files from the why/ repository .
	 * 
	 * @return true
	 */
	public boolean make() {

		tag = "";
		File root = new File(FileInfos.getRoot() + "why" + File.separator );

		final FileFilter xplFilter = new FileFilter() {
	        //Only accept those files that are .project
	        public boolean accept(File pathName) {
	            String path = pathName.getName();	            
	            return path.endsWith(".xpl");
	        }
	    };		
		
		int m = 1;		
		File[] list = root.listFiles(xplFilter);
		Arrays.sort(list);
		for (File f : list) {
			addNewPO(f.getPath(), m);
			m++;
		}
		FileInfos.numberOfGoals = m - 1;

		return true;
	}

	/**
	 * Make a new Function() object from .loc file and record it in the
	 * FileInfos class
	 * 
	 * @param tag
	 *            the title tag of the function
	 * @param is_lemma
	 *            true if the function is a lemma
	 * @return true if the name or the behavior of this new function have been
	 *         found
	 */
	private boolean addNewFunction(String tag, boolean is_lemma) {

		String name = null;
		String behavior = null;
		boolean r = false;

		String fname = "";
		String ffile = "";
		int[] floc = new int[3];

		// get the .loc file
		String fileX = FileInfos.getRoot() + FileInfos.getName() + ".loc";
		FileReader fr;
		BufferedReader in;

		try {
			fr = new FileReader(fileX);
			in = new BufferedReader(fr);
			String l;
			boolean into = false;

			// reading .loc file
			while ((l = in.readLine()) != null) {

				// the starting tag has been read and here is a blank line =>
				// Stop!
				if (into && l.trim().equals("")) {
					break;
				}

				// reading the starting tag
				if (l.trim().equals(tag)) {
					into = true;
				}

				// the starting tag has been read
				if (into) {
					if (l.startsWith("name =")) { // here is the function's name
						name = getPropertyValue(l.substring(6));
						r = true;
					}
					if (l.startsWith("behavior =")) { // here is the function's
														// behavior
						behavior = getPropertyValue(l.substring(10));
						r = true;
					}
					if (l.startsWith("file =")) { // here is the function's
													// source file
						ffile = getPropertyValue(l.substring(6));
					}
					if (l.startsWith("line =")) { // here is the function's code
													// location
						floc[0] = Integer.parseInt(l.substring(6).trim());
					}

					if (l.startsWith("begin =")) { // here is the function's
													// code location
						floc[1] = Integer.parseInt(l.substring(7).trim()) + 1;
					}

					if (l.startsWith("end =")) { // here is the function's code
													// location
						floc[2] = Integer.parseInt(l.substring(5).trim());
					}
				}
			}

			// make the behavior text
			if (behavior == null || behavior.trim().equals("")) {
				if (name == null || name.trim().equals(""))
					fname = "";
				else
					fname = name;
			} else {
				if (name == null || name.trim().equals(""))
					fname = behavior;
				else
					fname = behavior + " of " + name;
			}

			in.close();
			fr.close();

		} catch (FileNotFoundException f) {
			TraceView.print(MessageType.WARNING, "File " + fileX
					+ " doesn't exist");
		} catch (IOException io) {
			TraceView.print(MessageType.ERROR,
					"InfosMaker.getLocFunctionName() : " + io);
		}

		// here is the creation of the new Function() object :
		Function function = new Function();
		function.setName(tag.substring(1, tag.length() - 1));
		if (is_lemma)
				function.putToLemma();
		function.setBehavior(fname);
		function.setFile(ffile);
		function.setLoc(floc);
		FileInfos.functions.add(function);

		return r;
	}
	
	/**
	 * Record a new PO object in the FileInfos class from the description into
	 * the .xpl file.
	 * 
	 * @param  path the path to the file
	 * @return false if this function fails
	 */
	private boolean addNewPO(String path, int m) {

		String l, t;
		int index;
		int mprim = 0;
		int f = FileInfos.functions.size();

		boolean r = true;

		try {

			String kind = null, text = null, file = null, motif = null;
			int line = 0, char1 = 0, char2 = 0;
			boolean lemma = false;

			// get the .xpl file which corresponds to the goal
			FileReader fr = new FileReader(path);
			BufferedReader in = new BufferedReader(fr);

			// first line of the file => function's name
			l = in.readLine();
			if (l == null) {
				TraceView.print(MessageType.ERROR, "File is empty: " + path);				
				return false;
			}
			int l1 = l.lastIndexOf("_po_");
			// we get the function's name between '[' and ']'
			if (l1 >= 0) {
				t = l.substring(0, l1) + "]";
			} else {
				t = l.substring(0, l.lastIndexOf("]") + 1);
				lemma = true;
			} 
			
			// if the function found is unknown, we create it :
			if (!t.equals(tag) || lemma) {
				this.tag = t;
				addNewFunction(tag, lemma);
				f++;
				mprim = 1;
			} else {
				mprim = ((PO) FileInfos.goals.get(FileInfos.goals.size() - 1))
						.getNum_in_f() + 1;
			}

			// we continue to read the file and we record what is interesting
			// ('kind', 'text', 'file', 'line', 'begin' and 'end' fields)
			boolean text_mode = false;
			while (l != null) {
				if (l.startsWith("kind =")) {
					index = 6;
					kind = getPropertyValue(l.substring(index));
				} else if (l.startsWith("text =")) {
					index = 6;
					text = getPropertyValue(l.substring(index));
					text_mode = true;
				} else if (l.startsWith("file =")) {
					index = 6;
					file = getPropertyValue(l.substring(index));
				} else if (l.startsWith("line =")) {
					index = 6;
					line = Integer.parseInt(l.substring(index).trim());
				} else if (l.startsWith("begin =")) {
					index = 7;
					char1 = Integer.parseInt(l.substring(index).trim()) + 1;
				} else if (l.startsWith("end =")) {
					index = 5;
					char2 = Integer.parseInt(l.substring(index).trim());
				} else if (l.startsWith("pred = \"")) {
					index = 8;
					if (l.trim().endsWith("\"")) {
						motif = l.substring(index, l.length() - 1).trim();
					} else {
						motif = l.substring(index, l.length()).trim();
						while (!(l = in.readLine()).endsWith("\"")) {
							motif += l.substring(index, l.length()).trim();
						}
						motif += l.substring(index, l.length() - 1).trim();
					}
				} else if (text_mode) {
					if (l != null && !l.trim().equals("") && !l.contains(" = "))
						text += " " + l.trim();
					else
						text_mode = false;
				}
				l = in.readLine();
			}

			// now, we create the new PO and we add it in the ArrayList of goals
			// of the FileInfos class
			PO po = new PO();
			po.setNum(m);
			po.setFnum(f);
			po.setNum_in_f(mprim);
			po.setName("" + mprim);
			po.setFile(file);
			po.setLoc(new int[] { line, char1, char2 });
			po.setKind(kind);
			po.setText(text);
			po.makeTitle();
			po.initStates();
			po.setXplFile(path);
			FileInfos.goals.add(po);
			((Function) FileInfos.functions.get(FileInfos.functions.size() - 1)).addPO(po);

			in.close();
			fr.close();

		} catch (Exception e) {
			TraceView.print(MessageType.ERROR, "Error during parsing xpl file: " + e);
			r = false;
		}

		return r;
	}

	/**
	 * Get the value of the property from the <code>name = "value"</code> or
	 * <code>name = value</code> string.
	 * 
	 * @param s
	 *            the source string
	 * @return the value
	 */
	private String getPropertyValue(String s) {
		s = s.trim();
		if (s.startsWith("\"")) {
			s = s.substring(1, s.length() - 1);
		}
		return s.trim();
	}
}
