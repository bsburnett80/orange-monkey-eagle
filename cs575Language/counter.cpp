
class Counter {
	int count;
public:
    void init() {
		count=0;
	}
    void inc() {
		count=count+1;
	}
    void dec() {
		count=count-1;
	}
    int getCount() {
		return count;
	}
};

int getFive()
{
	return 5;
}

void main()
{
	Counter k;
	int i;
	i=getFive();
	k.init();
	while(k.getCount() < i)
		k.inc();
	cout<<k.getCount();
	cout<<endl;
	cout<<"Please enter a number less than 5: ";
	cin>>i;
	while(k.getCount() > i)
		k.dec();
	cout<<k.getCount();
	cout<<endl;
}


