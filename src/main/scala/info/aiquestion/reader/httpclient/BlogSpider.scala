package info.aiquestion.reader.httpclient
import dispatch._
import scala.util.Success
import scala.util.Try
import scala.util.Failure
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scala.collection.mutable.Map
import scala.collection.mutable.Set

class BlogSpider {
  val urlPathMap = Map[String, Boolean]()
  val blogUrls = Set[String]()

  def retrievePage(target: String)(implicit ctx: ExecutionContext): Future[String] = {
    dispatch.Http(url(target) OK as.String)
  }

  def parseUrl(content: String, nextPatten: String, blogPatten: String): (List[String], List[String]) = {
    val htmldoc = content
    println("""parsing....""")
    val nexts = (nextPatten.r findAllIn htmldoc).toList
    val blogs = (blogPatten.r findAllIn htmldoc).toList
    (nexts, blogs)
  }

  def retrieveBlogList(url: String, nextPatten: String, blogPatten: String) {

    def retrieveBlogLists(urls: List[String]): Unit = {
      for (u <- urls) {
        val page = retrievePage(u)
        val r = parseUrl(page(), nextPatten, blogPatten)
        val next = r._1.filter { (x) => !urlPathMap.contains(x) }
        for (b <- r._2)
          blogUrls += b
        for (n <- next)
          urlPathMap += (n -> true)
        retrieveBlogLists(next)
      }
    }
    retrieveBlogLists(List(url))
  }
}

object BlogSpider{
    def main(args: Array[String]) {
      val spider = new BlogSpider
    spider.retrieveBlogList("""http://www.cnblogs.com/Ninputer/""",
      """http://www\.cnblogs\.com/Ninputer/default\.html\?page=\d""",
      """http://www\.cnblogs\.com/Ninputer/archive/\d\d\d\d/\d\d/\d\d/\d\d\d\d\d\d\d.html""")
      
    val blogs = spider.blogUrls.toList.sorted
    for (b <- blogs)
      println(b)
  }
}