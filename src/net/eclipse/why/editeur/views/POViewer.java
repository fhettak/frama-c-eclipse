package net.eclipse.why.editeur.views;

import java.io.IOException;
import java.util.Vector;

import net.eclipse.why.editeur.Context;
import net.eclipse.why.editeur.Goal;
import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;


/**
 * The viewer of .why file : goals and context pretty print
 * 
 * @author A. Oudot
 */
public class POViewer extends ViewPart {
	
	
	private StyledText text; //the text
	private Action seeContext; //see the context file
	private Action seePO; //see the selected goal file
	private boolean POMode; //true when we see a goal in the view, false for a context
	private int ttext; //the view title
	
	
	/**
	 *  Class constructor
	 */
	public POViewer() {
		super();
		POMode = true;
		ttext = -1;
	}
	
	
	/**
	 * Set a Title for the view : this text is
	 * generally a goal number which will be showed
	 * with the view's name
	 * 
	 * @param a
	 */
	public void setTText(int a) {
		ttext = a;
	}

	
	public void createPartControl(Composite parent) {
		text = new StyledText(parent, SWT.H_SCROLL | SWT.V_SCROLL);		
		makeActions();
		contributeToActionBars();
	}

	
	public void setFocus() {
		text.setFocus();
	}
	
	
	/**
	 * The viewer getter
	 * 
	 * @return StyledText
	 */
	public StyledText getViewer() {
		return this.text;
	}
	
	
	/**
	 * Makes the actions of the view
	 *
	 */
	private void makeActions() {
		
		seeContext = new Action() {
			public void run() {
				seePO.setChecked(false); //uncheck the opposite button
				POMode = false; //put the PO mode to false
				try {
					inputCTX(); //prompt the context
				} catch (IOException e) {
					TraceView.print(MessageType.ERROR, "POViewer, can't display the CTX file : " + e);
				}
			}
		};
		seeContext.setChecked(false); //PO viewer by default
		seeContext.setText("CTX");
		
		seePO = new Action() {
			public void run() {
				seeContext.setChecked(false); //uncheck the opposite button
				POMode = true; //put the PO mode to true
				try {
					inputPO(); //prompt the selected goal
				} catch (IOException e) {
					TraceView.print(MessageType.ERROR, "POViewer, can't display the PO file : " + e);
				}
			}
		};
		seePO.setChecked(true); //PO viewer by default
		seePO.setText("PO");
	}
	
	
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(seePO);
		manager.add(seeContext);
	}
	
	
	/**
	 * Inputs in the view the selected PO whose pretty code
	 * is recorded in the Goal class
	 * 
	 * @throws IOException 
	 */
	public void inputPO() throws IOException {
		
		//we want to see a context, not the goal
		if(!POMode) {
			return;
		}
		
		if(ttext > 0) //if we've a title text
			setPartName(IConstants.PO_VIEW_TITLE + " : po " + ttext); //put it in the view
		else
			setPartName(IConstants.PO_VIEW_TITLE); //else, put the view title only
		
		//gets the PO pretty text
		String container = "";
		container = Goal.getGoal();
		
		//if the Goal is empty, we can return
		if(container.equals("")) {
			text.setText(container);
			return;
		}
		
		//get the style ranges
		Vector<StyleRange> range = Goal.getStyleRanges();
		int w = range.size()-1;
		//keep the ranges which can be applied in the text area
		while( ((StyleRange)range.get(w)).start >= container.length()) {
			w--;
		}
		
		//put the bar between the goal and the arrow result excerpted from this same goal
		container += "\n_______________________________________________\n\n";
		container += Goal.getResult();
		text.setText(container);
		
		//apply style ranges to the first part of the goal (before the bar)
		for(int e=0; e<=w; e++) {
			text.setStyleRange((StyleRange)range.get(e));
		}
		
		//puts a style range to color the bar in black
		StyleRange srg = new StyleRange();
		srg.start += Goal.getGoal().length();
		srg.length = 50;
		srg.foreground = new Color(null, 0, 0, 0);
		text.setStyleRange(srg);
		
		//puts a style range to color the end of the goal in blue
		srg = new StyleRange();
		srg.start += Goal.getGoal().length() + 50;
		srg.length = Goal.getResult().length();
		srg.foreground = new Color(null, 0, 0, 255);
		text.setStyleRange(srg);
		
		//sets the cursor at the end of the code
		text.setSelection(text.getText().length(),text.getText().length());
	}
	
	
	/**
	 * Inputs in the view the context file whose pretty code
	 * is recorded in the Context class
	 * 
	 * @throws IOException 
	 */
	public void inputCTX() throws IOException {
		
		//we want to see a goal, not the context
		if(POMode) {
			return;
		}
				
		// no tooltip text and no goal number to show in the title
		setTitleToolTip(null);
		setPartName(IConstants.PO_VIEW_TITLE);
		
		//shows the context text
		text.setText(Context.getContext());
		
		//with style ranges
		Vector<StyleRange> range = Context.getStyleRanges();
		if(range!=null) {
			for(int e=0; e<range.size(); e++) {
				text.setStyleRange((StyleRange)range.get(e));
			}
		}
	}
		
	/**
	 * Update the view : check new colors and print the
	 * Context file or the selected PO.
	 */
	public void update() {
		try {
			if(POMode) {
				inputPO();
			} else {
				inputCTX();
			}
		} catch(IOException io) {
			TraceView.print(MessageType.ERROR, "POViewer.update() : " + io);
		}
	}
}
