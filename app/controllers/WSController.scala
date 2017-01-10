package controllers

import com.google.inject.Inject
import models.UserGetter
import play.api.inject.Injector
import play.api.mvc.{Action, Controller}

/**
  * Created by hswietlicki on 2017-01-10.
  */
class WSController @Inject() (injector: Injector) extends Controller {

  private val userGetter = injector.instanceOf[UserGetter]

  def getUser = Action { request =>
    request.session.get("EPUAP_SESSION_ID").fold(
      Redirect(routes.ApplicationController.index())
    )(

      sessionId => Ok(views.html.getUser(sessionId, userGetter.get(sessionId)))
    )
  }
}
