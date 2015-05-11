package info.aiquestion.reader.mail
import courier._
import javax.mail.internet.InternetAddress
import java.io.File
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import info.aiquestion.reader.Config

object Poster {

  val logger = LoggerFactory.getLogger(getClass)
  def send(file: String, email: String) {
    logger.info("send file:" + file + " to" + email)
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