package org.zalando.komang.tools

import org.flywaydb.core.Flyway
import slick.codegen.SourceCodeGenerator
import slick.driver.H2Driver
import slick.driver.H2Driver.api._
import slick.jdbc.meta.MTable

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.control.NonFatal
import scala.concurrent.ExecutionContext.Implicits.global

object KomangSlickCodeGenerator {

  def main(args: Array[String]) {
    val url = "jdbc:h2:mem:komang;DB_CLOSE_DELAY=-1"
    val outputFolder = "src/main/scala-gen"
    val jdbcDriver = "org.h2.Driver"
    val slickDriver = "slick.driver.H2Driver"
    val pkg = "org.zalando.komang.persistence"

    val flyway = new Flyway
    flyway.setDataSource(
      url,
      null,
      null
    )
    flyway.setLocations("db.migration")
    flyway.migrate()

    val db = Database.forURL(url = url, driver = jdbcDriver)
    val tables = MTable.getTables(None, Some("PUBLIC"), None, None)

    def codeGenerator(model: slick.model.Model) = new SourceCodeGenerator(model) {
      override def code =
        """
        |import java.util.UUID
        |import org.zalando.komang.model.Model.ApplicationId
        |import org.zalando.komang.model.Model.ProfileId
        |
        |/** Implicit for mapping ApplicationId to String and reverse */
        |implicit val applicationIdColumnType = MappedColumnType.base[ApplicationId, String](
        |  { aId => aId.value.toString },
        |  { str => ApplicationId(UUID.fromString(str)) }
        |)
        |
        |/** Implicit for mapping ProfileId to String and reverse */
        |implicit val profileIdColumnType = MappedColumnType.base[ProfileId, String](
        |  { pId => pId.value.toString },
        |  { str => ProfileId(UUID.fromString(str)) }
        |)
      """.stripMargin + "\n" + super.code

      override def Table = new Table(_) { table =>
        override def Column = new Column(_) {
          override def rawType = this.model.name match {
            case "application_id" => "ApplicationId"
            case "profile_id" => "ProfileId"
            case _ => super.rawType
          }
        }
      }
    }

    val exec = db.run(H2Driver.createModel(Some(tables))) map {
      codeGenerator(_).writeToFile(
        profile = slickDriver,
        folder = outputFolder,
        pkg = pkg,
        container = "Tables",
        fileName = "Tables.scala"
      )
    } recover { case NonFatal(ex) => print(ex) }

    println("====Wait for code generation====")
    Await.result(exec, 30.seconds)
    println("====Code generation completed===")
  }
}
