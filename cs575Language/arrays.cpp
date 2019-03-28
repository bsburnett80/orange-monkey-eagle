
// another example Mini-C++ program

class Safe_1D_Array {
	int a[10];
	bool b[10];
public:
	/*void init ( ) {
		int k;
		k=0;
		do { b[k]=false; k=k+1; }
		while (k<10);
	}*/

	void store (int i, int value) {
		if (i<0 || i>9) return;
		a[i]=value;
		b(i)=true;
	}

	int fetch (int i) {
		return (i<0 || i>9 || ! b[i]) ? 0 : a[i];
	}
};

class Safe_2D_Array {
	Safe_1D_Array a[10];
	bool b[10];
public:
	void init ( ) {
		int k;
		k=0;
		do { b[k]=false; k=k+1; }
		while (k<10);
	}

	void store (int i, int j, int value) {
		if (i<0 || i>9) return;
		if (! b[i]) a[i].init ( );
		a[i].store (j, value);
		b[i]=true;
	}

	int fetch (int i, int j) {
		return (i>=0 && i<=9 && b[i]) ? a[i].fetch (j) : 0;
	}
};

void main() {
	Safe_2D_Array safe;
	safe.init ( );
	safe.store (2, 5, 99);
	int m;
	m = safe.fetch (2, 5);
	cout<<m;
	cout<<endl;
	m = safe.fetch (7, 8);
	cout<<m;
	cout<<endl;
	m = safe.fetch (2, 10);
	cout<<m;
	cout<<endl;

	int unsafe[10][10];
	unsafe[2][5]=99;
	m = unsafe[2][5];
	cout<<m;
	cout<<endl;
	m = unsafe[7][8];
	cout<<m;
	cout<<endl;
	m = unsafe[2][10];
	cout<<m;
	cout<<endl;
}