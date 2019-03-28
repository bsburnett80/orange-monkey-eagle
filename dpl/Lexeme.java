import java.util.ArrayList;


public class Lexeme extends Types
{
	Punct punct;
    Operator op;
    Type T;
    Keywords k;
	int ival;
	double rval;
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
	
	public Lexeme(Type t, double value)
	{
        T = t;
		type = t.toString();
		rval = value;
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
}
