VerticalSpider
==============

#It is a web crawler for java

## Example
### First crawler to :
```java
package com.ecnu.ica.spider.crawl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;


import com.ecnu.ica.spider.util.CrawlResult;
import com.ecnu.ica.spider.util.CrawlTools;
import com.ecnu.ica.spider.util.URL;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
/** You can extents the CrawlJob to crawl for a page.
** 
**/
public class CrawlBaiduZhidao extends CrawlJob {

	@Override
	public CrawlResult crawlContent(URL url, WebClient client, CrawlTools tools) {
		String crawlUrl = url.getUrl();
		CrawlResult result = null;
		try {
			HtmlPage page = (HtmlPage) client.getPage(crawlUrl);
			DomNode node = (DomNode) (page
					.getByXPath("//*[@id='wgt-ask']/h1/span").get(0));

			result = new CrawlResult(null, null, null);
			result.setMethod("String");

			HashMap map = new HashMap();
			map.put("title", node.asText());
			
			node=(DomNode)(page.getByXPath("//*[@class='line mt-10 q-content']").get(0));
			map.put("Q", node.asText());
			System.out.println(node.asXml());
			map.put("url", url.getUrl());

			result.setContent(tools.getMapToJSON().mapToJSON(map));

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

		return result;
	}

	@Override
	public void preWebClient() {
		// TODO Auto-generated method stub

	}

}


```
### Thanks:
* **HtmlUnit**

	A java GUI-Less browser, which allows high-level manipulation of web pages, such as filling forms and clicking links; just getPage(url), find a hyperlink, click() and you have all the HTML, JavaScript, and Ajax are automatically processed.
	[http://sourceforge.net/projects/htmlunit/](http://sourceforge.net/projects/htmlunit/)
