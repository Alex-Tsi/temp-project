package ru.test.alex

import org.apache.commons.io.IOUtils
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream
import org.jsoup.Jsoup
import org.slf4s.Logging
import org.springframework.stereotype.Service
import ru.test.alex.MailMessageReceiverService.temporaryParsedMailMessageModel.{AttachmentObject, TemporaryMailMessage}

import java.io.{BufferedOutputStream, File, FileOutputStream, OutputStream}
import java.nio.charset.StandardCharsets
import java.util.{Base64, UUID}
import javax.mail.internet.{InternetAddress, MimeUtility}
import javax.mail.{Message, Multipart, Part}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}


@Service
class MailMessageReceiverService extends Logging {

  def processMessage(implicit m: Message): Unit = {
    val temporaryMailMessage: TemporaryMailMessage = Try {
      TemporaryMailMessage(
        from = getFromAttribute,
        subject = m.getSubject,
        description = getDescription,
        links = getLinks,
        body = getBase64Eml,
        attachments = getByteArrayAttachments
      )
    } match {
      case Success(value) => value
      case Failure(exception) =>
        log.error("Exception during mail message parsing")
        throw new RuntimeException(exception.getLocalizedMessage)
    }
    println()
    println()
  }

  def getFromAttribute(implicit m: Message): Array[String] = m.getFrom.map { case address: InternetAddress => address.getAddress; case _ => toString }

  def getDescription(implicit part: Part): String = {
    getOriginalMessagePayload // TODO: need filter message
  }

  def getLinks(implicit part: Part): String = {
    log.info("Getting inlined links")
    val originalContent: String = getOriginalMessagePayload
    val linksRegex: Regex = "https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/\\/\\=]*)".r
    linksRegex.findAllIn(originalContent).filter(_.nonEmpty).mkString(";")
  }

  private def getOriginalMessagePayload(implicit part: Part): String = {
    val dumpedPart: Option[Part] = dump2RequiredPartByContentType(part, "text/plain")
    dumpedPart.map(_.getContent.toString).getOrElse {
      val htmlPart: Option[Part] = dump2RequiredPartByContentType(part, "text/html")
      Jsoup.parse(htmlPart.getOrElse(throw new NoSuchElementException("No message body found")).getContent.toString).text()
    }
  }

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
    val dumpedPartWrap: Option[Part] = dump2RequiredPartByContentType(part, "multipart/mixed")
    if (dumpedPartWrap.isEmpty) {
      log.info("Message without attachments")
      return ListBuffer.empty
    }
    val attachments: ListBuffer[AttachmentObject] = new ListBuffer[AttachmentObject]
    dumpedPartWrap.foreach { m =>
      val mPart: Multipart = m.getContent.asInstanceOf[Multipart]
      Range(1, mPart.getCount).foreach { pNumber =>
        val bp: Part = mPart.getBodyPart(pNumber)
        if (bp.getDisposition == null || Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition)) attachmentToByteArray(bp, attachments)
      }
    }
    attachments
  }

  private def attachmentToByteArray(part: Part, attachments: ListBuffer[AttachmentObject]): ListBuffer[AttachmentObject] = {
    val fName: String = Option(part.getFileName).filter(_.nonEmpty).map(MimeUtility.decodeText).getOrElse("FileName_" + UUID.randomUUID().toString)
    val payload = IOUtils.toByteArray(part.getInputStream)
    attachments += AttachmentObject(fileName = fName, payload = payload)
  }

  private def dump2RequiredPartByContentType(part: Part, mimeType: String): Option[Part] = {
    if (part.isMimeType(mimeType)) return Option(part)
    else if (part.isMimeType("multipart/*")) {
      val mp: Multipart = part.getContent.asInstanceOf[Multipart]
      for (p <- 0 until mp.getCount) {
        val dumpedPart: Option[Part] = dump2RequiredPartByContentType(mp.getBodyPart(p), mimeType)
        if (dumpedPart.nonEmpty) return dumpedPart
      }
    }
    None
  }
}

object MailMessageReceiverService {

  object temporaryParsedMailMessageModel {
    case class TemporaryMailMessage(from: Array[String], subject: String, description: String, links: String, body: String, attachments: Seq[AttachmentObject])

    case class AttachmentObject(fileName: String, payload: Array[Byte])
  }
}
