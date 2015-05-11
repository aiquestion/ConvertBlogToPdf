package info.aiquestion.reader

import info.aiquestion.reader.httpclient.BlogSpider
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.slf4j.{Logger, LoggerFactory}

class MyScalatraServlet extends ReaderStack with JacksonJsonSupport {
  val logger = LoggerFactory.getLogger(getClass)
  protected implicit val jsonFormats: Formats = DefaultFormats

  get("/") {
    redirect("index.html")
  }

  get("/BlogList") {
    val blogList = params("bloglist")
    val next = params("next")
    val pattern = params("pattern")
    val spider = (new BlogSpider)
    spider.retrieveBlogList(blogList, next, pattern)
    spider.blogUrls.toList.sorted
  }

  post("/PDF") {
    val pdfInfo = parsedBody.extract[PDFInfo]
    logger.info("Start converting,urls:{0}", pdfInfo.list.toString())
    (new Processor).process(pdfInfo.list, pdfInfo.email)
    null
  }
}

case class PDFInfo(list: List[String], email: String)
