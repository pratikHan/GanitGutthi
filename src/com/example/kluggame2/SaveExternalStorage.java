package com.example.kluggame2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;

public class SaveExternalStorage  {

	File file;
	//String data = "";
	String filepath = "";
	String filename = "";

	private static String TAG = "SaveExternalStorage";

	
	public static void writeFile(String data,String _filename)
	{
		String filename = _filename;

		try {
			//File sdcard = Environment.getExternalStorageDirectory();
			// to this path add a new directory path
			//File dir = new File(MainActivity.getUserDir()+filename);
			
			// create this directory if not already created
			//dir.mkdir();
			//Log.d("XXXXX", ""+dir.toString());
			// create the file in which we will write the contents

			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile(); // This line will create new blank line.
			}

			FileOutputStream os = new FileOutputStream(file);

			os.write(data.getBytes());
			os.flush();
			os.close();
		} catch (IOException e) {
			Log.e("Exception", "Cant find Data.");
		}
		
	}
	
	public void writeInternalStorage(String  data, String _filename) {

		

		filename = _filename;

		try {
			//File sdcard = Environment.getExternalStorageDirectory();
			// to this path add a new directory path
			File dir = new File(MainActivity.getUserDir()+filename);
			
			// create this directory if not already created
			if(!dir.exists())
			{
			dir.mkdir();
			}
			Log.d("XXXXX", ""+dir.toString());
			// create the file in which we will write the contents

			File file = new File(new File(MainActivity.getUserDir()), filename.concat(".json"));
			if (!file.exists()) {
				file.createNewFile(); // This line will create new blank line.
			}

			FileOutputStream os = new FileOutputStream(file);

			os.write(data.getBytes());
			os.flush();
			os.close();
		} catch (IOException e) {
			Log.e("Exception", "Cant find Data.");
		}

	}

	/*
	public void writeExternalStorage(String data, String _filename) {
		
		// filepath=_filepath;
		filename = _filename;

		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			Log.d(TAG, "Memory not available");
			if (!isExternalStorageAvailable()) {
				writeInternalStorage(data, filename);
			}

		} else {
			String secStore = System.getenv("SECONDARY_STORAGE");

		
			Log.d(TAG, data);
			Log.d(TAG, filename);

			try {
				
				
				
				
				File dir = new File(secStore + "/kluggg/");
				// create this directory if not already created
				dir.mkdir();
				file = new File(dir, filename);

				if (!file.exists()) {
					file.createNewFile(); // This line will create new blank
											// line.
				}
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data.getBytes());
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Log.d(TAG, "saved data succesfully");
			Log.d(TAG, "Secondarypath is " + secStore);
		}

	}
*/
	/*public String readfromExternalStorage(String _filename) {
		//filename=_filename;
		String data="";
		if (!isExternalStorageAvailable()) {
			readInternalStorage(filename);
		} else {
			try {
				String secStore = System.getenv("SECONDARY_STORAGE");
				file=new File(secStore+ "/kluggg/"+_filename);
				
				FileInputStream fis = new FileInputStream(file);
				DataInputStream in = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					data = data + strLine;
				}
				
				in.close();
				return data;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
*/
	public static String readFile(String filex)
	{
		
		String data="";
		//filename=_filename;
		//File sdcard = Environment.getExternalStorageDirectory();
		// to this path add a new directory path
		File file = new File(filex);
		try {
			FileInputStream fis = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				data = data + strLine;
			}
			in.close();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String readInternalStorage(String _filename) {

		String data="";
		//filename=_filename;
		//File sdcard = Environment.getExternalStorageDirectory();
		// to this path add a new directory path
		File file = new File(MainActivity.getUserDir()+_filename+"/"+_filename.concat(".txt"));
		try {
			FileInputStream fis = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				data = data + strLine;
			}
			in.close();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	private static boolean isExternalStorageReadOnly() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	private static boolean isExternalStorageAvailable() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
			return true;
		}
		return false;
	}
	
	
	
	

}
