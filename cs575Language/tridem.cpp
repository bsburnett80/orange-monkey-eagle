class Integer
{
	int x;
public:
	void set_x(int xx) {x = xx;}
	int get_x() {return x;}
};

void main()
{
	Integer tridimensional[5][5][5];
	int i,j,k,counter;

	i=0;
	counter=0;
	while(i < 5)
	{
		j=0;
		while(j < 5)
		{
			k=0;
			while(k < 5)
			{
				tridimensional[i][j][k].set_x(counter);
				counter = counter + 1;
				k = k + 1;
			}
			j = j + 1;
		}
		i = i + 1;
	}

	i=0;
	while(i < 5)
	{
		j=0;
		while(j < 5)
		{
			k=0;
			while(k < 5)
			{
				cout<<tridimensional[i][j][k].get_x();
				cout<<endl;
				k = k + 1;
			}
			j = j + 1;
		}
		i = i + 1;
	}
}