package test.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TeseFile {
	public static void main(String args[]){
		FileInputStream fis;
		try {
			fis = new FileInputStream("scripts\\1.txt");
			System.out.println(fis.available());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
