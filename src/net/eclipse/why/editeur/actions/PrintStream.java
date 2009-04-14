package net.eclipse.why.editeur.actions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.eclipse.why.editeur.actions.TraceDisplay.MessageType;

/**
 * Print class
 * 
 * @author A. Oudot
 */
class PrintStream extends Thread {
	
	private InputStream __is = null;
	private String __rec;
	
	/**
	 * Class constructor
	 * 
	 * @param InputStream
	 */
	public PrintStream(InputStream is) {
		__is = is;
		__rec = "";
	} 

	/**
	 * Return a String made from an InputStream object
	 */
	public void run() {
		try {
			BufferedReader __br = new BufferedReader(new InputStreamReader(__is));
			while(this != null) {
				String _s = __br.readLine();
				if(_s != null) {
					__rec += _s + "\n";
				}
				else
					break;
			}
		} 
		catch (Exception e) {
			TraceDisplay.print(MessageType.ERROR, "Executor#PrintStream.run() : " + e);
		} 
	}
	
	/**
	 * Recorded string getter
	 * 
	 * @return the recorded string
	 */
	public String getRecString() {
		return __rec;
	}
}
