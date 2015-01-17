package com.ecnu.ica.spider.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.ecnu.ica.spider.crawl.CrawlJob;
import com.ecnu.ica.spider.file.LoadStatus;
import com.ecnu.ica.spider.util.URL;

/**
 * 
 * @author dozy
 * 
 */
public class JobControl {
	private static List<URL> urlList = new ArrayList<URL>();
	private static List<Thread> jobs = new ArrayList<Thread>();
	private static final Logger log = Logger.getLogger(JobControl.class);
	public static Integer idx = new Integer(0);
	private static String statusPath;
	private static HashMap urlStatus;
	private static boolean connection = true;
	private static int delay = 1000;

	/**
	 * 断点续传文件路径载入
	 * 
	 * @return String path 路径
	 */
	public static String getStatusPath() {
		return statusPath;
	}

	public static void setConnectionIsOK(boolean bool) {
		connection = bool;
	}

	public static boolean getConnectionStatus() {
		return connection;
	}

	public static void main(String[] args) {
		int threadNum = 0;
		Class c;
		Constructor constructor = null;
		CrawlJob job = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-t")) {
				threadNum = Integer.parseInt(args[i + 1]);
			}
			if (args[i].equals("-c")) {

				try {
					c = Class.forName(args[i + 1]);
					log.info("load class : " + args[i + 1] + "over	!");
					constructor = c.getConstructor(new Class[] {});

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (args[i].equals("-i")) {
				/**
				 * TODO 处理输入
				 */

				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(args[i + 1]));
					String data = br.readLine();
					while (data != null) {
						String[] temps = data.split("\t");
						URL url = new URL();
						if (temps.length == 1) {
							url.setUrl(temps[0]);
							url.setContent(null);
						} else if (temps.length == 2) {
							url.setUrl(temps[0]);
							url.setContent(temps[1]);
						}
						urlList.add(url);
						data = br.readLine();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (args[i].equals("-o")) {
				/**
				 * TODO 处理输出，同时载入断点续传文件.
				 */
				String outpath = args[i + 1];
				statusPath = args[i + 1] + "re.status";
				urlStatus = LoadStatus.LoadStatus(statusPath);
				JobControl.statusPath = statusPath;
				CrawlJob.setPath(outpath);
			}
			if (args[i].equals("-d")) {
				delay = Integer.valueOf(args[i + 1]);
			}

		}

		Executors.newFixedThreadPool(threadNum);
		for (int i1 = 0; i1 < threadNum; i1++) {

			try {

				job = (CrawlJob) constructor.newInstance();
				job.setDelayTime(delay);

			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread mt = new Thread(job);
			jobs.add(mt);
		}
		for (int i1 = 0; i1 < threadNum; i1++) {
			((Thread) jobs.get(i1)).start();
		}
	}

	public static void endThread() {
		/**
		 * TODO
		 */
	}

	public static URL getUrl() {
		URL temp = null;
		synchronized (idx) {
			int i = idx.intValue();
			if (i % 10 == 0)
				System.out.println("抓取进度:" + (float) (i) / urlList.size() * 100
						+ "%");
			if (i >= urlList.size())
				return null;
			temp = urlList.get(i);
			while (urlStatus.containsKey(temp.getUrl())) {
				idx = idx + 1;
				if (idx<urlList.size())
					temp = urlList.get(idx.intValue());
				else{
					temp=null;
					break;
				}
			}
			idx = idx + 1;
		}
		return temp;
	}

}
