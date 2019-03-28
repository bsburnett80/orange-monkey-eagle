
void main( ) {
    int m,n,p;
    m=100;
    n=10*m-1;
    p=-m-3+n*2*(-5+n/m);
	bool a,b,c,d;
	a=m>n || n>=p;
	b=m<n && n<=p;
	c=a && !b || !a && b;
	d=c ? a==b : a!=b;
	cout<<m;
	cout<<endl;
	cout<<n;
	cout<<endl;
	cout<<p;
	cout<<endl;
	cout<<a;
	cout<<endl;
	cout<<b;
	cout<<endl;
	cout<<c;
	cout<<endl;
	cout<<d;
	cout<<endl;
}