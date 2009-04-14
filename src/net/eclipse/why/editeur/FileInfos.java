package net.eclipse.why.editeur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;

import net.eclipse.why.editeur.actions.TraceDisplay;
import net.eclipse.why.editeur.actions.TraceDisplay.MessageType;

/**
 * Class used to store all the global informations
 * needed during the plugin execution
 * 
 * @author A. Oudot
 */
public class FileInfos {
	
	
	volatile private static File program; //Source file
	volatile private static String rootDir = ""; //Source file directory
	volatile private static String commonFileName = ""; //Common part name of all generated .why files
	
	public static ArrayList<int[]> doubleCharsInCFile = new ArrayList<int[]>(); //location of '\r\n' in the source file
	public static ArrayList<PO> goals = new ArrayList<PO>(); //POs
	public static ArrayList<Function> functions = new ArrayList<Function>(); //Functions
	volatile public static int whyFileNumber = 0; //Nb of generated .why files
	volatile public static int endHighlightChar = 0; //character number where cursor is in main editor
	public static String[] provers = new String[]{("")}; //Prover names
	public static String[] status = new String[]{("")}; //Status of provers (assistant or prover)
	public static String[] commands = new String[]{""}; //Commands by prover
	public static String[] proverStats = new String[]{("")}; //Provers statistics
	volatile public static boolean showOnlyUnprovedGoals = false; //'Show only unproved POs' option
	volatile public static boolean images_mode = false; //Color(by default) or image mode
	volatile public static int markedGoal = 0; //Number of the marked PO, if exists...
	
	
	/**
	 * Reset, init, clean all fields
	 */
	public static void reset() {
		doubleCharsInCFile.clear();
		functions.clear();
		goals.clear();
		rootDir = "";
		commonFileName = "";
		whyFileNumber = 0;
		endHighlightChar = 0;
		showOnlyUnprovedGoals = false;
		markedGoal = 0;
	}
	
	/**
	 * Sets source file
	 * 
	 * @param file the file name and location
	 */
	public static void setFile(String file) {
		program = new File(file);
	}
	
	/**
	 * Gets the common part of files names
	 * 
	 * @return the common part of files names
	 */
	public static String getName() {
		return commonFileName;
	}
	
	/**
	 * Gets the full name (directory + name + extension) of the source file
	 * 
	 * @return the full name of the source file
	 */
	public static String getFullName() {
		return program.getPath();
	}
	
	/**
	 * Gets the source file directory
	 * 
	 * @return the source file directory
	 */
	public static String getRoot() {
		return rootDir;
	}
	
	
	/**
	 * Initialisation of provers from the Preferences Page
	 */
	public static void initProvers() {
		
		provers = EditeurWHY.getDefault().getPreferenceStore().getString(IConstants.PREF_LIST_OF_PROOF_COMMANDS).split(IConstants.LINE_SEPARATOR);
		int l = provers.length;
		
		//Ici on récupère le noms de chacun des prouveurs ainsi que l'extension
		//correspondante et l'exécutable s'il existe!
		//We get here the names of each prover with his extension and the
		//executable file name, if it exists!
		if( l > 1 || (l == 1 && !(provers[0].trim().equals("")) ) ) {
			status 			= new String[l];
			commands		= new String[l];
			proverStats 	= new String[l];
			
			for(int t=0; t<l; t++) {
				String[] str = provers[t].split(IConstants.ELEMENT_SEPARATOR);
				status[t] 			= str[0].trim();
				provers[t] 			= str[1].trim();
				commands[t]			= "";
				int x = 2;
				while(x < str.length) {
					commands[t] += (str[x]+"\n");
					x++;
				}
				proverStats[t] 		= "";
			}
		}
	}
	
	/**
	 * Knowing the source file, this function builds
	 * few other fields of the class.
	 */
	public static void complete() {
		if(program != null) {
			String commonName = program.getName();
			commonFileName = commonName.substring(0, commonName.lastIndexOf("."));
			rootDir = program.getParent();
			
			if(!rootDir.endsWith(File.separator)) {
				rootDir += File.separator;
			}
			rootDir += commonFileName + ".jessie" + File.separator;
		}
	}
	
