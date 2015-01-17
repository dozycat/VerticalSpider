package com.ecnu.ica.spider.crawl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.ecnu.ica.spider.file.DownLoadFile;
import com.ecnu.ica.spider.util.CrawlResult;
import com.ecnu.ica.spider.util.CrawlTools;
import com.ecnu.ica.spider.util.URL;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CrawlDrugbank extends CrawlJob {

	private static final Logger log = Logger.getLogger(CrawlDrugbank.class);

	@Override
	public CrawlResult crawlContent(URL url, WebClient client, CrawlTools tools) {
		mtj = tools.getMapToJSON();
		String u = url.getUrl();
		CrawlResult result = new CrawlResult(null, null, null);
		HtmlPage page = null;
		try {
			page = (HtmlPage) client
					.getPage("http://www.drugbank.ca/search?utf8=%E2%9C%93&query="
							+ u);
			log.info("crawl for :"
					+ "http://www.drugbank.ca/search?utf8=%E2%9C%93&query=" + u);
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
		if (page != null) {
			try {
				DomNode node = (DomNode) (page
						.getFirstByXPath("/html/body/main/table/tbody/tr/td[2]/div[1]/strong/a"));
				String[] temp = node.getAttributes().getNamedItem("href")
						.getNodeValue().split("/");
				String newID = temp[temp.length - 1];
				log.info("crawl http://www.drugbank.ca/structures/structures/small_molecule_drugs/"
						+ newID + ".mol");

				try {
					BufferedInputStream bf = DownLoadFile
							.getHttpInputStream("http://www.drugbank.ca/structures/structures/small_molecule_drugs/"
									+ newID + ".mol");
					result.setContent(bf);
					result.setMethod("BufferedInputStream");
					result.setName(u + ".mol");
					return result;
				} catch (Exception e) {
					log.info(e.toString());
				}

			} catch (Exception e) {

			}
		}

		return null;
	}

	@Override
	public void preWebClient() {
		// TODO Auto-generated method stub

	}

}
