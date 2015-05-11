package info.aiquestion.reader

import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import info.aiquestion.reader.phantomjs.PdfMergeTool
import info.aiquestion.reader.phantomjs.PhantomJs
import info.aiquestion.reader.mail.Poster
import info.aiquestion.reader.db.MongoUtil

class Processor {
  val logger = LoggerFactory.getLogger(getClass)

  def process(urls: List[String], email:String): String = {
    val result = FileManager.generateDestFilePath
    global.execute(new Runnable {
      def run() {
        val urlFileMap = urls.map { x =>
          MongoUtil.getPath(x) match{
            case Some(path) => x -> (path, false)
            case None => x -> (FileManager.generateBlogPdfPath, true)
          }
        }.toMap
        
        var count = 1
        for (u <- urls) {
          if (urlFileMap(u)._2)
          {
            // add retry
            var retryCount = 5
            while (retryCount > 0 && (new PhantomJs).generate(u, urlFileMap(u)._1) != 0){
              retryCount = retryCount - 1
            }
            MongoUtil.createCache(u, urlFileMap(u)._1)
          }
          logger.debug(count + "/" + urls.length)
          count = count + 1
        }
        val files = urls.map(x => urlFileMap(x)._1)

        (new PdfMergeTool).Merge(files, result)
        Poster.send(result, email)
      }
    })
    result
  }
}
