package net.eclipse.why.editeur.properties;

import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;


/**
 * The source file Property Page
 * 
 * @author A. Oudot
 */
public class WhyPropertyPage extends PropertyPage {
	
	
	//option buttons = options defined for verification tools commands
	private Button separation, split, overflow, fastWP;
	

	/**
	 * Class constructor
	 */
	public WhyPropertyPage() {
		super();
	}
	
	
	protected Control createContents(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		
		//text to explain things
		Label lbl = new Label(composite, SWT.NONE);
		lbl.setText("Check options you want to use with Frama-C, Krakatoa \n" +
					"and Jessie Tools");

		
		Group group = new Group(composite, SWT.NONE);
		group.setLayout(new GridLayout());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		group.setText("Options");
		
		
		Composite c = new Composite(group, SWT.NONE);
		c.setLayout(new GridLayout());
		
		//Check button to use or not the "separation" option
		separation = new Button(c, SWT.CHECK);
		separation.setText("separation");
		
		//Check button to use or not the "split" option
		split = new Button(c, SWT.CHECK);
		split.setText("split");
		
		//Check button to use or not the "overflow" option
		overflow = new Button(c, SWT.CHECK);
		overflow.setText("overflow");
		
		//Check button to use or not the "fastWP" option
		fastWP = new Button(c, SWT.CHECK);
		fastWP.setText("fast WP");
		
		//get prefered values
		try {
			IAdaptable adaptable = (IAdaptable) getElement();
			IResource resource = (IResource) adaptable.getAdapter(IResource.class);
			String sopt = resource.getPersistentProperty(new QualifiedName("", IConstants.PROP_WHYOPT));
			if(sopt == null) sopt = IConstants.PROP_WHYOPT_DEFAULT;
			separation.setSelection(sopt.substring(0,1).equals("1"));
			split.setSelection(sopt.substring(1,2).equals("1"));
			overflow.setSelection(sopt.substring(2,3).equals("1"));
			fastWP.setSelection(sopt.substring(3).equals("1"));
		} catch (Exception e) {
			TraceView.print(MessageType.ERROR, "Error reading properties : " + e);
		}
		
		return composite;
	}
	
	
	protected void performDefaults() {
		//get the default values
		String sopt = IConstants.PROP_WHYOPT_DEFAULT;
		separation.setSelection(sopt.substring(0,1).equals("1"));
		split.setSelection(sopt.substring(1,2).equals("1"));
		overflow.setSelection(sopt.substring(2,3).equals("1"));
		fastWP.setSelection(sopt.substring(3).equals("1"));
	}

	
	public boolean performOk() {
		
		String sopt = "";
		
		//make the Preference variable to save
		sopt += separation.getSelection() ? "1" : "0";
		sopt += split.getSelection() ? "1" : "0";
		sopt += overflow.getSelection() ? "1" : "0";
		sopt += fastWP.getSelection() ? "1" : "0";
		
		try {
			IAdaptable adaptable = (IAdaptable) getElement();
			IResource resource = (IResource)adaptable.getAdapter(IResource.class);
			resource.setPersistentProperty(new QualifiedName("", IConstants.PROP_WHYOPT), sopt);
		} catch (CoreException e) {
			TraceView.print(MessageType.ERROR, "Error saving properties : " + e);
			return false;
		}
		return true;
	}
}
