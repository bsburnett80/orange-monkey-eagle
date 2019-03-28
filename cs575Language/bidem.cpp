void main()
{
	int bidimensional[3][3];
	int i,j,counter;

	i=0;
	counter=0;
	while(i < 3)
	{
		j=0;
		while(j < 3)
		{
			bidimensional[i][j] = counter;
			counter = counter + 1;
			j = j + 1;
		}
		i = i + 1;
	}

	i=0;
	while(i < 3)
	{
		j=0;
		while(j < 3)
		{
			cout<<bidimensional[i][j];
			cout<<endl;
			j = j + 1;
		}
		i = i + 1;
	}
}