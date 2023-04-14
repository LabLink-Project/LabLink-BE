package com.example.lablink.feedback.service;

import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.feedback.dto.Request.FeedBackRequestDto;
import com.example.lablink.feedback.dto.Response.DetailFeedBackResponseDto;
import com.example.lablink.feedback.dto.Response.FeedBackResponseDto;
import com.example.lablink.feedback.entity.Feedback;
import com.example.lablink.feedback.exception.FeedBackErrorCode;
import com.example.lablink.feedback.exception.FeedBackException;
import com.example.lablink.feedback.repository.FeedBackRepository;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.service.GetStudyService;
import com.example.lablink.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final FeedBackRepository feedBackRepository;
    private final GetStudyService getStudyService;
    @Transactional
    public void addFeedBack(UserDetailsImpl userDetails, Long studyId, FeedBackRequestDto feedBackRequestDto) {
        Study study = getStudyService.getStudy(studyId);

        feedBackRepository.save(new Feedback(userDetails.getUser(),study,feedBackRequestDto.getFeedbackMessage(),false));
    }
    @Transactional(readOnly = true)
    public List<FeedBackResponseDto> getFeedBack(CompanyDetailsImpl companyDetails, Long studyId) {
        if(!isMatchCompany(studyId,companyDetails)) {
            throw new FeedBackException(FeedBackErrorCode.NOT_HAVE_PERMISSION);
        }

        List<Feedback> feedbacks= feedBackRepository.findAllByStudyId(studyId);
        List<FeedBackResponseDto> result = new ArrayList<>();
        for (Feedback feedback: feedbacks) {
            result.add(new FeedBackResponseDto(feedback));
        }
        return result;
    }
    @Transactional
    public DetailFeedBackResponseDto getDetailFeedBack(CompanyDetailsImpl companyDetails, Long studyId, Long feedbackId) {
        if(!isMatchCompany(studyId,companyDetails)){
            throw new FeedBackException(FeedBackErrorCode.NOT_HAVE_PERMISSION);
        }
        Feedback feedback = feedBackRepository.findById(feedbackId).orElseThrow(
                () -> new FeedBackException(FeedBackErrorCode.FeedBack_NOT_FOUND)
        );
        feedback.updateViewStatus();
        return new DetailFeedBackResponseDto(feedback);
    }

    public void excelDownloadFeedBack(CompanyDetailsImpl companyDetails, Long studyId)throws IOException {
        if(!isMatchCompany(studyId,companyDetails)){
            throw new FeedBackException(FeedBackErrorCode.NOT_HAVE_PERMISSION);
        }
        List<Feedback> feedbacks= feedBackRepository.findAllByStudyId(studyId);
        List<FeedBackResponseDto> result = new ArrayList<>();
        for (Feedback feedback: feedbacks) {
            result.add(new FeedBackResponseDto(feedback));
        }
        String filePath = "../..//Downloads";//다운로드 경로
        String fileName = "FeedBack.xlsx";//다운로드 파일 이음

        XSSFWorkbook workbook = new XSSFWorkbook();

        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // 빈 Sheet를 생성
        XSSFSheet sheet = workbook.createSheet("FeedBack");
        //컬럼 너비 값
        sheet.setColumnWidth(0,3000);
        sheet.setColumnWidth(1,5000);
        sheet.setColumnWidth(2,20000);
        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("이름");
        cell = row.createCell(1);
        cell.setCellValue("이메일");
        cell = row.createCell(2);
        cell.setCellValue("피드백 내용");

        // Body
        for (int i=0; i<feedbacks.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(result.get(i).getUserName());
            cell = row.createCell(1);
            cell.setCellValue(result.get(i).getUserEmail());
            cell = row.createCell(2);
            cell.setCellValue(result.get(i).getFeedbackMessage());
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(filePath, fileName));
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isMatchCompany(Long studyId, CompanyDetailsImpl companyDetails) {

        return studyId.equals(companyDetails.getCompany().getId());
    }

}
