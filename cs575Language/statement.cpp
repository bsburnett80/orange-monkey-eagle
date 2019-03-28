
void main ( ) {
	int i,j,k,m,n,p,q;
	i=0;
	j=0;
	k=0;
	n=0;
	p=0;
	while (i<10) {
		j=j+i;
		if (j%2==0)
			n=n+1;
		m=j;
		do {
			k=k+m;
			if (k%3==0)
				p=p+1;
			else
				p=p-1;
			m=m-1;
		} while (m>=0);
		i=i+1;
	}
	if (p>0)
		if (j<k)
			q=999;
		else
			q=1000;


	cout<<i;
	cout<<endl;
	cout<<j;
	cout<<endl;
	cout<<k;
	cout<<endl;
	cout<<n;
	cout<<endl;
	cout<<p;
	cout<<endl;
	cout<<q;
	cout<<endl;
}
