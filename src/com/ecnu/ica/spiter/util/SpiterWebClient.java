package com.ecnu.ica.spiter.util;

import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class SpiterWebClient {
	public static WebClient ConstructWebClient() {
		BrowserVersion.getDefault();

//		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
//				"org.apache.commons.logging.impl.NoOpLog");
//		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")
//				.setLevel(Level.OFF);
//		java.util.logging.Logger.getLogger("org.apache.commons.httpclient")
//				.setLevel(Level.OFF);

		// 设置模拟浏览器的版本型号
		WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_9);
		// 设置支持浏览器是否解析javascript和 css
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		
        webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理
//        webClient.getCookieManager().
		// webClient.getOptions()webClient.set
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		// 设置浏览器链接超时时间
		webClient.getOptions().setTimeout(60000);
		// webClient.getOptions().setTimeout(2400);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setAppletEnabled(false);
		// webClient.getOptions().setUseInsecureSSL(true);
		return webClient;
	}
}