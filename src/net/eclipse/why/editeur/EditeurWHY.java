package net.eclipse.why.editeur;

import net.eclipse.why.editeur.IConstants;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class EditeurWHY extends AbstractUIPlugin {

	//The shared instance.
	private static EditeurWHY plugin;


	/**
	 * The constructor.
	 */
	public EditeurWHY() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static EditeurWHY getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(IConstants.PLUGIN_ID, path);
	}
	
	
	
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		
		// Number of lines to show in the Prover View
		store.setDefault(IConstants.PREF_SHOW_NB_LINES, IConstants.PREF_SHOW_NB_LINES_DEFAULT_VALUE);
		store.setDefault(IConstants.PREF_SHOW_ALL_LINES, IConstants.PREF_SHOW_ALL_LINES_DEFAULT_VALUE);
		
		//List of default commands
		store.setDefault(IConstants.PREF_LIST_OF_COMMANDS, IConstants.PREF_LIST_OF_COMMANDS_DEFAULT_VALUE);
		store.setDefault(IConstants.PREF_LIST_OF_PROOF_COMMANDS, IConstants.PREF_LIST_OF_PROOF_COMMANDS_DEFAULT_VALUE);
		store.setDefault(IConstants.PREF_LIST_OF_RECOGNIZED_FILES, IConstants.PREF_LIST_OF_RECOGNIZED_FILES_DEFAULT_VALUE);
		
		//Files cleaning option
		store.setDefault(IConstants.PREF_CLEAN_FILES1, IConstants.PREF_CLEAN_FILES1_DEFAULT_VALUE);
		
		//run commands
		store.setDefault(IConstants.PREF_RUN_OPTIONS, IConstants.PREF_RUN_OPTIONS_NORMAL_MODE);
				
		//options concerning .dtd file
		store.setDefault(IConstants.PREF_DTD_USING_FILE, IConstants.PREF_DTD_USING_FILE_DEFAULT_VALUE);
		store.setDefault(IConstants.PREF_DTD_FILE_LOCATION, IConstants.PREF_DTD_FILE_LOCATION_DEFAULT_VALUE);
	}
}
