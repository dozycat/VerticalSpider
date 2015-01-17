package com.ecnu.ica.spider.crawl;

import java.io.BufferedInputStream;
import java.text.DateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;

import com.ecnu.ica.spider.file.DownLoadFile;
import com.ecnu.ica.spider.file.SimpleToFile;
import com.ecnu.ica.spider.main.JobControl;
import com.ecnu.ica.spider.util.CrawlResult;
import com.ecnu.ica.spider.util.CrawlTools;
import com.ecnu.ica.spider.util.MapToJSON;
import com.ecnu.ica.spider.util.SpiterWebClient;
import com.ecnu.ica.spider.util.URL;
import com.gargoylesoftware.htmlunit.WebClient;

public abstract class CrawlJob implements Runnable {

	protected WebClient webClient;
	protected MapToJSON mtj;
	protected int delay_time = 1000;
	protected DateFormat fm = new java.text.SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	private static String PATH = "defaultpath";
	private URL temp = null;
	private String DEFAULT_SPLIT = "|";

	private String fileName = "";
	private int threadNum;
	private CrawlTools ct;

	private static final Logger log = Logger.getLogger(CrawlJob.class);

	public static void setPath(String path) {
		PATH = path;
	}

	/*
	 * 获得待抓取的URL
	 */
	public abstract CrawlResult crawlContent(URL url, WebClient client,
			CrawlTools tools);

	public abstract void preWebClient();

	public void setDelayTime(int t) {
		this.delay_time = t;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public CrawlJob() {
		webClient = SpiterWebClient.ConstructWebClient();
		ct = new CrawlTools();
	}

	public void run() {
		while (true) {
			temp = JobControl.getUrl();
			CrawlResult result = null;
			if (!JobControl.getConnectionStatus()) {
				log.info("OVER!");
				break;
			}
			try {
				Thread.sleep(delay_time);
				if (temp!=null)
					log.info(temp.getUrl());
				if (temp != null) {
					result = crawlContent(temp, webClient, ct);
					if (result != null) {
						if (result.getMethod().equals("String")) {
							log.info("String");
							if (result.getOutpath() == null) {

								SimpleToFile.easyWriteData(
										PATH + SimpleToFile.getRandomName(1000)
												+ ".txt", result.getContent()
												+ "\n");
							} else {
								SimpleToFile.easyWriteData(
										PATH + result.getOutName(),
										result.getContent() + "\n");
							}
						}
						if (result.getMethod().equals("BufferedInputStream")) {
							log.info("String");
							BufferedInputStream bf = (BufferedInputStream) result
									.getContent();
							DownLoadFile.download(bf,
									PATH + result.getOutName());
						}
					}
					SimpleToFile.easyWriteData(JobControl.getStatusPath(),
							temp.getUrl() + "\t" + "over!\n");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (temp == null)
				break;
			if (result != null)
				System.out.println("crawl-over:" + temp + "\t"
						+ fm.format(System.currentTimeMillis()));
			else
				System.out.println("crawl-nothing:" + temp + "\t"
						+ fm.format(System.currentTimeMillis()));
		}
	}

}
