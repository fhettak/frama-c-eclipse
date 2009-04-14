package net.eclipse.why.editeur.preferences;

import java.io.File;

import net.eclipse.why.editeur.EditeurWHY;
import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.views.ProverView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;


/**
 * The provers preferences page
 * 
 * @author A. Oudot
 *
 */
public class ProversPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {
		
	/*
	 * Elements in the Preferences Page 
	 */
	Composite entry;
	Table table;
	Button n, duplic, edit, remove, up, down;
	
	/*
	 * Elements in windows used to define provers and assistants
	 */
	private Shell shell;
	private Combo combo1;
	private StringFieldEditor name;
	private Text commands;
	
	/*
	 * Elements in the Help window used to generate commands automatically
	 */
	private Shell hshell;
	private StringFieldEditor nem, ext, opt, dir;
	
	/*
	 * Preferences
	 */
	private String prover_commands;

	
	/**
	 * The class constructor.
	 */
	public ProversPreferencesPage() {
		super();
		setPreferenceStore(EditeurWHY.getDefault().getPreferenceStore());
	}
	
	protected Control createContents(Composite parent) {
		
		
		entry = new Composite(parent, SWT.NONE);
		GridLayout fill = new GridLayout();
		fill.numColumns = 2;
		entry.setLayout(fill);
		
		//the provers table :
		table = new Table(entry, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		GridData grid = new GridData(GridData.FILL_BOTH);
		grid.grabExcessVerticalSpace = true;
		table.setLayoutData(grid);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		
		Composite buttonComposite = new Composite(entry, SWT.NONE);
		buttonComposite.setLayout(new GridLayout());
		buttonComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
		//The "New Provers" button :
		n = new Button(buttonComposite, SWT.NONE);
		GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gridData.widthHint = 80;
        n.setLayoutData(gridData);
		n.setText("New...");
		n.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//creates a new window to define a prover
				createShell(entry);
				shell.setData("new", new Boolean(true));
				openShell(true, null, null);
			}
		});
		
		//The "duplicate prover" button
		duplic = new Button(buttonComposite, SWT.NONE);
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gridData.widthHint = 80;
        duplic.setLayoutData(gridData);
        duplic.setText("Duplicate");
		
        duplic.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				//you have to select only one prover
				if(items.length == 1) {
					TableItem item = items[0];
					String l0 = (String)item.getData("status");
					String l1 = item.getText();
					String l234 = (String)item.getData("cmds");
					boolean p = (l0.equals("prover"))?true :false;
					//creates a new window with the prover to duplicate parameters
					createShell(entry);
					shell.setData("new", new Boolean(true));
					openShell(p, l1, l234);
				} else {
					MessageDialog.openError(new Shell(), "Selection Error", "You must select one prover or assistant in the table");
				}
			}
		});
		
        //The "edit prover" button
		edit = new Button(buttonComposite, SWT.NONE);
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gridData.widthHint = 80;
        edit.setLayoutData(gridData);
		edit.setText("Edit");
		
		edit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				if(items.length == 1) {
					TableItem item = items[0];
					String l0 = (String)item.getData("status");
					String l1 = item.getText();
					String l234 = (String)item.getData("cmds");
					boolean p = (l0.equals("prover"))?true :false;
					//creates a new window with the prover parameters
					createShell(entry);
					shell.setData("new", new Boolean(false));
					openShell(p, l1, l234);
				} else {
					MessageDialog.openError(new Shell(), "Selection Error", "You must select one prover or assistant in the table");
				}
			}
		});
		
		//The "remove prover" button
		remove = new Button(buttonComposite, SWT.NONE);
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gridData.widthHint = 80;
        remove.setLayoutData(gridData);
		remove.setText("Remove");
		
		remove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//remove the table item
				int index = table.getSelectionIndex();
				if(index >= 0) table.remove(index);
			}
		});
		
		//The "rise prover item" button
		up = new Button(buttonComposite, SWT.NONE);
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gridData.widthHint = 80;
        up.setLayoutData(gridData);
		up.setText("Up");
		
		up.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				moveItem(-1);
			}
		});
		
		//The "get off prover item" button
		down = new Button(buttonComposite, SWT.NONE);
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gridData.widthHint = 80;
        down.setLayoutData(gridData);
		down.setText("Down");
		
		down.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				moveItem(1);
			}
		});
		
		IPreferenceStore store = getPreferenceStore();
		prover_commands = store.getString(IConstants.PREF_LIST_OF_PROOF_COMMANDS);
		//creates all provers items into the table
		createItems();
		
		return entry;
	}
	
	
	/**
	 * Creates the prover items into the table to
	 * define provers and assistants.
	 */
	private void createItems() {
		
		//remove all element from the table
		table.removeAll();
		String[] g = prover_commands.split(IConstants.LINE_SEPARATOR);
		
		for(int v=0; v<g.length; v++) {
			TableItem item = new TableItem(table, SWT.NONE);
			String[] h = g[v].split(IConstants.ELEMENT_SEPARATOR);
			item.setData("status", h[0].trim());
			item.setText(h[1].trim());
			String hprim = "";
			for(int i=2; i<h.length; i++) {
				hprim += h[i]+"\n";
			}
			item.setData("cmds", hprim);
			if(h[0].trim().equals("prover")) {
				item.setImage(IConstants.IMAGE_BALL_PINK);
			} else {
				item.setImage(IConstants.IMAGE_BALL_PURP);
			}
		}
	}
	
	/**
	 * Move up or down an item in the table.
	 * 
	 * @param int slot: <0 for up, >0 for down
	 */
	private void moveItem(int slot) {
		
		int index = table.getSelectionIndex();
		if(index == -1) {
			return;
		}
		
		int max = table.getItemCount() -1;
		if( (index==0 && slot==-1) || (index==max && slot==1)) {
			return;
		}
		
		TableItem[] items = table.getSelection();
		if(items.length > 1) {
			MessageDialog.openError(new Shell(), "Selection Error", "You must select one prover or assistant in the table");
			return;
		}
		
		TableItem item = items[0];
		String l0 = (String)item.getData("status");
		String l1 = item.getText();
		String l234 = (String)item.getData("cmds");
		
		table.remove(index);
		index+=slot;
		
		TableItem i = new TableItem(table, SWT.NONE, index);
		i.setData("status", l0);
		i.setText(l1);
		i.setData("cmds", l234);
		if(l0.equals("prover")) {
			i.setImage(IConstants.IMAGE_BALL_PINK);
		} else {
			i.setImage(IConstants.IMAGE_BALL_PURP);
		}
		
		table.select(index);
	}
	
	/**
	 * Creates a shell window which allows users to define
	 * or edit provers and assistants
	 * 
	 * @param Composite
	 */
	private void createShell(Composite composite) {
		
		shell = new Shell(composite.getShell(), SWT.TITLE | SWT.PRIMARY_MODAL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 10;
        gridLayout.marginWidth = 10;
        gridLayout.verticalSpacing = 10;
        shell.setLayout(gridLayout);
        shell.setText("Define a prover or an assistant");
        
        
        Composite comboComposite1 = new Composite(shell, SWT.BORDER);
        comboComposite1.setLayout(new GridLayout());
        combo1 = new Combo(comboComposite1, SWT.BORDER);
        combo1.add("prover");
        combo1.add("assistant");
        
        Composite editor = new Composite(shell, SWT.NONE);
        editor.setLayout(new GridLayout());
        GridData g = new GridData(GridData.FILL_BOTH);
        g.widthHint = 500;
        editor.setLayoutData(g);

        
        name = new StringFieldEditor("line1", "Name :", editor);
        
		
		Composite editor2 = new Composite(shell, SWT.NONE);
        editor2.setLayout(new GridLayout());
        g = new GridData(GridData.FILL_BOTH);
        g.widthHint = 520;
        editor2.setLayoutData(g);
        
        Label expl = new Label(editor2, SWT.NONE);
		expl.setText("Commands [you may use %s, %r and %n into editable commands to represent\n" +
					 "respectively the name of the source file (without extension), his directory and\n" +
					 "the Proof Obligation number] :");
		
        
        commands = new Text(editor2, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gdt = new GridData(GridData.FILL_BOTH);
        gdt.heightHint = 100;
        gdt.widthHint = 300;
        commands.setLayoutData(gdt);
        
        
        Composite buttonComposite = new Composite(shell, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        buttonComposite.setLayout(layout);
        buttonComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        
        //button OK : validate the new prover
        Button ok_button = new Button(buttonComposite, SWT.NONE);
        ok_button.setText("Ok");
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 60;
        ok_button.setLayoutData(gridData);
        ok_button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				String l1 = name.getStringValue(); //name of the prover
				String l234 = commands.getText(); //commands
				String l0 = "prover"; //status : assistant or prover
				if(combo1.getSelectionIndex()==1) {
					l0 = "assistant";
				}
				
				//the name can't be null
				if(l1==null || l1.trim().equals("")) {
					MessageDialog.openError(new Shell(), "Parameter Error", "The name can't be null");
					return;
				}
				//and can't be used twice
				boolean nw = ((Boolean)shell.getData("new")).booleanValue();
				if(nw) {
					for(int k=0; k<table.getItemCount(); k++) {
						String s = table.getItem(k).getText().trim();
						if(l1.trim().equals(s)) {
							MessageDialog.openError(new Shell(), "Parameter Error", "This name is ever used : please choose another one!");
							return;
						}
					}
				}
				
				//create a new item for this prover
				TableItem item;
				if(((Boolean)shell.getData("new")).booleanValue()) {
					item = new TableItem(table, SWT.NONE);
				} else {
					item = table.getSelection()[0];
				}
				
				if(l0.equals("prover")) {
					item.setImage(IConstants.IMAGE_BALL_PINK);
				} else {
					item.setImage(IConstants.IMAGE_BALL_PURP);
				}
				
				item.setData("status", l0);
				item.setData("cmds", l234);
				item.setText(l1);
				shell.close();
			}
		});
        
        //button help : create a new prover and generate default commands
        //using the name of the prover
        Button h_button = new Button(buttonComposite, SWT.NONE);
        h_button.setText("Help");
        h_button.setToolTipText("Give a help to create a set of commands for using prover");
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 60;
        h_button.setLayoutData(gridData);
        h_button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				createHShell(); //cretae a help window
				openHShell();   //and open it!
			}
		});
        
        //Cancel button : stop
        Button c_button = new Button(buttonComposite, SWT.NONE);
        c_button.setText("Cancel");
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 60;
        c_button.setLayoutData(gridData);
        c_button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
        
        shell.pack();
	}
	
	
	/**
	 * Create the shell object to help users to
	 * write provers commands : giving the prover
	 * name, a file extension, the 'why' command
	 * options(optional) and the working directory
	 * (optional), user can click on the 'Generate'
	 * button and commands will appear in the
	 * new provers window fields.
	 */
	private void createHShell() {
		
		hshell = new Shell(shell, SWT.TITLE | SWT.PRIMARY_MODAL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 10;
        gridLayout.marginWidth = 10;
        gridLayout.verticalSpacing = 10;
        hshell.setLayout(gridLayout);
        hshell.setText("SOS Commands");
        
        
        Composite editor = new Composite(hshell, SWT.NONE);
        editor.setLayout(new GridLayout());
        GridData g = new GridData(GridData.FILL_BOTH);
        g.widthHint = 300;
        editor.setLayoutData(g);
        
        //get the prover name
        nem = new StringFieldEditor("nem", "Name(*)", editor);
        nem.setStringValue(name.getStringValue());
        //get the file extension
        ext = new StringFieldEditor("ext", "File extension(*)", editor);
        //get options for the 'why' command
        opt = new StringFieldEditor("opt", "WHY option\n(=Name if null)", editor);
        //get the working directory (place to find files to run the prover)
        dir = new StringFieldEditor("dir", "Directory\n(=Name if null)", editor);
        
        
        Composite buttonComposite = new Composite(hshell, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        buttonComposite.setLayout(layout);
        buttonComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        
        //generate button : generate the commands
        Button ok_button = new Button(buttonComposite, SWT.NONE);
        ok_button.setText("Generate...");
        ok_button.setToolTipText("Create new commands");
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 90;
        ok_button.setLayoutData(gridData);
        ok_button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				String l0 = nem.getStringValue();
				if(l0==null || l0.trim().equals("")) {
					MessageDialog.openError(new Shell(), "Parameter Error", "The Name field can't be null");
					return;
				}
				String l1 = ext.getStringValue();
				if(l1==null || l1.trim().equals("")) {
					MessageDialog.openError(new Shell(), "Parameter Error", "The Extension field can't be null");
					return;
				}
				String l2 = opt.getStringValue();
				if(l2==null || l2.trim().equals("")) {
					l2 = nem.getStringValue().toLowerCase();
				}
				String l3 = dir.getStringValue();
				if(l3==null || l3.trim().equals("")) {
					l3 = nem.getStringValue().toLowerCase();
				}
				
				name.setStringValue(l0);
				String c = "why --" + l2 + " -dir " + l3 + " -no-prelude why" + File.separator + "%s_ctx.why why" + File.separator + "%s_po%n.why\n";
				c += IConstants.dp + " " + l3 + File.separator + "%s_po%n_why." + l1;
				commands.setText(c);
				combo1.select(0);
				
				hshell.close();
			}
		});
        
        //Cancel button : stop
        Button c_button = new Button(buttonComposite, SWT.NONE);
        c_button.setText("Cancel");
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 70;
        c_button.setLayoutData(gridData);
        c_button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				hshell.close();
			}
		});
        
        hshell.pack();
	}
	
	
	/**
	 * Open the Shell Object defined in the <code>createWindow()</code> method
	 * with different value which can be null.
	 * 
	 * @param boolean : true for a prover, false for an assistant
	 * @param String : the prover name
	 * @param String : the prover commands
	 * @see createWindow()
	 */
	private void openShell(boolean prover, String l1, String l234) {
		//Combo Box selection
		if(prover) {
        	combo1.select(0);
        } else {
        	combo1.select(1);
        }
		
		//texts in fields
		name.setStringValue(l1);
		commands.setText((l234 != null)?l234 :"");
		
		//open the Shell
		shell.open();
		shell.layout();
	}

	
	/**
	 * Open the help window
	 */
	private void openHShell() {
		//open the Shell
		hshell.open();
		hshell.layout();
	}
	
	
	public void init(IWorkbench workbench) {
	}
	
	/**
	 * Modify columns of Prover View and update all buttons
	 */
	private void updateView() {
		//edit, delete or modify the columns of Prover View
		//which represent all provers
		IViewReference[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
		for(int e=0; e<views.length; e++) {
			IViewPart viewPart = views[e].getView(false);
			if( viewPart instanceof ProverView ) {
				FileInfos.initColumns();
				((ProverView)viewPart).makeColumns();
				((ProverView)viewPart).updateView();
			}
		}
	}
	
	
	public boolean performOk() {
		
		IPreferenceStore store = getPreferenceStore();
		
		//save provers
		String record = "";
		for(int x=0; x<table.getItemCount(); x++) {
			TableItem z = table.getItem(x);
			
			String[] k = ((String)z.getData("cmds")).split("\n");
			record += z.getData("status") + IConstants.ELEMENT_SEPARATOR +
					  z.getText()		  + IConstants.ELEMENT_SEPARATOR;
			for(int d=0; d<k.length; d++) {
				record += k[d]			  + IConstants.ELEMENT_SEPARATOR;
			}
			record += IConstants.LINE_SEPARATOR;
			
		}
		store.setValue(IConstants.PREF_LIST_OF_PROOF_COMMANDS, record);
		
		//update Prover View
		updateView();
		
		//save preferences
		EditeurWHY.getDefault().savePluginPreferences();
		return super.performOk();
	}
	
	
	public void performDefaults() {
		IPreferenceStore store = getPreferenceStore();
		prover_commands = store.getDefaultString(IConstants.PREF_LIST_OF_PROOF_COMMANDS);
		createItems();
		super.performDefaults();
	}
}
