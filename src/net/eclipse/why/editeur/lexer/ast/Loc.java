package net.eclipse.why.editeur.lexer.ast;

public class Loc {

	public int loc_line;
	public int loc_char;
	
	public Loc(int loc_line_init, int loc_char_init) {
		loc_line = loc_line_init;
		loc_char = loc_char_init;
	}
}
