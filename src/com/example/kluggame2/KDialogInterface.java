package com.example.kluggame2;

import java.util.ArrayList;

public interface KDialogInterface
{
	public void leave(int id);
	public void retry(int id);
	public ArrayList<String> getDialog();
	public int getHints();
	
};

