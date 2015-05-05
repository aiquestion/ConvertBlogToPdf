package info.aiquestion.reader

import scala.concurrent.ExecutionContext.Implicits.global
import info.aiquestion.reader.phantomjs.PdfMergeTool
import info.aiquestion.reader.phantomjs.PhantomJs
import info.aiquestion.reader.mail.Poster
import info.aiquestion.reader.db.MongoUtil

class Processor {
  

  def process(urls: List[String], email:String): String = {
    val result = FileManager.generateDestFilePath
    global.execute(new Runnable {
      def run() {
        urls.map { x => println(x) }
        val urlFileMap = (urls.map { x => 
          MongoUtil.getPath(x) match{
            case Some(path) => (x -> (path, false))
            case None => (x -> (FileManager.generateBlogPdfPath, true))
          }
        }).toMap
        
        var count = 1
        for (u <- urls) {
          if (urlFileMap(u)._2)
          {
            (new PhantomJs).generate(u, urlFileMap(u)._1)
            MongoUtil.createCache(u, urlFileMap(u)._1)
          }
          println((count) + "/" + urls.length)
          count = count + 1
        }
        val files = urls.map(x => urlFileMap(x)._1)

        (new PdfMergeTool).Merge(files, result)
        Poster.send(result, email)
      }
    })
    
    result
  }

  def uuid = java.util.UUID.randomUUID.toString

}

object Processor {
  def main(args: Array[String]) {
  }

}