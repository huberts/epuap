package controllers

import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.zip.{Deflater, DeflaterOutputStream}

import org.apache.commons.codec.binary.Base64
import play.api.mvc.{Action, Controller}

/**
  * Created by hswietlicki on 2017-01-10.
  */
class AuthPostController extends Controller {

  def index = Action { request =>
    request.session.get("EPUAP_SESSION_ID").fold(
      Redirect(routes.AuthPostController.authRequestPage())
    )(
      sessionId => Ok(views.html.index(sessionId))
    )
  }

  def authRequestPage = Action { request =>
    Ok(views.html.authRequestPage(getSAMLRequest))
  }

  private def getSAMLRequest: String = {
    val baos = new ByteArrayOutputStream()
    val dos = new DeflaterOutputStream(baos, new Deflater(Deflater.DEFAULT_COMPRESSION, true))
    dos.write(views.xml.authnRequestPost.render().toString().getBytes("UTF-8"))
    dos.close()
    baos.close()
    URLEncoder.encode(new String(Base64.encodeBase64(baos.toByteArray)), "UTF-8")
  }
}
