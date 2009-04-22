package net.eclipse.why.editeur.actions;

import net.eclipse.why.editeur.Context;
import net.eclipse.why.editeur.Goal;
import net.eclipse.why.editeur.WhyElement;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * The job to generate conditions for verification
 */
public class ConditionGeneratorJob extends WorkspaceJob {

	private VerificationToolExecutor exec;

	public ConditionGeneratorJob(VerificationToolExecutor evt) {
		super("Verification Conditions Generation");
		exec = evt;
		return;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor)
			throws CoreException {
		boolean result = false;

		try {

			monitor.beginTask("Cleaning up", 3);

			WhyElement.clean();
			Context.clean();
			Goal.clean();
			
			monitor.worked(1);
			monitor.setTaskName("Static analysis");
			
			result = exec.exec();
			
			monitor.worked(1);
			monitor.setTaskName("Processing results");

			if (result) {
				Context.make();
				WhyElement.saveAsContext();
				InfosMaker infM = new InfosMaker();
				infM.make();
				monitor.worked(1);
			} else {
				String error = exec.getError();
				if (error != null) {
					Highlightor.selectError(error);
				}
			}
			monitor.done();
		} catch (Exception e) {
			TraceView.print(MessageType.ERROR, "VCGThread.run() : " + e);
		}
		return Status.OK_STATUS;
	}
}
