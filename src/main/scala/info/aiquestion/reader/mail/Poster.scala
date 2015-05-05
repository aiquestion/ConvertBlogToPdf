package info.aiquestion.reader.mail
import courier._
import javax.mail.internet.InternetAddress
import java.io.File
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._
import info.aiquestion.reader.Config

object Poster {

  def send(file: String, email: String) {
    val mailer = Mailer("smtp.gmail.com", 587)
      .auth(true)
      .as(Config.EMAIL, Config.PASSWORD)
      .startTtls(true)()
    val content = Multipart().attach(new File(file), Option("ebook.pdf"))
    mailer(Envelope.from(new InternetAddress(Config.EMAIL)).to(new InternetAddress(email)).subject("You blog book").content(content)).onComplete {
      case _ => println("Sent")
    }
  }
}