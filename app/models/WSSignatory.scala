package models

import org.apache.ws.security.{WSConstants, WSSConfig, WSSecurityEngine}
import org.apache.ws.security.components.crypto.CryptoFactory
import org.apache.ws.security.message.{WSSecHeader, WSSecSignature}
import org.w3c.dom.Document

class WSSignatory {

  def sign(body: Document): Document = {
    WSSConfig.init()
//    val securityEngine = new WSSecurityEngine
    val crypto = CryptoFactory.getInstance()


    val builder = new WSSecSignature
    builder.setUserInfo("systherminfo", "12345")
    builder.setKeyIdentifierType(WSConstants.X509_KEY_IDENTIFIER)
    builder.setUseSingleCertificate(true)

    val secHeader = new WSSecHeader
    secHeader.insertSecurityHeader(body)

    builder.build(body, crypto, secHeader)
  }
}
