package cs575Language;

public class Types {

	public Types()
	{
	}
	
	public enum Operator
	{
		PLUS,
		MINUS,
		STAR,
		SLASH,
		MODULO,
		ASSIGN,
		EQUALS,
		GTHAN,
		GTHANEQUAL,
		LTHAN,
		LTHANEQUAL,
		CONDITIONAL,
		NOT,
		NOTEQUAL,
		AND,
		OR,
		DOT,
		SHIFTLEFT,
		SHIFTRIGHT
	}
	
	public enum Punct
	{
		COMMA,
		OPAREN,
		CPAREN,
		OBRACE,
		CBRACE,
		OBRACKET,
		CBRACKET,
		COLON,
		SEMICOLON,
		DQUOTE
	}
	
	public enum Keywords
	{
		PUBLIC,
		CLASS,
		STATIC,
		VOID,
		INT,
		BOOL,
		DO,
		WHILE,
		IF,
		ELSE,
		TRUE,
		FALSE,
		CIN,
		COUT,
		ENDL,
		RETURN,
		ID
	}
	
	public Keywords findKeyword(String word)
	{
		if (word.equalsIgnoreCase("public")) return Keywords.PUBLIC;
		else if (word.equalsIgnoreCase("class")) return Keywords.CLASS;
		else if (word.equalsIgnoreCase("static")) return Keywords.STATIC;
		else if (word.equalsIgnoreCase("void")) return Keywords.VOID;
		else if (word.equalsIgnoreCase("int")) return Keywords.INT;
		else if (word.equalsIgnoreCase("do")) return Keywords.DO;
		else if (word.equalsIgnoreCase("while")) return Keywords.WHILE;
		else if (word.equalsIgnoreCase("if")) return Keywords.IF;
		else if (word.equalsIgnoreCase("else")) return Keywords.ELSE;
		else if (word.equalsIgnoreCase("true")) return Keywords.TRUE;
		else if (word.equalsIgnoreCase("false")) return Keywords.FALSE;
		else if (word.equalsIgnoreCase("cin")) return Keywords.CIN;
		else if (word.equalsIgnoreCase("cout")) return Keywords.COUT;
		else if (word.equalsIgnoreCase("endl")) return Keywords.ENDL;
		else if (word.equalsIgnoreCase("return")) return Keywords.RETURN;
		else return Keywords.ID;
	}
	
	public enum Type
	{
		INTEGER,
		STRING,
		UNKNOWN,
		ENDofINPUT
	}
}
