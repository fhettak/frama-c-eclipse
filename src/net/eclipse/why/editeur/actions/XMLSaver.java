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
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;


/**
 * Makes a XML formated file(XMLFF) that contains all necessary informations(NI)
 * to save the set of states(SOS) of goals into a proving work project(GIAPWP).
 * 
 * @author Aurélien Oudot
 */
public class XMLSaver {

	
	private static StringFieldEditor name;
	private static DirectoryFieldEditor directory;
	private static Shell shell;
	private static FileWriter writer;
	
	private static final String DATE_FORMAT = "dd-MM-yyyy_HH:mm:ss";
	private static String wspace;
	
	/**
	 * Creates and opens the main window which allows users to
	 * define the name and the path of the new XML file.
	 */
	public static void save() {
		createShell();
		openShell();
	}
	
	/**
	 * Inits the 'wspace' variable which represents an indentation
	 * in XML created file.
	 */
	private static void initSpace() {
		wspace = "";
	}
	
	/**
	 * Increments the 'wspace' variable of 3 whitespaces
	 */
	private static void indent3() {
		wspace += "   ";
	}
	
	/**
	 * Increments the 'wspace' variable of 6 whitespaces
	 */
	private static void indent6() {
		wspace += "      ";
	}
	
	/**
	 * Decrements the 'wspace' variable of 3 whitespaces
	 */
	private static void unindent3() {
		wspace = wspace.substring(3);
	}
	
	/**
	 * Decrements the 'wspace' variable of 6 whitespaces
	 */
	private static void unindent6() {
		wspace = wspace.substring(6);
	}
	
	
	/**
	 * Creates a new Shell object used to define the name and the path of
	 * the new XML file.
	 */
	private static void createShell() {
		
		
		Color bgcolor = new Color(null, 200, 255, 225);
		
		shell = new Shell(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.TITLE | SWT.PRIMARY_MODAL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 10;
        gridLayout.marginWidth = 10;
        gridLayout.verticalSpacing = 10;
        shell.setLayout(gridLayout);
        shell.setText("Save results into a XML file");
        shell.setBackground(bgcolor);
        
        
        Composite main = new Composite(shell, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        GridData data = new GridData(GridData.FILL_BOTH);
        data.widthHint = 600;
        main.setLayout(layout);
        main.setLayoutData(data);
        main.setBackground(bgcolor);
        
        
        name = new StringFieldEditor("name", "Name :", main);
        name.fillIntoGrid(main, 3);
        name.getLabelControl(main).setBackground(bgcolor);
        name.getTextControl(main).setForeground(new Color(null, 0, 0, 230));
        name.getLabelControl(main).setForeground(new Color(null, 0, 0, 230));
        
        
        directory = new DirectoryFieldEditor("dir", "Directory :", main);
        directory.getLabelControl(main).setBackground(bgcolor);
        directory.getTextControl(main).setForeground(new Color(null, 0, 0, 230));
        directory.getLabelControl(main).setForeground(new Color(null, 0, 0, 230));
        
        
        Composite buttonComposite = new Composite(shell, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 2;
        buttonComposite.setLayout(layout);
        buttonComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        buttonComposite.setBackground(bgcolor);
        
        Button button1 = new Button(buttonComposite, SWT.NONE);
        button1.setText("Ok");
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 80;
        button1.setLayoutData(gridData);
        button1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				String l1 = name.getStringValue();
				String l2 = directory.getStringValue();
				
				if(l1==null || l1.trim().equals("")) {
					MessageDialog.openError(new Shell(), "Parameter Error", "The name can't be null");
					return;
				}
				
				if(l2==null || l2.trim().equals("")) {
					MessageDialog.openError(new Shell(), "Parameter Error", "The directory can't be null");
					return;
				}
				
				makeFile();
				shell.close();
			}
		});
        
        Button button2 = new Button(buttonComposite, SWT.NONE);
        button2.setText("Cancel");
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 80;
        button2.setLayoutData(gridData);
        button2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
        
        
        shell.pack();
	}
	
	
	/**
	 * Opens the new Shell object.
	 * 
	 * @see createShell()
	 */
	private static void openShell() {
		String nmn = createName();
		name.setStringValue(nmn);
		String rtl = FileInfos.getRoot();
		directory.setStringValue(rtl);
		shell.open();
		shell.layout();
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
	private static void makeFile() {
		
		String nm = name.getStringValue();
		if(!nm.trim().endsWith(".xml")) {
			nm += nm + ".xml";
		}
		
		try {
			File file = new File(directory.getStringValue() + nm);
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
			for(int f=0; f<FileInfos.functions.size(); f++) {
				Function function = (Function)FileInfos.functions.get(f);
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
					if(po.getNbSubGoals() > 0) {
						for(int i=1; i<=po.getNbSubGoals(); i++) {
							PO op = (PO)po.getSubGoal(i);
							writeGoal(op);
							unindent3();
							writer.write(wspace + "</goal>\n");
						}
					}
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
		int x;
		if((x = goal.getSubNum())>0) name += ("-"+x);
		
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
