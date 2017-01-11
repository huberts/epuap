package models

import java.io.{ByteArrayOutputStream, StringReader}
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.soap.{MessageFactory, SOAPConnectionFactory, SOAPMessage}

import org.w3c.dom.Document
import org.xml.sax.InputSource

object SoapRequester {

  private val connection = SOAPConnectionFactory.newInstance.createConnection()

  def resolveArtifact(body: String): String = {
    val response = connection.call(buildMessage(body), "https://konsolahetman-int.epuap.gov.pl/axis2/services/EngineSAMLArtifact")
    val baos = new ByteArrayOutputStream
    response.writeTo(baos)
    baos.close()
    new String(baos.toByteArray, "UTF-8")
  }

  private def buildMessage(body: String): SOAPMessage = {
    val request = MessageFactory.newInstance.createMessage()
    request.getSOAPBody.addDocument(buildDocument(body))
    request.getMimeHeaders.addHeader("SOAPAction", "https://konsolahetman-int.epuap.gov.pl/axis2/services/EngineSAMLArtifact/artifactResolve")
    request
  }

  private def buildDocument(body: String): Document = {
    val factory = DocumentBuilderFactory.newInstance
    factory.setNamespaceAware(true)
    factory.newDocumentBuilder.parse(new InputSource(new StringReader(body.trim())))
  }
}
