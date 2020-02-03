package com.metalheart.service.impl;

import com.metalheart.config.AppProperties;
import com.metalheart.exception.SMTPException;
import com.metalheart.service.EmailService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static com.metalheart.service.I18nService.KEY.EMAIL_FOOTER_CONTACT;
import static com.metalheart.service.I18nService.KEY.EMAIL_REGISTRATION_BTN;
import static com.metalheart.service.I18nService.KEY.EMAIL_REGISTRATION_CONTENT;
import static com.metalheart.service.I18nService.KEY.EMAIL_REGISTRATION_FOOTER;
import static com.metalheart.service.I18nService.KEY.EMAIL_REGISTRATION_SUBJECT;
import static com.metalheart.service.I18nService.KEY.EMAIL_REGISTRATION_TITLE;
import static com.metalheart.service.I18nService.KEY.EMAIL_RESET_PASSWORD_BTN;
import static com.metalheart.service.I18nService.KEY.EMAIL_RESET_PASSWORD_CONTENT;
import static com.metalheart.service.I18nService.KEY.EMAIL_RESET_PASSWORD_FOOTER;
import static com.metalheart.service.I18nService.KEY.EMAIL_RESET_PASSWORD_SUBJECT;
import static com.metalheart.service.I18nService.KEY.EMAIL_RESET_PASSWORD_TITLE;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSenderImpl sender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private I18nServiceImpl i18n;

    @Value("${spring.mail.username}")
    private String springMailUsername;

    @Override
    public void sendResetPassword(String email, String link)throws SMTPException {

        BaseEmailDTO data = BaseEmailDTO.builder()
            .subject(i18n.translate(EMAIL_RESET_PASSWORD_SUBJECT))
            .header(i18n.translate(EMAIL_RESET_PASSWORD_TITLE))
            .body(i18n.translate(EMAIL_RESET_PASSWORD_CONTENT))
            .submit(i18n.translate(EMAIL_RESET_PASSWORD_BTN))
            .footer(i18n.translate(EMAIL_RESET_PASSWORD_FOOTER))
            .to(email)
            .submitUrl(link)
            .build();
        sendBaseEmail(data);
    }

    @Override
    public void sendRegistration(String email, String link) throws SMTPException {

        BaseEmailDTO data = BaseEmailDTO.builder()
            .subject(i18n.translate(EMAIL_REGISTRATION_SUBJECT))
            .header(i18n.translate(EMAIL_REGISTRATION_TITLE))
            .body(i18n.translate(EMAIL_REGISTRATION_CONTENT))
            .submit(i18n.translate(EMAIL_REGISTRATION_BTN))
            .footer(i18n.translate(EMAIL_REGISTRATION_FOOTER))
            .to(email)
            .submitUrl(link)
            .build();
        sendBaseEmail(data);

        log.info("Confirm registration has been sent");
    }

    private void sendBaseEmail(BaseEmailDTO data) throws SMTPException{

        Session session = sender.getSession();

        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(os, true, UTF_8)) {

            session.setDebugOut(ps);
            session.setDebug(true);


            String logo = "metalheart_logo";

            final Context ctx = new Context(Locale.getDefault());
            ctx.setVariable("url", appProperties.getRest().getFrontUrl());
            ctx.setVariable("url_submit", data.submitUrl);
            ctx.setVariable("imageResourceName", logo);


            ctx.setVariable("header", data.header);
            ctx.setVariable("body", data.body);
            ctx.setVariable("submit", data.submit);
            ctx.setVariable("footer", data.footer);
            ctx.setVariable("contact", i18n.translate(EMAIL_FOOTER_CONTACT));


            final String htmlContent = this.templateEngine.process("mail/base_mail.html", ctx);

            MimeMessage msg = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

            helper.setFrom(springMailUsername);
            helper.setTo(data.to);
            helper.setSubject(data.subject);
            helper.setText(htmlContent, true);
            helper.addInline(logo, new ClassPathResource("/images/ava_inverted.png"));

            try {
                sender.send(msg);
            } catch (Exception e) {
                throw new SMTPException(os.toString(UTF_8), e);
            }

        } catch (IOException | MessagingException e) {
            throw new SMTPException(e);
        }
    }

    @Builder
    private static class BaseEmailDTO {
        String subject;
        String to;
        String header;
        String body;
        String submit;
        String footer;
        String submitUrl;
    }
}
