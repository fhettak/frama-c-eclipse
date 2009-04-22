package net.eclipse.why.editeur.actions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

/**
 * Print class
 * 
 * @author A. Oudot
 */
class StreamReader extends Thread {
	
	private InputStream stream = null;
	private String result;
	
	/**
	 * Class constructor
	 * 
	 * @param InputStream
	 */
	public StreamReader(InputStream is) {
		stream = is;
		result = "";
	} 

	/**
	 * Return a String made from an InputStream object
	 */
	public void run() {
		try {
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(stream));
			while(this != null) {
				String _s = bufferReader.readLine();
				if(_s != null) {
					result += _s + "\n";
				}
				else
					break;
			}
		} 
		catch (Exception e) {
			TraceView.print(MessageType.ERROR, "Executor#PrintStream.run() : " + e);
		} 
	}
	
	/**
	 * Recorded string getter
	 * 
	 * @return the recorded string
	 */
	public String getResult() {
		return result;
	}
}
