package com.ecnu.ica.spiter.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import net.sf.json.JSONObject;

public class TestReadFile {
	private static 	FileReader fr ;

	private static void ReadFile(String path) {
		File dir = new File(path);
		File[] files = dir.listFiles();
		BufferedReader br;  
		int sum=0;
		String data = null;
		for (File f:files){
			try {
				fr=new FileReader(f);
				br=new BufferedReader(new FileReader(f));
				data=br.readLine();
				while (data!=null){
					JSONObject jsonObject = JSONObject.fromObject( data ); 
					Object bean = JSONObject.toBean( jsonObject ); 
					String url;
					try {
					url=(String) PropertyUtils.getProperty(bean, "crawlURL");
					}catch (Exception e) {
						url = "";
					}
					String title=(String) PropertyUtils.getProperty(bean, "标题");
					SimpleToFile.easyWriteData("/home/dozy/mtime/sun.txt", title+"\t"+url+"\n");
					sum=sum+1;
					data=br.readLine();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(data);
			}
			
		}
		System.out.println(sum);
	}

	public static void main(String[] args) {
		ReadFile("/home/dozy/mtime/movie_tv_6_23");
	}
}
