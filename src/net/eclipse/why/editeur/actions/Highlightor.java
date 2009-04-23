package net.eclipse.why.editeur.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.editors.GeneralEditor;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;

import org.eclipse.cdt.internal.ui.editor.CEditor;


/**
 * Static methods which are used to highlight (change the
 * background color) of source code in files, knowing the
 * name of a .xpl file or a message containing a reference
 * to a place in a file.
 * 
 * @author A. Oudot
 */
public class Highlightor {
	
	
	private static int line;			// Line number
	private static int char1;			// First character number
	private static int char2;			// Last character number
	private static String kind, motif;
	private static boolean adjust_zone = false;
	
	private static StyledText text, old;   // current working text and previous one
	private static MRListener oldlistener; // the old text modify listener
	private static int x = -1;             // old first character to highlight
	private static int y;                  // old last character to highlight
	
	
	/**
	 * A modify listener for StyledText objects
	 * 
	 * @author A. Oudot
	 */
	private static class MRListener implements ExtendedModifyListener {
		/**
		 * If the current text is modified and was highlighted before,
		 * the color disappears immediately.
		 * 
		 * @param event the event
		 */
		public void modifyText(ExtendedModifyEvent event) {
			
			if(text.equals(old)) {
				
				if(event.start <= x) {
					x += event.length;
					x -= event.replacedText.length();
				}
				
				StyleRange[] rangers = text.getStyleRanges(x, y);
				int n = 0;
				int m = rangers.length;
				for(int w=0; w<m; w++) {
					if(rangers[w].background.equals(IConstants.HIGHLIGHT_GREEN) ||
					   rangers[w].background.equals(IConstants.HIGHLIGHT_RED) ) {
						rangers[w].background = null;
						n++;
					}
				}
				if(n > 0) {
					text.replaceStyleRanges(x, y, rangers);
					text.redrawRange(x, y, true);
					reinit();
				}
			}	
		}	
	}
	
	/**
	 * Initialization function : there is not previous
	 * StyledText and not previous ModifyListener
	 */
	private static void reinit() {
		old = null;
		x = -1; y = -1;
		text.removeExtendedModifyListener(oldlistener);
	}
	
	/**
	 * Goal setter
	 * 
	 * @param goal the goal number
	 */
	public static void setGoal(int goal) {
		kind = "";
		motif = "";
	}
	
