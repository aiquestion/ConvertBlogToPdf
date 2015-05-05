package info.aiquestion.reader.httpclient
import dispatch._, Defaults._
object BlogSpider {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(143); 
  val page = url("http://www.cnblogs.com/Ninputer");System.out.println("""page  : dispatch.Req = """ + $show(page ));$skip(41); 
  val content = Http(page OK(as.String));System.out.println("""content  : dispatch.Future[String] = """ + $show(content ));$skip(33); 
	for (c <- content)
		println(c)}



}
