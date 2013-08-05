package org.javali.util.test;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import org.gnu.stealthp.rsslib.RSSChannel;
import org.gnu.stealthp.rsslib.RSSException;
import org.gnu.stealthp.rsslib.RSSHandler;
import org.gnu.stealthp.rsslib.RSSItem;
import org.gnu.stealthp.rsslib.RSSParser;


public class RssNewsFetcher {
	private final static String[] rssArr = new String[] {
			"http://rss.zol.com.cn/labs.xml",
			"http://cn.engadget.com/rss.xml",
			"http://www.donews.com/GroupFeed.aspx?G=5B1D5178-138D-4D42-B370-5198FDF5AF34",
			"http://www.zaobao.com/zg/zg.xml",
			"http://news.163.com/special/r/00011K6L/rss_newstop.xml",
			"http://rss.zol.com.cn/news.xml",
			"http://www.douban.com/feed/review/movie",
			"http://blog.csdn.net/xiaoxian8023/rss/list" 
	};

	public void testFetchRssNews() throws IOException {
		for (int i = 0; i < rssArr.length; i++) {
			System.out.println("STARTING TO FETCH FROM : " + rssArr[i]);
			try {
				URL url = new URL(rssArr[i]);
				RSSHandler handler = new RSSHandler();
				
				RSSParser.parseXmlFile(url, handler, false);
				RSSChannel ch = handler.getRSSChannel();
				System.out.println(ch.toString());
				LinkedList<RSSItem> lst = handler.getRSSChannel().getItems();
				for (int j = 0; j < lst.size(); j++) {
				    RSSItem itm = lst.get(j);
				    System.out.println(itm.toString());
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (RSSException e) {
				e.printStackTrace();
			}
			
			
		}
	}
	
	public static void main(String[] args) throws IOException{
		RssNewsFetcher fetcher = new RssNewsFetcher();
		fetcher.testFetchRssNews();
	}

}
