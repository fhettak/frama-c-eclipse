package net.eclipse.why.editeur.actions;

import net.eclipse.why.editeur.views.ProverView;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.swt.SWTException;
import org.eclipse.ui.PartInitException;


/**
 * Class containing ProverView's update methods
 * 
 * @author A.Oudot
 */
public class ProverViewUpdater {
	
	
	ProverView view; //the instantiated ProverView
	
	/**
	 * Class Constructor
	 * 
	 * @param v the Prover View to update
	 */
	public ProverViewUpdater(ProverView v) {
		this.view = v;
	}
	
	/**
	 * Function which call the function of ProverView which
	 * changes the image and the color of a given button
	 * (line=goal; column=prover)
	 * 
	 * @param goal the goal number
	 * @param prover the prover number
	 * @throws PartInitException
	 */
	public synchronized void update(int goal, int prover) throws PartInitException {
		try {
			view.updateElementAt(goal, prover);
		} catch(SWTException swt) {
			TraceView.print(MessageType.ERROR, "ProverView.updateElementAt(" + goal + "," + prover + ") : " + swt);
		}
	}
	
	/**
	 * Function which call the function of ProverView which
	 * changes the image or the color of a button to signal
	 * that a prover is working on it.
	 * 
	 * @param goal the goal number
	 * @param prover the prover number
	 * @throws PartInitException
	 */
	public synchronized void color(int goal, int prover) throws PartInitException {
		view.working(goal);
	}
	
	/**
	 * Function which call the function of ProverView which
	 * enables or disables the 'kill' button
	 * 
	 * @param activate true => enable, false => disable
	 * @throws PartInitException
	 */
	public synchronized void activateKillProcessButton(boolean activate) throws PartInitException {
		view.killButton(activate);
	}
	
	/**
	 * Function which call the function of ProverView which
	 * removes an executed thread from the thread list
	 * 
	 * @param id the id of the thread
	 */
	public synchronized void removeThread(long id) {
		view.removeThread(id);
	}
	
	/**
	 * Function which call the function of ProverView which
	 * updates statistics of a prover
	 * 
	 * @param prover the prover number
	 * @throws PartInitException
	 */
	public synchronized void stats(int prover) throws PartInitException {
		view.makeStats(prover);
	}
}
