package net.eclipse.why.editeur.menu;

import net.eclipse.why.editeur.IConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Menu for right-click actions on Prover View's goals' buttons
 * 
 * @author oudot
 */
public class BtnMenu extends Menu {

	/**
	 * Menu constructor for right-click action
	 * on buttons of Prover View.
	 * 
	 * @param parent the parent Composite
	 */
	public BtnMenu(Control parent) {
		super(parent);
		
		MenuItem item1 = new MenuItem(this, SWT.NONE);
		item1.setText("validate");
		item1.setImage(IConstants.IMAGE_BALL_GREEN);
		item1.setData("admit", new Integer(1));
		
		MenuItem item2 = new MenuItem(this, SWT.NONE);
		item2.setText("invalidate");
		item2.setImage(IConstants.IMAGE_BALL_RED);
		item2.setData("admit", new Integer(2));
		
		//new Separator(IWorkbenchActionConstants.MB_ADDITIONS);
		new MenuItem(this, SWT.SEPARATOR);
		
		MenuItem item3 = new MenuItem(this, SWT.NONE);
		item3.setText("init");
		item3.setImage(IConstants.IMAGE_BALL_WHITE);
		item3.setData("admit", new Integer(0));
	}
	
	
	/**
	 * Suppress the subclassing exception overriding the
	 * Widget method and doing nothing into it
	 */
	protected void checkSubclass() {
		//do nothing!
	}
	
}
