//$Id: RSSParser.java,v 1.9 2004/03/28 13:07:16 taganaka Exp $
package org.gnu.stealthp.rsslib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.javali.util.UnicodeReader;
import org.mozilla.intl.chardet.Charset;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * RSS Parser.
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * </blockquote>
 *
 * @since RSSLIB4J 0.1
 * @author Francesco aka 'Stealthp' stealthp[@]stealthp.org
 * @version 0.2<br />
 * 
 * modified by sawen21  2009-01-15<br />
 * 修复了rsslib4j解析xml编码错误以及UTF-8读取BOM标识错误
 */

public class RSSParser {

  private  SAXParserFactory factory = RSSFactory.getInstance();
  private DefaultHandler hnd;
  private File f;
  private URL u;
  private InputSource is;  //newly added by liqiang
  private boolean validate;
  public RSSParser(){
    validate = false;
  }

  /**
   * Set the event handler
   * @param h the DefaultHandler
   *
   */
  public void setHandler(DefaultHandler h){
    hnd = h;
  }

  /**
   * Set rss resource by local file name
   * @param file_name loca file name
   * @throws RSSException
   */
  public void setXmlResource(String file_name) throws RSSException{
    f = new File(file_name);
    try{
      is = new InputSource(new FileInputStream(f));   //TODO  需要判断bom及编码
    }catch(Exception e){
      throw new RSSException("RSSParser::setXmlResource fails: "+e.getMessage());
    }
  }

  /**
   * Set rss resource by URL
   * @param ur the remote url
   * @throws RSSException
   */
  public void setXmlResource(URL ur) throws RSSException{
    try{
      
      URLConnection con = u.openConnection();
      
      //-----------------------------
      //添加时间：2013-08-14 21:00:17
      //人员：@龙轩
      //博客：http://blog.csdn.net/xiaoxian8023
      //添加内容：由于服务器屏蔽java作为客户端访问rss，所以设置User-Agent
      con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
      //-----------------------------

      con.setReadTimeout(10000);
	  String charset = Charset.guess(ur);
	  is = new InputSource (new UnicodeReader(con.getInputStream(),charset));
      if (con.getContentLength() == -1 && is == null){
        this.fixZeroLength();
      }
    }catch(IOException e){
      throw new RSSException("RSSParser::setXmlResource fails: "+e.getMessage());
    }
  }

  /**
   * set true if parse have to validate the document
   * defoult is false
   * @param b true or false
   */
  public void setValidate(boolean b){
    validate = b;
  }

  /**
   * Parse rss file
   * @param filename local file name
   * @param handler the handler
   * @param validating validate document??
   * @throws RSSException
   */
  public static void parseXmlFile (String filename, DefaultHandler handler, boolean validating) throws RSSException{
    RSSParser p = new RSSParser();
    p.setXmlResource(filename);
    p.setHandler(handler);
    p.setValidate(validating);
    p.parse();
  }

  /**
   * Parse rss file from a url
   * @param remote_url remote rss file
   * @param handler the handler
   * @param validating validate document??
   * @throws RSSException
 * @throws IOException 
   */
  public static void parseXmlFile(URL remote_url, DefaultHandler handler, boolean validating) throws RSSException{
    RSSParser p = new RSSParser();
    p.u = remote_url;
    p.setXmlResource(remote_url);
    p.setHandler(handler);
    p.setValidate(validating);
    p.parse();
  }

  /**
   * Try to fix null length bug
   * @throws IOException
   * @throws RSSException
   */
  private void fixZeroLength() throws IOException, RSSException {

    File ft = File.createTempFile(".rsslib4jbugfix", ".tmp");
    ft.deleteOnExit();
    FileWriter fw = new FileWriter(ft);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is.getByteStream()));
    BufferedWriter out = new BufferedWriter(fw);
    String line = "";
    while ( (line = reader.readLine()) != null) {
      out.write(line + "\n");
    }
    out.flush();
    out.close();
    reader.close();
    fw.close();
    setXmlResource(ft.getAbsolutePath());

  }

  /**
   * Call it at the end of the work to preserve memory
   */
  public  void free(){
    this.factory = null;
    this.f       = null;
    this.is = null;
    this.hnd     = null;
    System.gc();
  }

  /**
   * Parse the document
   * @throws RSSException
   */
  public  void parse() throws RSSException{
    try {
      factory.setValidating(validate);
      // Create the builder and parse the file
      factory.newSAXParser().parse(is,hnd);
    }
    catch (SAXException e) {
      throw new RSSException("RSSParser::parse fails: "+e.getMessage());
    }
    catch (ParserConfigurationException e) {
     throw new RSSException("RSSParser::parse fails: "+e.getMessage());
    }
    catch (IOException e) {
     throw new RSSException("RSSParser::parse fails: "+e.getMessage());
    }

  }

}