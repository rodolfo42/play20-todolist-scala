package controllers

import play.api._
import play.api.mvc._

object TaskController extends Controller {

  // redirect
  def index = Action {
    Redirect(routes.TaskController.tasks);
  }

  // new task form
  import play.api.data._
  import play.api.data.Forms._

  val taskForm = Form(
    "label" -> nonEmptyText
  )

  // task list
  import models.Task
  
  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }
  
  
  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Task.create(label)
        Redirect(routes.TaskController.tasks)
      }
    )
  }

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.TaskController.tasks)
  }
  
  def taskDetails(id: Long) = Action {
    val task = Task.getTask(id)
    Ok(views.html.details(task))
  }
}