package info.aiquestion.reader

import java.io.File

object FileManager {
  // use this to make sure that two process will not generate into one folder
  val myuuid = java.util.UUID.randomUUID.toString()
  def generateDestFilePath: String = {
    Config.RESULTROOT + uuid + """.pdf"""
  }
  
  def generateBlogPdfPath: String = {
    Config.TEMPFILEROOT + myuuid + File.separator + uuid + ".pdf"
  }
  def uuid = java.util.UUID.randomUUID.toString
}