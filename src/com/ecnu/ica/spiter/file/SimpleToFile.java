package com.ecnu.ica.spiter.file;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

public class SimpleToFile {


//		private static PrintWriter printWriter;
		private static Random random = new Random(System.currentTimeMillis());
		public static  synchronized void easyWriteData(String path, String Res) {
			try {
				OutputStreamWriter osw;
				osw = new OutputStreamWriter(new FileOutputStream(path, true),"UTF-8");
				osw.write(Res);
				osw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public static String getRandomName(int N)
		{
			return String.valueOf(random.nextInt(N));
		}
	
		public static int getRandomNum(int N)
		{
			return random.nextInt(N);
		}
	
}
