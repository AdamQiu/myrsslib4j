package org.mozilla.intl.chardet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * @author liqiang 2009-01-15
 * judge charset from InputStream , File  and URL
 *
 */
public class Charset {

	private static String encoding = null;
	private static boolean found = false;
	
	/**
	 * judge charset from byte stream source 
	 * @param in  byte_stream
	 * @return    encoding 
	 * @throws IOException
	 */
	public static String guess(InputStream in) throws IOException {
		nsDetector det = new nsDetector();
		det.Init(new nsICharsetDetectionObserver() {
			public void Notify(String charset) {
				encoding = charset;
			}
		});

		byte[] buf = new byte[1024];
		int len;
		boolean done = false;
		boolean isAscii = true;

		while ((len = in.read(buf, 0, buf.length)) != -1) {

			if (isAscii)
				isAscii = det.isAscii(buf, len);

			if (!isAscii && !done)
				done = det.DoIt(buf, len, false);
		}
		det.DataEnd();

		if (isAscii) {
			found = true;
		}

		if (!found) {
			String prob[] = det.getProbableCharsets();
			for (int i = 0; i < prob.length; i++) {
			}
		}
		return encoding;
	}
	//judge from url  
	public static String guess(URL url) throws IOException {
		
		//-----------------------------
		//修改时间：2013-08-14 21:00:17
		//人员：@龙轩
		//博客：http://blog.csdn.net/xiaoxian8023
		//修改内容：注释InputStream，创建URLConnection，设置User-Agent，通过URLConnection对象创建InputStream
		
		//InputStream in = url.openStream();
		URLConnection con = url.openConnection();
	    con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
	    InputStream in = con.getInputStream();
	 	//-----------------------------

		return guess(in);
	}
	
	//judge from URLConnection  
	public static String guess(URLConnection conn) throws IOException {
		InputStream in = conn.getInputStream();
		return guess(in);
	}
	
	//judge from file path 
	public static String guess(String path) throws IOException {
		InputStream in = new FileInputStream(path);
		return guess(in);
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println(Charset.guess(new URL("http://www.google.com/ig/api?hl=zh_cn&weather=beijing")));
		System.out.println(Charset.guess("d:/solidot.xml"));
	}

}
