package net.eclipse.why.editeur.preferences;

import java.util.ArrayList;
import java.util.Vector;

import net.eclipse.why.editeur.EditeurWHY;
import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The verification tools preferences page
 * 
 * @author A. Oudot
 */
public class VerificationToolsPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {

	
	private TreeViewer viewer;
	private Vector<Extension> vector;
	private String COMMANDS;
	private String TYPES;
	private String MODE;
	
	private BooleanFieldEditor clean;
	private RadioGroupFieldEditor rfe;
	private Group grX;
	private Button n, add, delete, up, down;
	private Tree tree;
	
	
	/**
	 * The class constructor
	 */
	public VerificationToolsPreferencesPage() {
		super();
		setPreferenceStore(EditeurWHY.getDefault().getPreferenceStore());
	}
	
	
	/**
	 * General object in the TreeViewer which can be
	 * an Extension object or a Command object
	 * 
	 * @author A. Oudot
	 */
	abstract class TreeElement {
		
		private String ext;		//the extension, the file type
		private String command; //the command
		
		/**
		 * Class constructor for Extension objects
		 * 
		 * @param String[] the extension
		 */
		public TreeElement(String[] e) {
			ext = e[0];
		}
		
		/**
		 * Class constructor for Command objects
		 * 
		 * @param String the commands
		 */
		public TreeElement(String a) {
			command = a;
		}
		
		/**
		 * Extension getter
		 * 
		 * @return String[1]
		 */
		public String[] getExt() {
			return new String[]{ext};
		}
		
		/**
		 * Command getter
		 * 
		 * @return String
		 */
		public String getCommand() {
			return command;
		}
		
		/**
		 * Extension setter
		 * 
		 * @param String[]
		 */
		public void setExt(String[] e) {
			ext = e[0];
		}
		
		/**
		 * Command setter
		 * 
		 * @param String
		 */
		public void setCommand(String a) {
			command = a;
		}
		
		public abstract ArrayList<TreeElement> getCmds();
		public abstract Extension getParent();
		
	}
	
	/**
	 * Extension object which contains a set of commands
	 * 
	 * @author A. Oudot
	 */
	class Extension extends TreeElement {
	
		private ArrayList<TreeElement> cmd_set; //commands set
		
		/**
		 * Class constructor
		 * 
		 * @param String
		 */
		public Extension(String e) {
			super(new String[]{e});
			cmd_set = new ArrayList<TreeElement>();
		}
		
		/**
		 * Add a new command in the set of commands associated
		 * with this file type.
		 * 
		 * @param Command
		 */
		public void addCmd(Command c) {
			c.setExt(getExt());
			cmd_set.add(c);
		}
		
		/**
		 * Command getter
		 * 
		 * @return ArrayList
		 */
		public ArrayList<TreeElement> getCmds() {
			return cmd_set;
		}

		/**
		 * Unusual function in this object
		 * 
		 * @return null
		 */
		public Extension getParent() {
			return null;
		}
	}

	
	/**
	 * Command object which contains an Extension object
	 * 
	 * @author A. Oudot
	 */
	class Command extends TreeElement {
		
		private Extension parent; //the file type associated with the command
		
		/**
		 * Class constructor
		 * 
		 * @param Extension the file type
		 * @param String a command
		 */
		public Command(Extension p, String a) {
			super(a);
			p.addCmd(this);
			this.parent = p;
		}

		/**
		 * Unusual function in this object
		 * 
		 * @return null
		 */
		public ArrayList<TreeElement> getCmds() {
			return null;
		}

