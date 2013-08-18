package org.javali.util.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.gnu.stealthp.rsslib.RSSChannel;
import org.gnu.stealthp.rsslib.RSSException;
import org.gnu.stealthp.rsslib.RSSHandler;
import org.gnu.stealthp.rsslib.RSSItem;
import org.gnu.stealthp.rsslib.RSSParser;




public class RssNewsFetcher {
	
	/**
	 * rss订阅列表
	 */
	private final static String[] rssArr = new String[] {
//			"http://news.baidu.com/n?cmd=1&class=civilnews&tn=rss",
			"http://xiaoxian100.blog.163.com/rss",
			"http://blog.csdn.net/xiaoxian8023/rss/list" 
	};

	/**
	 * 测试解析rss订阅
	 * @throws IOException
	 */
	public void testFetchRssNews() throws IOException {
		for (int i = 0; i < rssArr.length; i++) {
			try {
				
				//获取rss订阅地址
				URL url = new URL(rssArr[i]);
				RSSHandler handler = new RSSHandler();
				
				//解析rss
				RSSParser.parseXmlFile(url, handler, false);
				
				//获取Rss频道信息
				RSSChannel ch = handler.getRSSChannel();
				
				System.out.println("---------------------------------------------");
				System.out.println("博客标题：" + ch.getTitle());
				System.out.println("博客链接：" + ch.getLink());
				System.out.println("博客描述：" + ch.getDescription());
				System.out.println("博客语言：" + ch.getLanguage());
				System.out.println("发布时间：" + ch.getPubDate());
				System.out.println("图片地址：" +ch.getRSSImage().getUrl());
				System.out.println("图片指向：" +ch.getRSSImage().getLink());

				System.out.println("共有文章数目为：" + ch.getItems().size());
//				for(i=0;i<ch.getItems().size();i++){
				for(int j=0;j<1;j++){
					RSSItem item = (RSSItem)ch.getItems().get(j);
					System.out.println("文章标题：" + item.getTitle());
					System.out.println("文章摘要：" + item.getSummary());
					//System.out.println("文章正文：" + item.getDescription());
					//这里为了演示方便，我取正文的前100个字符，真实使用时，选用上面那句代码
					System.out.println("文章正文：" + item.getDescription().substring(0, 100));
					System.out.println("文章链接：" + item.getLink());
					System.out.println("发表时间：" + item.getPubDate());
					System.out.println("文章作者：" + item.getAuthor());
					System.out.println("最新修改：" + item.getDate());
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
