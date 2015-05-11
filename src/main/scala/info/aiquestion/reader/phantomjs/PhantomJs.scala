package info.aiquestion.reader.phantomjs
import info.aiquestion.reader.Config
import scala.sys.process._

class PhantomJs {

  
  def generate(url:String, destination:String) : Int =
  {
    Seq(Config.PHANTOMJSBIN,"""--disk-cache=true""", Config.PHANTOMJSSCRIPT, url, destination, Config.KINDLESIZE).!
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