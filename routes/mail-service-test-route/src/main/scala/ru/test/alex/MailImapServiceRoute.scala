package ru.test.alex

import org.apache.camel.attachment.AttachmentMessage
import org.apache.camel.component.mail.MailMessage
import org.apache.camel.util.FileUtil
import org.apache.tomcat.util.http.fileupload.FileUtils
import org.springframework.stereotype.Component

import java.io.{BufferedOutputStream, BufferedReader, File, FileOutputStream, InputStream, InputStreamReader}
import java.nio.file.Files
import javax.mail.internet.{MimeBodyPart, MimeUtility}
import javax.mail.{BodyPart, Message, Multipart}
import scala.io.{BufferedSource, Source}
import scala.reflect.io.Path

@Component
class MailImapServiceRoute(mailMessageReceiverService: MailMessageReceiverService) extends AbstractRouteBuilder {

  rest.post("/test").route().id("test")
    .log("hi")
//[cid:6f5ed12c-dd5e-4591-8d65-80a3862de95b]
  //mailMessage.getContent.asInstanceOf[Multipart].getBodyPart(0).getContent.asInstanceOf[Multipart].getBodyPart(0).getContent.asInstanceOf[Multipart].getBodyPart(0).getContent
  from("imaps://outlook.office365.com?username=***@outlook.com&password=***&unseen=true&mapMailMessage=true&useInlineAttachments=true&delay=6000&decodeFilename=true")
    .process { ex: E =>
      val mailMessage: Message = ex.getMessage(classOf[MailMessage]).getOriginalMessage
      mailMessageReceiverService.processMessage(mailMessage)
//      val m: Option[Multipart] = if (mailMessage.getContentType.contains("multipart/mixed")) Option(mailMessage.getContent.asInstanceOf[Multipart]) else None
//      val at: AttachmentMessage
//      val attachments: List[Array[Byte]] = mailMessageReceiverService.findAttachments(mailMessage)(Nil)
      val m: Multipart = mailMessage.getContent.asInstanceOf[Multipart]
//      val file: File = new File("test.eml")
//      m.writeTo(new BufferedOutputStream(new FileOutputStream(file)))
//      m.getBodyPart(0).getContentType("text/html")
      val contentFirst = m.getBodyPart(0).getContent.asInstanceOf[Multipart].getBodyPart(1).getContent
      val decoded = MimeUtility.decodeText(m.getBodyPart(1).getFileName)
      var content: String = ""
      for (p <- 0 until m.getCount) {
        val bp: BodyPart = m.getBodyPart(p)
        val disposition: String = bp.getDisposition
        if (disposition != null && disposition.equalsIgnoreCase("ATTACHMENT")) {
          System.out.println("Mail have some attachment")
          val handler = bp.getDataHandler
//          val source: Array[Byte] = Stream.continually(bp.asInstanceOf[MimeBodyPart].getInputStream.read).takeWhile(_ != -1).map(_.toByte).toArray
          System.out.println("file name : " + handler.getName)
        }
        else {
          System.out.println("Body: " + bp.getContent)
          content = bp.getContent.toString
        }
      }
    }

}

//ex.getIn(classOf[MailMessage]).getOriginalMessage.getContent.asInstanceOf[MimeMultipart].getBodyPart(0).writeTo(new FileOutputStream(new File("test3.eml")))

