package com.ecnu.ica.spiter.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;

public class SimpleSplitFile {
	private static final Logger log = Logger.getLogger(SimpleSplitFile.class);

	public static void main(String[] args) {
		log.info("begin split file form " + args[0]);
		File file = new File(args[0]);
		int split = Integer.parseInt(args[1]);
		int n = 0;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				n = n + 1;
//				System.out.println(file.getPath());
				SimpleToFile.easyWriteData(
						file.getPath() + String.valueOf(n % split), tempString+"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