	/**
	 * Giving the line number, the beginning and ending characters
	 * this function highlights the matching zone in a file.
	 * 
	 * @param file the file
	 * @param false if the zone is an error area, true otherwise
	 */
	private static void highLight(String file, boolean green) {
		int a, b;
		FileReader fr;
		BufferedReader in;
			
		try {
				
			String c;
			int ch = 0;
			int i = 1;
			
			File test = new File(file);
			if (!test.exists() || test.isDirectory()) {
					return;
			}
			//Open the source file
			IEditorPart editor = null;
			try {
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
					IFile ifile = root.getFileForLocation(new Path(file));
					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					IWorkbenchPage page = window.getActivePage();
					editor = IDE.openEditor(page, ifile, true);
					adjust_zone = file.equals(FileInfos.getFullName());
			} catch (Exception e) {
				TraceView.print(MessageType.WARNING, "File is unknown: " + file);
				e.printStackTrace();
				return;
			}			
				
			if(line > 0) {
				
				//Computation of first character number after which the text will be highlighted
				fr = new FileReader(file);
				in = new BufferedReader(fr);
				
				while( (c = in.readLine()) != null ) {
					if( i < line ) {
						ch += c.length() + 1;
						i++;
					}
				}
				fr.close();
				in.close();
				
				//Here we adjust the landmarks of highlighted text zone
				//to compensate for '\r\n' in the text.
				a = char1;
				b = char2;
				
				if(adjust_zone) {
					try {
						int[] rec = new int[3];
						rec = FileInfos.adjustHighLightZone(ch, a, b);
						ch = rec[0];
						a = rec[1];
						b = rec[2];
					} catch(Exception e){}
				}
					
				//... et highlighting of the code which corresponds to selected goal 	
				
				EditorPart part = (EditorPart)editor;
				Color bColor;
				if(green) {
					bColor = IConstants.HIGHLIGHT_GREEN;
				} else {
					bColor = IConstants.HIGHLIGHT_RED;
				}
				ISourceViewer source = null;
				
			    if(part instanceof CEditor) { //C editor
				
					CEditor edit = (CEditor)part;
					source = edit.getViewer();
					text = source.getTextWidget(); //the text widget
				} else if (part instanceof GeneralEditor) {
					source = ((GeneralEditor)part).getSViewer();
					text = source.getTextWidget();	//hello text widget					

				} else {
					//Another editor
					text = null;
				}
				
				if(text != null) {
					//text <- new ModifyListener()
					MRListener listener = new MRListener();
					text.addExtendedModifyListener(listener);
					
					
					if(old != null && !old.isDisposed()) {
						//the StyleRange object created before(during
						//a previous execution) has to
						//be deleted when a new zone is highlighted
						
						StyleRange[] rangers = old.getStyleRanges(x, y);
						for(int k=0; k<rangers.length; k++) {
							rangers[k].background = null;
						}
						old.replaceStyleRanges(x, y, rangers);
						old.redrawRange(x, y, true);
						old.removeExtendedModifyListener(oldlistener);
					}
					
					//the given landmarks are not the same in
					//error messages than in location indications
					x = ch + a -((green)?1:0);
					y = b  - a +((green)?1:0);
					StyleRange[] rangers = text.getStyleRanges(x, y);
					for(int g=0; g<rangers.length; g++) {
						rangers[g].background = bColor;
					}
					
					//we validate the new highlight zone and
					//the text becomes the old text for the
					//next execution of this function...
					text.replaceStyleRanges(x, y, rangers);
					text.redrawRange(x, y, true);
					old = text;
					oldlistener = listener;
					
					
					ITextHover hover = null;
					source.setTextHover(hover, "");
					
					//we put the cursor at the end of the new highlighted zone
					ISelection select = new TextSelection(ch+b, 0);
					editor.getEditorSite().getSelectionProvider().setSelection(select);
					
				} else {
					//if no text widget has been found, we'll content ourselves with a simple text selection
					ISelection select = new TextSelection(ch+a, b-a);
					editor.getEditorSite().getSelectionProvider().setSelection(select);
				}
				
				//the position of the first character not highlighted 
				FileInfos.endHighlightChar = ch + b;
				
			} else {
				//if the zone is not identified, we put the cursor as before...
				ISelection select = new TextSelection(FileInfos.endHighlightChar +1, 0);
				editor.getEditorSite().getSelectionProvider().setSelection(select);
			}
			
		} catch(Exception e) {
			TraceView.print(MessageType.ERROR, "Highlightor.highLight() : " + e);
		}
	}
	
	
	/**
	 * Check the zone which will be highlighted in a .xpl file
	 * 
	 * @return the file in which this zone is
	 */
	private static String checkZoneFromXPLFile(String file) {
		
		String c = "";
		String result_file = "";
		
		try {
			
			FileReader fr = new FileReader(file);
			BufferedReader in = new BufferedReader(fr);
			int index = 0;
			
			while( (c = in.readLine()) != null ) {
				
				if(c.startsWith("kind =")) {
					index = 6;
					kind = c.substring(index).trim();
				} else if(c.startsWith("text =")) {
					index = 6;
					//text = c.substring(index).trim();
				} else if(c.startsWith("file = \"")) {
					index = 8;
					result_file = c.substring(index,c.length()-1).trim();
				} else if(c.startsWith("line =")) {
					index = 6;
					line = Integer.parseInt(c.substring(index).trim());
				} else if(c.startsWith("begin =")) {
					index = 7;
					char1 = Integer.parseInt(c.substring(index).trim())+1;
				} else if(c.startsWith("end =")) {
					index = 5;
					char2 = Integer.parseInt(c.substring(index).trim());
				} else if(c.startsWith("pred = \"")) {
					index = 8;
					if(c.trim().endsWith("\"")) {
						motif = c.substring(index,c.length()-1).trim();
					} else {
						motif = c.substring(index,c.length()).trim();
						while( !(c = in.readLine()).endsWith("\"") ) {
							motif += c.substring(index,c.length()).trim();
						}
						motif += c.substring(index,c.length()-1).trim();
					}
				}
			}
			
			fr.close();
			in.close();
			
		} catch(Exception e) {
			line = 0;
			char1 = 0;
			char2 = 0;
		}
		
		return result_file;
	}
	
	
	/**
	 * Check of line and characters numbers from an
	 * error message to highlight a source file
	 * 
	 * @param errorMsg the error message
	 */
	private static String checkZoneFromErrorMessage(String errorMsg) {
		
		/*
		 * Error line model : File "tierce.c", line 23, characters 18-34:
		 */
		
		int x, y;
		String[] err = errorMsg.split("\n");
		String file = "";
		
		for(int e=0; e<err.length; e++) {
			if( !err[e].startsWith("Warning:") && err[e].contains("File") && err[e].contains("line") && err[e].contains("characters") ) {
				x = err[e].indexOf("\"");
				err[e] = err[e].substring(x+1);
				x = err[e].indexOf("\"");
				file = err[e].substring(0, x);
				
				x = err[e].indexOf("line");
				err[e] = err[e].substring(x);
				x = err[e].indexOf(" ");
				y = err[e].indexOf(",");
				line = Integer.parseInt(err[e].substring(x+1,y)); //the 23
				
				x = err[e].indexOf("characters");
				err[e] = err[e].substring(x);
				x = err[e].indexOf(" ");
				y = err[e].indexOf("-");
				char1 = Integer.parseInt(err[e].substring(x+1,y)); //the 18
				
				x = err[e].indexOf("-");
				y = err[e].indexOf(":");
				char2 = Integer.parseInt(err[e].substring(x+1,y)); //last but not least, the 34 !				
				break;
			}
		}
		
		return file;
	}
	
	/**
	 * Call the highlight method giving an error message
	 * 
	 * @param clue the error message
	 */
	public static void selectError(String clue) {
		String file = checkZoneFromErrorMessage(clue);
		if(exists(file)) {
			highLight(file,false);
		}
	}
	
	/**
	 * Call the highlight() method searching the .xpl file
	 * which checks whith the concerned Proof Obligation.
	 * 
	 * @return the 'kind' attribute of the PO
	 */
	public static String selectFromXPL(String xplFile) {
		String file = checkZoneFromXPLFile(xplFile);
		if(exists(file)) {
			highLight(file,true);
		} else {
			TraceView.print(MessageType.WARNING, "No source file found in .xpl file for this goal");
		}
		return kind;
	}
	
	/**
	 * Check if a file exists
	 * 
	 * @param file the file
	 * @return the result
	 */
	private static boolean exists(String file) {
		if(file == null || file.trim().equals("")) return false;
		File f = new File(file);
		if(f.exists()) return true;
		else {
			file = FileInfos.getRoot() + file;
			f = new File(file);
			if(f.exists()) return true;
			return false;
		}
	}
	
}
