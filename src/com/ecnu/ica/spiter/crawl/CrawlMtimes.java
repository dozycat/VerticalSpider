package com.ecnu.ica.spiter.crawl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecnu.ica.spiter.file.SimpleToFile;
import com.ecnu.ica.spiter.main.Clock;
import com.ecnu.ica.spiter.main.JobControl;
import com.ecnu.ica.spiter.util.MapToJSON;
import com.ecnu.ica.spiter.util.SpiterWebClient;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CrawlMtimes extends CrawlJob {

	private long beginNum = 0;
	private long size = 0;
	private String baseUrl = "";

	public CrawlMtimes() {
		super();
	}

	public CrawlMtimes(String[] args) {
		super();
		for (int i = 0; i < args.length; i++) {

			if (args[i].equals("-baseurl")) {
				baseUrl = args[i + 1];
			}
			if (args[i].equals("-beginNum")) {
				beginNum = Long.parseLong(args[i + 1]);
			}
			if (args[i].equals("-sizeNum")) {
				size = Long.parseLong(args[i + 1]);
			}
			if (args[i].equals("-help")) {
					
			}

		}
	}

	@Override
	public ArrayList createUrl() {
		ArrayList urlList = new ArrayList();
		for (long i = beginNum; i < beginNum + size; i++) {
			urlList.add(baseUrl + String.valueOf(i));
		}
		return urlList;
	}

	@Override
	public String crawlContent(String url,WebClient client) {
		HtmlPage page = null;
		DomNode node = null;
		HtmlAnchor a = null;
		HtmlDivision div = null;
		String content = "";
		Map map = new HashMap();

		try {
			page = webClient.getPage(url);

			// webClient.waitForBackgroundJavaScript(10000*3);
			// webClient.setJavaScriptTimeout(0);
			node = page.getFirstByXPath("//*[@id='widget-playcount']");

			System.out.println(page.getTitleText());

			String title = null;
			// *[@id="db_head"]/div[3]/div/div[1]/h1
			title = ((HtmlDivision) page
					.getFirstByXPath("//*[@id='db_head']/div[2]/div/div[1]"))
					.asText().replace("&quot;", "").replace("?", "")
					.replace(":", "").replace("*", "").replace("\n", "|");
			if (title == "")
				return null;
			map.put("标题", title);
			map.put("crawlURL", url);
			map.put("crawlTime", fm.format(System.currentTimeMillis()));
			map.put("Num", Clock.getIdx());
			// *[@id="movie_warp"]/div[4]/div[2]/div[1]/div/div/dl/dd[1]/a
			map.put("导演",
					((DomNode) page
							.getFirstByXPath("//*[@id='movie_warp']/div[2]/div[3]/div/div[4]/div[2]/div[1]/div/div/dl/dd[1]/a"))
							.asText());

			page = webClient.getPage(url + "/fullcredits.html");

			List credictList = page.getByXPath("//div[@class='credits_list']");
			credictList = page.getByXPath("//dd");

			Map actorMap = new HashMap();
			for (int idx = 0; idx < credictList.size(); idx++) {
				DomNode node2;
				node2 = (DomNode) credictList.get(idx);
				List temp = node2.getChildNodes();
				// System.out.println(temp.size());

				if ((temp.size() != 5) && (temp.size() != 3))
					continue;
				try {
					actorMap.put(
							((DomNode) temp.get(1)).asText().replace("\n", " "),
							((DomNode) temp.get(3)).asText().replace("\n", " "));
				} catch (Exception e) {
					actorMap.put(
							((DomNode) temp.get(1)).asText().replace("\n", " "),
							"");
				}
			}
			map.put("演员角色表", actorMap);
			page = webClient.getPage(url + "/plots.html");
			credictList = page.getByXPath("//div[@class='plots_box']");
			Map plotMap = new HashMap();
			for (int idx = 0; idx < Math.min(credictList.size(), 3); idx++) {
				div = (HtmlDivision) credictList.get(idx);
				plotMap.put(String.valueOf(idx), div.asText());
			}
			map.put("剧情简介", plotMap);
			// System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (map.size() == 0)
			return null;
		return mtj.mapToJSON(map);
		// return content;

	}

	public static void main(String[] args) {
		CrawlMtimes cm = new CrawlMtimes();
		WebClient webClient = SpiterWebClient.ConstructWebClient();
		MapToJSON mtj = new MapToJSON();
		System.out.println(cm
				.crawlContent("http://www.dianping.com/shop/16964585",webClient));

	}

	@Override
	public  void preWebClient() {
		
	}

}
