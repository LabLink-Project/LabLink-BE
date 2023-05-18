package com.example.lablink.domain.feedback.service;

import com.example.lablink.domain.company.security.CompanyDetailsImpl;
import com.example.lablink.domain.feedback.dto.Request.FeedBackRequestDto;
import com.example.lablink.domain.feedback.dto.Response.DetailFeedBackResponseDto;
import com.example.lablink.domain.feedback.dto.Response.FeedBackResponseDto;
import com.example.lablink.domain.feedback.repository.FeedBackRepository;
import com.example.lablink.domain.study.service.StudyService;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.domain.feedback.entity.Feedback;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.study.service.GetStudyService;
import com.example.lablink.global.exception.GlobalErrorCode;
import com.example.lablink.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static java.lang.System.out;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final FeedBackRepository feedBackRepository;
    private final GetStudyService getStudyService;
    private final StudyService studyService;

    @Transactional
    public void addFeedBack(UserDetailsImpl userDetails, Long studyId, FeedBackRequestDto feedBackRequestDto) {
        Study study = getStudyService.getStudy(studyId);

        feedBackRepository.save(new Feedback(userDetails.getUser(),study,feedBackRequestDto.getFeedbackMessage(),false));
    }
    @Transactional(readOnly = true)
    public List<FeedBackResponseDto> getFeedBack(CompanyDetailsImpl companyDetails, Long studyId) {
        studyService.checkRole(studyId,companyDetails.getCompany());

        List<Feedback> feedbacks= feedBackRepository.findAllByStudyId(studyId);
        List<FeedBackResponseDto> result = new ArrayList<>();
        for (Feedback feedback: feedbacks) {
            result.add(new FeedBackResponseDto(feedback));
        }
        return result;
    }
    @Transactional
    public DetailFeedBackResponseDto getDetailFeedBack(CompanyDetailsImpl companyDetails, Long studyId, Long feedbackId) {
        studyService.checkRole(studyId,companyDetails.getCompany());
        Feedback feedback = feedBackRepository.findById(feedbackId).orElseThrow(
                () -> new GlobalException(GlobalErrorCode.FeedBack_NOT_FOUND)
        );
        feedback.updateViewStatus();
        return new DetailFeedBackResponseDto(feedback);
    }

    public void excelDownloadFeedBack(CompanyDetailsImpl companyDetails, Long studyId){
        studyService.checkRole(studyId,companyDetails.getCompany());
        List<Feedback> feedbacks= feedBackRepository.findAllByStudyId(studyId);
        List<FeedBackResponseDto> result = new ArrayList<>();
        for (Feedback feedback: feedbacks) {
            result.add(new FeedBackResponseDto(feedback));
        }
        String filePath = "../..//Downloads";//다운로드 경로
        String fileName = "FeedBack.xlsx";//다운로드 파일 이음

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("FeedBack");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // 빈 Sheet를 생성

        //컬럼 너비 값
        sheet.setColumnWidth(0,3000);
        sheet.setColumnWidth(1,5000);
        sheet.setColumnWidth(2,5000);
        sheet.setColumnWidth(3,5000);
        sheet.setColumnWidth(4,20000);
        // Header

        cell = row.createCell(0);
        cell.setCellValue("이름");
        cell = row.createCell(1);
        cell.setCellValue("이메일");
        cell = row.createCell(2);
        cell.setCellValue("성별");
        cell = row.createCell(3);
        cell.setCellValue("전화번호");
        cell = row.createCell(4);
        cell.setCellValue("피드백 내용");

        row = sheet.createRow(rowNum++);
        // Body
        for (int i=0; i<feedbacks.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(result.get(i).getUserName());
            cell = row.createCell(1);
            cell.setCellValue(result.get(i).getUserEmail());
            cell = row.createCell(2);
            cell.setCellValue(result.get(i).getUserGender());
            cell = row.createCell(3);
            cell.setCellValue(result.get(i).getUserPhone());
            cell = row.createCell(4);
            cell.setCellValue(result.get(i).getFeedbackMessage());
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(filePath, fileName));
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }
    }

    public XSSFWorkbook emailSendFeedBack(Long studyId){
        List<Feedback> feedbacks= feedBackRepository.findAllByStudyId(studyId);
        List<FeedBackResponseDto> result = new ArrayList<>();
        for (Feedback feedback: feedbacks) {
            result.add(new FeedBackResponseDto(feedback));
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("FeedBack");
        Row row = sheet.createRow(0);
        Cell cell = null;
        int rowNum = 0;

        // 빈 Sheet를 생성
        out.println("------------------------1");
        //컬럼 너비 값
        sheet.autoSizeColumn(0);
        sheet.setColumnWidth(1,5000);
        sheet.setColumnWidth(2,5000);
        sheet.setColumnWidth(3,5000);
        sheet.setColumnWidth(4,20000);
        // Header
        out.println("------------------------2");
        cell = row.createCell(0);
        cell.setCellValue("이름");
        cell = row.createCell(1);
        cell.setCellValue("이메일");
        cell = row.createCell(2);
        cell.setCellValue("성별");
        cell = row.createCell(3);
        cell.setCellValue("전화번호");
        cell = row.createCell(4);
        cell.setCellValue("피드백 내용");

        out.println("------------------------3");
        // Body
        for (int i=0; i<result.size(); i++) {

            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(result.get(i).getUserName());
            cell = row.createCell(1);
            cell.setCellValue(result.get(i).getUserEmail());
            cell = row.createCell(2);
            cell.setCellValue(result.get(i).getUserGender());
            cell = row.createCell(3);
            cell.setCellValue(result.get(i).getUserPhone());
            cell = row.createCell(4);
            cell.setCellValue(result.get(i).getFeedbackMessage());
        }

        return workbook;

    }

    public XSSFWorkbook studentExcelDownloadXSSF (Long studyId) throws Exception {

        List<Feedback> feedbacks= feedBackRepository.findAllByStudyId(studyId);
        List<FeedBackResponseDto> result = new ArrayList<>();
        for (Feedback feedback: feedbacks) {
            result.add(new FeedBackResponseDto(feedback));

        }
        String filePath = "../..//Downloads";//다운로드 경로
        String fileName = "FeedBack.xlsx";//다운로드 파일 이음
        //엑셀 다운 시작
        XSSFWorkbook workbook = new XSSFWorkbook();
        //엑셀 시트명 생성
        Sheet sheet = workbook.createSheet("FeedBack");
        //행,열
        Row row = null;
        Cell cell = null;
        int bodyNUm=0;
        //헤더명
        String[] headerKey = {"이름", "이메일", "성별", "전화번호", "피드백 내용"};
        String[] BodyKey = {result.get(bodyNUm).getUserName(), result.get(bodyNUm).getUserEmail(), result.get(bodyNUm).getUserGender(),result.get(bodyNUm).getUserPhone(), result.get(bodyNUm).getFeedbackMessage()};


        row = sheet.createRow(0);
        for(int i=0; i<headerKey.length; i++) {		//헤더 구성
            cell = row.createCell(i);
            cell.setCellValue(headerKey[i]);

        }

        for(int i=0; i<result.size(); i++) {	//데이터 구성
            cell = row.createCell(i);
            cell.setCellValue(BodyKey[i]);

        }

        //셀 넓이 자동 조정
        for (int i=0; i<headerKey.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i));
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(filePath, fileName));
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return workbook;
    }

}
