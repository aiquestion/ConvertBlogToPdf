package info.aiquestion.reader.phantomjs
import scala.sys.process._
import info.aiquestion.reader.Config

class PhantomJs {

  
  def generate(url:String, destination:String) = 
  {
    Seq(Config.PHANTOMJSBIN,"""--disk-cache=true""", Config.PHANTOMJSSCRIPT, url, destination, Config.KINDLESIZE).!
  }
}

object PhantomJs 
{
  def main(args: Array[String]) {
    val phantomjs = new PhantomJs
    phantomjs.generate("""http://www.cnblogs.com/Ninputer/archive/2011/06/07/2074632.html""",
        """D:\d9.pdf""")
  }
}