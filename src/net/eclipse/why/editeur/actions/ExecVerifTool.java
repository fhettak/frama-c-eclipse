package net.eclipse.why.editeur.actions;

import java.io.File;
import java.util.ArrayList;

import net.eclipse.why.editeur.Commands;
import net.eclipse.why.editeur.EditeurWHY;
import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.actions.TraceDisplay.MessageType;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
 * @author Aurélien Oudot
 */
public class ExecVerifTool {
	
	private String ERROR; //error message from execution
	private FileEditorInput finput; //source file
	
	/**
	 * Class constructor
	 */
	public ExecVerifTool() {
		this.ERROR = "";
	}
	
	/*
	 *  Lance les commandes d'exécution des outils de vérification
	 *  associées à un type de fichier précis.<BR>
	 * 
	 * @return boolean true si tout s'est bien déroulé, false sinon.
	 *
	public boolean exec() {
		
		
		int sep = SOURCE.lastIndexOf("/");
		//int dot = SOURCE.lastIndexOf(".");
		
		String cmd;
		String name = FileInfos.commonFileName;
		boolean res = true;
		
		CmdExecutor cexec = new CmdExecutor();
			
		
		try {
		
			cmd = "cd " + SOURCE.substring(0,sep+1);
			if(!cexec.exec2(cmd, false)) {
				if(!cexec.exec2(cmd, true)) {
					cexec.close();
					return false;
				}
			}
			
			
			
			IPreferenceStore store = EditeurWHY.getDefault().getPreferenceStore();
			boolean clean = store.getBoolean(IConstants.PREF_CLEAN_FILES1);
//			if(clean) {
//				cmd = "rm -f " + name + ".makefile";
//				cexec.exec2(cmd, true);
//			}
			if(clean) {
				cmd = "rm -f why/" + name + "*.why";
				cexec.exec2(cmd, true);
			}
			
		    
		    String[] cmds = new String[0];
			cmd = "";
		    
			
			cmds = store.getString(IConstants.PREF_LIST_OF_COMMANDS).split(IConstants.LINE_SEPARATOR);
			if(cmds==null) {
				TraceDisplay.printMsg("No command to execute!");
				return false;
			}
			int b = 0;
			while(b<cmds.length && !cmds[b].equals(TYPE)) {
				b++;
			}
			b++;
			while(b<cmds.length && cmds[b].endsWith(IConstants.ELEMENT_SEPARATOR)) {
				if(cmds[b].trim().equals("")) {
					TraceDisplay.printMsg("No command to execute!");
				} else {
					//String[] elts = cmds[b].split(IConstants.ELEMENT_SEPARATOR);
					//cmd = elts[0] + " " + name + "." + elts[1] + " " + elts[2];
					//cmd = cmd.trim();
					String[] doggy = cmds[b].split(IConstants.ELEMENT_SEPARATOR)[0].split(IConstants.FILE_DEMONSTRATOR);
					cmd = doggy[0];
					for(int d=1; d<doggy.length; d++) {
						cmd += FileInfos.commonFileName + doggy[d];
					}
					cmd = cmd.trim();
					if(!cexec.exec(cmd,true)) {
						ERROR = cexec.getError();
						cexec.close();
						return false;
					}
				}
				b++;
			}

			
			
//			if(cmds==null) {
//				TraceDisplay.printMsg("No command to execute!");
//				return false;
//			}
//			
//			for(int t=0; t<cmds.length; t++) {
//				if(cmds[t].trim().equals("")) {
//					TraceDisplay.printMsg("No command to execute!");
//				} else {
//					String[] elts = cmds[t].split(IConstants.ELEMENT_SEPARATOR);
//					cmd = elts[0] + " " + name + "." + elts[1] + " " + elts[2];
//					cmd = cmd.trim();
//					if(!cexec.exec(cmd,true)) {
//						cexec.close();
//						return false;
//					}
//				}
//			}
		
		} catch(IOException e) {
			TraceDisplay.printErr("ExecVerifTool.exec() ~> CmdExecutor.executeCommand() : " + e);
			res = false;
		}
		
		ERROR = cexec.getError();
		cexec.close();
		return res;
	}/**/
	
	
	/**
	 *  Run commands for executing verification tools which are
	 *  associated with the type(extension) of our file
	 * 
	 * @return true si tout s'est bien déroulé, false sinon.
	 */
	public boolean exec() {
		
		CommandExecutor executor = new CommandExecutor();

		//cleaning method
		IPreferenceStore store = EditeurWHY.getDefault().getPreferenceStore();
		boolean clean = store.getBoolean(IConstants.PREF_CLEAN_FILES1);
		if(clean) {
			Executor.rm();
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
		
		//and try to execute it
		if(!executor.execute(new File(FileInfos.getRoot()), array, true)) {
			ERROR = executor.getError();
			return false;
		}
		
		return true;
	}

	
	/*
	 *  On récupère ici le fichier C sélectionné précédé de son Path.
	 * On renseigne ensuite certains champs de la classe <code>FileInfos</code> en appelant
	 * la méthode <code>FileInfos.complete()</code>.<BR>
	 *  
	 * @deprecated
	 * 		{@link #checkOpenFile()}
	 *
	public String checkSelectedCFile() {
		
		String result = null;
		IResource resource = null;
		
		try {
			
			IViewReference[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
			for(int e=0; e<views.length; e++) {
				IViewPart viewPart = views[e].getView(false);
				
				// Vue 'C/C++ Projects'
				if( viewPart instanceof CView) {
					CView cview = (CView)viewPart;
					TreeSelection tselect = (TreeSelection)cview.getViewer().getSelection();
					Object o = tselect.getFirstElement();
					// fichier .c
					if( o instanceof TranslationUnit ) {
						TranslationUnit unit = (TranslationUnit)tselect.getFirstElement();
						IResource r = unit.getResource();
						String type_tmp = r.getFileExtension();
						if(recognize(type_tmp)) {
							//type = type_tmp;
							result = r.getLocation().toString();
							resource = r;
							break;
						}
					}
					// autre type de fichier
					IFile file = (IFile)tselect.getFirstElement();
					IPath path = file.getLocation();
					String type_tmp = file.getFileExtension();
					if(recognize(type_tmp)) {
						//type = type_tmp;
						result = path.toString();
						resource = (IResource)file;
						break;
					}
				}
				
				// Vue 'Package Explorer'
				if( viewPart instanceof PackageExplorerPart ) {
					PackageExplorerPart p = (PackageExplorerPart)viewPart;
					TreeSelection i = (TreeSelection)p.getTreeViewer().getSelection();
					// fichier autre que .java
					if(i.getFirstElement() instanceof IFile) {
						IFile file = (IFile)i.getFirstElement();
						IPath path = file.getLocation();
						String type_tmp = file.getFileExtension();
						if(recognize(type_tmp)) {
							//type = type_tmp;
							result = path.toString();
							resource = (IResource)file;
							break;
						}
					}
					// fichier .java
					if(i.getFirstElement() instanceof CompilationUnit) {
						CompilationUnit unit = (CompilationUnit)i.getFirstElement();
						IResource r = unit.getResource();
						String type_tmp = r.getFileExtension();
						if(recognize(type_tmp)) {
							//type = type_tmp;
							result = r.getLocation().toString();
							resource = r;
							break;
						}
					}
				}
				
				// Vue 'Navigateur' : tous les fichiers sont traités de  manière équivalente
				// au sein de cette vue!
				if( viewPart instanceof ResourceNavigator) {
					ResourceNavigator r = (ResourceNavigator)viewPart;
					TreeSelection i = (TreeSelection)r.getViewer().getSelection();
					IFile file = (IFile)i.getFirstElement();
					IPath path = file.getLocation();
					String type_tmp = file.getFileExtension();
					if(recognize(type_tmp)) {
						//type = type_tmp;
						result = path.toString();
						resource = (IResource)file;
					}
				}
			}

		} catch(Exception e) {
			TraceDisplay.print(MessageType.ERROR, "ExecVerifTool.checkSelectedCFile() : " + e);
			return null;
		}
				
		if(result!=null) {
			//SOURCE = result;
			//TYPE = type;
			RESOURCE = resource;
		}
		return result;
	}/**/
	
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
			TraceDisplay.print(MessageType.ERROR, "ExecVerifTool.checkOpenFile() : " + e);
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
		return ERROR;
	}
	
	/**
	 * The source file getter
	 * 
	 * @return the source file
	 */
	public String getResource() {
		return finput.getPath().toString();
	}
	
	
	/**
	 * An unused console writer!!!
	 */
	public void displayConsole() {
		
		String m = EditeurWHY.getDefault().getBundle().getSymbolicName();
		ILog l = EditeurWHY.getDefault().getLog();
		Status status = new Status(IStatus.ERROR, m, 0, "Coucou :-)))", null);
		l.log(status);
	}
}
