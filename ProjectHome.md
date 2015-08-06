描述在rsslib4j和newrsslib4j源码基础上，保持外接口不变，添加了rss字节流编码探测及内部编码转换，理论上支持所有的编码，组件使用较原版更健壮、更稳定。修改了RSSParse类的部分代码，添加了User-Agent，解决了访问url时，返回403 for HTTP的错误。

代码库
svn checkout http://myrsslib4j.googlecode.com/svn/trunk/ myrsslib4j -read-only ----

change log
#1,引入了mozilla的jchardet包，作为组件一部分，用于探测rss字节流编码，新增了一辅助类Chaset.java 提供了几个静态编码探测方法：

String guess(URL url)；
String guess(String path)
String guess(InputStream in)
#2,引入UnicodeReader替换原有的InputStreamReader，不用InputStreamReader的原因在于某些UTF-8编码开始会带有BOM (Byte Order Mark) ;导致解析xml失败：Content is not allowed in prolog. 这是jdk1.5的bug,1.6已修复；在sun的buglist里发现了这个问题，并找到了UnicodeReader

#3，改造RSSParser解析类，新加一个类属性 private InputSource is; 替换原有的InputStream，因原有没有考虑国际化编码问题，而InputSource可以Reader构造产生，改造完如下：

/
  * Set rss resource by URL
  * @param ur the remote url
  * @throws RSSException
  * 
> public void setXmlResource(URL ur) throws RSSException{
> > try{


> URLConnection con = u.openConnection();
> con.setReadTimeout(10000);
> > String charset = Charset.guess(ur);
> > is = new InputSource (new UnicodeReader(con.getInputStream(),charset));

> if (con.getContentLength() == -1 && is == null){
> > this.fixZeroLength();

> }
> }catch(IOException e){
> > throw new RSSException("RSSParser::setXmlResource fails: "+e.getMessage());

> }
> }
user guide 代码片段：

> URL url = new URL(rssArr[i](i.md));
> RSSHandler handler = new RSSHandler();

> RSSParser.parseXmlFile(url, handler, false);
> RSSChannel ch = handler.getRSSChannel();
> System.out.println(ch.toString());
> List

&lt;RSSItem&gt;

 lst = handler.getRSSChannel().getItems();
> for (int j = 0; j < lst.size(); j++) {
> RSSItem itm = lst.get(j);
> System.out.println(itm.toString());
> }
如果使用过程中有任何问题请与我联系: cclsuperstar@126.com @龙轩