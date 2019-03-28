	/* This
	is
	a
	multi-line
	comment.
	*/

class Object {
	int a;
	static int b;
public:
	int get_a( ) {return a;}
	void put_a(int aa) {a=aa;}
	static int get_b( ) {return b;}
	static void put_b(int bb) {b=bb;}
};

int /* This is an in-line comment */ main( ) {
	Object x,y;
	int k;

	cin>>k;
	x.put_a(k);
	cin >> k;
	y.put_a(k);
	cin  >>  k;
	x.put_b(k);

	cout<<"first=";
	cout<<x.get_a();
	cout<<endl;
	cout << "sec\tond=";
	cout << y.get_a( );
	cout << endl;
	cout  <<  "\"third=\"";
	cout  <<  y.get_b(  );
	cout  <<  endl;
}