		/**
		 * The Extension getter
		 * 
		 * @return Extension
		 */
		public Extension getParent() {
			return parent;
		}
	}
	
	
	/**
	 * The preferences page TreeViewer content provider
	 * 
	 * @author A. Oudot
	 */
	class CContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			//the children of an Extension object is a set of
			//commands
			if(parentElement instanceof Extension) {
				return ((Extension)parentElement).getCmds().toArray();
			}
			return null;
		}

		public Object getParent(Object element) {
			//the prent of a Command is an Extension
			if(element instanceof Command) {
				return ((Command)element).getParent();
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			//if there are defined commands in an extension,
			//children exist...
			if(element instanceof Extension) {
				if(((Extension)element).getCmds().size() > 0) {
					return true;
				}
			}
			return false;
		}

		public Object[] getElements(Object inputElement) {
			//return the set of all defined Extension in the TreeViewer
			return vector.toArray();
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			/*do nothing*/
		}

		public void dispose() {
			/*do nothing*/
		}
	}
	
	
	/**
	 * The preferences page TreeViewer label provider
	 * 
	 * @author A. Oudot
	 */
	class CLabelProvider extends LabelProvider implements ITableLabelProvider {

		//no image defined
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		//get the text to write in TreeViewer
		public String getColumnText(Object element, int columnIndex) {
			switch(columnIndex) {
			case 0: //first column : the extension from Extension objects
				if(element instanceof Extension) return (((TreeElement)element).getExt())[0];
				else return "";
			case 1: //second column : the command exerpted from Command objects
				return ((TreeElement)element).getCommand();
			default:
				return null;
			}
		}
	}
	
	
	/**
	 * Tree Viewer cell modifier
	 * 
	 * @author A. Oudot
	 */
	class CModifier implements ICellModifier {

		public boolean canModify(Object element, String property) {
			//for Extension objects in the viewer, the first column only
			//can be modified : here is the extension text!
			if(element instanceof Extension) {
				if(property.equals("one")) { //first column
					return true;
				} else { //second column
					return false;
				}
			}
			//for Command objects in the viewer, the second column only
			//can be modified : here is the command text!
			else {
				if(property.equals("one")) { //first column
					return false;
				} else { //second column
					return true;
				}
			}
		}

		//get the text in the viewer determined upon the column number
		public Object getValue(Object element, String property) {
			if(property.equals("one")) //first column : an extension
				return (((TreeElement)element).getExt())[0];
			if(property.equals("two")) //second column : a command
				return ((TreeElement)element).getCommand();
			else //next columns : empty!
				return null;
		}

		//when a field is modified in the tree viewer
		public void modify(Object element, String property, Object value) {
			//get the conserned object
			TreeElement e = (TreeElement)((TreeItem)element).getData();
			
			if(property.equals("one")) { //first column
				//set the new extension in Extension object
				e.setExt(new String[]{(String)value});
				viewer.refresh(); //and refresh
			} else if(property.equals("two")) { //second column
				//set the new command in Command object
				e.setCommand((String)value);
				viewer.refresh(); //and refresh
			}
		}
	}
	
	
	protected Control createContents(Composite parent) {
		
		final Composite entry = new Composite(parent, SWT.NULL);
		entry.setLayout(new GridLayout());
		
		
		Group gr2 = new Group(entry, SWT.NONE);
		gr2.setText("Before running tools");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		gr2.setLayoutData(data);
		gr2.setLayout(new GridLayout());
		
		Composite c2 = new Composite(gr2, SWT.NONE);
		c2.setLayout(new GridLayout());
		
		//boolean field : clean files which were generated in the past for the source file
		clean = new BooleanFieldEditor(IConstants.PREF_CLEAN_FILES1, "Clean the why/ directory", c2);
		
		
		final Composite c3 = new Composite(entry, SWT.NONE);
		c3.setLayout(new GridLayout());
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		c3.setLayoutData(data);

		//radio button : do we use commands defined in this preferences page
		//or default ones defined by property pages options
		rfe = new RadioGroupFieldEditor(IConstants.PREF_RUN_OPTIONS, "Running preferences", 1, new String[][] {
					{ "Use default verification tools with options defined in properties", IConstants.PREF_RUN_OPTIONS_NORMAL_MODE },
		            { "Use advanced mode and define commands in the 'Verification Tools'\n" +
		              " preferences page", IConstants.PREF_RUN_OPTIONS_ADVANCED_MODE },
		      }, c3, true);
		rfe.setPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				//the mode has just been changed
				MODE = event.getNewValue().toString();
				enable();
			}
		});
		
		
		grX = new Group(entry, SWT.NONE);
		grX.setText("Advanced mode commands :");
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		grX.setLayoutData(data);
		grX.setLayout(new GridLayout());
		
		Composite cX = new Composite(grX, SWT.NONE);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		cX.setLayoutData(data);
		cX.setLayout(new GridLayout());
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		
		//THE TreeViewer
		tree = new Tree(cX, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setLayoutData(gridData);
		tree.setToolTipText("Click on 'New...' to define a new file type.\n" +
				"Select a file type and click on 'Add' to add a new command.\n" +
				"Use %s and %r in commands to represent respectively the\n" +
				"name of the source file (without extension) and its directory.");
		
		TreeColumn t0 = new TreeColumn(tree, SWT.RIGHT);
		t0.setText("File");
		t0.setWidth(105);
		TreeColumn t1 = new TreeColumn(tree, SWT.LEFT);
		t1.setText("Command");
		t1.setWidth(250);
		
		
		viewer = new TreeViewer(tree);
		viewer.setUseHashlookup(true);
		//"one" => first column
		//"two" => second column
		viewer.setColumnProperties(new String[]{"one", "two"});

		//a CellEditor to be able to edit text fields
		CellEditor[] editors = new CellEditor[2];
		editors[0] = new TextCellEditor(tree);
		editors[1] = new TextCellEditor(tree);
		viewer.setCellEditors(editors);
		
		//Providers and CellModifier
		viewer.setContentProvider(new CContentProvider());
		viewer.setLabelProvider(new CLabelProvider());
		viewer.setCellModifier(new CModifier());
		
		
		//New, Add and Delete buttons
		Composite composite = new Composite(cX, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		layout.makeColumnsEqualWidth = true;
		composite.setLayout(layout);
		
		//The 'New...' button : create a new Extension type
		n = new Button(composite, SWT.PUSH);
		n.setText("New...");
		n.setToolTipText("Create a new file type...");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		n.setLayoutData(gridData);
		n.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//add a new file type
				Extension t;
				vector.add(t = new Extension(""));
				//refresh the viewer and
				viewer.refresh();
				//edit the first column for this new element(the extension)
				viewer.editElement(t, 0);
			}
		});
		
		//The 'Add' button : add a new command in an Extension
		add = new Button(composite, SWT.PUSH);
		add.setText("Add");
		add.setToolTipText("Add a command.\nFirst, you must select a file type.");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		add.setLayoutData(gridData);
		add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//get the selected item
				TreeItem[] t = viewer.getTree().getSelection();
				//it can be an Extension item
				if(t[0] != null && t[0].getData() instanceof Extension) {
					//get the extension
					Extension x = (Extension)t[0].getData();
					//create a new Command for this extension
					Command c = new Command(x,"");
					//refresh the view and
					viewer.refresh();
					//edit the second column for this new element(the command)
					viewer.editElement(c, 1);
				} else {
					MessageDialog.openError(entry.getShell(), "", "You must select an extension item before\ncreating a new command.");
				}
			}
		});
		
		//The 'Delete' button
		delete = new Button(composite, SWT.PUSH);
		delete.setText("Delete");
		delete.setToolTipText("Delete the selected item.");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		delete.setLayoutData(gridData);
		delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//get the selected item
				TreeItem[] selection = viewer.getTree().getSelection();
				for(int s=0; s<selection.length; s++) {
					//get the selected object
					TreeElement t = (TreeElement)(selection[s].getData());
					if(t instanceof Extension) {
						//remove the extension from the Vector, set of all extensions
						vector.remove(t);
					}
					if(t instanceof Command) {
						//remove the command from its parent, the Extension!
						t.getParent().getCmds().remove(t);
					}
				}
				viewer.refresh(); //refresh the view of course...
			}
		});
		
		//The 'Up' button : rise a command in the tree viewer
		//This function is not available for the extension because
		//the notion of order in the definition of extensions
		//is not important!
		up = new Button(composite, SWT.PUSH);
		up.setText("Up");
		up.setToolTipText("Move the selected command up");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		up.setLayoutData(gridData);
		up.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//get the selected item
				TreeItem[] selection = viewer.getTree().getSelection();
				for(int s=0; s<selection.length; s++) {
					//get the selected object
					TreeElement t = (TreeElement)(selection[s].getData());
					if(t instanceof Command) {
						//get the index of the command in the set
						int b = t.getParent().getCmds().indexOf(t);
						if(b > 0) {
							t.getParent().getCmds().remove(b); //remove the command
							t.getParent().getCmds().add(b-1, t); //and add it higher
						}
					}
				}
				viewer.refresh(); //refresh the view
			}
		});
		
		//The 'Down' button : down a command in the tree viewer
		//This function is not available for the extension for the
		//same reason that the 'Up' function is not.
		down = new Button(composite, SWT.PUSH);
		down.setText("Down");
		down.setToolTipText("Move the selected command down");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		down.setLayoutData(gridData);
		down.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//get the selected item
				TreeItem[] selection = viewer.getTree().getSelection();
				for(int s=0; s<selection.length; s++) {
					//get the selected object
					TreeElement t = (TreeElement)(selection[s].getData());
					if(t instanceof Command) {
						int b = t.getParent().getCmds().indexOf(t);
						if(b < t.getParent().getCmds().size()-1) {
							t.getParent().getCmds().remove(b); //remove the command
							t.getParent().getCmds().add(b+1, t); //add it lower
						}
					}
				}
				viewer.refresh(); //refresh the view
			}
		});
		
		
		//create the set of extensions
		vector = new Vector<Extension>();
		viewer.setInput(vector); //and make the relation with the viewer
		
		//get preferences values
		IPreferenceStore store = getPreferenceStore();
		COMMANDS = store.getString(IConstants.PREF_LIST_OF_COMMANDS);
		clean.setPreferenceStore(store);
		rfe.setPreferenceStore(store);
		clean.load();
		rfe.load();
		checkVerifTools(); //make TreeViewer fields
		MODE = EditeurWHY.getDefault().getPreferenceStore().getString(IConstants.PREF_RUN_OPTIONS);
		enable();
		
		return entry;
	}
	
	
	/**
	 * Create all Extension and Command objects using
	 * preferences values and put it in the TreeViewer
	 */
	private void checkVerifTools() {
		
		String[] commandSet = COMMANDS.split(IConstants.LINE_SEPARATOR);
		vector.clear();
		
		//We put the commands into the table
		Extension x = new Extension("");
		for(int h=0; h<commandSet.length; h++) {
			if(!commandSet[h].endsWith(IConstants.ELEMENT_SEPARATOR)) { //an extension
				x = new Extension(commandSet[h]);
				vector.add(x);
			} else { //a command
				String cmd = "";
				//separate all commands
				String[] champs = commandSet[h].split(IConstants.ELEMENT_SEPARATOR);
				try {
					//get the first one
					cmd = champs[0];
				} catch(Exception e) {
					TraceView.print(MessageType.ERROR, "VerificationToolsPreferencesPage, error checking a command : " + e);
				}
				//create the new command in the Extension x
				new Command(x, cmd);
			}
		}
		viewer.refresh(); //refresh
	}
	
	
	/**
	 * Make the COMMAND and TYPES strings from defined commands
	 * annd extensions values.<BR>
	 * Store this values in Preferences variables.
	 */
	private void checkOut() {
		
		COMMANDS = "";
		TYPES = "";
		
		for(int y=0; y<vector.size(); y++) {
			
			Extension e = (Extension)vector.get(y);
			
			COMMANDS += e.getExt()[0] + IConstants.LINE_SEPARATOR;
			TYPES += e.getExt()[0] + IConstants.ELEMENT_SEPARATOR;
			for(int z=0; z<e.getCmds().size(); z++) {
				String tmp;
				COMMANDS += (((tmp = ((Command)e.getCmds().get(z)).getCommand()).equals("")) ? " " : tmp );
				COMMANDS += IConstants.ELEMENT_SEPARATOR;
				COMMANDS += IConstants.LINE_SEPARATOR;
			}
		}
		
		IPreferenceStore store = getPreferenceStore();
		store.setValue(IConstants.PREF_LIST_OF_COMMANDS, COMMANDS);
		store.setValue(IConstants.PREF_LIST_OF_RECOGNIZED_FILES, TYPES);
		clean.store();
		rfe.store();
	}
	
	/**
	 * Enable or disable the the TreeViewer. It depends on the
	 * mode we have selected with the radio button (normal or
	 * advanced mode)
	 */
	private void enable() {
		if(MODE.equals(IConstants.PREF_RUN_OPTIONS_NORMAL_MODE)) {
			//normal mode: we use the options defined in the PropertyPage
			//of the source file to create commands
			setEnabled(false); //so we can disable the TreeViewer
		} else {
			//advanced mode: we use the objects defined in the TreeViewer
			//to recognize source file types and use the associated
			//commands
			setEnabled(true); //thus, the TreeViewer has to be enabled
		}
	}
	
	/**
	 * Enable or disable the Group which contains the TreeViewer
	 * and all functional buttons.
	 * 
	 * @param boolean true to enable, false to disable the group
	 */
	private void setEnabled(boolean enabled) {
		grX.setEnabled(enabled); //the Group
		n.setEnabled(enabled); //the 'New...' button
		add.setEnabled(enabled); //the 'Add' button
		delete.setEnabled(enabled); //the 'Delete' button
		up.setEnabled(enabled); //the 'Up' button
		down.setEnabled(enabled); //the 'Down' button
		tree.setEnabled(enabled); //the TreeViewer
	}

	public void init(IWorkbench workbench) {
		/*do nothing*/
	}
	
	public boolean performOk() {
		checkOut(); //store commands and extensions
		EditeurWHY.getDefault().savePluginPreferences();
		return super.performOk();
	}
	
	public void performDefaults() {
		//load the preferences default values
		clean.loadDefault();
		rfe.loadDefault();
		IPreferenceStore store = getPreferenceStore();
		COMMANDS = store.getDefaultString(IConstants.PREF_LIST_OF_COMMANDS);
		checkVerifTools(); //make TreeViewer fields
		super.performDefaults();
	}
}
