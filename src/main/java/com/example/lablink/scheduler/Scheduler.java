package com.example.lablink.scheduler;

import com.example.lablink.feedback.service.FeedBackService;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.entity.StudyStatusEnum;
import com.example.lablink.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final StudyRepository studyRepository;
    /*private final JavaMailSender emailSender;
    private final FeedBackService feedBackService;*/
    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 0 * * *")
    // todo : exception ?
    public void updateStatus() throws InterruptedException {
        log.info("studystatus 업데이트 실행");
        // findAll이 아니라 enddate가 LocalDateTime보다 이후면 들고오기 (쿼리짜서)
        List<Study> studies = studyRepository.getAllByEndDate();
        for (Study study : studies) {
            // endDate와 localDate 비교 및 studystatus 변경
            LocalDateTime currentDateTime = LocalDateTime.now();
            // endDate가 localDate보다 이전인 경우
            if (study.getEndDate().isBefore(currentDateTime)) {
                study.updateStatus(StudyStatusEnum.CLOSED);
            }
        }
    }
    /*@Scheduled(fixedDelay = 500000)
    public void emailExcelSend(){
       List<Study> studies = studyRepository.findAllByEmailSendIsFalse();
        for (Study study : studies) {
            // endDate와 localDate 비교 및 studystatus 변경
            LocalDateTime currentDateTime = LocalDateTime.now();
            // endDate가 localDate보다 이전인 경우
            if (study.getEndDate().isBefore(currentDateTime)) {
                MimeMessage mimeMessage = emailSender.createMimeMessage();

                try {
                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // use multipart (true)

                    mimeMessageHelper.setSubject(MimeUtility.encodeText("피드백 내용 메일로 전달 드립니다", "UTF-8", "B")); // Base64 encoding
                    mimeMessageHelper.setText("피드백 내용 메일로 전달 드립니다.");
                    mimeMessageHelper.setFrom(new InternetAddress("fghij7410@gmai.com"));
                    mimeMessageHelper.setTo(study.getCompany().getEmail());
                    *//*String filePath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "FeedBack.xlsx";
                    String filePath = System.setProperty(feedBackService.studentExcelDownloadXSSF(study.getId()),true);
                    FileSystemResource fileSystemResource = new FileSystemResource(new File(filePath));*//*
                    XSSFWorkbook fileOutputStream = feedBackService.studentExcelDownloadXSSF(study.getId());
                    String filePath = fileOutputStream.getCTWorkbook().xmlText();
                    System.out.println(filePath);
                    mimeMessageHelper.addAttachment(MimeUtility.encodeText("피드백 내용.xlsx","UTF-8", "B"),new File(filePath)));

                    emailSender.send(mimeMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }*/
}
