package net.eclipse.why.editeur.preferences;

import net.eclipse.why.editeur.EditeurWHY;
import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.views.ProverView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;


/**
 * The Main Preferences Page.
 * 
 * @author A. Oudot
 */
public class MainPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {
	
	
	private Group gr1, gr3;
	private Composite c1, c4;
	private IntegerFieldEditor nbLines;
	private BooleanFieldEditor all;
	private BooleanFieldEditor usedtd;
	private FileFieldEditor ffe;

	/**
	 * Class constructor
	 */
	public MainPreferencesPage() {
		super();
		setPreferenceStore(EditeurWHY.getDefault().getPreferenceStore());
	}

	
	protected Control createContents(Composite parent) {
		
		Composite entry = new Composite(parent, SWT.NONE);
		GridLayout g = new GridLayout();
		g.verticalSpacing = 10;
		entry.setLayout(g);
		
		
		gr1 = new Group(entry, SWT.NONE);
		gr1.setText("Number of lines to show in Prover View");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		gr1.setLayoutData(data);
		gr1.setLayout(new GridLayout());
		
		c1 = new Composite(gr1, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		c1.setLayout(layout);
		
		all = new BooleanFieldEditor(IConstants.PREF_SHOW_ALL_LINES, "Show all", c1);
		all.fillIntoGrid(c1, 2);
		all.setPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if(all.getBooleanValue())
					nbLines.setEnabled(false, c1);
				else
					nbLines.setEnabled(true, c1);
			}
		});
		
		
		nbLines = new IntegerFieldEditor(IConstants.PREF_SHOW_NB_LINES, "Lines", c1);
		nbLines.setTextLimit(4);
		
		/*
		gr2 = new Group(entry, SWT.NONE);
		gr2.setText("Before running tools :");
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		gr2.setLayoutData(data);
		gr2.setLayout(new GridLayout());
		
		Composite c2 = new Composite(gr2, SWT.NONE);
		c2.setLayout(new GridLayout());
		
		clean1 = new BooleanFieldEditor(IConstants.PREF_CLEAN_FILES1, "Clean .why files", c2);
		
		Composite c3 = new Composite(entry, SWT.NONE);
		c3.setLayout(new GridLayout());
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		c3.setLayoutData(data);

		rfe = new RadioGroupFieldEditor(IConstants.PREF_RUN_OPTIONS, "Running preferences", 1, new String[][] {
					{ "Use default verification tools with options defined in properties", IConstants.PREF_RUN_OPTIONS_NORMAL_MODE },
		            { "Use advanced mode and define commands in the 'Verification Tools'\n" +
		              " preferences page", IConstants.PREF_RUN_OPTIONS_ADVANCED_MODE },
		      }, c3, true);
		*/
		
		gr3 = new Group(entry, SWT.NONE);
		gr3.setText("DTD formating XML files to save sessions");
		data = new GridData(GridData.FILL_HORIZONTAL);
		gr3.setLayoutData(data);
		gr3.setLayout(new GridLayout());
		
		c4 = new Composite(gr3, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 3;
		c4.setLayout(layout);
		data = new GridData(GridData.FILL_HORIZONTAL);
		c4.setLayoutData(data);
		
		usedtd = new BooleanFieldEditor(IConstants.PREF_DTD_USING_FILE, "Use the defined DTD file", c4);
		usedtd.fillIntoGrid(c4, 3);
		usedtd.setPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if(usedtd.getBooleanValue())
					ffe.setEnabled(true, c4);
				else
					ffe.setEnabled(false, c4);
			}
		});
		
		ffe = new FileFieldEditor(IConstants.PREF_DTD_FILE_LOCATION, "DTD file", c4);
		ffe.setFileExtensions(new String[]{"*.dtd"});
		
		
		nbLines.setPreferenceStore(getPreferenceStore());
		all.setPreferenceStore(getPreferenceStore());
		usedtd.setPreferenceStore(getPreferenceStore());
		ffe.setPreferenceStore(getPreferenceStore());
		
		
		nbLines.load();
		all.load();
		usedtd.load();
		ffe.load();
		
		
		if(all.getBooleanValue()) //all lines shown in PV :
			nbLines.setEnabled(false, c1);
		else //else, choose the nb of lines to show :
			nbLines.setEnabled(true, c1);
		
		if(usedtd.getBooleanValue()) //use a dtd formatting file :
			ffe.setEnabled(true, c4);
		else //else, disable the text field :
			ffe.setEnabled(false, c4);
		
		return entry;
	}

	
	public void init(IWorkbench workbench) {
		/*do nothing*/
	}
	
	/**
	 * Updates the Prover View parameters
	 */
	private void updateView() {
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
		
		if(!all.getBooleanValue()) {
			
			int r = 0;
			try {
				r = nbLines.getIntValue();
			} catch(NumberFormatException e) {
				MessageDialog.openError(new Shell(), "Invalid number of lines", "You must enter an integer value between 2 and 9999");
				return super.performOk();
			}
		
			if(r < 2) {
				MessageDialog.openError(new Shell(), "Invalid number of lines", "You must enter an integer value between 2 and 9999");
				return super.performOk();
			}
		
			nbLines.store();
		}
		
		all.store();
		usedtd.store();
		ffe.store();
		updateView();
		
		EditeurWHY.getDefault().savePluginPreferences();
		return super.performOk();
	}
	
	
	protected void performDefaults() {
		nbLines.loadDefault();
		all.loadDefault();
		usedtd.loadDefault();
		ffe.loadDefault();
		
		if(IConstants.PREF_SHOW_ALL_LINES_DEFAULT_VALUE) {
			nbLines.setEnabled(false, c1);
		} else {
			nbLines.setEnabled(true, c1);
		}
		
		super.performDefaults();
	}
}
