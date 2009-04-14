package net.eclipse.why.editeur.editors;

import java.io.IOException;

import net.eclipse.why.editeur.actions.TraceDisplay;
import net.eclipse.why.editeur.actions.TraceDisplay.MessageType;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.core.runtime.Assert;

public class CharPairMatcher implements ICharacterPairMatcher {

	// Constants ---------------------------------------------------------------

	private static final char PAIRS[] = {
		'{', '}', '(', ')', '[', ']'
	};

	// Instance Variables ------------------------------------------------------

	private IDocument document;

	private int offset;

	private int anchor;

	// ICharacterPairMatcher Implementation ------------------------------------

	/*
	 * @see ICharacterPairMatcher#clear
	 */
	public void clear() {
		document = null;
		offset = -1;
		anchor = 0;
	}

	/*
	 * @see ICharacterPairMatcher#dispose
	 */
	public void dispose() {
		document = null;
	}

	/*
	 * @see ICharacterPairMatcher#match
	 */
	public IRegion match(IDocument document, int offset) {
		Assert.isNotNull(document);
		Assert.isLegal(offset >= 0);
		this.document = document;
		this.offset = offset;

		IRegion retVal = null;
		try {
			retVal = matchPairsAt();
		} catch (BadLocationException e) {
			// ignore, there's probably no matching character to highlight
		}
		return retVal;
	}

	/*
	 * @see ICharacterPairMatcher#getAnchor
	 */
	public int getAnchor() {
		return anchor;
	}

	// Private Methods ---------------------------------------------------------

	private boolean isClosingCharacter(char ch) {
		for (int i = 1; i < PAIRS.length; i += 2) {
			if (ch == PAIRS[i]) {
				return true;
			}
		}
		return false;
	}

	private boolean isOpeningCharacter(char ch) {
		for (int i = 0; i < PAIRS.length; i += 2) {
			if (ch == PAIRS[i]) {
				return true;
			}
		}
		return false;
	}

	private IRegion matchPairsAt() throws BadLocationException {
		int startPos = -1, endPos = -1;
		char prevChar = document.getChar(Math.max(offset - 1, 0));
		if (isOpeningCharacter(prevChar)) {
			startPos = offset - 1;
			if (startPos >= 0) {
				anchor = LEFT;
				endPos = findMatchingClosingPeer(
					document, startPos + 1, prevChar);
				if (endPos > -1) {
					return new Region(startPos, endPos - startPos + 1);
				}
			}
		}
		if (isClosingCharacter(prevChar)) {
			endPos = offset - 1;
			if (endPos >= 0) {
				anchor = RIGHT;
				startPos = findMatchingOpeningPeer(
					document, endPos - 1, prevChar);
				if (startPos > -1) {
					return new Region(startPos, endPos - startPos + 1);
				}
			}
		} 
		return null;
	}
	
	
	
	
	public static int findMatchingClosingPeer(IDocument document, int offset,
			char openingPeer) {
			try {
				CharCodeReader reader = new CharCodeReader(document, offset,
					document.getLength(), CharCodeReader.FORWARD);
				char closingChar = getPeerCharacter(openingPeer);
				int stack = 1;
				int c = reader.read();
				while (c != -1) {
					if ((c == openingPeer) && (c != closingChar)) {
						stack++;
					} else if (c == closingChar) {
						stack--;
					}
					if (stack == 0) {
						return reader.getOffset();
					}
					c = reader.read();
				}
			} catch (IOException e) {
				TraceDisplay.print(MessageType.ERROR,
					"Failed to find matching closing peer for '" + //$NON-NLS-1$
					openingPeer + '\'' + " : " +  e);
			}
			return -1;
		}

		public static int findMatchingOpeningPeer(IDocument document, int offset,
			char closingPeer) {
			try {
				CharCodeReader reader = new CharCodeReader(document, offset + 1,
					document.getLength(), CharCodeReader.BACKWARD);
				char openingChar = getPeerCharacter(closingPeer);
				int stack = 1;
				int c = reader.read();
				while (c != -1) {
					if ((c == closingPeer) && (c != openingChar)) {
						stack++;
					} else if (c == openingChar) {
						stack--;
					}
					if (stack == 0) {
						return reader.getOffset();
					}
					c = reader.read();
				}
			} catch (IOException e) {
				TraceDisplay.print(MessageType.ERROR,
					"Failed to find matching opening peer for '" + //$NON-NLS-1$
					closingPeer + '\'' + " : " + e);
			}
			return	-1;
		}

		public static char getPeerCharacter(char c) {
			switch (c) {
				case '{': return '}';
				case '}': return '{';
				case '[': return ']';
				case ']': return '[';
				case '(': return ')';
				case ')': return '(';
				case '"': return '"';
				case '\'': return '\'';
				default: return 0;
			}
		}

		public static boolean isCssIdentifierPart(char c) {
			return ((c == '_') || Character.isLetterOrDigit(c) || (c == '-'));
		}

		public static boolean isCssIdentifierStart(char c) {
			return ((c == '_') || Character.isLetter(c));
		}

		public static boolean isCssNumberPart(char c) {
			return ((c == '.') || Character.isDigit(c));
		}

		public static boolean isCssNumberStart(char c) {
			return ((c == '-') || (c == '.') || Character.isDigit(c));
		}

		public static boolean isCssWhitespace(char c) {
			return Character.isWhitespace(c);
		}



}