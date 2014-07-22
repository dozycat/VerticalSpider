package com.ecnu.ica.spiter.crawl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Clueweb12 extends CrawlJob {
	public Clueweb12(String[] args){
		super();
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-o")) {
				this.setPath(args[i + 1]);
			}
			if (args[i].equals("-d")) {
				// this.setPath(args[i + 1]);
				this.delay_time = Integer.valueOf(args[i + 1]);
			}
		}
	}
	private static final Logger log = Logger.getLogger(Clueweb12.class);

	@Override
	String crawlContent(String url, WebClient client) {
		try {
			HtmlPage page = client.getPage(url);
			
			log.info(page.asText());
			
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public ArrayList createUrl() {
		ArrayList list = new ArrayList();
		list.add("http://boston.lti.cs.cmu.edu/Services/clueweb12_batch/?username=echinanormalu&password=VivianMeansAlive");
		return list;
	}

	@Override
	public void preWebClient() {
		// TODO Auto-generated method stub

	}

}
