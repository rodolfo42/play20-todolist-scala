package models

import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat

case class Task(id: Long, label: String, created: Date) {
  def this(id: Long, label: String) = this(id, label, Calendar.getInstance().getTime)
}

object Task {

  import anorm._
  import anorm.SqlParser._

  val task = {
    get[Long]("id") ~ 
    get[String]("label") map {
      case id~label => new Task(id, label)
    }
  }

  import play.api.db._
  import play.api.Play.current
  
  def getTask(id: Long): Task = DB.withConnection { implicit c =>
      SQL("select * from task where id = {id}").on(
        'id -> id
      ).as(task *).head
  }

  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }
  
  def create(label: String) {
    val created = Calendar.getInstance().getTime
    val fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    DB.withConnection { implicit c =>
      SQL(
      """
        insert into task (label, created) values ({label}, '%s')
      """.format(created)).on(
        'label -> label
      ).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }
}