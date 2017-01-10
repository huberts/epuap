package models

import com.google.inject.Inject
import play.api.libs.ws.WSClient

/**
  * Created by hswietlicki on 2017-01-10.
  */
class UserGetter @Inject() (ws: WSClient) {

  def get(sessionId: String) = {
    User("1", "Hubert")
  }
}
