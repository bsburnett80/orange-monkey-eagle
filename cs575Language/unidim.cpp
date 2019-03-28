void main()
{
	int unidimensional[3];
	int i,counter;

	i=0;
	counter=0;
	while(i < 3)
	{
		unidimensional[i] = counter;
		counter = counter + 1;
		i = i + 1;
	}

	i=0;
	while(i < 3)
	{
		cout<<unidimensional[i];
		cout<<endl;
		i = i + 1;
	}
}