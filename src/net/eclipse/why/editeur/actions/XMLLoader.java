package net.eclipse.why.editeur.actions;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.eclipse.why.editeur.Context;
import net.eclipse.why.editeur.EditeurWHY;
import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.Function;
import net.eclipse.why.editeur.Goal;
import net.eclipse.why.editeur.IConstants;
import net.eclipse.why.editeur.PO;
import net.eclipse.why.editeur.WhyElement;
import net.eclipse.why.editeur.views.ProverView;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Giving a XML file, load all useful parameters and
 * create a past configuration of Prover View we wanted
 * to save
 * 
 * @author A. Oudot
 * @see XMLSaver
 */
public class XMLLoader extends DefaultHandler {

	ProverView view;
	private File file;
	
	private boolean inFunction = false;
	private boolean inLemma = false;
	private boolean inGoal = false;
	
	private Function func;
	private PO po;
	private int[] local;
	private int m, n, o;
	
	
	/**
	 * Class constructor.
	 * 
	 * @param w the current Prover View
	 */
	public XMLLoader(ProverView w) {
		view = w;
	}
	
	/**
	 * Creates and opens the main window which allows users to
	 * select the XML file to load.
	 */
	public void load() {

		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		String title = "Choose dump file to load proof obligations";
		FileDialog dlg = new FileDialog(shell, SWT.OPEN);
		dlg.setText(title);
		String path = null;
		while (path == null) {
			path = dlg.open();

			if (path == null)
				return;
			file = new File(path);
			if (file.exists()) {
				if (!file.canRead()) {
					final String msg = "Could not read the file " + path;
					MessageDialog.openError(shell, title, msg);
					path = null;
				}
			} else {
				final String msg = "File " + path + " doesn't exist";
				MessageDialog.openError(shell, title, msg);
				path = null;
			}
		}
		if (path != null) {
			parse();
		}
		return;
	}
	
	/**
	 * XML file parsing.
	 */
	private void parse() {
		try {
			IPreferenceStore store = EditeurWHY.getDefault().getPreferenceStore();
			boolean used = store.getBoolean(IConstants.PREF_DTD_USING_FILE);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(used); //do we use a .dtd file?
			SAXParser parser = factory.newSAXParser();
			parser.parse(file, this);
		} catch (ParserConfigurationException e) {
			TraceView.print(MessageType.ERROR, "XMLLoader.parse(), ParserConfigurationException : " + e);
		} catch (SAXException e) {
			TraceView.print(MessageType.ERROR, "XMLLoader.parse(), SAXException : " + e);
		} catch (IOException e) {
			TraceView.print(MessageType.ERROR, "XMLLoader.parse(), IOException : " + e);
		}
		//make Prover View elements after loading...
		FileInfos.initColumns();
		view.makeColumns();
		view.updateView();
	}
	
	/**
	 * If the parser encounters an error.
	 */
	public void error(SAXParseException e)
	throws SAXParseException {
		throw e;
	}
	
	/**
	 * Function called when the parser starts to parse the XML file.
	 */
	public void startDocument ()
	throws SAXException {
		//clean operations
		WhyElement.clean();
		Context.clean();
		Goal.clean();
    }
	
	/**
	 * Function called when the parser ends to parse the XML file.
	 */
	public void endDocument ()
	throws SAXException {
		Context.make();
		WhyElement.saveAsContext();
	}
	
