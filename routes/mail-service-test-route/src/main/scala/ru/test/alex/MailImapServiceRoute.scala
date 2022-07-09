package ru.test.alex

import org.apache.camel.component.mail.MailMessage
import org.springframework.stereotype.Component

import javax.mail.Message

@Component
class MailImapServiceRoute(mailMessageReceiverService: MailMessageReceiverService) extends AbstractRouteBuilder {

  rest.post("/test").route().id("test")
    .log("hi")
//  from("imap://outlook.sigma-exchtest.sbrf.ru?username=SIGMA-TEST\\afl_test_ift01&password=Qq123456&unseen=true&mapMailMessage=true&useInlineAttachments=true&delay=6000&decodeFilename=true")
  from("imaps://outlook.office365.com?username=sevenlalz@outlook.com&password=1997382528sev&unseen=true&mapMailMessage=true&useInlineAttachments=true&delay=6000&decodeFilename=true")
    .process { ex: E =>
      val mailMessage: Message = ex.getMessage(classOf[MailMessage]).getOriginalMessage
      mailMessageReceiverService.processMessage(mailMessage)
    }

}

