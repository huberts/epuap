package models

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, StringReader, StringWriter}
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.soap.{MessageFactory, SOAPConnectionFactory, SOAPMessage}
import javax.xml.transform.{OutputKeys, TransformerFactory, dom, stream}
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import com.google.inject.Inject
import org.w3c.dom.Document
import org.xml.sax.InputSource
import play.api.libs.ws.WSClient


/**
  * Created by hswietlicki on 2017-01-10.
  */
class UserGetter @Inject() (ws: WSClient) {

  private val connection = SOAPConnectionFactory.newInstance.createConnection()
  private val signatory = new WSSignatory

  def get(sessionId: String) = {
//    System.setProperty("java.net.useSystemProxies", "true")
//    System.setProperty("http.proxyHost", "localhost")
//    System.setProperty("http.proxyPort", "8888")
//    System.setProperty("https.proxyHost", "localhost")
//    System.setProperty("https.proxyPort", "8888")
//    System.setProperty("socksProxyHost", "localhost")
//    System.setProperty("socksProxyPort", "8888")
//    System.setProperty("com.sun.net.ssl.checkRevocation", "false")
    val response = connection.call(buildMessage(sessionId), "https://int.pz.gov.pl/dt-services/idpIdentityInfoService")
    val baos = new ByteArrayOutputStream
    response.writeTo(baos)
    baos.close()
    val text = new String(baos.toByteArray, "UTF-8")
    User("1", "Hubert")
  }

  private def buildMessage(sessionId: String): SOAPMessage = {
    val request = MessageFactory.newInstance.createMessage()
    request.getSOAPBody.addDocument(buildDocument(sessionId))
    val signature = signatory.sign(request.getSOAPPart.getEnvelope.getOwnerDocument)
//    request
    signatureToMessage(signature)
  }

  private def buildDocument(sessionId: String): Document = {
    val factory = DocumentBuilderFactory.newInstance
    factory.setNamespaceAware(true)
    factory.newDocumentBuilder.parse(new InputSource(new StringReader(loadXml(sessionId))))
  }

  private def loadXml(sessionId: String): String = {
    views.xml.resolveUserId.render(sessionId).toString()
  }

  private def signatureToMessage(document: Document): SOAPMessage = {
    val text = print(document)
    val baos = new ByteArrayOutputStream
    val source = new dom.DOMSource(document)
    val result = new stream.StreamResult(baos)
    TransformerFactory.newInstance.newTransformer.transform(source, result)
    val bais = new ByteArrayInputStream(baos.toByteArray)
    MessageFactory.newInstance.createMessage(null, bais)
  }

  private def print(document: Document) = {
    val domSource= new DOMSource(document)
    val writer = new StringWriter()
    val result = new StreamResult(writer)
    val tf = TransformerFactory.newInstance()
    val transformer = tf.newTransformer()
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")
    transformer.transform(domSource, result)
    val str = writer.toString
  }
}
