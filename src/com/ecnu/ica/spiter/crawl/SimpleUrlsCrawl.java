package com.ecnu.ica.spiter.crawl;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class SimpleUrlsCrawl {
	private static String DEFALUT_URL = "http://movie.mtime.com/999547/";
	private static long DELAY_TIME = 100;

	private static boolean connectUrl(String url, int repeatTimes) {
		for (int i = 0; i < repeatTimes; i++) {
			try {
				Thread.sleep(DELAY_TIME);
				URL urlTest = new URL(url);
				InputStream in = urlTest.openStream();
			} catch (Exception e) {
				// e.printStackTrace();
				if (i + 1 == repeatTimes)
					return false;
				continue;
			}
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(connectUrl("http://www.sina.com", 2));
	}
}
