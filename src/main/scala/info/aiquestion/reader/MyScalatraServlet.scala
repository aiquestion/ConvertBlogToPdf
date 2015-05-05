package info.aiquestion.reader

import org.scalatra._
import scalate.ScalateSupport
import info.aiquestion.reader.httpclient.BlogSpider
// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

// JSON handling support from Scalatra
import org.scalatra.json._

class MyScalatraServlet extends ReaderStack with JacksonJsonSupport {
protected implicit val jsonFormats: Formats = DefaultFormats

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }
  
  get("/BlogList"){
    val blogList = params("bloglist")
    val next = params("next")
    val pattern = params("pattern")
    val spider = (new BlogSpider)
    spider.retrieveBlogList(blogList, next, pattern)
    spider.blogUrls.toList.sorted
  }
  
  
  post("/PDF"){
    val pdfInfo = parsedBody.extract[PDFInfo]
    println(pdfInfo)
    val res = (new Processor).process(pdfInfo.list, pdfInfo.email)
    null
  }
}
case class PDFInfo(list:List[String], email:String)
