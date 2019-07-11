package controllers


import javax.inject._


import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class HomeController @Inject()(repository: PersonRepository,
                               cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def index() = Action.async {implicit request =>
    repository.list().map { people =>
      Ok(views.html.index(
        "People Data.", people
      ))
    }
  }

  def add() = Action {implicit request =>
    Ok(views.html.add(
      "フォームを記入して下さい。",
      Person.personForm
    ))
  }


  def create() = Action.async { implicit request =>
    Person.personForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.add("error.", errorForm)))
      },
      person => {
        repository.create(person.name, person.mail, person.tel).map { _ =>
          Redirect(routes.HomeController.index)
        }
      }
    )
  }

  def show(id:Int) = Action.async {implicit request =>
    repository.get(id).map { person =>
      Ok(views.html.show(
        "People Data.", person
      ))
    }
  }

  def edit(id:Int) = Action.async {implicit request =>
    repository.get(id).map { person =>
      val fdata:Form[PersonForm] = Person.personForm
        .fill(PersonForm(person.name, person.mail, person.tel))
      Ok(views.html.edit(
        "Edit Person.", fdata, id
      ))
    }
  }

  def update(id:Int) = Action.async { implicit request =>
    Person.personForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.edit("error.", errorForm, id)))
      },
      person => {
        repository.update(id, person.name, person.mail, person.tel).map { _ =>
          Redirect(routes.HomeController.index)
        }
      }
    )
  }

  def delete(id:Int) = Action.async {implicit request =>
    repository.get(id).map { person =>
      Ok(views.html.delete(
        "Delete Person.", person, id
      ))
    }
  }

  def remove(id:Int) = Action.async {implicit request =>
    repository.delete(id).map { _ =>
      Redirect(routes.HomeController.index)
    }
  }

  def find() = Action {implicit request =>
    Ok(views.html.find(
      "Find Data.", Person.personFind, Seq[Person]()
    ))
  }


  def search() = Action.async {implicit request =>
    Person.personFind.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.find("error.", errorForm, Seq[Person]())))
      },
      find => {
        repository.find(find.find).map { result =>
          Ok(views.html.find(
            "Find: " + find.find, Person.personFind, result
          ))
        }
      }
    )
  }


}
