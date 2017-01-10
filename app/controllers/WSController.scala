package controllers

import play.api.mvc.{Action, Controller}

/**
  * Created by hswietlicki on 2017-01-10.
  */
class WSController extends Controller {

  def getUser = Action { request =>
    request.session.get("EPUAP_SESSION_ID").fold(
      Redirect(routes.ApplicationController.index())
    )(
      sessionId => Ok(views.html.getUser(sessionId))
    )
  }
}
