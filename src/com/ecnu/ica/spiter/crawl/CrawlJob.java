package com.ecnu.ica.spiter.crawl;

import java.text.DateFormat;
import java.util.ArrayList;

import com.ecnu.ica.spiter.file.SimpleToFile;
import com.ecnu.ica.spiter.main.JobControl;
import com.ecnu.ica.spiter.util.MapToJSON;
import com.ecnu.ica.spiter.util.SpiterWebClient;
import com.gargoylesoftware.htmlunit.WebClient;

public abstract class CrawlJob implements Runnable {

	protected WebClient webClient;
	protected MapToJSON mtj;
	protected int delay_time = 1000;
	protected DateFormat fm = new java.text.SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	private String temp = "";
	private String DEFAULT_SPLIT = "|";
	private String PATH = "defaultpath";
	private String fileName = "";
	private int threadNum;

	abstract String crawlContent(String url, WebClient client);

	public abstract ArrayList createUrl();

	public abstract void preWebClient();

	public void setPath(String path) {
		this.PATH = path;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public CrawlJob() {
		webClient = SpiterWebClient.ConstructWebClient();
		mtj = new MapToJSON();
	}

	public void run() {
		while (true) {
			temp = JobControl.getUrl();
			String strl = null;
			try {
				Thread.sleep(delay_time+SimpleToFile.getRandomNum(delay_time*2));
				if (temp != null) {
					strl = crawlContent(temp, webClient);
					if (strl != null)
						if (this.fileName == "") {
							SimpleToFile.easyWriteData(
									PATH + SimpleToFile.getRandomName(1000)
											+ ".txt", strl + "\n");
						} else {
							SimpleToFile.easyWriteData(
									PATH + fileName + ".txt", strl + "\n");
						}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (temp == null)
				break;
			if (strl != null)
				System.out.println("crawl-over:" + temp + "\t"
						+ fm.format(System.currentTimeMillis()));
			else
				System.out.println("crawl-nothing:" + temp + "\t"
						+ fm.format(System.currentTimeMillis()));
		}
	}

}
