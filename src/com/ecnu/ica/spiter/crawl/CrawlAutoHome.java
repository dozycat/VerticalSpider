package com.ecnu.ica.spiter.crawl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ecnu.ica.spiter.main.JobControl;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CrawlAutoHome extends CrawlJob {
	private static final Logger log = Logger.getLogger(CrawlAutoHome.class);

	public CrawlAutoHome(String[] args) {
		super();
		this.setPath("/home/dozy/auto/autohome/");
		this.setFileName("BMW");
	}

	@Override
	public ArrayList createUrl() {
		ArrayList list = new ArrayList();
		for (int i = 1; i <=131; i++) {
			list.add("http://k.autohome.com.cn/66/index_" + String.valueOf(i)
					+ ".html");
		}
		return list;
	}

	@Override
	String crawlContent(String url, WebClient client) {
		log.info("begin Crawl :" + url);
		Map map = new HashMap();
		HtmlPage page = null;
		DomNode node = null;
		HtmlAnchor a = null;
		HtmlDivision div = null;
		String res = "";
		try {
			page = client.getPage(url);
			List list = (List) page
					.getByXPath("//div[@class='mouthcon js-koubeidataitembox']");
			for (int i = 0; i < list.size(); i++) {
				Map userMap = new HashMap();
				node = (DomNode) list.get(i);
				node = (DomNode) (node
						.getFirstByXPath(".//div[@class='user-name']/a[last()]"));
				String time = node.asText().replace(" 发表", "");

				userMap.put("pubtime", time);

				List chooseConList = null;

				/**
				 * 抓取用户信息,购车信息,车型信息
				 */
				node = ((DomNode) list.get(i))
						.getFirstByXPath(".//div[@class='choose-con']");
				// System.out.println(node.asText());
				chooseConList = node.getChildNodes();
				for (int j = 0; j < chooseConList.size(); j++) {
					DomNode node2 = ((DomNode) chooseConList.get(j));
					// log.info(j);
					// log.info(node2.asText());
					List tempList = null;
					tempList = ((DomNode) chooseConList.get(j)).getChildNodes();
					// log.info(tempList.size());
					if (tempList.size() > 0) {
						// log.info(((DomNode) tempList.get(0)).asText()+0);
						// log.info(((DomNode) tempList.get(1)).asText());
						// log.info(((DomNode) tempList.get(2)).asText());
						// log.info(((DomNode) tempList.get(3)).asText());
						// log.info(((DomNode) tempList.get(4)).asText());
					}
				}

				for (int j = 0; j < chooseConList.size(); j++) {
					List tempList = null;
					try {
						tempList = ((DomNode) chooseConList.get(j))
								.getChildNodes();

					} catch (Exception e) {
						log.info(i + "\t" + j + "\t"
								+ "can not get user info from " + url);
						e.printStackTrace();
						continue;
					}
					if (tempList.size() == 0)
						continue;

					if (tempList != null) {
						if (((DomNode) tempList.get(0)).asText().contains("\n")) {
							userMap.put(
									((DomNode) tempList.get(0)).asText().split(
											"\n")[0],
									((DomNode) tempList.get(1)).asText().split(
											"\n")[1]);
							userMap.put(
									((DomNode) tempList.get(0)).asText().split(
											"\n")[0],
									((DomNode) tempList.get(1)).asText().split(
											"\n")[1]);
						} else
							userMap.put(((DomNode) tempList.get(0)).asText(),
									((DomNode) tempList.get(1)).asText());
					}
					// if (tempList != null) {
					// if (((DomNode) tempList.get(1)).asText().contains("\n"))
					// {
					// userMap.put(
					// ((DomNode) tempList.get(1)).asText().split(
					// "\n")[0],
					// ((DomNode) tempList.get(3)).asText().split(
					// "\n")[1]);
					// userMap.put(
					// ((DomNode) tempList.get(1)).asText().split(
					// "\n")[0],
					// ((DomNode) tempList.get(3)).asText().split(
					// "\n")[1]);
					// } else
					// userMap.put(((DomNode) tempList.get(1)).asText(),
					// ((DomNode) tempList.get(3)).asText());
					// }
				}

				node = ((DomNode) list.get(i))
						.getFirstByXPath(".//div[@class='choose-con mt-20']");
				chooseConList = node.getChildNodes();
				for (int j = 0; j < chooseConList.size(); j++) {
					List tempList = null;
					try {
						tempList = ((DomNode) chooseConList.get(j))
								.getChildNodes();
					} catch (Exception e) {
						continue;
					}
					if (tempList.size() > 0) {
						// log.info(((DomNode)
						// tempList.get(0)).asText().split(" ")[0]);
						// log.info(((DomNode)
						// tempList.get(0)).asText().split(" ")[1]);
					}
					if (tempList.size() > 0) {
						Map tempMap = new HashMap();
						tempMap.put("rank", ((DomNode) tempList.get(0))
								.asText().split(" ")[1]);
						userMap.put(
								((DomNode) tempList.get(0)).asText().split(" ")[0],
								tempMap);

					}

				}
				String[] temp;
				try {
					node = ((DomNode) list.get(i))
							.getFirstByXPath(".//div[@class='text-con height-none']");
					temp = node.asText().split("\n");
				} catch (Exception e) {
					node = ((DomNode) list.get(i))
							.getFirstByXPath(".//div[@class='text-con height-list']");
					temp = node.asText().split("\n");
				}
				// temp = null;
				String content = "";
				String key = "";

				String splitwordr = "】";
				String splitwordl = "【";

				splitwordr = new String(splitwordr.getBytes("UTF-8"), "UTF-8");
				splitwordl = new String(splitwordl.getBytes("UTF-8"), "UTF-8");

				HashMap commentMap = new HashMap();
				if (temp != null) {
					for (int j = 0; j < temp.length; j++) {
						if (temp[j].contains(splitwordr)) {
							key = temp[j].split(splitwordr)[0].replace(
									splitwordl, "");
							try{
							commentMap.put(key, temp[j].split(splitwordr)[1]);
							}
							catch(Exception e){
								commentMap.put(key,"");
							}
						} else {
							if (!key.equals(""))
								commentMap.put(key, commentMap.get(key)
										+ temp[j]);
						}
					}
				}
				// Set commentSet = commentMap.keySet();
				Iterator<String> it = commentMap.keySet().iterator();
				while (it.hasNext()) {
					String k = it.next();
					String comment = (String) commentMap.get(k);
					if (userMap.containsKey(k)) {
						Map tempM = (Map) userMap.get(k);
						tempM.put("comment", comment);
						userMap.put(k, tempM);
					} else {
						Map tempM = new HashMap();
						tempM.put("comment", comment);
						userMap.put(k, tempM);
					}
				}
				userMap.put("crawlURL", url);
				res = res + mtj.mapToJSON(userMap) + "\n";
				userMap = new HashMap();
			}

		} catch (Exception e) {
			log.info("can not get HTML from " + url);
			e.printStackTrace();
		}
		return res;

	}

	@Override
	public void preWebClient() {

	}
}
