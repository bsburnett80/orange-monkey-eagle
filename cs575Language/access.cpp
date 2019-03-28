class A {
	int a;
public:
    int get_a() {return a;}
    void set_a(int aa) {a=aa;}
};

void main() {
	A a[2];
	int a0,a1;
	a[0].set_a(1);
	a0=a[0].get_a();
	a[1].set_a(0);
	a1=a[1].get_a();
	cout<<a0;
	cout<<endl;
	cout<<a1;
	cout<<endl;
}