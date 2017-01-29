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
  
  import java.util.UUID
  import org.zalando.komang.model.Model.ApplicationId
  import org.zalando.komang.model.Model.ProfileId

  /** Implicit for mapping ApplicationId to String and reverse */
  implicit val applicationIdColumnType = MappedColumnType.base[ApplicationId, String](
    { aId => aId.value.toString },
    { str => ApplicationId(UUID.fromString(str)) }
  )

  /** Implicit for mapping ProfileId to String and reverse */
  implicit val profileIdColumnType = MappedColumnType.base[ProfileId, String](
    { pId => pId.value.toString },
    { str => ProfileId(UUID.fromString(str)) }
  )
        
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Application.schema ++ Profile.schema ++ SchemaVersion.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Application
   *  @param applicationId Database column application_id SqlType(VARCHAR), PrimaryKey
   *  @param name Database column name SqlType(VARCHAR) */
  case class ApplicationRow(applicationId: ApplicationId, name: String)
  /** GetResult implicit for fetching ApplicationRow objects using plain SQL queries */
  implicit def GetResultApplicationRow(implicit e0: GR[ApplicationId], e1: GR[String]): GR[ApplicationRow] = GR{
    prs => import prs._
    ApplicationRow.tupled((<<[ApplicationId], <<[String]))
  }
  /** Table description of table application. Objects of this class serve as prototypes for rows in queries. */
  class Application(_tableTag: Tag) extends Table[ApplicationRow](_tableTag, "application") {
    def * = (applicationId, name) <> (ApplicationRow.tupled, ApplicationRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(applicationId), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> ApplicationRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column application_id SqlType(VARCHAR), PrimaryKey */
    val applicationId: Rep[ApplicationId] = column[ApplicationId]("application_id", O.PrimaryKey)
    /** Database column name SqlType(VARCHAR) */
    val name: Rep[String] = column[String]("name")
  }
  /** Collection-like TableQuery object for table Application */
  lazy val Application = new TableQuery(tag => new Application(tag))

  /** Entity class storing rows of table Profile
   *  @param profileId Database column profile_id SqlType(VARCHAR), PrimaryKey
   *  @param applicationId Database column application_id SqlType(VARCHAR)
   *  @param name Database column name SqlType(VARCHAR) */
  case class ProfileRow(profileId: ProfileId, applicationId: ApplicationId, name: String)
  /** GetResult implicit for fetching ProfileRow objects using plain SQL queries */
  implicit def GetResultProfileRow(implicit e0: GR[ProfileId], e1: GR[ApplicationId], e2: GR[String]): GR[ProfileRow] = GR{
    prs => import prs._
    ProfileRow.tupled((<<[ProfileId], <<[ApplicationId], <<[String]))
  }
  /** Table description of table profile. Objects of this class serve as prototypes for rows in queries. */
  class Profile(_tableTag: Tag) extends Table[ProfileRow](_tableTag, "profile") {
    def * = (profileId, applicationId, name) <> (ProfileRow.tupled, ProfileRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(profileId), Rep.Some(applicationId), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> ProfileRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column profile_id SqlType(VARCHAR), PrimaryKey */
    val profileId: Rep[ProfileId] = column[ProfileId]("profile_id", O.PrimaryKey)
    /** Database column application_id SqlType(VARCHAR) */
    val applicationId: Rep[ApplicationId] = column[ApplicationId]("application_id")
    /** Database column name SqlType(VARCHAR) */
    val name: Rep[String] = column[String]("name")

    /** Foreign key referencing Application (database name CONSTRAINT_ED) */
    lazy val applicationFk = foreignKey("CONSTRAINT_ED", applicationId, Application)(r => r.applicationId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Profile */
  lazy val Profile = new TableQuery(tag => new Profile(tag))

  /** Entity class storing rows of table SchemaVersion
   *  @param versionRank Database column version_rank SqlType(INTEGER)
   *  @param installedRank Database column installed_rank SqlType(INTEGER)
   *  @param version Database column version SqlType(VARCHAR), PrimaryKey, Length(50,true)
   *  @param description Database column description SqlType(VARCHAR), Length(200,true)
   *  @param `type` Database column type SqlType(VARCHAR), Length(20,true)
   *  @param script Database column script SqlType(VARCHAR), Length(1000,true)
   *  @param checksum Database column checksum SqlType(INTEGER)
   *  @param installedBy Database column installed_by SqlType(VARCHAR), Length(100,true)
   *  @param installedOn Database column installed_on SqlType(TIMESTAMP)
   *  @param executionTime Database column execution_time SqlType(INTEGER)
   *  @param success Database column success SqlType(BOOLEAN) */
  case class SchemaVersionRow(versionRank: Int, installedRank: Int, version: String, description: String, `type`: String, script: String, checksum: Option[Int], installedBy: String, installedOn: java.sql.Timestamp, executionTime: Int, success: Boolean)
  /** GetResult implicit for fetching SchemaVersionRow objects using plain SQL queries */
  implicit def GetResultSchemaVersionRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]], e3: GR[java.sql.Timestamp], e4: GR[Boolean]): GR[SchemaVersionRow] = GR{
    prs => import prs._
    SchemaVersionRow.tupled((<<[Int], <<[Int], <<[String], <<[String], <<[String], <<[String], <<?[Int], <<[String], <<[java.sql.Timestamp], <<[Int], <<[Boolean]))
  }
  /** Table description of table schema_version. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class SchemaVersion(_tableTag: Tag) extends Table[SchemaVersionRow](_tableTag, "schema_version") {
    def * = (versionRank, installedRank, version, description, `type`, script, checksum, installedBy, installedOn, executionTime, success) <> (SchemaVersionRow.tupled, SchemaVersionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(versionRank), Rep.Some(installedRank), Rep.Some(version), Rep.Some(description), Rep.Some(`type`), Rep.Some(script), checksum, Rep.Some(installedBy), Rep.Some(installedOn), Rep.Some(executionTime), Rep.Some(success)).shaped.<>({r=>import r._; _1.map(_=> SchemaVersionRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column version_rank SqlType(INTEGER) */
    val versionRank: Rep[Int] = column[Int]("version_rank")
    /** Database column installed_rank SqlType(INTEGER) */
    val installedRank: Rep[Int] = column[Int]("installed_rank")
    /** Database column version SqlType(VARCHAR), PrimaryKey, Length(50,true) */
    val version: Rep[String] = column[String]("version", O.PrimaryKey, O.Length(50,varying=true))
    /** Database column description SqlType(VARCHAR), Length(200,true) */
    val description: Rep[String] = column[String]("description", O.Length(200,varying=true))
    /** Database column type SqlType(VARCHAR), Length(20,true)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(20,varying=true))
    /** Database column script SqlType(VARCHAR), Length(1000,true) */
    val script: Rep[String] = column[String]("script", O.Length(1000,varying=true))
    /** Database column checksum SqlType(INTEGER) */
    val checksum: Rep[Option[Int]] = column[Option[Int]]("checksum")
    /** Database column installed_by SqlType(VARCHAR), Length(100,true) */
    val installedBy: Rep[String] = column[String]("installed_by", O.Length(100,varying=true))
    /** Database column installed_on SqlType(TIMESTAMP) */
    val installedOn: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("installed_on")
    /** Database column execution_time SqlType(INTEGER) */
    val executionTime: Rep[Int] = column[Int]("execution_time")
    /** Database column success SqlType(BOOLEAN) */
    val success: Rep[Boolean] = column[Boolean]("success")

    /** Index over (installedRank) (database name schema_version_ir_idx) */
    val index1 = index("schema_version_ir_idx", installedRank)
    /** Index over (success) (database name schema_version_s_idx) */
    val index2 = index("schema_version_s_idx", success)
    /** Index over (versionRank) (database name schema_version_vr_idx) */
    val index3 = index("schema_version_vr_idx", versionRank)
  }
  /** Collection-like TableQuery object for table SchemaVersion */
  lazy val SchemaVersion = new TableQuery(tag => new SchemaVersion(tag))
}
