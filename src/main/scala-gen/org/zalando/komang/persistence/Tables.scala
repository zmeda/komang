package org.zalando.komang.persistence
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.H2Driver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Application.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Application
    *  @param applicationId Database column application_id SqlType(VARCHAR)
    *  @param name Database column NAME SqlType(VARCHAR) */
  case class ApplicationRow(applicationId: String, name: String)

  /** GetResult implicit for fetching ApplicationRow objects using plain SQL queries */
  implicit def GetResultApplicationRow(implicit e0: GR[String]): GR[ApplicationRow] = GR { prs =>
    import prs._
    ApplicationRow.tupled((<<[String], <<[String]))
  }

  /** Table description of table application. Objects of this class serve as prototypes for rows in queries. */
  class Application(_tableTag: Tag) extends Table[ApplicationRow](_tableTag, "application") {
    def * = (applicationId, name) <> (ApplicationRow.tupled, ApplicationRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      (Rep.Some(applicationId), Rep.Some(name)).shaped.<>({ r =>
        import r._; _1.map(_ => ApplicationRow.tupled((_1.get, _2.get)))
      }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column application_id SqlType(VARCHAR) */
    val applicationId: Rep[String] = column[String]("application_id")

    /** Database column NAME SqlType(VARCHAR) */
    val name: Rep[String] = column[String]("NAME")
  }

  /** Collection-like TableQuery object for table Application */
  lazy val Application = new TableQuery(tag => new Application(tag))
}
