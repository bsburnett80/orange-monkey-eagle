package cs575Language;

import java.util.ArrayList;

public class Lexeme extends Types
{
	Punct punct;
	Operator op;
	Type T;
	Keywords k;
	int ival, i;
	String sval;
	String type;
	Lexeme left;
	Lexeme right;
	ArrayList<Lexeme> aval = new ArrayList<Lexeme>();
	
	public Lexeme()
	{
		type = "LEXEME";
	}
	public Lexeme(String s)
	{
		type = s;
	}
	public Lexeme(Punct p)
	{
		type = p.toString();
		punct = p;
	}
	public Lexeme(Punct p, int value)
	{
		type = p.toString();
		punct = p;
		i = value;
	}
	public Lexeme(Operator o)
	{
		type = o.toString();
		op = o;
	}
	public Lexeme(Type t, int value)
	{
		T = t;
		type = t.toString();
		ival = value;
	}
	public Lexeme(Type t, String s)
	{
		T = t;
		type = t.toString();
		sval = s;
	}
	public Lexeme(Keywords word, String s)
	{
		k = word;
		type = word.toString();
		sval = s;
	}
	public Lexeme(Type t)
	{
		T = t;
		type = t.toString();
	}
	
	public static Lexeme node(Lexeme lex1, Lexeme lex2)
	{
		Lexeme newLex = new Lexeme();
		newLex.left = lex1;
		newLex.right = lex2;
		return newLex;
	}
}
