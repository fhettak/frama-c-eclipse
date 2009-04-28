package net.eclipse.why.editeur.actions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.eclipse.why.editeur.EditeurWHY;
import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.Function;
import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.PO;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;


/**
 * Makes a XML formated file(XMLFF) that contains all necessary informations(NI)
 * to save the set of states(SOS) of goals into a proving work project(GIAPWP).
 * 
 * @author A. Oudot
 */
public class XMLSaver {

	
	private static Shell shell;
	private static FileWriter writer;
	
	private static final String DATE_FORMAT = "dd-MM-yyyy_HH:mm:ss";
	private static String wspace;
	
	/**
	 * Initializes the 'whitespace' variable which represents an indentation
	 * in XML created file.
	 */
	private static void initSpace() {
		wspace = "";
	}
	
	/**
	 * Increments the 'whitespace' variable of 3 spaces
	 */
	private static void indent3() {
		wspace += "   ";
	}
	
	/**
	 * Increments the 'whitespace' variable of 6 spaces
	 */
	private static void indent6() {
		wspace += "      ";
	}
	
	/**
	 * Decrements the 'whitespace' variable of 3 spaces
	 */
	private static void unindent3() {
		wspace = wspace.substring(3);
	}
	
	/**
	 * Decrements the 'whitespace' variable of 6 spaces
	 */
	private static void unindent6() {
		wspace = wspace.substring(6);
	}
	
	
	/**
	 * Creates a new Shell object used to define the name and the path of
	 * the new XML file.
	 */
	public static void save() {
		
		shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		String title = "Choose file name for dump file";
		FileDialog dlg = new FileDialog (shell, SWT.SAVE);
		dlg.setFileName(createName());
		dlg.setText(title);
		String path= null;
		while(path == null) {
			path= dlg.open();
			if (path == null)
				return;

			File file= new File(path);		
			if (file.exists()) {
				if (!file.canWrite()) {
					final String msg= "File " + path + " is read only";
					MessageDialog.openError(shell, title, msg);
					path= null;
				}
				else {
					final String msg= "File " + path + " already exists. Overwrite it?";
					if (!MessageDialog.openQuestion(shell, title, msg)) {
						path = null;
					}
				}
			}
		}
		if (path != null) {
			makeFile(path);
		}
		return;
	}
	
	/**
	 * Method which creates a default name for the new XML file.
	 * 
	 * @return the default name for the new XML file
	 */
	private static String createName() {
		if(FileInfos.getName() == "") return "";
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return FileInfos.getName() + "_" + sdf.format(cal.getTime()) + ".xml";
	}
	
	
	/**
	 * Method which makes the new XML file.
	 */
	private static void makeFile(String path) {
		
		try {
			File file = new File(path);
			writer = new FileWriter(file);
			IPreferenceStore store = EditeurWHY.getDefault().getPreferenceStore();
			boolean used = store.getBoolean(IConstants.PREF_DTD_USING_FILE);
			String dtd = store.getString(IConstants.PREF_DTD_FILE_LOCATION);
			initSpace();
			if(used && dtd!=null && !dtd.trim().equals("")) {
				writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
				writer.write("\n<!DOCTYPE project SYSTEM \"" + dtd + "\">");
			} else {
				writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			}
			writer.write("\n" + wspace + "<project");
			indent6();
			writer.write("\n" + wspace + "name=\"" + FileInfos.getFullName() + "\"");
			writer.write("\n" + wspace + "context=\"" + FileInfos.getRoot() + "why" + File.separator + FileInfos.getName() + "_ctx.why\">");
			unindent6();
			indent3();
			for(Function function : FileInfos.functions) {
				boolean lemma = function.isLemma();
				if(lemma)
					writer.write("\n" + wspace + "<lemma ");
				else
					writer.write("\n" + wspace + "<function ");
				writer.write("name=\"" + function.getName() + "\">");
				indent3();
				writer.write("\n" + wspace + "<location");
				indent6();
				writer.write("\n" + wspace + "file=\"" + function.getFile() + "\"");
				writer.write("\n" + wspace + "line=\"" + function.getLoc(0) + "\"");
				writer.write("\n" + wspace + "begin=\"" + function.getLoc(1) + "\"");
				writer.write("\n" + wspace + "end=\"" + function.getLoc(2) + "\"");
				unindent6();
				writer.write("\n" + wspace + "/>\n");
				if(!lemma) {
					writer.write(wspace + "<behavior name=\"");
					writer.write(function.getBehavior() + "\">\n");
					indent3();
				}
				for(PO po : function.getPOList()) {
					writeGoal(po);
					unindent3();
					writer.write(wspace + "</goal>\n");
				}
				if(!lemma) {
					unindent3();
					writer.write(wspace + "</behavior>\n");
				}
				unindent3();
				if(lemma)
					writer.write(wspace + "</lemma>");
				else
					writer.write(wspace + "</function>");
			}
			unindent3();
			writer.write("\n" + wspace + "</project>");
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			TraceView.print(MessageType.ERROR, "XMLSaver.makeFile() : " + e);
		}	
	}
	
	/**
	 * Writes a goal in XML format
	 * 
	 * @param goal the written goal
	 * @throws IOException 
	 */
	private static void writeGoal(PO goal) throws IOException {
		
		String name = ""+goal.getNum();
		writer.write(wspace + "<goal ");
		writer.write("why_file=\"");
		writer.write(FileInfos.getRoot() + "why" + File.separator + FileInfos.getName() + "_po" + name + ".why\">");
		indent3();
		writer.write("\n" + wspace + "<location");
		indent6();
		writer.write("\n" + wspace + "file=\"" + goal.getFile() + "\"");
		writer.write("\n" + wspace + "line=\"" + goal.getLoc(0) + "\"");
		writer.write("\n" + wspace + "begin=\"" + goal.getLoc(1) + "\"");
		writer.write("\n" + wspace + "end=\"" + goal.getLoc(2) + "\"");
		unindent6();
		writer.write("\n" + wspace + "/>");
		writer.write("\n" + wspace + "<explain");
		indent6();
		writer.write("\n" + wspace + "kind=\"");
		writer.write(goal.getKind() + "\"");
		writer.write("\n" + wspace + "text=\"");
		writer.write(goal.getText() + "\"");
		unindent6();
		writer.write("\n" + wspace + "/>\n");
		if(FileInfos.provers.length > 0) {
			for(int h=0; h<FileInfos.provers.length; h++) {
				int intstate = goal.getState(h);
				String strstate = null;;
				switch(intstate) {
					case 1: strstate = "valid";		break;
					case 2: strstate = "invalid";	break;
					case 3: strstate = "unknown";	break;
					case 4: strstate = "timeout";	break;
					case 5: strstate = "failure";	break;
				}
				if(intstate > 0) {
					writer.write(wspace + "<proof");
					indent6();
					writer.write("\n" + wspace + "prover=\"");
					writer.write(FileInfos.provers[h] + "\"");
					writer.write("\n" + wspace + "status=\"");
					writer.write(strstate + "\"");
					writer.write("\n" + wspace + "timelimit=\"\"");
					writer.write("\n" + wspace + "date=\"\"");
					writer.write("\n" + wspace + "scriptfile=\"\">");
					unindent6();
					writer.write("\n" + wspace + "</proof>\n");
				}
			}
		}
	}
}
