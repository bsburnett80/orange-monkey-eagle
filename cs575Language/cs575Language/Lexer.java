package cs575Language;
import java.io.*;

import cs575Language.Types;

public class Lexer extends Types {
	BufferedInputStream _bis;
	int ENDofINPUT = 65535;
	boolean charPushed;
	char nextChar;
	int i = 0;
	
	public Lexer(String Filename)
	{
		File file = new File(Filename);
		try
		{
			_bis = new BufferedInputStream(new FileInputStream(file));
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
	}
	
	public void closeStream() throws IOException
	{
		_bis.close();
	}
	
	public Lexeme Lex() throws IOException
	{
		char ch = 0;
		
		try
		{
			ch = getNextChar();
		}
		catch (IOException ex1)
		{
			ex1.printStackTrace();
		}
		try
		{
			ch = skipWhiteSpace(ch);
		}
		catch (IOException ex2)
		{
			ex2.printStackTrace();
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
		case '%':
			return new Lexeme(Operator.MODULO);
		case '=':
			if (checkForEquality(ch)) return new Lexeme(Operator.EQUALS);
			else return new Lexeme(Operator.ASSIGN);
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
			return new Lexeme(Punct.SEMICOLON, i++);
		case '>':
			if (checkForEquality(ch)) return new Lexeme(Operator.GTHANEQUAL);
			else if (checkForBitwise(ch)) return new Lexeme(Operator.SHIFTRIGHT);
			else return new Lexeme(Operator.GTHAN);
		case '<':
			if (checkForEquality(ch)) return new Lexeme(Operator.LTHANEQUAL);
			else if (checkForBitwise(ch)) return new Lexeme(Operator.SHIFTLEFT);
			else return new Lexeme(Operator.LTHAN);
		case '!':
			if (checkForEquality(ch)) return new Lexeme(Operator.NOTEQUAL);
			else return new Lexeme(Operator.NOT);
		case '&':
			ch = getNextChar();
			if (ch == '&') return new Lexeme(Operator.AND);
			else return new Lexeme(Type.UNKNOWN, ch);
		case '|':
			ch = getNextChar();
			if (ch == '|') return new Lexeme(Operator.OR);
			else return new Lexeme(Type.UNKNOWN, ch);
		case '?':
			return new Lexeme(Operator.CONDITIONAL);
		case '/':
			if (!checkForComment(ch)) return new Lexeme(Operator.SLASH);
			else ch = skipChars(ch);
		default:
			if (isDigit(ch)) return lexInteger(ch);
			else if (ch == '\"') return lexChar(ch);
			else if (isChar(ch)) return lexVariable(ch);
			else return new Lexeme(Type.UNKNOWN, ch);
			}
		
	}

	private boolean checkForBitwise(char ch) throws IOException {
		if (ch == '<')
		{
			ch = getNextChar();
			if (ch == '<') return true;
			else
			{
				pushBack(ch);
				return false;
			}
		}
		else
		{
			ch = getNextChar();
			if (ch == '>') return true;
			else
			{
				pushBack(ch);
				return false;
			}
		}
	}

	private boolean checkForEquality(char ch) throws IOException {
		ch = getNextChar();
		if (ch == '=') return true;
		else 
		{
			pushBack(ch);
			return false;
		}
	}

	private boolean isDigit(char ch) {
		if (Character.isDigit(ch)) return true;
		else return false;
	}

	private boolean isChar(char ch) {
		if (Character.isLetter(ch)) return true;
		else return false;
	}

	private Lexeme lexInteger(char ch) throws IOException {
		String s = "";
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
		
		return new Lexeme(Type.INTEGER, Integer.parseInt(s));
	}

	private Lexeme lexChar(char ch) throws IOException {
		String s = "";
		ch = getNextChar();
		while (ch != ENDofINPUT
				//&& Character.isLetterOrDigit(ch)
				&& ch != '\"'
				&& ch != '\n')
		{
			if (ch != '\\')
			{
				s += ch;
				ch = getNextChar();
			}
			else 
			{
				ch = getNextChar();
				if (ch != 'n' && ch != 't' && ch != '"' && ch != '\\')
				{
					System.out.println("\nInvalid escape character: " + ch + "\n");
					ch = getNextChar();
				}
				else if (ch == 't') 
				{
					ch = getNextChar();
					s += "     ";
				}
				else if (ch == '"') 
				{
					ch = getNextChar();
					s += "\"";
				}
				else if (ch == '\\') 
				{
					ch = getNextChar();
					s += "\\";
				}
				else if (ch == 'n') 
				{
					ch = getNextChar();
					continue;
				}
			}
			if (ch == '\n') System.out.println("String is missing closing \"");
		}
		
		return new Lexeme(Type.STRING, s);
	}
	
	private Lexeme lexVariable(char ch) throws IOException {
		Types type = new Types();
		String s = "";
		s += ch;
		ch = getNextChar();
		while (ch != ENDofINPUT
				&& !(Character.isWhitespace(ch))
				&& Character.isLetterOrDigit(ch)
				|| ch == '_')
		{
			s += ch;
			ch = getNextChar();
		}
		
		pushBack(ch);
		
		return new Lexeme(type.findKeyword(s), s);
	}

	private char skipChars(char c) throws IOException {
		c = getNextChar();
		if (c == '/')
		{
			while (c != '\n') c = getNextChar();
		}
		else if (c == '*')
		{
			c = getNextChar();
			while (c != ENDofINPUT)
			{
				c = getNextChar();
				if (c == '*')
				{
					c = getNextChar();
					if (c == '/') break;
					else pushBack(c);
				}
			}
		}
		c = getNextChar();
		return c;
	}

	private boolean checkForComment(char c) throws IOException {
		c = getNextChar();
		if (c == '/' || c == '*')
		{
			pushBack(c);
			return true;
		}
		else 
		{
			pushBack(c);
			return false;
		}
	}

	private char skipWhiteSpace(char c) throws IOException {
		if (c == '/')
		{
			char ch = getNextChar();
			if (ch == '/' || ch == '*')
			{
				pushBack(ch);
				c = skipChars(ch);
			}
			else pushBack(ch);
		}
		while (Character.isWhitespace(c))
		{
			c = getNextChar();
			if (c == '/')
			{
				if (checkForComment(c))
				{
					c = skipChars(c);
				}
				else pushBack(c);
				c = getNextChar();
			}
		}
		return c;
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
	
	private void pushBack(char ch)
	{
		nextChar = ch;
		charPushed = true;
	}
}
