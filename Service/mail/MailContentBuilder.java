package com.ndirituedwin.Service.mail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
@Slf4j
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    public String build(String message){
        Context context=new Context();
         context.setVariable("message",message);
         return templateEngine.process("mailTemplate",context);

    }
}
