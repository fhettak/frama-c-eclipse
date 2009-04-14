package net.eclipse.why.editeur.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.Function;
import net.eclipse.why.editeur.PO;
import net.eclipse.why.editeur.actions.TraceDisplay.MessageType;

/**
 * Makes all PO and Function objects in FileInfos class.
 * 
 * @author A. Oudot
 */
public class InfosMaker {
	
	
	private String tag;
	private String LTAG = "[Lemma]";
	
	/**
	 * Class constructor
	 */
	public InfosMaker() {
		this.tag = "";
	}
	
	/**
	 * Main function to make PO and Function objects into the FileInfos class
	 * reading .xpl or .why files from the why/ repository .
	 * 
	 * @return true
	 */
	public boolean make() {
		
		int m = 1;
		this.tag = "";
		
		
		while(isFileExists(m, 0, ".why")) {
			if(isFileExists(m, 0, ".xpl")) {
				setNewPO(m); //make the PO from xpl file
			} else {
				setNewPO2(m); //make the PO from 
			}
			int n = 1;
			//make subgoals
			while(isFileExists(m, n, ".why")) {
				int size = FileInfos.goals.size();
				((PO)FileInfos.goals.get(size-1)).addSubGoal();
				n++;
			}
			m++;
		}
		
		//the number of goals
		FileInfos.whyFileNumber = m-1;
		
		int N = 1;
		//the first PO field of each created Function object
		for(int v=0; v<FileInfos.functions.size(); v++) {
			Function function = (Function)FileInfos.functions.get(v);
			function.setFirst_po(N);
			N += function.getPo();
		}
		
		return true;
	}
	
	
	/**
	 * Make a new Function() object from .loc file
	 * and record it in the FileInfos class
	 * 
	 * @param tag the title tag of the function
	 * @param is_lemma true if the function is a lemma
	 * @return true if the name or the behavior of this new function have been found
	 */
	private boolean setNewFunction(String tag, boolean is_lemma) {
		
		String name = null;
		String behavior = null;
		boolean r = false;
		
		String fname = "";
		String ffile = "";
		int [] floc = new int[3];
		
		//get the .loc file
		String fileX = FileInfos.getRoot() + FileInfos.getName() + ".loc";
		FileReader fr;
		BufferedReader in;
		
		try {
			fr = new FileReader(fileX);
			in = new BufferedReader(fr);
			String l;
			boolean into = false;
			
			//reading .loc file
			while( (l = in.readLine()) != null ) {
				
				//the starting tag has been read and here is a blank line => Stop!
				if(into && l.trim().equals("")) {
					break;
				}
				
				//reading the starting tag
				if(l.trim().equals(tag)) {
					into = true;
				}
				
				//the starting tag has been read
				if(into) {
					if(l.startsWith("name =")) { //here is the function's name
						name = getMotif(l.substring(6));
						r = true;
					}
					if(l.startsWith("behavior =")) { //here is the function's behavior
						behavior = getMotif(l.substring(10));
						r = true;
					}
					if(l.startsWith("file =")) { //here is the function's source file
						ffile = getMotif(l.substring(6));
					}
					if(l.startsWith("line =")) { //here is the function's code location
						floc[0] = Integer.parseInt(l.substring(6).trim());
					}
					
					if(l.startsWith("begin =")) { //here is the function's code location
						floc[1] = Integer.parseInt(l.substring(7).trim())+1;
					}
					
					if(l.startsWith("end =")) { //here is the function's code location
						floc[2] = Integer.parseInt(l.substring(5).trim());
					}
				}
			}
			
			//make the behavior text
			if(behavior==null || behavior.trim().equals("")) {
				if(name == null || name.trim().equals(""))
					fname = "";
				else
					fname = name;
			} else {
				if(name == null || name.trim().equals(""))
					fname = behavior;
				else
					fname = behavior + " of " + name;
			}
			
			in.close();
			fr.close();
			
		} catch (FileNotFoundException f) {
			TraceDisplay.print(MessageType.WARNING, "File " + fileX + " doesn't exist");
		} catch (IOException io) {
			TraceDisplay.print(MessageType.ERROR, "InfosMaker.getLocFunctionName() : " + io);
		}
		
		//here is the creation of the new Function() object :
		Function function = new Function();
		function.setName(tag.substring(1, tag.length()-1));
		if(is_lemma) function.putToLemma();
		function.setBehavior(fname);
		function.setFile(ffile);
		function.setLoc(floc);
		FileInfos.functions.add(function);
		
		return r;
	}
	
	
	/**
	 * Make a new Function() object from tag text
	 * and record it in the FileInfos class
	 * 
	 * @param tag the title tag of the function
	 * @param is_lemma true if the function is a lemma
	 * @return true
	 */
	private boolean setNewFunction2(String tag, boolean is_lemma) {
		
		Function function = new Function();
		function.setName(tag.substring(1, tag.length()-1));
		if(is_lemma) function.putToLemma();
		FileInfos.functions.add(function);
		
		return true;
	}
	
	
	/**
	 * Record a new PO object in the FileInfos class
	 * from the description into the .xpl file.
	 * 
	 * @param m the PO number
	 * @return false if this function fails
	 */
	private boolean setNewPO(int m) {
		
		String l, t;
		int index;
		int mprim = 0;
		int f = FileInfos.functions.size();
		
		boolean r = true;
		
		try {
			
			String	kind=null, text=null, file=null, motif=null;
			int		line=0, char1=0, char2=0;
			boolean lemma = false;
			
			//get the .xpl file which corresponds to the goal
			String fileX = FileInfos.getRoot() + "why" + File.separator + FileInfos.getName() + "_po" + m + ".xpl";
			FileReader fr = new FileReader(fileX);
			BufferedReader in = new BufferedReader(fr);
			
			//first line of the file => fonction's name
			l = in.readLine();
			int l1 = l.lastIndexOf("_po_");
			//we get the function's name between '[' and ']'
			if( l1 >= 0 ) {
				t = l.substring(0, l1) + "]";
			} else if(  l.startsWith("[") && l.lastIndexOf("]") > 0 ) {
				t = l.substring(0, l.lastIndexOf("]")+1);
				lemma = true;
			} else { //if this name doesn't exist, we create it using the goal's .why file
				String fileY = FileInfos.getRoot() + "why" + File.separator + FileInfos.getName() + "_po" + m + ".why";
				FileReader rf = new FileReader(fileY);
				BufferedReader ni = new BufferedReader(rf);
				String ll = ni.readLine();
				int ll1 = ll.lastIndexOf("_po_");
				if( ll1 >= 0 ) {
					t = "[" + ll.substring(5, ll1) + "]";
				} else {
					int ll2 = ll.lastIndexOf(":");
					if(ll2 >= 0)
						t = "[" + ll.substring(5, ll2) + "]";
					else //this is a lemma!
						t = LTAG;
					lemma = true;
				}
			}
			
			
			//if the function found is unknown, we create it :
			if(!t.equals(tag) || lemma) {
				this.tag = t;
				if(isFileExists(0, 0, ".loc")) {
					setNewFunction(tag, lemma);
				} else {
					setNewFunction2(tag, lemma);
				}
				f++;
				mprim = 1;
			} else {
				mprim = ((PO)FileInfos.goals.get(FileInfos.goals.size()-1)).getNum_in_f() + 1;
			}
			
			
			//we continue to read the file and we record what is intesting
			//('kind', 'text', 'file', 'line', 'begin' and 'end' fields)
			boolean text_mode = false;
			while( l != null ) {
				if(l.startsWith("kind =")) {
					index = 6;
					kind = getMotif(l.substring(index));
				} else
				if(l.startsWith("text =")) {
					index = 6;
					text = getMotif(l.substring(index));
					text_mode = true;
				} else
				if(l.startsWith("file =")) {
					index = 6;
					file = getMotif(l.substring(index));
				} else
				if(l.startsWith("line =")) {
					index = 6;
					line = Integer.parseInt(l.substring(index).trim());
				} else
				if(l.startsWith("begin =")) {
					index = 7;
					char1 = Integer.parseInt(l.substring(index).trim())+1;
				} else
				if(l.startsWith("end =")) {
					index = 5;
					char2 = Integer.parseInt(l.substring(index).trim());
				} else
				if(l.startsWith("pred = \"")) {
					index = 8;
					if(l.trim().endsWith("\"")) {
						motif = l.substring(index,l.length()-1).trim();
					} else {
						motif = l.substring(index,l.length()).trim();
						while( !(l = in.readLine()).endsWith("\"") ) {
							motif += l.substring(index,l.length()).trim();
						}
						motif += l.substring(index,l.length()-1).trim();
					}
				} else
				if(text_mode) {
					if(l != null && !l.trim().equals("") && !l.contains(" = ")) text += " " + l.trim();
					else text_mode = false;
				}
				l = in.readLine();
			}
			
			//now, we create the new PO and we add it in the ArrayList of goals of the FileInfos class
			PO po = new PO();
			po.setNum(m);
			po.setFnum(f);
			po.setNum_in_f(mprim);
			po.setName("" + mprim);
			po.setFile(file);
			po.setLoc(new int[]{line,char1,char2});
			po.setKind(kind);
			po.setText(text);
			po.makeTitle();
			po.initStates();
			FileInfos.goals.add(po);
			((Function)FileInfos.functions.get(FileInfos.functions.size()-1)).increase_po();
			
			in.close();
			fr.close();
			
		} catch(Exception e) {
			TraceDisplay.print(MessageType.ERROR, "InfosMaker.setNewPO() : " + e);
			r = false;
		}
		
		return r;
	}
	
	
	/**
	 * Record a new PO object in the FileInfos class
	 * from the description into the .why file.
	 * 
	 * @param m the PO number
	 * @return true if ok
	 */
	private boolean setNewPO2(int m) {
		
		boolean r = true;
		
		//we get the .why file
		String fileX = FileInfos.getRoot() + "why" + File.separator + FileInfos.getName() + "_po" + m + ".why";
		int mprim = 0;
		int f = FileInfos.functions.size();
		
		try {
			FileReader fr = new FileReader(fileX);
			BufferedReader in = new BufferedReader(fr);
			boolean lemma = false;
		
			// first line : here is the function's name
			String line = in.readLine();
			String t; //the tag, necessary to recognize or create functions
			int l1 = line.lastIndexOf("_po_");
			if( l1 >= 0 ) {
				t = "[" + line.substring(5, l1) + "]";
			} else {
				int l2 = line.lastIndexOf(":");
				if(l2 >= 0) {
					t = "[" + line.substring(5,l2) + "]";
				} else {
					t = LTAG;
				}
				lemma = true;
			}
			
			//if the function found is unknown, we create it :
			if(!t.equals(this.tag) || lemma) {
				this.tag = t;
				if(isFileExists(0, 0, ".loc")) {
					setNewFunction(this.tag, lemma);
				} else {
					setNewFunction2(this.tag, lemma);
				}
				f++;
				mprim = 1;
			} else {
				mprim = ((PO)FileInfos.goals.get(FileInfos.goals.size()-1)).getNum_in_f() + 1;
			}
			
			//we continue to read the file and we record what is intesting
			//('kind', 'text', 'file', 'line', 'begin' and 'end' fields)
			String file = "";
			int x, y, a=0, b=0, c=0;
			while( (line = in.readLine()) != null ) {
				if(line.contains("File")) {
					
					x = line .indexOf("File");
					line = line.substring(x+4);
					x = line.indexOf("\"");
					line = line.substring(x+1);
					x = line.indexOf("\"");
					file = line.substring(0, x);
					if(file.startsWith("\\"))
						file = file.substring(1);
					if(file.endsWith("\\"))
						file = file.substring(0, file.length()-1);
					
					x = line.indexOf("line");
					line = line.substring(x);
					x = line.indexOf(" ");
					y = line.indexOf(",");
					a = Integer.parseInt(line.substring(x+1,y));
					
					x = line.indexOf("characters");
					line = line.substring(x);
					x = line.indexOf(" ");
					y = line.indexOf("-");
					b = Integer.parseInt(line.substring(x+1,y));
					
					x = line.indexOf("-");
					y = line.indexOf("\"");
					c = Integer.parseInt(line.substring(x+1,y));
				}
			}
			
			//creation of a new PO()
			PO po = new PO();
			po.setNum(m);
			po.setFnum(f);
			po.setNum_in_f(mprim);
			po.setName("" + mprim);
			po.setFile(file);
			po.setLoc(new int[]{a,b,c});
			po.initStates();
			FileInfos.goals.add(po);
			((Function)FileInfos.functions.get(FileInfos.functions.size()-1)).increase_po();
			
			in.close();
			fr.close();
			
		} catch(FileNotFoundException e) {
			TraceDisplay.print(MessageType.WARNING, "File " + fileX + " not found");
			r = false;
		} catch(IOException e) {
			TraceDisplay.print(MessageType.ERROR, "InfosMaker.setNewPO2() : " + e);
			r = false;
		}
		
		return r;
	}
	
	
	
