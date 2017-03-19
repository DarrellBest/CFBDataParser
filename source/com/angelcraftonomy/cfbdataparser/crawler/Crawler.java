package com.angelcraftonomy.cfbdataparser.crawler;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.angelcraftonomy.cfbdataparser.singleton.CFBSingleton;

public class Crawler {

	private CFBSingleton store = CFBSingleton.getInstance();
	private int depth;
	private Document doc;

	public Crawler(int depth) {
		this.depth = depth;
	}

	public void getLinks(String url) {
		// get initial page links
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception e) {
			System.out.println("Could not connect to : \n" + url + "    ---> Skipping");
		}

		Elements links = doc.select("a[href]");
		for (Element link : links) {
			String absHref = link.attr("abs:href");
			store.saveLink(absHref);
		}
		// get links per depth
		ArrayList<String> depthUrls = store.getLinks();
		System.out.println("Depth 0 Completed");
		for (int i = 0; i < depth; i++) {

			for (String nextUrl : depthUrls) {

				try {
					doc = Jsoup.connect(nextUrl).get();
				} catch (Exception e) {
					System.out.println("Could not connect to : \n" + nextUrl + "    ---> Skipping");
				}
				links.clear();
				links = doc.select("a[href]");
				for (Element link : links) {
					String absHref = link.attr("abs:href");
					store.saveLink(absHref);
				}
			}
			depthUrls = store.getLinks();
			System.out.println("Depth " + (i + 1) + " Completed");
		}
	}

}
