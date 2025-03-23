package com.g25.mailer.user.service;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;



    /**
     * 메일 송신 기능
     * @param to 수신자
     * @param title 제목
     * @param content 내용
     * @param from 송신자
     */
    public void sendSimpleMail(String to, String title, String content, String from) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            /**
             * helper 필요함 -> 보내는사람, 받는사람 적용됨
             * helper.setTo(); //받는사람
             * helper.setFrom(); //보내는사람
             */
            String senderEmail = (from != null) ? from : "no-reply@temporary.com"; // 송신자가 미입력시에 디폴트값

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("메일이 전송 완료. 받는 사람: {}", to);
        } catch (MessagingException e) {
            log.error("메일 전송 실패: {}", e.getMessage());
            throw new RuntimeException("메일 전송 오류 발생");
        }

    }


    /**
     * 첨부파일 포함 메일 송신
     * @param to
     * @param title
     * @param content
     * @param fileKeys
     */
    public void sendMailWithAttachment(String to, String title, String content, String from, List<String> fileKeys) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String senderEmail = (from != null) ? from : "no-reply@temporary.com"; // 송신자가 미입력시에 디폴트값

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content, true);

            for (String key : fileKeys) {
                S3Object s3Object = s3Service.getObject(key);
                S3ObjectInputStream inputStream = s3Object.getObjectContent();
                String originalFilename = key.substring(key.lastIndexOf("/") + 1);

                helper.addAttachment(originalFilename, new InputStreamSource() {
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return inputStream;
                    }
                });
            }

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }




}
