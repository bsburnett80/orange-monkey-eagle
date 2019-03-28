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
        GTHAN,
        LTHAN,
        AT,
        DOT,
        NOT,
        AND,
        OR,
        EXP,
        SET
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
		DQUOTE,
	}
	
	public enum Keywords
	{
		IF,
		ELSE,
		WHILE,
		TYPE_INT,
		TYPE_STRING,
		TYPE_REAL,
        ID,
		FUNCTION,
		RETURN,
		CLASS,
        DEFINE,
        ARRAY,
        PRINT,
        PRINTLN,
        SETARRAY,
        NULL
	}
	
	public Keywords findKeyword(String word)
	{
		if (word.equalsIgnoreCase("if")) return Keywords.IF;
		else if (word.equalsIgnoreCase("else")) return Keywords.ELSE;
		else if (word.equalsIgnoreCase("while")) return Keywords.WHILE;
		else if (word.equalsIgnoreCase("int")) return Keywords.TYPE_INT;
		else if (word.equalsIgnoreCase("string")) return Keywords.TYPE_STRING;
		else if (word.equalsIgnoreCase("real")) return Keywords.TYPE_REAL;
		else if (word.equalsIgnoreCase("function")) return Keywords.FUNCTION;
		else if (word.equalsIgnoreCase("return")) return Keywords.RETURN;
		else if (word.equalsIgnoreCase("class")) return Keywords.CLASS;
        else if (word.equalsIgnoreCase("define")) return Keywords.DEFINE;
        else if (word.equalsIgnoreCase("array")) return Keywords.ARRAY;
        else if (word.equalsIgnoreCase("print")) return Keywords.PRINT;
        else if (word.equalsIgnoreCase("println")) return Keywords.PRINTLN;
        else if (word.equalsIgnoreCase("set")) return Keywords.SETARRAY;
        else if (word.equalsIgnoreCase("null")) return Keywords.NULL;
		else return Keywords.ID;
	}
	
	public enum Type
	{
		INTEGER,
		STRING,
		REAL,
		UNKNOWN,
		ENDofINPUT
	}
}
