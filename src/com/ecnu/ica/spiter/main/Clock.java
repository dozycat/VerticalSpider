package com.ecnu.ica.spiter.main;

public class Clock {
	private static int idx=0;
	public static synchronized int getIdx(){
		Clock.idx=idx+1;
		return idx-1;
	}
}
