package com.ecnu.ica.spiter.crawl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class CrawlGoogle extends CrawlJob {

	public CrawlGoogle(String[] args) {
		super();
		this.setPath("/home/dozy/google/");
		this.setFileName("googleTest");
	}

	@Override
	String crawlContent(String url, WebClient client) {

		HtmlPage page = null;
		DomNode node = null;
		HtmlAnchor a = null;
		HtmlDivision div = null;
		HtmlElement ele = null;
		HtmlSubmitInput ntn = null;

		try {
			page = client.getPage(url);
			ele = (HtmlElement) page.getFirstByXPath("//*[@id='lst-ib']");
			ele.click();
			ele.type("黄保荃");

			ntn = (HtmlSubmitInput) page.getFirstByXPath("//*[@id='tsf']/div[2]/div[3]/center/input[1]");
			page=ntn.click();
			System.out.println(page.asText());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return page.asText();
	}

	@Override
	public ArrayList createUrl() {
		ArrayList list = new ArrayList();
		list.add("https://www.google.com.hk/");
		return list;
	}

	@Override
	public void preWebClient() {
		// TODO Auto-generated method stub

	}

}
