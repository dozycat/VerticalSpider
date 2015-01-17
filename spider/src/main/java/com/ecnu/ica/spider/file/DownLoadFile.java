package com.ecnu.ica.spider.file;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;

import com.ecnu.ica.spider.crawl.CrawlDrugbank;

public class DownLoadFile {
	private static final Logger log = Logger.getLogger(DownLoadFile.class);
	public static BufferedInputStream getHttpInputStream(String url) {
		try {
			URL u = new URL(url);
			HttpURLConnection httpUrl = (HttpURLConnection) u.openConnection();
			BufferedInputStream bf = new BufferedInputStream(
					httpUrl.getInputStream());
			return bf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized void download(BufferedInputStream bf,
			String file) {
		try {
			log.info(file);
			FileOutputStream fs = new FileOutputStream(file);
			int size;
			byte[] buf = new byte[100];
			while ( (size = bf.read(buf)) != -1){
				fs.write(buf, 0, size);
			}
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
