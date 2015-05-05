package info.aiquestion.reader.phantomjs

import org.apache.pdfbox.util.PDFMergerUtility

class PdfMergeTool {
  
  def Merge(files:List[String], destFile:String) = 
  {
    val ut = new PDFMergerUtility()
    files.map { x => ut.addSource(x) }
    ut.setDestinationFileName(destFile)
    ut.mergeDocuments()
  }
}

object PdfMergeTool {
  def main(args: Array[String]) {
    val tool = new PdfMergeTool
    tool.Merge(List("""D:\a.pdf""", """D:\b.pdf""","""D:\c.pdf"""), """D:\test.pdf""")
  }
}