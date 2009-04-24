package net.eclipse.why.editeur.actions;

import java.io.File;
import java.util.ArrayList;

import net.eclipse.why.editeur.Commands;
import net.eclipse.why.editeur.EditeurWHY;
import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;


/**
 * Execute verification tools commands
 * 
 * @author A. Oudot
 */
public class VerificationToolExecutor {
	
	private String error; //error message from execution
	private FileEditorInput finput; //source file
	
	/**
	 *  Run commands for executing verification tools which are
	 *  associated with the type(extension) of our file
	 * 
	 * @return true on success
	 */
	public boolean exec() {
		
		Executor executor = new Executor();

		//cleaning method
		IPreferenceStore store = EditeurWHY.getDefault().getPreferenceStore();
		boolean clean = store.getBoolean(IConstants.PREF_CLEAN_FILES1);
		if(clean) {
			Executor.clean();
		}
		
		IResource resource = finput.getFile(); //our source file
		Commands c = new Commands(resource); //a new general command
		if(!c.make()) { //we try to make real commands
			return false;
		}
		
		//we get these commands
		ArrayList<String> array;
		if( (array=c.getCommands()) == null ) {
			return false;
		}
		File rootFolder = new File(FileInfos.getRoot());
		
		if (!rootFolder.exists()) {
			if (!rootFolder.mkdir())
					return false;
		}	
		
		//and try to execute it
		if(!executor.run(rootFolder, array, true)) {
			error = executor.getError();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Get the path and the name of the active file opened
	 * in an Eclipse editor.
	 * 
	 * @return 0: type not recognized, -1: error, 1: ok
	 */
	public int checkOpenFile() {
		IResource resource = null;
		FileEditorInput editor = null;
		try {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IEditorPart part = window.getActivePage().getActiveEditor();
			if(part == null) {
				MessageDialog.openWarning(window.getShell(), "", "You have to open a file before running verification tools...");
				return 0;
			}
			IEditorInput einput = part.getEditorInput();
			editor = (FileEditorInput) einput;
			resource = (IResource) editor.getFile();
			if(!recognize(resource.getFileExtension())) {
				MessageDialog.openError(window.getShell(), "", "This type of file is not recognized");
				return 0;
			}
		} catch(Exception e) {
			TraceView.print(MessageType.ERROR, "ExecVerifTool.checkOpenFile() : " + e);
			return -1;
		}
		finput = editor;
		return 1;
	}
	
	/**
	 * Is the type of source file recognizable?
	 * 
	 * @param type the file type
	 * @return the result
	 */
	private boolean recognize(String type) {
		if(type==null)
			return false;
		IPreferenceStore store = EditeurWHY.getDefault().getPreferenceStore();
		String list = store.getString(IConstants.PREF_LIST_OF_RECOGNIZED_FILES);
		String[] types = list.split(IConstants.ELEMENT_SEPARATOR);
		for(int o=0; o<types.length; o++) {
			if(types[o].equals(type)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * The error message getter
	 * 
	 * @return the error message
	 */
	public String getError() {
		return error;
	}
	
	/**
	 * The source file getter
	 * 
	 * @return the source file
	 */
	public String getResource() {
		return finput.getPath().toString();
	}
}
