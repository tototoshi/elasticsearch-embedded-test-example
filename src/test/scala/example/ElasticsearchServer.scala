package example

import java.io.File

import org.apache.commons.io.FileUtils
import org.elasticsearch.common.settings.{ImmutableSettings, Settings}
import org.elasticsearch.node.Node
import org.elasticsearch.node.NodeBuilder._

import scala.util.control.NonFatal

object ElasticsearchServer {

  /**
   * Helper to create Settings object
   *
   * @param props
   * @return
   */
  private def settings(props: Iterable[(String, String)]): Settings = {
    def go(props: List[(String, String)], builder: ImmutableSettings.Builder): Settings = {
      props match {
        case Nil => builder.build
        case (k, v) :: tail => go(tail, builder.put(k, v))
      }
    }
    go(props.toList, ImmutableSettings.settingsBuilder())
  }

  /**
   * Loan pattern for embedded elasticsearch server
   *
   * @param props
   * @param f
   * @tparam A
   * @return
   */
  def running[A](props: (String, String)*)(f: Node => A): A = {
    val server = nodeBuilder().local(true).settings(settings(props)).build
    server.start()
    try {
      f(server)
    } finally {
      server.stop()
      try {
        // cleanup data directory
        Option(server.settings.get("path.data")).foreach { dataDir =>
          FileUtils.forceDelete(new File(dataDir))
        }
      } catch {
        case NonFatal(e) =>
          e.printStackTrace()
      }
    }
  }

}