package com.ecnu.ica.spiter.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.ecnu.ica.spiter.crawl.CrawlJob;
import com.ecnu.ica.spiter.crawl.CrawlMtimes;

/**
 * 
 * @author dozy
 * 
 */
public class JobControl {
	// private static ExecutorService pool =
	private static ExecutorService pool;
	private static List<String> urlList = new ArrayList<String>();
	private static List<Thread> jobs = new ArrayList<Thread>();
	private static final Logger log = Logger.getLogger(JobControl.class);
	public static Integer idx = new Integer(0);

	/**
	 * 控制多线程调度的抓取器
	 * 
	 * @param args
	 *            参数配置
	 * 
	 *            -u baseUrl base size -u http://www.baidu.com 0 10000 就会产生
	 *            http://www.baidu.com/0 - http://www.baidu.com/9999 的url 以供抓取
	 * 
	 *            -f 直接从文件中读取Urls
	 */
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

					constructor = c
							.getConstructor(new Class[] { String[].class });

					// log.info(c.getMethods().length);
					Method method = c.getMethod("createUrl");
					Object o = constructor.newInstance((Object) args);
					urlList = (List<String>) method.invoke(o);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (args[i].equals("-i")) {

			}
		}

		pool = Executors.newFixedThreadPool(threadNum);
		for (int i1 = 0; i1 < threadNum; i1++) {

			try {
				job = (CrawlJob) constructor.newInstance((Object) args);
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

	public static String getUrl() {
		String temp = null;
		synchronized (idx) {
			int i = idx.intValue();
			if (i % 10 == 0)
				System.out.println("抓取进度:" + (float) (i) / urlList.size() * 100
						+ "%");
			if (i >= urlList.size())
				return null;
			temp = urlList.get(i);
			idx = idx + 1;
		}
		return temp;
	}

}