	/**
	 * Is a file exists?
	 * 
	 * @param wwf the goal number
	 * @param xwf the subgoal number
	 * @param ext the file extension (.why, .xpl)
	 * @return true if the file exists, false otherwise
	 */
	private boolean isFileExists(int wwf, int xwf, String ext) {
		
		String gueustri, tmpName;
		
		if(wwf > 0) {
			if(xwf > 0) {
				tmpName = FileInfos.getName() + "_po" + wwf + "-" + xwf + ext;
			} else {
				tmpName = FileInfos.getName() + "_po" + wwf + ext;
			}
			gueustri = FileInfos.getRoot() + "why" + File.separator + tmpName;
		} else {
			tmpName = FileInfos.getName() + ext;
			gueustri = FileInfos.getRoot() + tmpName;
		}
		
		File leufi = new File(gueustri);
		return leufi.exists();
	}
	
	
	/**
	 * Used, for example, to return "taré" from the String <code>behavior = "taré"</code>
	 * or "blaireau" from the String <code>name = blaireau</code>
	 * 
	 * @param s the start string
	 * @return the end string
	 */
	private String getMotif(String s) {
		s = s.trim();
		if(s.startsWith("\"")) {
			s = s.substring(1, s.length()-1);
		}
		return s.trim();
	}
}
