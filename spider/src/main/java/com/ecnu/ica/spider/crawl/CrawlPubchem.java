package com.ecnu.ica.spider.crawl;

import java.io.IOException;
import java.net.MalformedURLException;

import com.ecnu.ica.spider.util.CrawlResult;
import com.ecnu.ica.spider.util.CrawlTools;
import com.ecnu.ica.spider.util.URL;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CrawlPubchem extends CrawlJob {

	@Override
	public CrawlResult crawlContent(URL url, WebClient client, CrawlTools tools) {
		// TODO Auto-generated method stub
		String cr_url=url.getUrl();
		HtmlPage page;
		try {
			page = client.getPage(cr_url);
			System.out.println(client.getOptions().isJavaScriptEnabled());
			System.out.println(page.asXml());
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void preWebClient() {
		// TODO Auto-generated method stub
		
	}

}
