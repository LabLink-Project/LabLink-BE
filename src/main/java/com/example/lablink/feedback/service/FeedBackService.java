package com.example.lablink.feedback.service;


import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.feedback.dto.Request.FeedBackRequestDto;
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
        feedBackRepository.save(new Feedback(userDetails.getUser(),study,feedBackRequestDto.getFeedbackMessage(),feedBackRequestDto.isManagement()));
    }
    @Transactional(readOnly = true)
    public List<FeedBackResponseDto> getFeedBack(CompanyDetailsImpl companyDetails, Long studyId) {
       List<Feedback> feedbacks= feedBackRepository.findAllByStudyId(studyId);
       List<FeedBackResponseDto> result = new ArrayList<>();
        for (Feedback feedback: feedbacks) {
            result.add(new FeedBackResponseDto(feedback));
        }
        return result;
    }
    @Transactional(readOnly = true)
    public Object getDetailFeedBack(CompanyDetailsImpl companyDetails, Long studyId, Long feedbackId) {

        Feedback feedback =feedBackRepository.findById(feedbackId).orElseThrow(
                ()-> new FeedBackException(FeedBackErrorCode.Feed_Back_NOT_FOUND)
        );
        if(!isMatchFeedBack(feedback,studyId,companyDetails.getCompany().getId())){
            throw new FeedBackException(FeedBackErrorCode.NOT_HAVE_PERMISSION);
        }

        return null;
    }

    public void excelDownloadFeedBack(CompanyDetailsImpl companyDetails, Long studyId)throws IOException {
        List<Feedback> feedbacks= feedBackRepository.findAllByStudyId(studyId);
        List<FeedBackResponseDto> result = new ArrayList<>();
        for (Feedback feedback: feedbacks) {
            result.add(new FeedBackResponseDto(feedback));
        }
        String filePath = "../..//Downloads";
        String fileName = "FeedBack.xlsx";

        XSSFWorkbook workbook = new XSSFWorkbook();

        // 빈 Sheet를 생성
        XSSFSheet sheet = workbook.createSheet("FeedBack");

        // Sheet를 채우기 위한 데이터들을 Map에 저장
        Map<Integer, Object[]> data = new TreeMap<>();
        data.put(1, new Object[]{"이름", "이메일", "피드백 내용"});
        int rowdata=1;
        for(Integer i=0;i<result.size();i++){
            data.put(++rowdata, new Object[]{result.get(i).getUserName(),result.get(i).getUserEmail() ,result.get(i).getFeedbackMessage()});
        }


        // data에서 keySet를 가져온다. 이 Set 값들을 조회하면서 데이터들을 sheet에 입력한다.
        Set<Integer> keyset = data.keySet();
        int rownum = 0;

        // 알아야할 점, TreeMap을 통해 생성된 keySet는 for를 조회시, 키값이 오름차순으로 조회된다.
        for (Integer key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String) {
                    cell.setCellValue((String)obj);
                } else if (obj instanceof Integer) {
                    cell.setCellValue((Integer)obj);
                }
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(filePath, fileName));
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public boolean isMatchFeedBack(Feedback feedback,Long studyId,Long companuId){
       return feedback.getStudy().getId().equals(studyId) || feedback.equals(companuId);
    }


}
