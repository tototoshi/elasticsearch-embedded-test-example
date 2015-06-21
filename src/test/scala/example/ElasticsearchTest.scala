package example

import java.nio.file.Files

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest
import org.scalatest.FunSuite

class ElasticsearchTest extends FunSuite {

  test("it works fine!") {

    ElasticsearchServer.running(
      "cluster.name" -> "estest",
      "path.data" -> Files.createTempDirectory("elasticsearch_data_").toFile.toString
    ) { server =>

      val client = ElasticClient.local

      client.execute { index into "bands" / "artists" fields "name" -> "coldplay" }.await

      client.admin.indices().refresh(new RefreshRequest()).actionGet()

      val resp = client.execute { search in "bands" / "artists" query "coldplay" }.await

      assert(resp.getHits.getTotalHits === 1)
    }
  }

}
