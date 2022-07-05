package ru.test.alex

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream
import org.slf4s.Logging
import org.springframework.stereotype.Service
import ru.test.alex.MailMessageReceiverService.temporaryParsedMailMessageModel.{AttachmentObject, TemporaryMailMessage}

import java.io.{BufferedOutputStream, OutputStream}
import java.nio.charset.StandardCharsets
import java.util.{Base64, UUID}
import javax.mail.internet.{InternetAddress, MimeUtility}
import javax.mail.{Address, Message, Multipart, Part}
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}


@Service
class MailMessageReceiverService extends Logging {

  def processMessage(implicit m: Message): Unit = {
    Try {
      TemporaryMailMessage(
        from = getFromAttribute,
        subject = m.getSubject,
        description = null,
        body = getBase64Eml,
        attachments = getByteArrayAttachments
      )
    } match {
      case Success(_) =>
      case Failure(exception) =>
        log.error("Exception during mail message parsing")
        throw new RuntimeException(exception.getLocalizedMessage)
    }
  }

  def getFromAttribute(implicit m: Message): Array[String] = m.getFrom.map { case address: InternetAddress => address.getAddress; case _ => toString}

//  def getLinks(implicit part: Part): String = {
//
//  }
//
//  private def dumpToPlainTextMsg(part: Part): String = {
//
//  }

  def getBase64Eml(implicit part: Part): String = {
    log.info("Creating Base64 .eml file for sending")
    val dumpedMessage = dumpParts4Eml(part)
    val os: OutputStream = new ByteArrayOutputStream()
    dumpedMessage.writeTo(os)
    val emlBase64: String = new String(Base64.getEncoder.encode(os.asInstanceOf[ByteArrayOutputStream].toByteArray), StandardCharsets.UTF_8)
    emlBase64
  }

  private def dumpParts4Eml(part: Part): Part = {
    val relatedPart: Part = {
      if (part.isMimeType("multipart/related") || part.isMimeType("multipart/alternative")) part
      else if (part.isMimeType("multipart/*")) {
        val bp = part.getContent.asInstanceOf[Multipart].getBodyPart(0)
        dumpParts4Eml(bp)
      } else if (part.isMimeType("text/*")) part
      else part
    }
    relatedPart
  }

  def getByteArrayAttachments(implicit part: Part): ListBuffer[AttachmentObject] = {
    if (!part.isMimeType("multipart/mixed")) {
      log.info("message without attachments")
      return ListBuffer.empty
    }
    val mpMessage: Multipart = part.getContent.asInstanceOf[Multipart]
    val attachments: ListBuffer[AttachmentObject] = new ListBuffer[AttachmentObject]
    Range(1, mpMessage.getCount).foreach { mn =>
      val bp: Part = mpMessage.getBodyPart(mn)
      if (bp.getDisposition == null || Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition)) attachmentToByteArray(bp, attachments)
    }
    attachments
  }

  private def attachmentToByteArray(part: Part, attachments: ListBuffer[AttachmentObject]): ListBuffer[AttachmentObject] = {
    val os: OutputStream = new ByteArrayOutputStream()
    val fName: String = Option(part.getFileName).filter(_.isEmpty).map(MimeUtility.decodeText).getOrElse("FileName_" + UUID.randomUUID().toString)
    part.writeTo(new BufferedOutputStream(os))
    val payload = os.asInstanceOf[ByteArrayOutputStream].toByteArray
    attachments += AttachmentObject(fileName = fName, payload = payload)
    attachments
  }

}

object MailMessageReceiverService {

  object temporaryParsedMailMessageModel {
    case class TemporaryMailMessage(from: Array[String], subject: String, description: String, body: String, attachments: Seq[AttachmentObject])

    case class AttachmentObject(fileName: String, payload: Array[Byte])
  }

}