	/**
	 *  Update of provers in case of modifications (adds or removes) of the
	 *  Preferences Page
	 */
	public static void initColumns() {
		
		//Check of Preferences
		String[] newProvers = EditeurWHY.getDefault().getPreferenceStore().getString(IConstants.PREF_LIST_OF_PROOF_COMMANDS).split(IConstants.LINE_SEPARATOR);
		
		
		int l1 = provers.length;
		int l2 = newProvers.length;
		boolean equals = true;
		
		String[] newStatus 			= new String[]{("")};
		String[] newCommands		= new String[]{""};
		String[] newProverStats 	= new String[]{("")};
		
		
		//Ici on récupère le noms des nouveaux prouveurs ainsi que les extensions
		//correspondantes ainsi que l'exécutable s'il existe !
		//We get here the names of each new prover with his extension and the
		//executable file name, if it exists!
		if(l2 > 1 || (l2 == 1 && !newProvers[0].trim().equals("")) ) {
			newStatus 			= new String[l2];
			newCommands			= new String[l2];
			newProverStats 		= new String[l2];
			
			for(int t=0; t<l2; t++) {
				String[] str = newProvers[t].split(IConstants.ELEMENT_SEPARATOR);
				newStatus[t] 			= str[0].trim();
				newProvers[t] 			= str[1].trim();
				newCommands[t]			= "";
				int x = 2;
				while(x < str.length) {
					newCommands[t] += (str[x]+"\n");
					x++;
				}
				newProverStats[t] 		= "";
			}
		}
		
		
		//Comparaison anciens prouveurs - Nouveaux prouveurs !
		//We compare old and new provers
		if(l1 != l2) {
			equals = false; //not the same number of provers
		} else {
			for(int e=0; e<l2; e++) {
				if( !status[e].equals(newStatus[e]) ||
					!provers[e].equals(newProvers[e]) ||
					!commands[e].equals(newCommands[e])
				  ) {
					equals = false; //one of provers at least has been modified
					break;
				}
			}
		}
		
		//If modifications appeared
		if(!equals) {
			int[] permutations_tab = new int[l2];
			for(int o=0; o<l2; o++) {
				permutations_tab[o] = -1;
			}
			
			//On compare un à un les anciens prouveurs aux nouveaux en utilisant leur nom !
			//On estime qu'un prouveur s'appelant 'prout-prout-tagada' avant et après modif
			//reste le même ...
			//We compare old and new provers using their names! Indeed we consider that
			//a prover named 'alt-ergo', a total random named prover not to make advertising,
			//before and after having been modified is the same one...
			for(int r=0; r<l2; r++) {
				
				for(int o=0; o<l1; o++) {
					//si donc un prouveur existait, on récupère les infos sur les états pour les
					//placer dans le nouveau tableau !
					//if the prover was defined, we get all corresponding states fields
					if(provers[o].equals(newProvers[r])) {
						for(int v=0; v<whyFileNumber; v++) {
							permutations_tab[r] = o;
						}
						newProverStats[r] = proverStats[o];
					}
				}
			}
			
			//Reste à remplacer les anciennes valeurs par les nouvelles ...
			//We subsitute old values by new values
			provers = newProvers;
			status = newStatus;
			commands = newCommands;
			proverStats = newProverStats;
			
			//We make necessary switches in PO baord
			for(int d = 0; d<goals.size(); d++) {
				((PO)goals.get(d)).pswitch(permutations_tab);
			}
			
		}
		
		
		//	   A préciser : cette fonction opère les changements quel que soit le
		//   nombre des nouveaux prouveurs par apport aux anciens (<, = ou >), effectue
		//   les changement idoines en cas de simple inversion des prouveurs dans les
		//   Préférences et, bien sûr, en cas d'ajout ou de suppression !
	}
	
	/*
	 * Création d'un répertoire pour chacun des prouveurs. Les buts exprimés dans le langage
	 * de ces prouveurs seront placés dans les répertoires créés correspondament.<BR>
	 *
	private static void createProverDirectories() {
		
		if(program != null) {
			
			int l = provers.length;
		
			if(l > 1 || (l == 1 && !(provers[0].trim().equals("")) ) ) {
			
				for(int v=0; v<l; v++) {
					String destDir = rootDir + FileInfos.provers[v].toLowerCase();
					File directory = new File(destDir);
		
					if(!directory.isDirectory()) {
						directory.mkdir();
						TraceDisplay.printMsg("Directory " + destDir + " created");
					}
				}
			}
		}
	}/**/
	
	/**
	 * This function completes the ArrayList object <code>doubleCharsInCFile</code>.
	 * It contains locations of all "\r\n" strings which are in the source file. 
	 */
	public static void locateDoubleCharsInCFile() {
		
		doubleCharsInCFile = new ArrayList<int[]>();
		
		try {
			FileReader fread = new FileReader(program);
			InputStream is = new FileInputStream(program);
			
			char[] doubleCh = new char[is.available()];
			fread.read(doubleCh);
			
			int leng = (doubleCh.length)-1;
			for(int k=0; k<leng; k++) {
				if(doubleCh[k]=='\r' && doubleCh[k+1]=='\n') {
					int[] m = new int[1];
					m[0] = k+1;
					doubleCharsInCFile.add(m);
				}
			}
			fread.close();
			is.close();
			
		} catch(Exception e) {
			TraceDisplay.print(MessageType.ERROR, "FileInfos.locateDoubleCharsInCFile() : " + e);
		}
	}
	
	/**
	 * Compute highlight zones locations which depends on positions
	 * of "\r\n" strings in the source file.
	 * 
	 * @param mark nb of characters before lines to highlight
	 * @param h1 charcter of the line where starts the highlight
	 * @param h2 charcter of the line where stops the highlight
	 * @return the three new integer
	 */
	public static int[] adjustHighLightZone(int mark, int h1, int h2) {
		
		int k = 0;
		int[] res = new int[3];
		
		int H1 = h1;
		int H2 = h2;
		
		while(k<doubleCharsInCFile.size() && doubleCharsInCFile.get(k)!=null && mark>((int[])(doubleCharsInCFile.get(k)))[0]) {
			k++;
			mark++;
		}
		res[0] = mark;
		while(k<doubleCharsInCFile.size() && doubleCharsInCFile.get(k)!=null && mark+H1>((int[])(doubleCharsInCFile.get(k)))[0]) {
			k++;
			h1++;
		}
		res[1] = h1;
		while(k<doubleCharsInCFile.size() && doubleCharsInCFile.get(k)!=null && mark+H2>((int[])(doubleCharsInCFile.get(k)))[0]) {
			k++;
			h2++;
		}
		res[2] = h2;
		return res;
	}
}
