//$Id: RSSObject.java,v 1.6 2004/03/25 10:09:10 taganaka Exp $
package org.gnu.stealthp.rsslib;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for all common informations about rss elements.
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * </blockquote>
 *
 * @since RSSLIB4J 0.1
 * @author Francesco aka 'Stealthp' stealthp[@]stealthp.org
 * @version 0.2
 */


public abstract class RSSObject {

  protected String about;
  protected String title;
  protected String link;
  protected String description;
  protected String pdate;
  protected RSSDoublinCoreModule dc;
  protected Hashtable dc_container;



  public RSSObject(){
    dc_container = new Hashtable();
  }

  /**
   * Set the element title
   * @param t The title
   */
  public void setTitle(String t) {
    this.title = t;
  }

  /**
   * Set about attribute  of the element (if have)
   * @param ab The about content
   */
  public void setAboutAttribute(String ab) {
    this.about = ab;
  }

  /**
   * Set the link of the resource
   * @param l The link
   */
  public void setLink(String l) {
    this.link = l;
  }

  /**
   * Set the descriprion of the element
   * @param des The description
   */
  public void setDescription(String des) {
    this.description = des;
  }

  /**
   * The publication date for the content in the channel or in the items
   * @param pubDate The date
   */
  public void setPubDate(String pubDate){
    pdate = pubDate;
  }

  public void setRSSDoublinCoreModule(RSSDoublinCoreModule m){
    dc = m;
  }

  /**
   * Get about attribute of element
   * @return The attribute value
   */
  public String getAboutAttribute() {
    return this.about;
  }

  /**
   * Get the element's title
   * @return the title
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Get the publication date of the channel or of an item
   * @return The publication date for the content in the channel
   */
  public String getPubDate(){
    return pdate;
  }

  /**
   * Get the element's link
   * @return the link
   */
  public String getLink() {
    return this.link;
  }

  /**
   * Get the element's description
   * @return the descricption
   */
  public String getDescription() {
    return this.description;
  }
  
  //-----------------------------------
  //添加时间：2013-08-14 19:32:15
  //人员：@龙轩
  //添加内容：添加getSummary()方法，返回文章摘要信息
  /**
   * Get the element's summary
   * @return the summary
   */
  public String getSummary(){

		String summary = getDescription();
		if (summary.length() >= 300) {
			summary = summary.substring(0, 300);
		}
		
		String regEx_html = "\\s|<[^>]+>|&\\w{1,5};|&#[0-9]{1,5};"; // 定义HTML标签和特殊字符的正则表达式

		Pattern pattern = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(summary);
		summary = matcher.replaceAll(""); // 过滤script标签

		if (summary.length() >= 100) {
			summary = summary.substring(0, 100);
		}
		summary = summary + "...";

		return summary;
  }
  //添加结束-----------------------------------------------

  /**
   * Get the Roubin Core object from the RSS object
   * @return The object or null
   */
  public RSSDoublinCoreModule getRSSDoublinCoreModule(){
    if (dc != null)
      return dc;
    return RSSDoublinCoreModule.buildDcModule(dc_container);

  }

  /**
   * Add a doublin core element to the object
   * @param tag The dc tag
   * @param data the dc value
   */
  public void addDoublinCoreElement(String tag,String data){
    //Remove old value
    if (dc_container.containsKey(tag)){
      dc_container.remove(tag);
    }
    dc_container.put(tag,data);
  }

  /**
   * Get DC element by hashtable
   * @return the  hashtable with key as tag and value as tag's value
   */
  public Hashtable getDoublinCoreElements(){

   if  (dc_container.size() == 0)
     return null;

   return dc_container;
  }

  /**
   * Each class have to implement this information method
   * @return An information about element
   */
  public abstract String toString();
  
}