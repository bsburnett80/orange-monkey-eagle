import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedInputStream;


public class Lexer extends Types {
	
	BufferedInputStream _bis;
	int ENDofINPUT = 65535;
	boolean charPushed;
	char nextChar;
	
	public Lexer(String Filename)
	{
		File file = new File(Filename);
		try
		{
			_bis = new BufferedInputStream(new FileInputStream(file));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void closeStream() throws IOException
	{
		_bis.close();
	}
	
	public Lexeme Lex() throws IOException
	{
		char ch = 0;
		
		try {
			ch = getNextChar();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ch = skipWhiteSpace(ch);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ch == ENDofINPUT)
			return new Lexeme(Type.ENDofINPUT);
		switch (ch)
		{
		case '(':
			return new Lexeme(Punct.OPAREN);
		case ')':
			return new Lexeme(Punct.CPAREN);
		case ',':
			return new Lexeme(Punct.COMMA);
		case '.':
			return new Lexeme(Operator.DOT);
		case '+':
			return new Lexeme(Operator.PLUS);
		case '-':
			return new Lexeme(Operator.MINUS);
		case '*':
			return new Lexeme(Operator.STAR);
		case '/':
			return new Lexeme(Operator.SLASH);
		case '%':
			return new Lexeme(Operator.MODULO);
		case '=':
			return new Lexeme(Operator.ASSIGN);
		case '{':
			return new Lexeme(Punct.OBRACE);
		case '}':
			return new Lexeme(Punct.CBRACE);
		case '[':
			return new Lexeme(Punct.OBRACKET);
		case ']':
			return new Lexeme(Punct.CBRACKET);
		case ':':
			return new Lexeme(Punct.COLON);
		case ';':
			return new Lexeme(Punct.SEMICOLON);
		case '>':
			return new Lexeme(Operator.GTHAN);
		case '<':
			return new Lexeme(Operator.LTHAN);
		case '@':
			return new Lexeme(Operator.AT);
		case '!':
			return new Lexeme(Operator.NOT);
		case '&':
			return new Lexeme(Operator.AND);
		case '|':
			return new Lexeme(Operator.OR);
		case '^':
			return new Lexeme(Operator.EXP);
		case '$':
			return new Lexeme(Operator.SET);
		default:
			if (isDigit(ch)) return lexInteger(ch);
			else if (ch == '\"') return lexChar(ch);
			else if (isChar(ch)) return lexVariable(ch);
			else return new Lexeme(Type.UNKNOWN, ch);//If a char is not recognized, it's type is UNKNOWN
		}
	}

	private char getNextChar() throws IOException {
		if (charPushed)
		{
			charPushed = false;
			return nextChar;
		}
		else
		{
			char c = (char) _bis.read();
			if (c == 65535) return (char)ENDofINPUT;
			else return c;
		}
	}

	private boolean isChar(char ch) {
		if (Character.isLetter(ch)) return true;
		else return false;
	}

	private boolean isDigit(char ch) {
		if (Character.isDigit(ch)) return true;
		return false;
	}
	
	private void pushBack(char ch)
	{
		nextChar = ch;
		charPushed = true;
	}

	//Skips White space characters
	private char skipWhiteSpace(char c) throws IOException {
		if (c == '#') skipChars(c);
		while (Character.isWhitespace(c))
		{
			c = getNextChar();
			if (c == '#') c = skipChars(c);
		}
		
		return c;
	}
	
	private char skipChars(char c) throws IOException {
		c = getNextChar();
		while (c != '#')
		{
			c = getNextChar();
		}
		c = getNextChar();
		return getNextChar();
	}

	//If a char is a number, this gets called which
	//returns an integer value
	private Lexeme lexInteger(char ch) throws IOException
	{
		String s = "";
		s += ch;
		ch = getNextChar();
		while (ch != ENDofINPUT
				&& !(Character.isWhitespace(ch))
				&& Character.isDigit(ch)
				&& ch != '.')
		{
			s += ch;
			
			ch = getNextChar();
		}
		
		if (ch == '.')
		{
			return realNum(s, ch);
		}
		
		pushBack(ch);
		
		return new Lexeme(Type.INTEGER, Integer.parseInt(s));
	}
	
    //If a '.' is encountered after an integer, it calls this
    // through lexInteger and returns a REAL instead of INTEGER
	private Lexeme realNum(String s, char ch) throws IOException {
		s += ch;
		ch = getNextChar();
		while (ch != ENDofINPUT
				&& !(Character.isWhitespace(ch))
				&& Character.isDigit(ch))
		{
			s += ch;
			
			ch = getNextChar();
		}
		
		pushBack(ch);
		
		return new Lexeme(Type.REAL, Double.parseDouble(s));
	}

	//If a '"' is encountered, it calls this which returns
	//a string
	private Lexeme lexChar(char ch) throws IOException {
		String s = "";
		
		ch = getNextChar();
		while (ch != ENDofINPUT
				&& ch != '\"')
		{
			s += ch;
			ch = getNextChar();
		}
		
		
		return new Lexeme(Type.STRING, s);
	}

	//If a word begins with a char this gets called to return
	//a keyword or variable.
	private Lexeme lexVariable(char ch) throws IOException {
		Types type = new Types();
		String s = "";
		s += ch;
		ch = getNextChar();
		while (ch != ENDofINPUT
				&& !(Character.isWhitespace(ch))
				&& Character.isLetterOrDigit(ch))
		{
			s += ch;
			
			ch = getNextChar();
		}
		
		pushBack(ch);
		
		return new Lexeme(type.findKeyword(s), s);
	}
}
