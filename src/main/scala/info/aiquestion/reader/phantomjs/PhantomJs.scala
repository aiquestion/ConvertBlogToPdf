package info.aiquestion.reader.phantomjs
import dispatch._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

class PhantomJs {
  def generate(url:String, destination:String)(implicit ctx: ExecutionContext): Int =
  {
    // send request to phantomJs server,listener on 9999.
    val req = dispatch.url("http://localhost:9999")
    def myRequestAsJson = req.setContentType("application/json", "UTF-8")
    def myPostWithBody = myRequestAsJson << "{\"url\":\""+url+"\",\"output\":\""+destination+"\"}"
    val value = dispatch.Http(myPostWithBody OK as.String)
    value().toInt
  }
}

object PhantomJs 
{
  def main(args: Array[String]) {
    val phantomjs = new PhantomJs
    val res = phantomjs.generate("""http://www.cnblogs.com/Ninputer/archive/2011/06/07/2074632.html""",
        """c:\d9.pdf""")
    println(res)
  }
}