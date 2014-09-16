package com.ecnu.ica.spiter.crawl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.http.cookie.Cookie;
import org.apache.log4j.Logger;

import net.sf.ezmorph.Morpher;
import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.bean.BeanMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CrawlDianPing extends CrawlJob {
	private static final Logger log = Logger.getLogger(CrawlDianPing.class);

	private static final Object avgPrice = null;

	private TreeMap beforeMap = new TreeMap();
	private JSONObject jsonObject;
	private Object bean;
	private String inPath;
	private String outPath;
	private int delayTime = 1000;

	public CrawlDianPing(String[] args) {
		super();
		String path = "/default";
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-o")) {
				this.setPath(args[i + 1]);
				outPath = args[i + 1];
			}
			if (args[i].equals("-i")) {
				path = args[i + 1];
				inPath = args[i + 1];
			}
			if (args[i].equals("-d")) {
				// this.setPath(args[i + 1]);
				this.delay_time = Integer.valueOf(args[i + 1]);
			}
		}
		File file = new File(path);
		log.info("loading the pre-datas...please waiting... for each thread..");
		int n = 0;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				// HashMap map=new HashMap();
				n++;
				if (n % 10000 == 0)
					log.info("loading ....." + n);
				jsonObject = JSONObject.fromObject(tempString);
				bean = JSONObject.toBean(jsonObject);
				// HashMap map = (HashMap) JSONObject.toBean(jsonObject,
				// HashMap.class);
				this.beforeMap.put(new String(PropertyUtils.getProperty(bean, "URL")
						.toString().getBytes(),"UTF-8"), new String(tempString.getBytes(),"UTF-8"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("loading completely...for one thread");

	}

	private WebRequest createWebRequest(String url) {
		WebRequest request = null;
		try {
			request = new WebRequest(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		request.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		request.setAdditionalHeader("Accept-Encoding", "gzip,deflate,sdch");
		request.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		request.setAdditionalHeader("Connection", "keep-alive");
		request.setAdditionalHeader("Referer",
				"http://www.dianping.com/search/category/1/10/g102");
		request.setAdditionalHeader(
				"User-Agent",
				"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/34.0.1847.116 Chrome/34.0.1847.116 Safari/537.36");
		// request.setAdditionalHeader(
		// "Cookie",
		// "_hc.v=\"\\\"75c5b499-6fc2-4a60-9139-49a7cf714a2f.1403247777\\\"\"; m_rs=e8489bb1-ada0-466d-bce7-0ad40e903f0b; is=74844153031; pgv_pvi=9143238656; _tr.u=1pwG492fLpl6Ff6I; ipbh=1404125336323; abtest=\\\"51,131\\|48,124\\|52,133\\|47,122\\|44,107\\|45,115\\\"; tc=1; t_track=D6306066; t_rct=6306066; t_refer=1; PHOENIX_ID=0a016740-147347d9044-d797; _tr.s=E2hkKbs1Ve82m24Z; s_ViewType=2; JSESSIONID=E54553B87DCF720F0C4508DBB8623A6C; aburl=1; cy=1; cye=shanghai; __utma=1.104175536.1405147757.1405229210.1405343717.8; __utmc=1; __utmz=1.1405343717.8.2.utmcsr=t.dianping.com|utmccn=(referral)|utmcmd=referral|utmcct=/shanghai; ab=; lb.dp=3741450506.20480.0000");
		//
		return request;
	}

	@Override
	String crawlContent(String url, WebClient client) {
		/**
		 * 加载之前的信息
		 */
		WebRequest request;

		CookieManager CM = client.getCookieManager();

		String msg = (String) this.beforeMap.get(url);
		jsonObject = JSONObject.fromObject(msg);
		bean = JSONObject.toBean(jsonObject);
		HashMap map = (HashMap) JSONObject.toBean(jsonObject, HashMap.class);

		// String str = mtj.mapToJSON(map);
		HtmlPage page = null;
		DomNode node = null;
		HtmlAnchor a = null;
		HtmlDivision div = null;
		for (int retry = 0; retry < 3; retry++) {
			try {
				try {
					// request = this.createWebRequest(url);
					// page = client.getPage(request);
					page = client.getPage(url);
					node = page.getFirstByXPath("//*[@id='FAQ']");
					if (!node.asText().equals("")) {
						log.info("MayBe E:403 sleep 30sec for this thread !");
						Thread.sleep(1000 * 30);
						log.info("The thread is wake up !");
						continue;
					}
				} catch (Exception e) {
					Thread.sleep(this.delay_time);
				}
				List tempList = null;
				try {
					// log.info(tempList.size());
					node = (DomNode) page
							.getFirstByXPath("//div[@class='rst-taste']");
					try {
						String countNum = ((DomNode) (node
								.getFirstByXPath(".//*[@itemprop='count']")))
								.asText();
						map.put("点评数", countNum);
					} catch (Exception e) {
						log.info("cannot crawl countNum from " + url);
						map.put("点评数", 0);
					}

					try {
						tempList = node.getByXPath(".//*[@class='rst']/strong");
						map.put("口味", ((DomNode) tempList.get(0)).asText());
						map.put("环境", ((DomNode) tempList.get(1)).asText());
						map.put("服务", ((DomNode) tempList.get(2)).asText());

					} catch (Exception e) {
						log.info("cannot crawl rank from " + url);
						log.info(page.asXml());
						return null;
					}

				} catch (Exception e) {
					log.info("cannot crawl teaste msg from " + url);
				}
				/**
				 * 抓取目录信息
				 */
				try {
					node = (DomNode) page
							.getFirstByXPath("//*[@id='top']/div[@class='breadcrumb']");
					map.put("目录", node.asText());
				} catch (Exception e) {
					log.info("抓不到目录信息" + url);

				}

				/**
				 * 抓取位置电话信息
				 */
				node = (DomNode) page
						.getFirstByXPath("//div[@class='shop-location']");
				try {
					String address = ((DomNode) node
							.getFirstByXPath(".//a[@class='link-dk']"))
							.asText()
							+ ((DomNode) node.getFirstByXPath("./ul/li/span"))
									.asText();
					map.put("地址", address);

				} catch (Exception e) {
					log.info("抓不到位置信息");
				}

				/**
				 * 电话信息
				 */
				try {
					String phone = ((DomNode) node
							.getFirstByXPath("./ul/li[2]/span")).asText();
					map.put("电话", phone);
					// log.info(phone);
				} catch (Exception e) {
					log.info("抓不到电话信息" + url);
				}

				/**
				 * 抓取简介信息
				 */
				try {
					node = (DomNode) page
							.getFirstByXPath("//div[@class='desc-list Hide']");
					tempList = node.getByXPath("./ul/li");
					for (int i = 0; i < tempList.size(); i++) {
						String info = "";
						String key = ((DomNode) ((DomNode) (tempList.get(i)))
								.getFirstByXPath("./em")).asText();
						if (key.contains("餐厅简介")) {
							info = ((DomNode) ((DomNode) (tempList.get(i)))
									.getFirstByXPath("./text()")).asText();
							// log.info(info);
							map.put("餐厅简介", info);
						}
						if (key.contains("分类标签")) {
							info = ((DomNode) ((DomNode) (tempList.get(i)))
									.getFirstByXPath("./div")).asText()
									.replace("更多", "");
							// log.info(info);
							map.put("分类标签", info);
						}
					}
				} catch (Exception e) {
					log.info("抓不到简介信息" + url);
				}
				/**
				 * 抓取推介菜
				 */
				try {
					node = (DomNode) page
							.getFirstByXPath("//div[@class='vegetable']");
					tempList = node.getByXPath("./ul/li/a");
					Map temMap = new HashMap();

					for (int i = 0; i < tempList.size(); i++) {
						temMap.put(String.valueOf(i),
								((DomNode) tempList.get(i)).asText());
					}
					if (temMap.size() > 0) {
						map.put("推荐菜", temMap);
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("抓不到推荐菜" + url);
				}

				try {
					Thread.sleep(this.delay_time);
					// request = this.createWebRequest(url + "/photos/tag-价目表");
					// page = client.getPage(request);
					try {
						page = client.getPage(url + "/photos/tag-价目表");
					} catch (Exception e) {
						Thread.sleep(this.delay_time);
						page = client.getPage(url + "/photos/tag-价目表");
					}
					node = (DomNode) page
							.getFirstByXPath("//div[@class='picture-square']");
					tempList = node.getByXPath(".//div[@class='img']/a");
					Map temMap = new HashMap();
					for (int i = 0; i < tempList.size(); i++) {
						DomNode temNode = (DomNode) (tempList.get(i));
						temMap.put(String.valueOf(i), temNode.getFirstChild()
								.getAttributes().getNamedItem("src")
								.getNodeValue());
					}
					if (temMap.size() > 0)
						map.put("菜单", temMap);

				} catch (Exception e) {
					e.printStackTrace();
					log.info("抓不到菜单" + url + "/photos/tag-价目表");
				}

				try {
					int idx = 0;
					Map commentMap = new HashMap();
					for (int k = 1; k <= 1; k++) {
						Thread.sleep(this.delay_time);
						// request = this.createWebRequest(url
						// + "/review_all?pageno=" + String.valueOf(k));
						// page = client.getPage(request);
						try {
							page = client.getPage(url + "/review_all?pageno="
									+ String.valueOf(k));
						} catch (Exception e) {
							/**
							 * TODO!!!!!!!!!!!!!
							 */
							Thread.sleep(this.delay_time);
							page = client.getPage(url + "/review_all?pageno="
									+ String.valueOf(k));
						}
						tempList = page
								.getByXPath("//div[@class='J_brief-cont']");
						for (int i = 0; i < tempList.size(); i++) {
							commentMap.put(String.valueOf(idx),
									((DomNode) tempList.get(i)).asText());
							idx++;
						}
						if (commentMap.size() > 0) {
							map.put("评论", commentMap);
						}
					}
				} catch (Exception e) {
					log.info("抓不到菜单 or 评论不足10页" + url + "/review_all");
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mtj.mapToJSON(map);
		}
		return null;
	}

	@Override
	public ArrayList createUrl() {
		// TODO Auto-generated method stub
		log.info("loading the urls...please waiting...");
		File file = new File(this.inPath);
		BufferedReader reader = null;
		ArrayList list = new ArrayList();
		int n = 0;

		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			n++;
			if (n % 10000 == 0)
				log.info("loading ....." + n);
			while ((tempString = reader.readLine()) != null) {
				// HashMap map=new HashMap();
				JSONObject jsonObject = JSONObject.fromObject(tempString);
				Object bean = JSONObject.toBean(jsonObject);
				list.add(PropertyUtils.getProperty(bean, "URL"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("loading the urls completely!...the size is" + list.size());
		return list;
	}

	@Override
	public void preWebClient() {
		// TODO Auto-generated method stub

	}

}
