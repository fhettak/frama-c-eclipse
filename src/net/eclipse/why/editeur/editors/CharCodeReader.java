package net.eclipse.why.editeur.editors;

import java.io.IOException;
import java.io.Reader;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class CharCodeReader extends Reader {

	// Constants ---------------------------------------------------------------

	/**
	 * Constant for configuring a reader to read the document backwards.
	 */
	public static final int BACKWARD = -1;
	
	/**
	 * Constant for configuring a reader to read the document forwards.
	 */
	public static final int FORWARD = 1;

	// Instance Variables ------------------------------------------------------

	/**
	 * The document to read. Is <code>null</code> when the reader is closed.
	 */
	private IDocument document;

	/**
	 * The direction in which the document should be read. Can be either
	 * {@link CssCodeReader#BACKWARD} or {@link CssCodeReader#FORWARD}. 
	 */
	private int direction;

	/**
	 * The offset at which to start reading.
	 */
	private int offset;
	
	/**
	 * The offset at which to end reading.
	 */
	private int end = -1;
	
	/**
	 * Whether comments should be skipped.
	 */
	private boolean skipComments;
	
	/**
	 * Whether string literals should be skipped.
	 */
	private boolean skipStrings;
	
	/**
	 * Whether whitespace characters should be skipped.
	 */
	private boolean skipWhitespace;
	
	// Constructors ------------------------------------------------------------

	/**
	 * Constructor.
	 * 
	 * @param document the document to read
	 * @param offset the offset at which to start reading
	 */
	public CharCodeReader(IDocument document, int offset) {
		this(document, offset, document.getLength() - offset, FORWARD);
	}

	/**
	 * Constructor.
	 * 
	 * @param document the document to read
	 * @param offset the offset at which to start reading
	 * @param length the number of characters to read at most
	 */
	public CharCodeReader(IDocument document, int offset, int length) {
		this(document, offset, length, FORWARD);
	}

	/**
	 * Constructor.
	 * 
	 * @param document the document to read
	 * @param offset the offset at which to start reading
	 * @param length the number of characters to read at most
	 * @param direction the reading direction (either
	 */
	public CharCodeReader(IDocument document, int offset, int length,
		int direction) {
		this(document, offset, length, direction, true, false, false);
	}

	/**
	 * Constructor.
	 * 
	 * @param document the document to read
	 * @param offset the offset at which to start reading
	 * @param length the number of characters to read at most
	 * @param direction the reading direction
	 * @param skipComments whether the reader should skip comments
	 * @param skipStrings whether the reader should skip strings
	 * @param skipWhitespace whether the reader should skip whitespace
	 */
	public CharCodeReader(IDocument document, int offset, int length,
		int direction, boolean skipComments, boolean skipStrings,
		boolean skipWhitespace) {
		if ((direction != BACKWARD) && (direction != FORWARD)) {
			throw new IllegalArgumentException();
		}
		if ((offset < 0) || (offset > document.getLength())) {
			throw new IllegalArgumentException();
		}
		this.document = document;
		this.offset = offset;
		this.direction = direction;
		if (direction == FORWARD) {
			end = Math.min(document.getLength(), offset + length);
		} else {
			end = Math.max(0, offset - length);
		}
		this.skipComments = skipComments;
		this.skipStrings = skipStrings;
		this.skipWhitespace = skipWhitespace;
	}

	// Reader Implementation ---------------------------------------------------

	/**
	 * @see Reader#close()
	 */
	public void close() {
		document = null;
	}
	
	/**
	 * @see Reader#read(char[], int, int)
	 */
	public int read(char[] cbuf, int off, int len) throws IOException {
		for (int i = off; i < off + len; i++) {
			int c = read();
			if (c == -1) {
				if (i == off) {
					return -1;
				} else {
					return i - off;
				}
			}
			cbuf[i] = (char) c;
		}
		return len;
	}

	/**
	 * @see Reader#read()
	 */
	public int read() throws IOException {
		if (document == null) {
			throw new IllegalStateException();
		}
		try {
			if (direction == FORWARD) {
				return readForwards();
			} else {
				return readBackwards();
			}
		} catch (BadLocationException ble) {
			throw new IOException(ble.getMessage());
		}
	}

	// Public Methods ----------------------------------------------------------

	/**
	 * Returns the document that is being read.
	 * 
	 * @return The document
	 */
	public IDocument getDocument() {
		return document;
	}

	/**
	 * Returns the offset of the last read character. Should only be called 
	 * after read has been called.
	 * 
	 * @return The offset of the last read character
	 */
	public int getOffset() {
		return (direction == FORWARD) ? offset - 1 : offset;
	}
	
	/**
	 * Returns whether the reader is currently configured to skip comments.
	 * 
	 * @return <code>true</code> if the reader is skipping comments and
	 *         <code>false</code> otherwise
	 */
	public boolean isSkippingComments() {
		return skipComments;
	}

	/**
	 * Configures the reader to skip comment blocks.
	 * 
	 * @param skipComments Whether comments should be skipped
	 */
	public void setSkipComments(boolean skipComments) {
		this.skipComments = skipComments;
	}

	/**
	 * Returns whether the reader is currently configured to skip string
	 * literals.
	 * 
	 * @return <code>true</code> if the reader is skipping strings and
	 *         <code>false</code> otherwise
	 */
	public boolean isSkippingStrings() {
		return skipStrings;
	}

	/**
	 * Configures the reader to skip strings.
	 * 
	 * @param skipStrings Whether strings should be skipped
	 */
	public void setSkipStrings(boolean skipStrings) {
		this.skipStrings = skipStrings;
	}

	/**
	 * Returns whether the reader is currently configured to skip whitespace.
	 * 
	 * @return <code>true</code> if the reader is skipping whitespace and
	 *         <code>false</code> otherwise
	 */
	public boolean isSkippingWhitespace() {
		return skipWhitespace;
	}

	/**
	 * Configures the reader to skip whitespace.
	 * 
	 * @param skipWhitespace Whether whitespace should be skipped
	 */
	public void setSkipWhitespace(boolean skipWhitespace) {
		this.skipWhitespace = skipWhitespace;
	}

	// Private Methods ---------------------------------------------------------

	private int readBackwards() throws BadLocationException {
		while (offset > 0) {
			offset--;
			char current = document.getChar(offset);
			switch (current) {
				case '/': {
					if (skipComments && (offset > 1)) {
						char next = document.getChar(offset - 1);
						if (next == '*') {
							// a comment ends, advance to the comment start
							offset -= 2;
							skipUntilStartOfComment();
							continue;
						}
					}
					break;
				}
				case '"':
				case '\'': {
					if (skipStrings) {
						offset--;
						skipUntilStartOfString(current);
						continue;
					}
					break;
				}
				default: {
					if (skipWhitespace && Character.isWhitespace(current)) {
						continue;
					}
				}
			}
			return current;
		}
		return -1;
	}

	private int readForwards() throws BadLocationException {
		while (offset < end) {
			char current = document.getChar(offset++);
			switch (current) {
				case '/': {
					if (skipComments && (offset < end)) {
						char next = document.getChar(offset);
						if (next == '*') {
							// a comment starts, advance to the comment end
							offset++;
							skipUntilEndOfComment();
							continue;
						}
					}
					break;
				}
				case '"':
				case '\'': {
					if (skipStrings) {
						skipUntilEndOfString(current);
						continue;
					}
					break;
				}
				default: {
					if (skipWhitespace && Character.isWhitespace(current)) {
						offset++;
						continue;
					}
				}
			}
			return current;
		}
		return -1;
	}

	private void skipUntilStartOfComment() throws BadLocationException {
		while (offset > 0) {
			char current = document.getChar(offset--);
			if ((current == '*') && (0 <= offset)
			 && (document.getChar(offset) == '/')) {
				return;
			}
		}
	}

	private void skipUntilEndOfComment() throws BadLocationException {
		while (offset < end) {
			char current = document.getChar(offset++);
			if (current == '*') {
				if ((offset < end)
				 && (document.getChar(offset) == '/')) {
					offset++;
					return;
				}
			}
		}
	}

	private void skipUntilStartOfString(char delimiter)
		throws BadLocationException {
		while (offset > 0) {
			char current = document.getChar(offset);
			if (current == delimiter) {
				if (!((0 <= offset) && document.getChar(offset - 1) == '\\')) {
					return;
				}
			}
			offset--;
		}
	}

	private void skipUntilEndOfString(char delimiter)
		throws BadLocationException {
		while (offset < end) {
			char current = document.getChar(offset++);
			if (current == '\\') {
				// ignore escaped characters
				offset++;
			} else if (current == delimiter) {
				return;
			}
		}
	}

}
