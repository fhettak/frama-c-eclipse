/*@ ensures \result == 0; */
int non_main()
{
	return 0;
}

/*@ requires input > 0;
  @ ensures \result == 1; */
int other_function (int input)
{
	if (input > 0)
		return 1;
	return 0;
}