	/**
	 * Function called when the parser encounters a particular
	 * XML start marker.
	 */
	public void startElement (String namespaceURI, String simpleName, String qualifiedName, Attributes attrs)
	throws SAXException {
		
		if(qualifiedName.equals("project")) { //						<project ...>
			FileInfos.reset();
			m = 0; //function number
			n = 1; //goal number
			o = 1; //goal row in its function
			for(int q=0; q<attrs.getLength(); q++) {
				if(attrs.getQName(q).equals("name")) {
					FileInfos.setFile(attrs.getValue(q)); //we get here the complete name of the source file
					FileInfos.complete();
					FileInfos.locateDoubleCharsInCFile();
				} else if(attrs.getQName(q).equals("context")) {
					/*do nothing*/
				}
			}
		} else if(qualifiedName.equals("function")) { //				<function ...>
			inFunction = true;
			func = new Function();
			m++;
			o = 1;
			for(int q=0; q<attrs.getLength(); q++) {
				if(attrs.getQName(q).equals("name")) {
					func.setName(attrs.getValue(q));
				}
			}
		} else if(qualifiedName.equals("lemma")) { //					<lemma ...>
			inLemma = true;
			func = new Function();
			func.putToLemma();
			m++;
			o = 1;
			for(int q=0; q<attrs.getLength(); q++) {
				if(attrs.getQName(q).equals("name")) {
					func.setName(attrs.getValue(q));
				}
			}
		} else if(qualifiedName.equals("location")) { //				<location ...>
			//inLocation = true;
			local = new int[3];
			String file = "";
			for(int q=0; q<attrs.getLength(); q++) {
				if(attrs.getQName(q).equals("file")) {
					file = attrs.getValue(q);
				} else if(attrs.getQName(q).equals("line")) {
					local[0] = Integer.parseInt(attrs.getValue(q));
				} else if(attrs.getQName(q).equals("begin")) {
					local[1] = Integer.parseInt(attrs.getValue(q));
				} else if(attrs.getQName(q).equals("end")) {
					local[2] = Integer.parseInt(attrs.getValue(q));
				}
			}
			if(inGoal) {
				getPO().setFile(file);
				getPO().setLoc(local);
			} else if(inFunction || inLemma) {
				func.setFile(file);
				func.setLoc(local);
			}
		} else if(qualifiedName.equals("behavior")) { //				<behavior ...>
			//inBehavior = true;
			for(int q=0; q<attrs.getLength(); q++) {
				if(attrs.getQName(q).equals("name")) {
					func.setBehavior(attrs.getValue(q));
				}
			}
		} else if(qualifiedName.equals("goal")) { //					<goal ...>
			if(!inGoal) {
				inGoal = true;
				po = new PO();
				po.setFnum(m);
				po.setNum(n);
				po.setNum_in_f(o);
				po.setName("" + o);
				po.initStates();
				func.addPO(po);
				n++;
				o++;
			}
			for(int q=0; q<attrs.getLength(); q++) {
				if(attrs.getQName(q).equals("why_file")) {
					/*do nothing*/
				}
			}
		} else if(qualifiedName.equals("explain")) { //					<explain ...>
			//inExplain = true;
			for(int q=0; q<attrs.getLength(); q++) {
				String v = attrs.getValue(q);
				if(attrs.getQName(q).equals("kind")) {
					getPO().setKind(v);
				} else if(attrs.getQName(q).equals("text")) {
					getPO().setText(v);
				}
			}
			//we can make a goal title with the 'kind' and 'text' fields
			getPO().makeTitle();
		} else if(qualifiedName.equals("proof")) { //					<proof ...>
			//inProof = true;
			int rpnum = -1; //the prover number
			int stnum = 0; //goal state
			for(int q=0; q<attrs.getLength(); q++) {
				String a = attrs.getValue(q);
				if(attrs.getQName(q).equals("prover")) {
					//we search the row of the prover actually defined in
					//the Prover View columns which names corresponds to
					//the prover used to prove the goal.
					for(int z=0; z<FileInfos.provers.length; z++) {
						if(FileInfos.provers[z].equals(a)) {
							rpnum = z;
						}
					}
				} else if(attrs.getQName(q).equals("status")) {
					if(a.equals("valid")) {
						stnum = 1;
					} else if(a.equals("invalid")) {
						stnum = 2;
					} else if(a.equals("unknown")) {
						stnum = 3;
					} else if(a.equals("timeout")) {
						stnum = 4;
					} else if(a.equals("failure")) {
						stnum = 5;
					} else if(a.equals("running")) {
						/*do nothing*/
					}
				} else if(attrs.getQName(q).equals("timelimit")) {
					/*do nothing*/
				} else if(attrs.getQName(q).equals("date")) {
					/*do nothing*/
				} else if(attrs.getQName(q).equals("scriptfile")) {
					/*do nothing*/
				}
			}
			//if the prover used ages ago exists, and if a proving
			//action has been recorded, the result has to be saved!
			if(rpnum >= 0 && stnum > 0) {
				getPO().changeStateValue(rpnum, stnum);
			}
		}
	}
	
	/**
	 * Function called when the parser encounters a particular
	 * XML end marker.
	 */
	public void endElement (String namespaceURI, String simpleName, String qualifiedName)
	throws SAXException {
		
		if(qualifiedName.equals("function")) { //			</function>
			FileInfos.functions.add(func);
			inFunction = false;
		} else if(qualifiedName.equals("lemma")) { //		</lemma>
			FileInfos.functions.add(func);
			inLemma = false;
		} else if(qualifiedName.equals("goal")) { //		</goal>
			if(inGoal){
				FileInfos.goals.add(po);
				inGoal = false;
			}
		} 
	}
	
	/**
	 * Current PO getter
	 * 
	 * @return the PO
	 */
	private PO getPO() {
		if(inGoal) return po;
		else return null;
	}
}
