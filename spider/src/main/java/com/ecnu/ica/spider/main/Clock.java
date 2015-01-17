package com.ecnu.ica.spider.main;
/**
 * 
 * @author dozy
 * 这个类用于进行多线程并发操作时候的id值获取。
 */
public class Clock {
	private static int idx=0;
	public static synchronized int getIdx(){
		Clock.idx=idx+1;
		return idx-1;
	}
}
