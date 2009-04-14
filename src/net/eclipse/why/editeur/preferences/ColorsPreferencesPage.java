package net.eclipse.why.editeur.preferences;

import net.eclipse.why.editeur.EditeurWHY;
import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.views.ProverView;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;


/**
 * The colors preferences page
 * 
 * @author A. Oudot
 */
public class ColorsPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {
	
	private Group g0, g1, g2, g3, g4;
	private ColorFieldEditor bgColoredEditor;
	
	private ColorFieldEditor bgGoalsBtColor, unprovedGoalsBtColor, workingGoalsBtColor, provedGoalsBtColor;
	private ColorFieldEditor bgSubGoalsBtColor, unprovedSubGoalsBtColor, workingSubGoalsBtColor, provedSubGoalsBtColor;
	private ColorFieldEditor bgFuncsBtColor, unprovedFuncsBtColor, workingFuncsBtColor, provedFuncsBtColor;
	private ColorFieldEditor bgFuncsAssistantBtColor, bgGoalsAssistantBtColor, bgSubGoalsAssistantBtColor;
	private ColorFieldEditor markedGoalTextColor, markedFunctionTextColor;
	
	private Composite c01, c11, c12;
	
	/**
	 * Class constructor
	 */
	public ColorsPreferencesPage() {
		super();
		setPreferenceStore(EditeurWHY.getDefault().getPreferenceStore());
	}
	
