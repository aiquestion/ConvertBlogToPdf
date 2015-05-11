package info.aiquestion.reader.db

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject

import info.aiquestion.reader.Config

object MongoUtil {
  
  val mongoClient =  MongoClient("localhost", Config.DBPORT)
  val cache = mongoClient("reader")("filecache")
  cache.createIndex(MongoDBObject("url"->1), MongoDBObject("unique"->1))
  
  def getPath(url:String): Option[String] =
  {
    val cache = mongoClient("reader")("filecache")
    val collection = cache.findOne(MongoDBObject("url" -> url))
    collection match{
      case Some(file) => Some(file.get("file").toString())
      case None => None
    }
  }

  def createCache(url:String, file:String) : Boolean = {
    val cache = mongoClient("reader")("filecache")
    try{
      cache.insert(MongoDBObject("url"->url, "file"->file))
      true;
    }
    catch{
      case _:Throwable=> false
    }
  }
  
  def main(args: Array[String]) {
    println(getPath("""fortest"""))
    println(createCache("fortest", "file"))
    println(createCache("fortest", "file"))
    println(getPath("""fortest"""))
  }
}