package com.ndirituedwin.Service.mail;

import com.ndirituedwin.Entity.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

  private final JavaMailSender mailSender;
  private final MailContentBuilder contentBuilder;
  @Async
  public void  sendEmail(NotificationEmail notificationEmail){
      MimeMessagePreparator messagePreparator=mimeMessage -> {
          MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage);
          messageHelper.setFrom("ndiritu.edwin018@gmail.com");
          messageHelper.setTo(notificationEmail.getRecipient());
          messageHelper.setText(notificationEmail.getBody());
          messageHelper.setSubject(notificationEmail.getSubject());
      };
      try {
          mailSender.send(messagePreparator);
          log.info("email successfully sent, {}",messagePreparator);


      }catch (MailException exception){
          log.info("an exception has occurred while trying to send the mail "+exception.getMessage());
      }


  }

}
