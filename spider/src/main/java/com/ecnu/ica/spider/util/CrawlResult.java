package com.ecnu.ica.spider.util;

public class CrawlResult {
	private Object content = null;
	private String outpath = null;
	private String outname = null;
	private String method = "String";

	public CrawlResult(Object content, String outpath, String outname) {
		this.content = content;
		this.outpath = outpath;
		this.outname = outname;
	}

	public CrawlResult() {

	}

	public void setMethod(String m) {
		this.method = m;
	}

	public String getMethod() {
		return this.method;
	}

	public Object getContent() {
		return this.content;
	}

	public String getOutpath() {
		return this.outpath;
	}

	public String getOutName() {
		return this.outname;
	}

	public void setName(String name) {
		this.outname = name;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