	protected Control createContents(Composite parent) {
		
		Composite entry = new Composite(parent, SWT.NULL);
		entry.setLayout(new FillLayout());
		
		//A page for the Prover View colors,
		//another one for the PO Viewer colors
		TabFolder tabFolder = new TabFolder(entry, SWT.BORDER);
		TabItem t1 = new TabItem(tabFolder, SWT.NONE);
		t1.setText("Prover View");
		TabItem t2 = new TabItem(tabFolder, SWT.NONE);
		t2.setText("PO Viewer");
		
		Composite a = new Composite(tabFolder, SWT.NONE);
		a.setLayout(new GridLayout());
		a.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		t1.setControl(a);
		
		Composite c1 = new Composite(a, SWT.NONE);
		GridLayout f = new GridLayout();
		f.verticalSpacing = 10;
		f.numColumns = 3;
		c1.setLayout(f);
		GridData grid = new GridData(GridData.FILL_HORIZONTAL);
		c1.setLayoutData(grid);
		
		
		//3 groups : subgoals, goals and functions
		g0 = new Group(c1, SWT.NONE);
		g0.setText("Subgoal Buttons");
		GridLayout layout = new GridLayout();
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		g0.setLayout(layout);
		g0.setLayoutData(data);
		
		g1 = new Group(c1, SWT.NONE);
		g1.setText("Goal Buttons");
		layout = new GridLayout();
		data = new GridData(GridData.FILL_HORIZONTAL);
		g1.setLayout(layout);
		g1.setLayoutData(data);
		
		g2 = new Group(c1, SWT.NONE);
		g2.setText("Function Buttons");
		layout = new GridLayout();
		data = new GridData(GridData.FILL_HORIZONTAL);
		g2.setLayout(layout);
		g2.setLayoutData(data);
		
		
		c01 = new Composite(g0, SWT.NONE);
		c01.setLayout(new GridLayout());
		//subgoals Color Field Editors
		bgSubGoalsBtColor = new ColorFieldEditor(IConstants.PREF_SUBGOALS_BUTTON_BG_COLOR, "Background", c01);
		unprovedSubGoalsBtColor = new ColorFieldEditor(IConstants.PREF_SUBGOALS_BUTTON_UNPROVED_COLOR, "Unproved", c01);
		workingSubGoalsBtColor = new ColorFieldEditor(IConstants.PREF_SUBGOALS_BUTTON_WORKING_COLOR, "Working", c01);
		provedSubGoalsBtColor = new ColorFieldEditor(IConstants.PREF_SUBGOALS_BUTTON_PROVED_COLOR, "Proved", c01);
		bgSubGoalsAssistantBtColor = new ColorFieldEditor(IConstants.PREF_SUBGOALS_ASSISTANT_BUTTON_BG_COLOR, "Assistant", c01);
		
		
		c11 = new Composite(g1, SWT.NONE);
		c11.setLayout(new GridLayout());
		//goals Color Field Editors
		bgGoalsBtColor = new ColorFieldEditor(IConstants.PREF_GOALS_BUTTON_BG_COLOR, "Background", c11);
		unprovedGoalsBtColor = new ColorFieldEditor(IConstants.PREF_GOALS_BUTTON_UNPROVED_COLOR, "Unproved", c11);
		workingGoalsBtColor = new ColorFieldEditor(IConstants.PREF_GOALS_BUTTON_WORKING_COLOR, "Working", c11);
		provedGoalsBtColor = new ColorFieldEditor(IConstants.PREF_GOALS_BUTTON_PROVED_COLOR, "Proved", c11);
		bgGoalsAssistantBtColor = new ColorFieldEditor(IConstants.PREF_GOALS_ASSISTANT_BUTTON_BG_COLOR, "Assistant", c11);
		
		
		c12 = new Composite(g2, SWT.NONE);
		c12.setLayout(new GridLayout());
		//functions Color Field Editors
		bgFuncsBtColor = new ColorFieldEditor(IConstants.PREF_FUNCS_BUTTON_BG_COLOR, "Background", c12);
		unprovedFuncsBtColor = new ColorFieldEditor(IConstants.PREF_FUNCS_BUTTON_UNPROVED_COLOR, "Unproved", c12);
		workingFuncsBtColor = new ColorFieldEditor(IConstants.PREF_FUNCS_BUTTON_WORKING_COLOR, "Working", c12);
		provedFuncsBtColor = new ColorFieldEditor(IConstants.PREF_FUNCS_BUTTON_PROVED_COLOR, "Proved", c12);
		bgFuncsAssistantBtColor = new ColorFieldEditor(IConstants.PREF_FUNCS_ASSISTANT_BUTTON_BG_COLOR, "Assistant", c12);
		
		
		// Another group for the marked goals text color :
		Composite c3 = new Composite(a, SWT.NONE);
		GridLayout h = new GridLayout();
		h.verticalSpacing = 10;
		GridData gdt = new GridData(GridData.FILL_HORIZONTAL);
		c3.setLayout(h);
		c3.setLayoutData(gdt);
		
		g3 = new Group(c3, SWT.NONE);
		g3.setText("Others");
		layout = new GridLayout();
		layout.verticalSpacing = 10;
		layout.numColumns = 2;
		data = new GridData(GridData.FILL_HORIZONTAL);
		g3.setLayout(layout);
		g3.setLayoutData(data);
		
		Composite c31 = new Composite(g3, SWT.NONE);
		c31.setLayout(new GridLayout());
		
		Composite c32 = new Composite(g3, SWT.NONE);
		c32.setLayout(new GridLayout());
		
		markedGoalTextColor = new ColorFieldEditor(IConstants.PREF_MARKED_GOAL_TEXT_COLOR, "Marked goal/function", c31);
		markedFunctionTextColor = new ColorFieldEditor(IConstants.PREF_MARKED_FUNC_TEXT_COLOR, "", c32);
		
		
		// A last group to define 3 sets of colors as different modes
		g4 = new Group(c3, SWT.NONE);
		g4.setText("Few colors mode");
		layout = new GridLayout();
		layout.verticalSpacing = 10;
		layout.numColumns = 2;
		data = new GridData(GridData.FILL_HORIZONTAL);
		g4.setLayout(layout);
		g4.setLayoutData(data);
		
		Composite c4 = new Composite(g4, SWT.NONE);
		GridLayout gridL = new GridLayout();
		c4.setLayout(gridL);
		
		Button mode1 = new Button(c4, SWT.NONE);
        mode1.setText("Poor");
        GridData gridData = new GridData();
        gridData.widthHint = 120;
        mode1.setLayoutData(gridData);
        mode1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				RGB[] colors = new RGB[] {
					new RGB(255, 255, 255),
					new RGB(255, 0, 0),
					new RGB(255, 255, 0),
					new RGB(0, 255, 0),
					new RGB(255, 255, 255),
					new RGB(255, 255, 255),
					new RGB(255, 0, 0),
					new RGB(255, 255, 0),
					new RGB(0, 255, 0),
					new RGB(255, 255, 255),
					new RGB(255, 255, 255),
					new RGB(255, 0, 0),
					new RGB(255, 255, 0),
					new RGB(0, 255, 0),
					new RGB(255, 255, 255)
				};
				applyColors(colors);
			}
        });
        
        Button mode2 = new Button(c4, SWT.NONE);
        mode2.setText("Rich");
        mode2.setLayoutData(gridData);
        mode2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				RGB[] colors = new RGB[] {
					new RGB(243, 172, 255),
					new RGB(255, 85, 85),
					new RGB(255, 228, 0),
					new RGB(194, 246, 72),
					new RGB(173, 216, 230),
					new RGB(177, 50, 255),
					new RGB(211, 0, 38),
					new RGB(255, 177, 0),
					new RGB(0, 215, 65),
					new RGB(47, 47, 255),
					new RGB(87, 0, 142),
					new RGB(150, 0, 0),
					new RGB(212, 112, 0),
					new RGB(0, 145, 25),
					new RGB(0, 0, 113)
				};
				applyColors(colors);
			}
        });
        
        Button mode3 = new Button(c4, SWT.NONE);
        mode3.setText("Color-blind");
        mode3.setLayoutData(gridData);
        mode3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				RGB[] colors = new RGB[] {
					new RGB(175, 175, 175),
					new RGB(0, 0, 0),
					new RGB(255, 0, 0),
					new RGB(255, 255, 255),
					new RGB(175, 175, 175),
					new RGB(175, 175, 175),
					new RGB(0, 0, 0),
					new RGB(255, 0, 0),
					new RGB(255, 255, 255),
					new RGB(175, 175, 175),
					new RGB(105, 105, 105),
					new RGB(0, 0, 0),
					new RGB(255, 0, 0),
					new RGB(255, 255, 255),
					new RGB(105, 105, 105)
				};
				applyColors(colors);
			}
        });
		
		
        // For PO Viewer now, define the backgroundof the view :
		Composite c2prim = new Composite(tabFolder, SWT.NONE);
		c2prim.setLayout(new GridLayout());
		t2.setControl(c2prim);
		Composite c2 = new Composite(c2prim, SWT.NONE);
		c2.setLayout(new GridLayout());
		
		bgColoredEditor = new ColorFieldEditor(IConstants.PREF_POV_BACKGROUND_COLOR, "Background Color", c2);
		
		
		//GET THE PREFERENCES
		bgColoredEditor.setPreferenceStore(getPreferenceStore());
		bgGoalsBtColor.setPreferenceStore(getPreferenceStore());
		bgSubGoalsBtColor.setPreferenceStore(getPreferenceStore());
		bgFuncsBtColor.setPreferenceStore(getPreferenceStore());
		provedGoalsBtColor.setPreferenceStore(getPreferenceStore());
		provedSubGoalsBtColor.setPreferenceStore(getPreferenceStore());
		provedFuncsBtColor.setPreferenceStore(getPreferenceStore());
		workingGoalsBtColor.setPreferenceStore(getPreferenceStore());
		workingSubGoalsBtColor.setPreferenceStore(getPreferenceStore());
		workingFuncsBtColor.setPreferenceStore(getPreferenceStore());
		unprovedGoalsBtColor.setPreferenceStore(getPreferenceStore());
		unprovedSubGoalsBtColor.setPreferenceStore(getPreferenceStore());
		unprovedFuncsBtColor.setPreferenceStore(getPreferenceStore());
		bgGoalsAssistantBtColor.setPreferenceStore(getPreferenceStore());
		bgSubGoalsAssistantBtColor.setPreferenceStore(getPreferenceStore());
		bgFuncsAssistantBtColor.setPreferenceStore(getPreferenceStore());
		markedGoalTextColor.setPreferenceStore(getPreferenceStore());
		markedFunctionTextColor.setPreferenceStore(getPreferenceStore());
		//bgColoredEditor.setPreferenceName(IConstants.PREF_POV_BACKGROUND_COLOR);
		//bgGoalsBtColor.setPreferenceName(IConstants.PREF_GOALS_BUTTON_BG_COLOR);
		//bgSubGoalsBtColor.setPreferenceName(IConstants.PREF_SUBGOALS_BUTTON_BG_COLOR);
		//bgFuncsBtColor.setPreferenceName(IConstants.PREF_FUNCS_BUTTON_BG_COLOR);
		//provedGoalsBtColor.setPreferenceName(IConstants.PREF_GOALS_BUTTON_PROVED_COLOR);
		//provedSubGoalsBtColor.setPreferenceName(IConstants.PREF_SUBGOALS_BUTTON_PROVED_COLOR);
		//provedFuncsBtColor.setPreferenceName(IConstants.PREF_FUNCS_BUTTON_PROVED_COLOR);
		//workingGoalsBtColor.setPreferenceName(IConstants.PREF_GOALS_BUTTON_WORKING_COLOR);
		//workingSubGoalsBtColor.setPreferenceName(IConstants.PREF_SUBGOALS_BUTTON_WORKING_COLOR);
		//workingFuncsBtColor.setPreferenceName(IConstants.PREF_FUNCS_BUTTON_WORKING_COLOR);
		//unprovedGoalsBtColor.setPreferenceName(IConstants.PREF_GOALS_BUTTON_UNPROVED_COLOR);
		//unprovedSubGoalsBtColor.setPreferenceName(IConstants.PREF_SUBGOALS_BUTTON_UNPROVED_COLOR);
		//unprovedFuncsBtColor.setPreferenceName(IConstants.PREF_FUNCS_BUTTON_UNPROVED_COLOR);
		//bgGoalsAssistantBtColor.setPreferenceName(IConstants.PREF_GOALS_ASSISTANT_BUTTON_BG_COLOR);
		//bgSubGoalsAssistantBtColor.setPreferenceName(IConstants.PREF_SUBGOALS_ASSISTANT_BUTTON_BG_COLOR);
		//bgFuncsAssistantBtColor.setPreferenceName(IConstants.PREF_FUNCS_ASSISTANT_BUTTON_BG_COLOR);
		//markedGoalTextColor.setPreferenceName(IConstants.PREF_MARKED_GOAL_TEXT_COLOR);
		//markedFunctionTextColor.setPreferenceName(IConstants.PREF_MARKED_FUNC_TEXT_COLOR);
		
		// LOAD
		bgColoredEditor.load();
		bgGoalsBtColor.load();
		bgSubGoalsBtColor.load();
		bgFuncsBtColor.load();
		provedGoalsBtColor.load();
		provedSubGoalsBtColor.load();
		workingGoalsBtColor.load();
		workingSubGoalsBtColor.load();
		unprovedGoalsBtColor.load();
		unprovedSubGoalsBtColor.load();
		provedFuncsBtColor.load();
		workingFuncsBtColor.load();
		unprovedFuncsBtColor.load();
		bgGoalsAssistantBtColor.load();
		bgSubGoalsAssistantBtColor.load();
		bgFuncsAssistantBtColor.load();
		markedGoalTextColor.load();
		markedFunctionTextColor.load();
		
		
		return entry;
	}
	
	
	/**
	 * Apply a set of colors in ColorFieldEditor objects
	 * 
	 * @param RGB[15] a set of 15 colors 
	 */
	private void applyColors(RGB[] rgb) {
		
		//I call the load() method before calling setColorValue()
		//no to execute the doLoad() method but to put the isDefaultPresented
		//variable to false.
		//Otherwise, the setColorValue() method wouldn't have any effect
		//after having restored the default values!
		//Not beautiful, but efficient...
		//If you can find a better method (I know it exists), please
		//replace this horrible code! Thank You
		
		bgSubGoalsBtColor.load();
		bgSubGoalsBtColor.getColorSelector().setColorValue(rgb[0]);
		unprovedSubGoalsBtColor.load();
		unprovedSubGoalsBtColor.getColorSelector().setColorValue(rgb[1]);
		workingSubGoalsBtColor.load();
		workingSubGoalsBtColor.getColorSelector().setColorValue(rgb[2]);
		provedSubGoalsBtColor.load();
		provedSubGoalsBtColor.getColorSelector().setColorValue(rgb[3]);
		bgSubGoalsAssistantBtColor.load();
		bgSubGoalsAssistantBtColor.getColorSelector().setColorValue(rgb[4]);
		
		bgGoalsBtColor.load();
		bgGoalsBtColor.getColorSelector().setColorValue(rgb[5]);
		unprovedGoalsBtColor.load();
		unprovedGoalsBtColor.getColorSelector().setColorValue(rgb[6]);
		workingGoalsBtColor.load();
		workingGoalsBtColor.getColorSelector().setColorValue(rgb[7]);
		provedGoalsBtColor.load();
		provedGoalsBtColor.getColorSelector().setColorValue(rgb[8]);
		bgGoalsAssistantBtColor.load();
		bgGoalsAssistantBtColor.getColorSelector().setColorValue(rgb[9]);
		
		bgFuncsBtColor.load();
		bgFuncsBtColor.getColorSelector().setColorValue(rgb[10]);
		unprovedFuncsBtColor.load();
		unprovedFuncsBtColor.getColorSelector().setColorValue(rgb[11]);
		workingFuncsBtColor.load();
		workingFuncsBtColor.getColorSelector().setColorValue(rgb[12]);
		provedFuncsBtColor.load();
		provedFuncsBtColor.getColorSelector().setColorValue(rgb[13]);
		bgFuncsAssistantBtColor.load();
		bgFuncsAssistantBtColor.getColorSelector().setColorValue(rgb[14]);
		
	}

	
	public void init(IWorkbench workbench) {
		//setPreferenceStore(EditeurWHY.getDefault().getPreferenceStore());
	}
	
	
	/**
	 * Update colors in the Prover View
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
		// STORE
		bgColoredEditor.store();
		bgGoalsBtColor.store();
		bgSubGoalsBtColor.store();
		bgFuncsBtColor.store();
		provedGoalsBtColor.store();
		provedSubGoalsBtColor.store();
		workingGoalsBtColor.store();
		workingSubGoalsBtColor.store();
		unprovedGoalsBtColor.store();
		unprovedSubGoalsBtColor.store();
		provedFuncsBtColor.store();
		workingFuncsBtColor.store();
		unprovedFuncsBtColor.store();
		bgGoalsAssistantBtColor.store();
		bgSubGoalsAssistantBtColor.store();
		bgFuncsAssistantBtColor.store();
		markedGoalTextColor.store();
		markedFunctionTextColor.store();
		//update the view and save preferences
		updateView();
		EditeurWHY.getDefault().savePluginPreferences();
		return super.performOk();
	}
	
	protected void performDefaults() {
		// LOAD DEFAULT
		bgColoredEditor.loadDefault();
		bgGoalsBtColor.loadDefault();
		bgSubGoalsBtColor.loadDefault();
		bgFuncsBtColor.loadDefault();
		provedGoalsBtColor.loadDefault();
		provedSubGoalsBtColor.loadDefault();
		workingGoalsBtColor.loadDefault();
		workingSubGoalsBtColor.loadDefault();
		unprovedGoalsBtColor.loadDefault();
		unprovedSubGoalsBtColor.loadDefault();
		provedFuncsBtColor.loadDefault();
		workingFuncsBtColor.loadDefault();
		unprovedFuncsBtColor.loadDefault();
		bgGoalsAssistantBtColor.loadDefault();
		bgSubGoalsAssistantBtColor.loadDefault();
		bgFuncsAssistantBtColor.loadDefault();
		markedGoalTextColor.loadDefault();
		markedFunctionTextColor.loadDefault();
		//save the default values
		super.performDefaults();
	}
}
