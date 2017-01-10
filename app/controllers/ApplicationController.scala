package controllers

import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.zip.{Deflater, DeflaterOutputStream}

import com.google.inject.Inject
import models.SoapRequester
import org.apache.commons.codec.binary.Base64
import play.api.Configuration
import play.api.mvc.{Action, Controller}

class ApplicationController @Inject() (configuration: Configuration) extends Controller {

  def index = Action { request =>
    request.session.get("EPUAP_SESSION_ID").fold(
      Redirect(routes.ApplicationController.sendAuthnRequest())
    )(
      sessionId => Ok(views.html.index(sessionId))
    )
  }

  def invalidate = Action {
    Redirect(routes.ApplicationController.index()).withNewSession
  }

  def sendAuthnRequest = Action { request =>
    val baos = new ByteArrayOutputStream()
    val dos = new DeflaterOutputStream(baos, new Deflater(Deflater.DEFAULT_COMPRESSION, true))
    dos.write(views.xml.authnRequest.render().toString().getBytes("UTF-8"))
    dos.close()
    baos.close()
    Redirect("https://hetman-int.epuap.gov.pl/DracoEngine2/draco.jsf?SAMLRequest=" + URLEncoder.encode(new String(Base64.encodeBase64(baos.toByteArray)), "UTF-8"))
  }

  def resolveArtifact(SAMLart: Option[String]) = Action { request =>
    val nodes = xml.XML.loadString(SoapRequester.resolveArtifact(views.xml.artifactResolve.render(SAMLart).toString()))
    val sessionId = (nodes \ "Body" \ "ArtifactResponse" \ "Response" \ "Assertion").head.attribute("ID").head.text
    Redirect(routes.ApplicationController.index()).withSession(
      "EPUAP_SESSION_ID" -> sessionId
    )
  }
}
