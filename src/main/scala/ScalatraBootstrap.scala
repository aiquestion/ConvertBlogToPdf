import info.aiquestion.reader._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new MyScalatraServlet, "/*")
    context.initParameters("org.scalatra.environment") = "production"
  }
}
