package com.ecnu.ica.spider.util;

import com.gargoylesoftware.htmlunit.WebClient;

public class CrawlTools {
	protected MapToJSON mtj;
	public CrawlTools(){
		mtj = new MapToJSON();
	}
	public MapToJSON getMapToJSON(){
		return mtj;
	}
}
