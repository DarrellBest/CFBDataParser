package com.angelcraftonomy.cfbdataparser.main;

import com.angelcraftonomy.cfbdataparser.crawler.Crawler;
import com.angelcraftonomy.cfbdataparser.singleton.LinkStore;

public class MainDriver {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		LinkStore store = LinkStore.getInstance();
		Crawler crawler = new Crawler(0);

		// https://www2.bc.edu/ilker-yuce/Spring11/MT2100102/mt210test.html
		// https://people.cs.clemson.edu/~mark/330/
		String url = "https://people.cs.clemson.edu/~mark/330";
		crawler.getLinks(url);
		// crawler.getLinks("http://www.reddit.com");

		System.out.print(store.toString());
		// download files found
		store.downloadFiles();

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Elapsed Time: " + elapsedTime / 1000 + "." + elapsedTime % 1000 + " seconds");
	}

}
