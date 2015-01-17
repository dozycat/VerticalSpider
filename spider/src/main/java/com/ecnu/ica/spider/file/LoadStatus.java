package com.ecnu.ica.spider.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class LoadStatus {
	private static FileReader fr;

	public static HashMap LoadStatus(String path) {

		BufferedReader br;
		HashMap map = new HashMap();
		try {
			fr = new FileReader(path);
			br = new BufferedReader(new FileReader(path));
			String data;
			while ((data = br.readLine()) != null) {
				String key = data.split("\t")[0];
				String status = data.split("\t")[1];
				map.put(key, status);
			}
			return map;

		} catch (Exception e) {
			return  new HashMap();
		}

	}
}
