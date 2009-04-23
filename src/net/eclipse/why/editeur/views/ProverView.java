package net.eclipse.why.editeur.views;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

import net.eclipse.why.editeur.EditeurWHY;
import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.Function;
import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.PO;
import net.eclipse.why.editeur.actions.Highlightor;
import net.eclipse.why.editeur.actions.ProverExecutor;
import net.eclipse.why.editeur.actions.ProverThread;
import net.eclipse.why.editeur.actions.ProverViewUpdater;
import net.eclipse.why.editeur.actions.XMLLoader;
import net.eclipse.why.editeur.actions.XMLSaver;
import net.eclipse.why.editeur.lexer.GoalDisplayModifier;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class ProverView extends ViewPart {

	// Widgets
	private Tree viewer;
	private Button[][] goalsButton;
	private Button[][] functionsButton;
	
	// Actions
	private Action update;
	private Action reset;
	private Action showOnlyUnprovedGoals;
	private Action showAllGoals;
	private Action selectAction;
	private Action kill;
	private Action foldTree;
	private Action unfoldTree;
	private Action runAllProvers;
	private Action mark;
	private Action split;
	private Action save;
	private Action load;

	// Colors
	private Color markedGoalColor, markedFuncColor;

	// Others
	private int proversNumber;
	private boolean showAllLines;
	private Vector<ProverThread> threads = new Vector<ProverThread>();
	private ArrayList<int[]> goalsInView = new ArrayList<int[]>();
	private ArrayList<int[]> functionsInView = new ArrayList<int[]>();
	private int columnSize = 220;

	/**
	 * Tree getter
	 */
	public Tree getViewer() {
		return viewer;
	}

	/**
	 * Prover SelectionListener : defines the action which is executed when the
	 * user clicks on a column button. The corresponding prover is executed on
	 * all goals. It gets the column and the prover number and if goal is marked
	 * it prove all goals beginning with this marked goal. If no goal is marked,
	 * all goals from the first one will be proved.
	 */
	private class ProverSelectionListener implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			if (FileInfos.markedGoal > 0) {
				proveAll(false, !FileInfos.showOnlyUnprovedGoals);
			} else if (FileInfos.markedGoal == 0) { // if no goal is marked
				proveAll(true, !FileInfos.showOnlyUnprovedGoals);
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			return;
		}
	}

	/**
	 * The mouse listener. Defines the action which is executed when the user
	 * clicks on a goal button.
	 */
	private class BtnListener implements MouseListener {

		public void mouseDoubleClick(MouseEvent e) {
			return;
		}

		public void mouseDown(MouseEvent e) {

			if (e.button == 1) { // left click
				Button b = (Button) e.widget; // the clicked button
				int line = ((int[]) b.getData("goal"))[0];
				
				// Creates the goals set to prove : here it's just the
				// goal corresponding to the clicked button.
				// So, the first and the last goals to prove are the same one:
				ArrayList<Integer> array = new ArrayList<Integer>();
				array.add(line);
				
				prove(array, !FileInfos.showOnlyUnprovedGoals);
			}

			if (e.button == 3) { // right click
				// expand the menu which propose to modify manually
				// the state of the goal
				Button b = (Button) e.widget;
				b.getMenu().setData(b);
				b.getMenu().setVisible(true);
			}
		}

		public void mouseUp(MouseEvent e) {
			/* do nothing */
		}
	}

	/**
	 * Goal button menu SelectionListener. It gets the clicked value : 0=reset,
	 * 1=admitted(proved), 2=unproved. Then it gets the menu's button and the
	 * corresponding goal, and prover. Then PO is extracted and changed.
	 */
	private class ButtonMenuSelectionListener implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {

			try {

				MenuItem o = (MenuItem) e.widget;
				int a = ((Integer) o.getData("admit")).intValue();
				Button p = (Button) o.getParent().getData();
				int goal = ((int[]) p.getData("goal"))[0];
				int prover = ((Integer) p.getData("prover")).intValue();

				PO op = (PO) FileInfos.goals.get(goal - 1);
				op.setState(prover, a);

				updateElementAt(goal, prover);
				makeStats(prover);

			} catch (Exception exception) {
				TraceView.print(MessageType.ERROR,
						"ProverView.MenuItemSelectionListener.widgetSelected() :\n"
								+ exception);
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {

		}
	}

	/**
	 * Function button's SelectionListener : defines the action which is
	 * executed when the user clicks on a function's button. The prover of the
	 * corresponding column is executed on all goals included into the function.
	 */
	private class FunctionSelectionListener implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {

			// get the button and, the prover number and the function name
			Button b = (Button) e.widget;
			String function = (String) b.getData("function");

			int fgoal = 1; // the first goal to prove
			int lgoal = 1; // the last goal to prove

			// for all functions :
			for (int c = 0; c < FileInfos.functions.size(); c++) {
				Function f = (Function) FileInfos.functions.get(c);
				// if we have found our function
				if (f.getName().equals(function)) {
					// the first goal is ok
					// we've just to increment the last goal number
					lgoal = fgoal;
					lgoal += f.getPOList().size();
					lgoal--;
					break;
				} else { // if it's a previous function
					fgoal += f.getPOList().size(); // we increment the first
													// goal number to prove
				}
			}

			// we've to make now the set of goals to prove
			ArrayList<Integer> array = new ArrayList<Integer>();
			boolean all = !FileInfos.showOnlyUnprovedGoals;
			for (int i = fgoal - 1; i < lgoal; i++) {
				if (!((PO) FileInfos.goals.get(i)).isProved() || all) {
					array.add(i);
				}
			}
			// prove now the set of sets of goals
			prove(array, !FileInfos.showOnlyUnprovedGoals);

		}
		public void widgetDefaultSelected(SelectionEvent e) {
			// do nothing
		}
	}

	/**
	 * Item listener for 'Collapse' action
	 */
	private class CollapseListener implements Listener {

		public void handleEvent(Event event) {
			TreeItem t = (TreeItem) event.item;
			if (t.getData("function") != null) { // function tree item
				int a = ((int[]) t.getData("function"))[0];
				// collapse function item
				((Function) FileInfos.functions.get(a)).collapse();
				TreeItem[] tprim = t.getItems();
				for (int y = 0; y < tprim.length; y++) {
					// collapse all goal items
					int aprim = ((int[]) tprim[y].getData("goal"))[0];
					((PO) FileInfos.goals.get(aprim - 1)).collapse();
				}
				if (!showAllLines)
					countLines();
				updateView();
			} else if (t.getData("goal") != null) { // goal tree item
				int a = ((int[]) t.getData("goal"))[0];
				((PO) FileInfos.goals.get(a - 1)).collapse();
			}
		}
	}

	/**
	 * Item listener for 'Expand' action
	 */
	private class ExpandListener implements Listener {

		public void handleEvent(Event event) {
			TreeItem t = (TreeItem) event.item;
			if (t.getData("function") != null) { // function tree item
				int a = ((int[]) t.getData("function"))[0];
				// expand function's item
				((Function) FileInfos.functions.get(a)).expand();
				// a possible update of the slider if we don't
				// have to show all goals
				if (!showAllLines)
					countLines();
				updateView();
			} else if (t.getData("goal") != null) { // goal tree item
				int a = ((int[]) t.getData("goal"))[0];
				((PO) FileInfos.goals.get(a - 1)).expand();
			}
		}
	}

	/**
	 * Item Selection Listener
	 */
	private class CheckListener implements Listener {

		public void handleEvent(Event event) {

			selectAction.run();
		}
	}

	/**
	 * Keyboard listener : used when the user press F7, F8, F9, Ctrl-! or
	 * Ctrl-Shift-X keys
	 */
	private class PressListener implements KeyListener {

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {

			// use all provers to prove all unproved goals
			if (e.keyCode == SWT.F7) {
				runAllProvers.run();
			}

			// stop provers
			if (e.keyCode == SWT.F8) {
				kill.run();
			}

			// mark a goal
			if (e.keyCode == SWT.F9) {
				mark.run();
			}

			// update the view
			if (e.stateMask == SWT.CTRL) {
				if (e.keyCode == '!') {
					update.run();
				}
			}

			// reset all results
			if (e.stateMask == SWT.CTRL + SWT.SHIFT) {
				if (e.keyCode == 'X' || e.keyCode == 'x') {
					reset.run();
				}
			}
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		this.viewer = new Tree(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		this.viewer.setHeaderVisible(true);
		this.viewer.addListener(SWT.Collapse, new CollapseListener());
		this.viewer.addListener(SWT.Expand, new ExpandListener());
		this.viewer.addListener(SWT.Selection, new CheckListener());
		this.viewer.addKeyListener(new PressListener());

		FileInfos.initColumns();
		makeColumns();
		initView();
		makeActions();
		contributeToActionBars();
	}

	/**
	 * Creates all table columns (one by prover)
	 */
	public void makeColumns() {

		// the prover number
		proversNumber = FileInfos.provers.length;

		if (proversNumber == 1 && FileInfos.provers[0].equals("")) {
			proversNumber = 0;
		}

		// Delete the prover's columns from the table
		int num_of_columns = viewer.getColumnCount();
		for (int w = 0; w < num_of_columns; w++) {
			TreeColumn tcl = viewer.getColumn(0);
			tcl.dispose();
		}

		// Creates new columns : 1 column for goals, 1 column
		// for goal states and one column by prover
		final TreeColumn col;
		col = new TreeColumn(viewer, SWT.RIGHT);
		col.setWidth(columnSize);
		col.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {
				/* nothing to do */
			}

			public void controlResized(ControlEvent e) {
				columnSize = col.getWidth(); // get the column size
			}
		});

		TreeColumn square = new TreeColumn(viewer, SWT.CENTER);
		square.setWidth(IConstants.IMAGE_BALL_RED.getImageData().width + 6);
		square.setResizable(false); // can't resize this column
		square.addSelectionListener(new ProverSelectionListener());

		/* TreeColumn column;
		// for all provers
		/* for (int i = 0; i < proversNumber; i++) {
			int[] pNum = new int[1];
			pNum[0] = i;
			column = new TreeColumn(viewer, SWT.CENTER);
			column.setText(FileInfos.provers[i]);
			// sets statistics in the tooltip text
			column.setToolTipText(FileInfos.proverStats[i]);
			// sets the prover number into widget's data
			column.setData(pNum);
			if (FileInfos.status[i].equals("prover"))
				column.addSelectionListener(new ProverSelectionListener());
			column.pack();
		} */
	}

	/**
	 * Creates all goal and function items and all buttons (1 by provers for all
	 * goals and for all functions)
	 */
	private void initView() {

		int nb_lines_to_show;

		// displays a warning message if the number of lines
		// to show in the view is higher than a maximum number
		// fixed to 150
		checkLinesAndWarn();

		// gets if all goals will be displayed and adjust the slider
		IPreferenceStore store = EditeurWHY.getDefault().getPreferenceStore();
		showAllLines = store.getBoolean(IConstants.PREF_SHOW_ALL_LINES);
		if (!showAllLines) {
			nb_lines_to_show = store.getInt(IConstants.PREF_SHOW_NB_LINES);
			countLines();
		} else {
			nb_lines_to_show = FileInfos.functions.size()
					+ FileInfos.numberOfGoals + 1;
		}

		int goal_of_reference = 1;

		// gets all goals and functions which have to be created in the view
		// and save them in array lists goalsInView and functionsInView
		getGoals(goal_of_reference, nb_lines_to_show - 1);

		// gets here the number of goals to create
		int numberOfGoals = goalsInView.size();
		int numberOfFunctions = functionsInView.size();

		// initializes buttons boards
		goalsButton = new Button[numberOfGoals][proversNumber];
		TreeEditor[][] goalsEditor = new TreeEditor[numberOfGoals][proversNumber];
		functionsButton = new Button[numberOfFunctions][proversNumber];
		TreeEditor[][] functionsEditor = new TreeEditor[numberOfFunctions][proversNumber];
		
		int m = 0; /* function number */
		int n = 0; /* goal number */

		BtnMenu menu = new BtnMenu(viewer);
		for (int f = 0; f < menu.getItems().length; f++) {
			menu.getItem(f).addSelectionListener(
					new ButtonMenuSelectionListener());
		}
		viewer.setMenu(menu);
		
		String currentFunction;
		int current, current_M = 0;
		TreeItem func = null;

		// for all goals
		for (int i = 0; i < numberOfGoals; i++) {

			int g = ((int[]) goalsInView.get(n))[0] - 1;
			int l = 0;

			currentFunction = ((PO) FileInfos.goals.get(g)).getFname(); // name
			current = ((PO) FileInfos.goals.get(g)).getFnum(); // num

			// if we meet this function for the first time
			if ((func == null) || (current != current_M)) {

				// new Function item
				func = new TreeItem(viewer, SWT.NONE);
				l = ((PO) FileInfos.goals.get(g)).getFnum() - 1;
				func.setText(currentFunction);
				func.setData("function", new int[] { l });

				Function f = (Function) FileInfos.functions.get(l);
				// for all provers
				/* for (int j = 0; j < proversNumber; j++) {

					boolean b_assistant;
					// create new editors and new functions
					functionsEditor[m][j] = new TreeEditor(viewer);
					functionsButton[m][j] = new Button(viewer, SWT.PUSH);

					if (FileInfos.status[j].equals("prover")) {
						functionsButton[m][j].setBackground(funcBtColor);
						b_assistant = false;
					} else {
						functionsButton[m][j]
								.setBackground(assistantFuncBgColor);
						b_assistant = true;
					}

					boolean proved = true; // is the function proved?
					boolean zero = true; // is the function reset?

					// for all PO in the function
					for (PO potmp : f.getPOList()) {
						int a = potmp.getState(j);
						if (a != 0) { // proved or unproved, not null
							zero = false;
							if (!proved)
								break; // unproved
						}
						if (a != 1) { // not proved
							proved = false;
							if (!zero)
								break; // unproved
						}
					}

					if (proved)
						setButtonProved(functionsButton[m][j], false, false);
					else if (zero)
						setButtonStart(functionsButton[m][j], false, false,
								!b_assistant);
					else
						setButtonUnproved(functionsButton[m][j], false, false,
								0);

					functionsButton[m][j].computeSize(SWT.DEFAULT, viewer
							.getItemHeight());
					if (FileInfos.status[j].equals("prover"))
						functionsButton[m][j]
								.addSelectionListener(new FunctionSelectionListener());

					functionsEditor[m][j].grabHorizontal = true;
					functionsEditor[m][j].minimumHeight = functionsButton[m][j]
							.getSize().y;
					functionsEditor[m][j].minimumWidth = functionsButton[m][j]
							.getSize().x;
					functionsEditor[m][j].setEditor(functionsButton[m][j],
							func, j + 2);

					// sets function's name, prover number and item number in
					// button's data
					functionsButton[m][j].setData("function", currentFunction);
					functionsButton[m][j].setData("prover", new Integer(j));
					functionsButton[m][j].setData("item", new int[] { m });

					functionsButton[m][j].setRedraw(true);

				} */

				if (f.isProved()) {
					func.setImage(1, IConstants.IMAGE_BALL_GREEN);
				} else {
					func.setImage(1, IConstants.IMAGE_BALL_RED);
				}

				m++;
				current_M = current; // current_M <- current
			}

			// new PO item
			TreeItem item = new TreeItem(func, SWT.NONE);
			PO po = (PO) FileInfos.goals.get(g);
			item.setForeground(IConstants.COLOR_GREY);
			item.setText(0, po.getTitle());
			item.setImage(1, IConstants.IMAGE_BALL_RED);

			for (int y = 0; y < proversNumber; y++) {
				int state = po.getState(y);
				if (state == 1) {
					item.setImage(1, IConstants.IMAGE_BALL_GREEN);
					break;
				}
			}

			item.setData("goal", new int[] { g + 1, 0 });
			if ((g + 1) == FileInfos.markedGoal) {
				item.setForeground(0, markedGoalColor);
				item.getParentItem().setForeground(0, markedFuncColor);
			}
			n++;
		}

		// Expands items
		if (viewer.getItemCount() > 0) {
			for (int r = 0; r < numberOfFunctions; r++) {
				int a = ((int[]) functionsInView.get(r))[0];
				Function fct = (Function) FileInfos.functions.get(a - 1);
				if (fct.isItem_expanded()) {
					TreeItem tit = viewer.getItem(r);
					tit.setExpanded(true);
					for (int y = 0; y < tit.getItemCount(); y++) {
						int z = ((int[]) tit.getItem(y).getData("goal"))[0];
						PO e = (PO) FileInfos.goals.get(z - 1);
						if (e.isItem_expanded()) {
							tit.getItem(y).setExpanded(true);
						}
					}
				} else {
					viewer.getItem(r).setExpanded(false);
				}
			}
		}

		// layout() for button's view update
		viewer.getParent().layout();

		for (int x = 0; x < numberOfFunctions; x++) {
			for (int y = 0; y < proversNumber; y++) {
				functionsEditor[x][y].layout();
				functionsButton[x][y].getParent().layout();
			}
		}

		for (int x = 0; x < numberOfGoals; x++) {
			for (int y = 0; y < proversNumber; y++) {
				goalsEditor[x][y].layout();
				goalsButton[x][y].getParent().layout();
			}
		}
	}

	@SuppressWarnings("unused")
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ProverView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer);
		viewer.setMenu(menu);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	// Rolling menu in the high right corner of the view
	// with actions and corresponding icons
	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(showOnlyUnprovedGoals);
		manager.add(showAllGoals);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(foldTree);
		manager.add(unfoldTree);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(runAllProvers);
		manager.add(kill);
		manager.add(mark);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(reset);
		manager.add(update);
	}

	private void fillContextMenu(IMenuManager manager) {
		// manager.add(action);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(runAllProvers);
		manager.add(kill);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(mark);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(split);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(load);
		manager.add(save);
	}

	/**
	 * Creates all actions and defines all corresponding run() methods.
	 */
	private void makeActions() {

		// updates the view
		update = new Action() {
			public void run() {
				FileInfos.initColumns();
				makeColumns();
				updateView();
				split.setEnabled(false);
			}
		};
		update.setText("Update");
		update.setAccelerator(SWT.CTRL + '!');

		// clears results
		reset = new Action() {
			public void run() {
				if (isEnabled()) {
					for (int y = 0; y < FileInfos.functions.size(); y++) {
						FileInfos.functions.get(y).init();
					}
					for (int z = 0; z < FileInfos.goals.size(); z++) {
						FileInfos.goals.get(z).init();
					}
					FileInfos.showOnlyUnprovedGoals = false;
					showAllGoals.setChecked(true);
					showOnlyUnprovedGoals.setChecked(false);
					updateView();
				}
			}
		};
		reset.setText("Reset");
		reset.setAccelerator(SWT.CTRL + SWT.SHIFT + 'X');

		// marks the selected goal
		mark = new Action() {
			public void run() {
				mark();
			}
		};

		mark.setToolTipText("Create/Delete a start mark");
		mark.setText("Mark/Unmark PO");
		mark.setImageDescriptor(ImageDescriptor
				.createFromURL(IConstants.URL_MARK_BTN));
		mark.setAccelerator(SWT.F9);

		// runs all provers on all unproved goals
		runAllProvers = new Action() {
			public void run() {
				if (FileInfos.markedGoal > 0) {
					proveAll(false, false);
				} else if (FileInfos.markedGoal == 0) {
					proveAll(true, false);
				}
			}
		};
		runAllProvers
				.setToolTipText("Prove using all provers without reproving");
		runAllProvers.setText("Prove using all provers without reproving");
		runAllProvers.setImageDescriptor(ImageDescriptor
				.createFromURL(IConstants.URL_RUN_ALL_PROVERS));
		runAllProvers.setAccelerator(SWT.F7);

		// stops the prover actions
		kill = new Action() {
			public void run() {
				for (int e = 0; e < threads.size(); e++) {
					((ProverThread) threads.get(e)).cease();
				}
				threads.clear();
				setEnabled(false);
			}
		};
		kill.setText("Stop provers");
		kill.setToolTipText("Stop provers");
		kill.setImageDescriptor(ImageDescriptor
				.createFromURL(IConstants.URL_KILL_BUTTON));
		kill.setEnabled(false);
		kill.setAccelerator(SWT.F8);

		// hide proved goals
		showOnlyUnprovedGoals = new Action() {
			public void run() {
				if (!FileInfos.showOnlyUnprovedGoals) {
					FileInfos.showOnlyUnprovedGoals = true;
					if (!showAllLines) {
						countLines();
					}
					updateView();
				}
				showAllGoals.setChecked(false);
				setChecked(true);
			}
		};
		showOnlyUnprovedGoals.setChecked(false);
		showOnlyUnprovedGoals.setText("Show Unproved");
		showOnlyUnprovedGoals.setToolTipText("Show only unproved goals");

		// show proved goals
		showAllGoals = new Action() {
			public void run() {
				if (FileInfos.showOnlyUnprovedGoals) {
					FileInfos.showOnlyUnprovedGoals = false;
					if (!showAllLines) {
						countLines();
					}
					updateView();
				}
				showOnlyUnprovedGoals.setChecked(false);
				setChecked(true);
			}
		};
		showAllGoals.setChecked(true);
		showAllGoals.setText("Show All");
		showAllGoals.setToolTipText("Show proved and unproved goals");

		// fold the tree viewer
		foldTree = new Action() {
			public void run() {
				TreeItem[] y = viewer.getItems();
				for (int p = 0; p < y.length; p++) {
					y[p].setExpanded(false);
				}
				for (int p = 0; p < FileInfos.functions.size(); p++) {
					((Function) FileInfos.functions.get(p)).collapse();
				}
				if (!showAllLines)
					countLines();
				updateView();
			}
		};
		foldTree.setImageDescriptor(ImageDescriptor
				.createFromURL(IConstants.URL_COLLAPSE_BTN));
		foldTree.setText("Collapse");
		foldTree.setToolTipText("Collapse");

		// unfold the tree viewer
		unfoldTree = new Action() {
			public void run() {
				TreeItem[] y = viewer.getItems();
				for (int p = 0; p < y.length; p++) {
					y[p].setExpanded(true);
				}
				for (int p = 0; p < FileInfos.functions.size(); p++) {
					((Function) FileInfos.functions.get(p)).expand();
				}
				if (!showAllLines)
					countLines();
				updateView();
			}
		};
		unfoldTree.setImageDescriptor(ImageDescriptor
				.createFromURL(IConstants.URL_EXPAND_BTN));
		unfoldTree.setText("Expand");
		unfoldTree.setToolTipText("Expand");

		// when a goal is selected
		selectAction = new Action() {
			public void run() {
				searchAndSelect();
			}
		};

		// splits a goal
		split = new Action() {
			public void run() {
					proveManully();
					updateView();
			}
		};
		split.setImageDescriptor(ImageDescriptor
				.createFromURL(IConstants.URL_SPLIT_BTN));
		split.setToolTipText("Prove manually");
		split.setEnabled(false);

		// saves the results into a XML file
		save = new Action() {
			public void run() {
				XMLSaver.save();
			}
		};
		save.setImageDescriptor(ImageDescriptor
				.createFromURL(IConstants.URL_SAVE_BTN));
		save.setToolTipText("Save results");

		// loads results from a XML file
		load = new Action() {
			public void run() {
				load();
			}
		};
		load.setImageDescriptor(ImageDescriptor
				.createFromURL(IConstants.URL_LOAD_BTN));
		load.setToolTipText("Load archived results");

	}

	/**
	 * Initialize the contents of the view
	 */
	public void updateView() {

		int lines, columns;

		lines = goalsButton.length;
		try {
			columns = goalsButton[0].length;
		} catch (Exception e) {
			columns = 0;
		}

		// All buttons dispose :
		for (int p = 0; p < lines; p++) {
			for (int q = 0; q < columns; q++) {
				try {
					goalsButton[p][q].dispose();
				} catch (Exception e) {
				}
			}
		}

		// item with the third board of buttons
		lines = functionsButton.length;
		try {
			columns = functionsButton[0].length;
		} catch (Exception e) {
			columns = 0;
		}

		for (int p = 0; p < lines; p++) {
			for (int q = 0; q < columns; q++) {
				try {
					functionsButton[p][q].dispose();
				} catch (Exception e) {
				}
			}
		}

		// remove all items
		viewer.removeAll();

		// reinitialize the view => build new buttons and items
		initView();
	}

	/**
	 * Updates a goal state : changes the image or the color of the
	 * corresponding button in the view in accordance with its state.
	 * 
	 * @param goalNum
	 *            the goal number
	 * @param proverNum
	 *            the prover number
	 * @throws SWTException
	 */
	public synchronized void updateElementAt(int goalNum, int proverNum) throws SWTException {
		updateButtons (goalNum);

		
		int state = ((PO) FileInfos.goals.get(goalNum - 1)).getState(0);

		switch (state) {
		case 0:
			updateFButton(goalNum, false);
			break;
		case 1:
			updateFButton(goalNum, true);
			break;
		case 2:
			updateFButton(goalNum, false);
			break;
		case 3:
			updateFButton(goalNum, false);
			break;
		case 4:
			updateFButton(goalNum, false);
			break;
		case 5:
			updateFButton(goalNum, false);
			break;
		default:
			break;
		}
	}

	/**
	 * Modifies the goals items in the view (proved/unproved) and
	 * updates the colors and images of buttons.
	 * 
	 * @param goalNumber
	 *            the goal number
	 * @param proverNumber
	 *            the prover number
	 * @throws SWTException
	 */
	private void updateButtons(int goalNumber)
			throws SWTException {

		boolean stop = false;

		TreeItem gitem = null;
		// we get the goal item
		TreeItem[] content = viewer.getItems();
		for (int u = 0; u < content.length; u++) {
			if (content[u].getExpanded()
					|| (!content[u].getExpanded() && showAllLines)) {
				TreeItem[] underContent = content[u].getItems();
				for (int o = 0; o < underContent.length; o++) {
					if (((int[]) underContent[o].getData("goal"))[0] == goalNumber) {
						gitem = underContent[o];
						break;
					}
				}
			}
		}

		// is the goal proved?
		boolean is_proved = ((PO) FileInfos.goals.get(goalNumber - 1))
				.isProved();

		if (is_proved) { // if the goal has been proved
			if (gitem != null) {
				gitem.setImage(1, IConstants.IMAGE_BALL_GREEN);
			}
		}

		if (!is_proved) { // if the goal hasn't been proved
			// for all other provers
			for (int b = 0; b < FileInfos.provers.length; b++) {
				int etat = ((PO) FileInfos.goals.get(goalNumber - 1))
						.getState(b);
				if (etat == 1) { // if the goal is proved
								 // we can stop, the goal is proved
					stop = true;
					break;
				}
			}
			if (!stop) { // if we didn't stop, it means that the goal is
						 // unproved
				if (gitem != null) {
					if (!is_proved)
						gitem.setImage(1, IConstants.IMAGE_BALL_RED);
				}
			}
		}
	}

	/**
	 * Modifies the functions items in the proving view (proved/unproved) and
	 * updates the colors and images of buttons.
	 * 
	 * @param goalNumber
	 *            The goal number
	 * @param proverNumber
	 *            The prover number
	 * @param proved
	 *            true if the goal has been proved by the prover, false
	 *            otherwise.
	 * @throws SWTException
	 */
	private void updateFButton(int goalNumber, boolean proved)
			throws SWTException {

		boolean stop = false;
		PO po = (PO) FileInfos.goals.get(goalNumber - 1);
		int fnum = po.getFnum();
		Function fc = (Function) FileInfos.functions.get(fnum - 1);
		
		if (!proved) { // if the goal hasn't been proved
			bouclefor: for (int b = 0; b < FileInfos.provers.length; b++) { // for
				// all
				// other
				// provers
				int etat = ((PO) FileInfos.goals.get(goalNumber - 1))
						.getState(b);
				if (etat == 1) { // if goal is proved
					stop = true;
					break bouclefor; // stop
				}
			}
		}

		// get the function's position
		int g = getFunctionRow(goalNumber);

		if (g >= 0) { // if the function is in the view

			if (!stop) { // the goal is unproved
				TreeItem fitem = null;
				if (g >= 0) {
					TreeItem[] content = viewer.getItems();
					for (int u = 0; u < content.length; u++) {
						if (content[u].getExpanded()
								|| (!content[u].getExpanded() && showAllLines)) {
							TreeItem[] underContent = content[u].getItems();
							for (int o = 0; o < underContent.length; o++) {
								if (((int[]) underContent[o].getData("goal"))[0] == goalNumber) {
									fitem = content[u];
									break;
								}
							}
						}
					}
				}

				if (fitem != null) {
					if (fc.isProved()) { // if the function is proved
						fitem.setImage(1, IConstants.IMAGE_BALL_GREEN);
					} else { // else
						fitem.setImage(1, IConstants.IMAGE_BALL_RED);
					}
				}
			}

/*			int firstGoal = goalNumber;
			// gets the first goal of the function
			while (firstGoal > 0
					&& ((PO) FileInfos.goals.get(firstGoal - 1)).getFnum() == fnum) {
				firstGoal--;
			}
			firstGoal++;
			if (firstGoal < 1) {
				TraceView.print(MessageType.WARNING,
						"ProverView.updateButtons() : goal " + goalNumber
								+ " unknown for function '" + fc.getName()
								+ "'");
				return;
			}

			// for all goals of the function
   		    while ((firstGoal <= FileInfos.whyFileNumber)
					&& (((PO) FileInfos.goals.get(firstGoal - 1)).getFnum() == fnum)) {
				int state = ((PO) FileInfos.goals.get(firstGoal - 1))
						.getState(proverNumber);
				// one of goals is unproved
				if (state != 1) {
					functionProvedForProver = false;
					// if the function has been touched before, we can break
					if (!start)
						break;
				}
				// one of the goals has been touched before
				if (state != 0) {
					start = false;
					// if the function was proved but is not proved now
					// we can break
					if (!functionProvedForProver)
						break;
				}
				firstGoal++;
			}

			// changes the color of the function's button
			if (functionProvedForProver)
				setButtonProved(functionsButton[g][proverNumber], false, false);
			else if (start)
				setButtonStart(functionsButton[g][proverNumber], false, false,
						FileInfos.status[proverNumber].equals("prover"));
			else
				setButtonUnproved(functionsButton[g][proverNumber], false,
						false, 0);
			*/
		}
	}

	/**
	 * Puts orange color or an orange ball in the goal and function
	 * buttons to show that the prover is working on.
	 * 
	 * @param goalNum
	 *            the goal number
	 * @param proverNum
	 *            the prover number
	 */
	public synchronized void working(int goalNum, int proverNum) {
		/* TODO: set working image */
	}

	/**
	 * Gets the row of a goal in the goals' board
	 * 
	 * @param goalNum
	 *            goal number
	 * @return int index of the goal, -1 if it isn't in
	 */
	private int getGoalRow(int goalNum) {

		int line = -1;
		for (int p = 0; p < goalsInView.size(); p++) {
			if (((int[]) goalsInView.get(p))[0] == goalNum) {
				line = p;
				break;
			}
		}
		return line;
	}

	/**
	 * Gets the row of a function in the functions' board, giving a goal number
	 * which belongs to the function.
	 * 
	 * @param goalNum
	 *            a goal number
	 * @return int index of the function in the view, -1 if it isn't in
	 */
	private int getFunctionRow(int goalNum) {

		int line = -1; // function's row in the ArrayList functionsInView
		int frow = -1; // the function number (cf. FileInfos.functions[])

		PO po = (PO) FileInfos.goals.get(goalNum - 1);
		frow = po.getFnum();

		if (frow == 0) {
			TraceView.print(MessageType.WARNING,
					"WhyView.getFunctionRow() : function '" + po.getFname()
							+ "' unknown...");
			return -1;
		}

		for (int p = 0; p < functionsInView.size(); p++) {
			if (((int[]) functionsInView.get(p))[0] == frow) {
				line = p;
				break;
			}
		}
		return line;
	}

	/**
	 * Enables/Disables the button to stop provers
	 * 
	 * @param b
	 *            : true => enables the button ; false => disables the button
	 */
	public synchronized void killButton(boolean b) {

		if (b) {
			kill.setEnabled(true);
			reset.setEnabled(false);
		} else {
			if (threads.size() == 0)
				kill.setEnabled(false);
			reset.setEnabled(true);
		}
	}

	/**
	 * Removes a thread from the list of working jobs. This function is called
	 * when a thread concerning the execution of verification tools has
	 * finished.
	 * 
	 * @param id
	 *            the thread id
	 */
	public synchronized void removeThread(long id) {
		for (int r = 0; r < threads.size(); r++) {
			if (((ProverThread) threads.get(r)).getIdentity() == id) {
				threads.remove(r);
			}
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.setFocus();
	}

	/**
	 * Function called when the view is opened
	 */
	public void setInput() {
		FileInfos.initColumns();
	}

	/**
	 * This function gets the selected file in the view and runs the
	 * <code>Highlightor.highlight()</code> method to highlight the source code
	 * which corresponds to the goal.
	 */
	private void searchAndSelect() {

		int goalNum = 0;
		int funcNum = 0;
		int fnNum = 0;
		// String kind = "";
		String message;
		Image image;
		TreeItem m = null;

		try {
			m = viewer.getSelection()[0]; // gets the selected item
			Object data = m.getData("goal");
			if (data == null) {
				getViewSite().getActionBars().getStatusLineManager()
						.setMessage(null);
				return;
			}
			goalNum = ((int[])data)[0]; // goal number
			fnNum = FileInfos.goals.get(goalNum - 1).getNum_in_f();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (goalNum > 0) {
			String file;
			// this is a goal
				file = FileInfos.goals.get(goalNum - 1).getFname() + "_po_"
						+ fnNum + ".why";
				
			String sourceFile;
			sourceFile = FileInfos.getRoot() + "why" + File.separator
					+ FileInfos.goals.get(goalNum - 1).getFname() + "_po_"
					+ fnNum + ".xpl";

			Highlightor.setGoal(goalNum);
			// gets fields from the .xpl file
			Highlightor.selectFromXPL(sourceFile);
			showGoalInViewer(goalNum, file); // sets the pretty printed goal in
												// the PO
			// Viewer
			message = ((PO) FileInfos.goals.get(goalNum - 1)).getTitle();
			image = IConstants.IMAGE_PO;
			split.setEnabled(true);
		} else {
			// this is a function
			showGoalInViewer(-1, null); // clean the PO Viewer
			funcNum = ((int[]) m.getData("function"))[0];
			message = ((Function) FileInfos.functions.get(funcNum))
					.getBehavior();
			image = IConstants.IMAGE_FUNC;
			split.setEnabled(false);
		}

		if (message != null && !message.trim().equals("")) {
			// sets the item information in the low bar
			getViewSite().getActionBars().getStatusLineManager().setMessage(
					image, message);
		} else {
			getViewSite().getActionBars().getStatusLineManager().setMessage(
					null);
		}
		viewer.forceFocus();
	}

	/**
	 * Load a set of proving results associated with a program from a XML file
	 * to this view.
	 */
	private void load() {
		XMLLoader xload = new XMLLoader(this);
		xload.load();
	}

	/**
	 * Creates a thread which will run a prover on a set of goals
	 * 
	 * @param a
	 *            set of goals to prove
	 * @param proverNumber
	 *            the prover number, -1 to use all provers
	 * @param all
	 *            false to prove unproved goals only, true to prove all goals
	 * 
	 */
	private void prove(ArrayList<Integer> goals, boolean all) {
		ProverViewUpdater uvw = new ProverViewUpdater((ProverView) this);
		ProverThread m = new ProverThread(goals, all, uvw);
		threads.add(m);
	}

	/**
	 * Runs the prove() function on all goals
	 * 
	 * @param prover
	 *            the prover number, -1 to use all provers
	 * @param begin_first
	 *            true to begin with the first goal, false to begin with the
	 *            marked goal
	 * @param notall
	 *            true to prove unproved goals only, false to prove all goals
	 */
	private void proveAll (boolean begin_first, boolean all) {

		int first = 1;
		int last = FileInfos.numberOfGoals;
		if (last == 0) { // empty set
			return;
		}

		if (!begin_first) { // we begin with a marked goal
			first = FileInfos.markedGoal;
			if (first == 0) {
				return;
			}
		}

		ArrayList<Integer> goals = new ArrayList<Integer>();

		for (int i = first; i < last; i++) {
				if (!((PO) FileInfos.goals.get(i)).isProved() || all) {
					goals.add(i);
				}
		}
			
		prove(goals, all); // prove all the goals we've selected
	}

	/**
	 * Puts a mark on the goal selected in the Prover View
	 */
	private void mark() {

		TreeItem[] items = viewer.getSelection();
		TreeItem fitem = null;
		TreeItem gitem = null;

		if (items != null && items.length == 1) {
			// we get, from the selected item, the function or goal number
			int[] goal;
			int[] function = null;
			boolean is_function_item = false;
			goal = (int[]) items[0].getData("goal");
			if (goal == null) {
				is_function_item = true;
				function = (int[]) items[0].getData("function");
				if (function == null) {
					TraceView
							.print(MessageType.ERROR,
									"ProverView.mark() : selected item represents neither a goal nor a function !");
					return;
				}
			}

			// if it's a function item, we get the first goal of
			// this function which appears in the view
			if (is_function_item) {
				goal = (int[]) items[0].getItem(0).getData("goal");
				fitem = items[0];
				gitem = (items[0].getItems())[0];
			} else {
				if (goal[1] > 0) {
					fitem = items[0].getParentItem().getParentItem();
					gitem = items[0].getParentItem();
				} else {
					fitem = items[0].getParentItem();
					gitem = items[0];
				}
			}

			// if the function was ever marked, we can consider that the
			// selected
			// goal is the marked goal => we remove the marks of the goal and of
			// the function
			if (is_function_item && (FileInfos.markedGoal > 0)) {
				int fmarked = ((PO) FileInfos.goals
						.get(FileInfos.markedGoal - 1)).getFnum();
				if (fmarked == function[0]) {
					goal = new int[1];
					// selected goal = marked goal
					goal[0] = FileInfos.markedGoal;
					for (int s = 0; s < fitem.getItems().length; s++) {
						int t = ((int[]) fitem.getItem(s).getData("goal"))[0];
						if (t == goal[0]) {
							// with the corresponding item
							gitem = fitem.getItems()[s];
							break;
						}
					}
				}
			}

			// we begin with the goal we recovered
			if (goal != null) {
				// if the goal was ever marked
				if (FileInfos.markedGoal == goal[0]) {
					// we unmark it
					fitem.setForeground(0, IConstants.COLOR_BLACK);
					gitem.setForeground(0, IConstants.COLOR_GREY);
					FileInfos.markedGoal = 0;
				}
				// else
				else {
					// we mark the goal
					gitem.setForeground(0, markedGoalColor);
					// if a goal was marked before
					if (FileInfos.markedGoal > 0) {
						int g = FileInfos.markedGoal;
						int i = getGoalRow(g);
						// and if the item of this goal was in the view
						if (i != -1) {
							int j = getFunctionRow(g);
							if (j != -1) {
								TreeItem t = viewer.getItem(j);
								TreeItem[] tprim = t.getItems();
								for (int v = 0; v < tprim.length; v++) {
									TreeItem t2 = tprim[v];
									int gprim = ((int[]) t2.getData("goal"))[0];
									if (gprim == g) {
										// we unmark it!
										t2.setForeground(0,
												IConstants.COLOR_GREY);
										t2.getParentItem().setForeground(0,
												IConstants.COLOR_BLACK);
									}
								}
							}
						}
					}
					FileInfos.markedGoal = goal[0];
					fitem.setForeground(0, markedFuncColor);
				}
			}
		}
	}

	/**
	 * Splits the selected po into sub-pos creating files and items in the view
	 * 
	 * @return the number of sub-po which have just been created, -1 if
	 *         impossible to create them
	 */
	private int proveManully() {
		TreeItem[] items = viewer.getSelection();
		if (items != null && items.length == 1) {
			int num = ((int[]) items[0].getData("goal"))[0];
			ProverExecutor ex = new ProverExecutor();
			ex.prove(num, 1);
		}
		return -1;
	}

	/**
	 * Prints in PO Viewer a pretty printed goal
	 * 
	 * @param gnum
	 *            the goal number to make the view title
	 * @param whyFileName
	 *            the .why file name to print in the view
	 */
	private void showGoalInViewer(int gnum, String whyFileName) {

		try {
			String file = null;
			if (whyFileName != null) {
				file = FileInfos.getRoot() + "why" + File.separator
						+ whyFileName;
			}

			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			POViewer v = (POViewer) page.showView(IConstants.PO_VIEW_ID);
			v.setTText(gnum);

			GoalDisplayModifier display = new GoalDisplayModifier();
			display.whyToView(file);
			v.inputPO();

		} catch (PartInitException e) {
			TraceView.print(MessageType.ERROR, "ProverView.afficheGV() : " + e);
		} catch (IOException e) {
			TraceView.print(MessageType.ERROR, "ProverView.afficheGV() : " + e);
		}
	}

	/**
	 * Changes the slider draw in the view. It depends on items to show in the
	 * tree viewer (goals, expanded or not functions) and on the cursor.
	 */
	public void countLines() {

		int display;
		boolean pursuit = true;

		IPreferenceStore store = EditeurWHY.getDefault().getPreferenceStore();
		if (!store.getBoolean(IConstants.PREF_SHOW_ALL_LINES)) {
			display = store.getInt(IConstants.PREF_SHOW_NB_LINES);
		} else {
			display = 1;
			pursuit = false;
		}

		if (pursuit) {

			int i = FileInfos.functions.size() - 1;
			int res = 0;
			// we get the first function again visible in the view
			// when the slider is low
			bouclefor: while (i >= 0) {
				Function fctn = (Function) FileInfos.functions.get(i);
				if (!FileInfos.showOnlyUnprovedGoals
						|| (FileInfos.showOnlyUnprovedGoals && !fctn.isProved() /*
																				 * isChecked(
																				 * )
																				 */)) {
					if (fctn.isItem_expanded()) {
						if (FileInfos.showOnlyUnprovedGoals)
							res += fctn.getPo_unproved();
						else
							res += fctn.getPOList().size();
						res++;
					} else {
						res++;
					}
					if (res >= display) {
						break bouclefor;
					}
				}
				i--;
			}
			// we count now the number of expanded functions which
			// precede the previously recovered function
			int ne = 0;
			for (int y = 0; y < i; y++) {
				Function fctn = (Function) FileInfos.functions.get(y);
				if (!FileInfos.showOnlyUnprovedGoals
						|| (FileInfos.showOnlyUnprovedGoals && !fctn.isProved() /*
																				 * isChecked(
																				 * )
																				 */)) {
					if (fctn.isItem_expanded()) {
						ne++;
					}
				}
			}

			// we count the existing number of lines in the functions' tree
			int pp = 0;
			int e = FileInfos.functions.size();
			for (int a = 0; a < e; a++) {
				Function fctn = (Function) FileInfos.functions.get(a);
				if (!FileInfos.showOnlyUnprovedGoals
						|| (FileInfos.showOnlyUnprovedGoals && !fctn.isProved() /*
																				 * isChecked(
																				 * )
																				 */)) {
					if (fctn.isItem_expanded()) {
						if (FileInfos.showOnlyUnprovedGoals)
							pp += fctn.getPo_unproved();
						else
							pp += fctn.getPOList().size();
						pp++;
					} else {
						pp++;
					}
				}
			}
		}
	}

	/**
	 * Sets the ArrayList <code>goalsInView</code> and
	 * <code>functionsInView</code>. These object must contain respectively the
	 * numbers of all goals shown in the view and all functions items appearing
	 * in the tree viewer.
	 * 
	 * @param startGoal
	 *            the first goal (hidden or not) which belongs to the view
	 * @param nbLine
	 *            the number of lines to show in the view
	 */
	private void getGoals(int startGoal, int nbLine) {

		int i = 0;
		int g = startGoal;
		int max = FileInfos.numberOfGoals;
		int y;

		goalsInView.clear();
		functionsInView.clear();

		if (FileInfos.goals.size() == 0) {
			return;
		}

		PO po = (PO) FileInfos.goals.get(g - 1);
		y = po.getFnum();

		String f = "";

		// while we haven't exceed the nb of lines to show
		// and whie there are again goals
		while ((i <= nbLine) && (g <= max)) {

			PO op = (PO) FileInfos.goals.get(g - 1);

			// if we show all goals or if we show only unproved
			// goals and this goal's unproved
			if (!FileInfos.showOnlyUnprovedGoals
					|| (FileInfos.showOnlyUnprovedGoals && !op.isProved())) {
				String fn = op.getFname();
				if (!fn.equals(f)) { // unknown function : creates a new one
					f = fn;
					y = op.getFnum();
					i++;
					if (i <= nbLine + 1)
						functionsInView.add(new int[] { y });
				}
				// the function is expanded => we record the goal
				if ((i <= nbLine + 1)
						&& ((Function) FileInfos.functions.get(y - 1))
								.isItem_expanded()) {
					goalsInView.add(new int[] { g });
					i++;
					g++;
				}
				// else
				if ((i <= nbLine + 1)
						&& !((Function) FileInfos.functions.get(y - 1))
								.isItem_expanded()) {
					goalsInView.add(new int[] { g });
					if (showAllLines) { // we have either keep all goals
						g++;
					} else { // or search the next goal into the next function
						while ((g <= max)
								&& ((PO) FileInfos.goals.get(g - 1)).getFname()
										.equals(f)) {
							g++;
						}
					}
				}
			} else {
				// here, we go to search the next unproved goal
				while ((g <= max) && op.isProved()) {
					g++;
					if (g > max)
						break;
					op = (PO) FileInfos.goals.get(g - 1);
				}
			}
		}
	}

	/**
	 * Makes prover's statistics
	 * 
	 * @param proverNum
	 *            the prover number
	 */
	public synchronized void makeStats(int proverNum) {

		int goalNum = FileInfos.numberOfGoals;

		int state;
		int proved = 0;
		int invalid = 0;
		int unknown = 0;
		int timeout = 0;
		int failure = 0;

		// gets the number of proved, invalid, unknown, timeout and
		// failure results for all goals for this prover
		for (int g = 1; g <= goalNum; g++) {
			state = ((PO) FileInfos.goals.get(g - 1)).getState(proverNum);
			switch (state) {
			case 1:
				proved++;
				break;
			case 2:
				invalid++;
				break;
			case 3:
				unknown++;
				break;
			case 4:
				timeout++;
				break;
			case 5:
				failure++;
				break;
			default:
				break;
			}
		}

		// makes the statistics string
		String stats = "PROVED : " + proved + "/" + goalNum + "\n";
		stats += "INVALID : " + invalid + "/" + goalNum + "\n";
		stats += "UNKNOWN : " + unknown + "/" + goalNum + "\n";
		stats += "TIMEOUT : " + timeout + "/" + goalNum + "\n";
		stats += "FAILURE : " + failure + "/" + goalNum;

		// puts this results into a FileInfos field and
		// into the column tooltip text
		FileInfos.proverStats[proverNum] = stats;
		viewer.getColumn(proverNum + 2).setToolTipText(stats);
	}

	/**
	 * Displays a warning message if the number of lines to show in the view is
	 * higher than a maximum number fixed to 150
	 */
	public void checkLinesAndWarn() {

		IPreferenceStore store = EditeurWHY.getDefault().getPreferenceStore();
		boolean all = store.getBoolean(IConstants.PREF_SHOW_ALL_LINES);
		int max = IConstants.PREF_SHOW_NB_LINES_MAX;

		int lines;

		if (all) {
			lines = FileInfos.functions.size() + FileInfos.numberOfGoals + 1;
			if (lines > max) {
				MessageDialog.openWarning(new Shell(), "Overflow",
						"Beware, the number of goals and functions that will be displayed,\n"
								+ "estimated to " + lines
								+ ", exceeds the automatic line limitation\n"
								+ "of the proving view which is fixed to "
								+ max + ".\n"
								+ "This value can be manually defined in the\n"
								+ "'WHY' main preferences page\n");
				store.setValue(IConstants.PREF_SHOW_ALL_LINES, false);
				store.setValue(IConstants.PREF_SHOW_NB_LINES, max);
				EditeurWHY.getDefault().savePluginPreferences();
			}
		}
	}

}
