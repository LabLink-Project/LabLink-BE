//package com.example.lablink.study.service;
//
//import com.example.lablink.S3Image.dto.S3ResponseDto;
//import com.example.lablink.S3Image.entity.S3Image;
//import com.example.lablink.S3Image.service.S3Service;
//import com.example.lablink.S3Image.service.S3UploaderService;
//import com.example.lablink.bookmark.service.BookmarkService;
//import com.example.lablink.company.entity.Company;
//import com.example.lablink.company.security.CompanyDetailsImpl;
//import com.example.lablink.study.dto.requestDto.StudyRequestDto;
//import com.example.lablink.study.dto.responseDto.StudyDetailResponseDto;
//import com.example.lablink.study.entity.CategoryEnum;
//import com.example.lablink.study.entity.Study;
//import com.example.lablink.study.exception.StudyException;
//import com.example.lablink.study.repository.StudyRepository;
//import com.example.lablink.user.entity.User;
//import com.example.lablink.user.security.UserDetailsImpl;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static com.example.lablink.support.StudyStub.Study1;
//import static com.example.lablink.support.StudyStub.Study2;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class StudyServiceTest {
//    @InjectMocks
//    private StudyService studyService;
//
//    @Mock
//    private StudyRepository studyRepository;
//
//    @Mock
//    private GetStudyService getStudyService;
//
//    @Mock
//    private BookmarkService bookmarkService;
//
//    @Mock
//    private S3UploaderService s3UploaderService;
//
//    @Mock
//    private S3Service s3Service;
//
////    @BeforeEach
////    void setUp() {
////        ReflectionTestUtils.setField(studyService, "studyRepository", studyRepository);
////    }
//
//    private StudyRequestDto requestDto = StudyRequestDto.builder()
//            .title("Study Title")
//            .studyInfo("Study Info")
//            .studyPurpose("Study Purpose")
//            .studyAction("Study Action")
//            .subjectCount(10L)
//            .category(CategoryEnum.ONLINE)
//            .date(LocalDateTime.now())
//            .address("Study Address")
//            .pay(10000)
//            .subjectGender("Male")
//            .subjectMinAge(20)
//            .subjectMaxAge(30)
//            .repeatCount(5)
//            .endDate(LocalDateTime.now().plusDays(30))
//            .image(new MockMultipartFile("image", "image.png", "image/png", new byte[0]))
//            .build();
//
//    @Test
//    @DisplayName("공고 작성 with image")
//    void testCreateStudyWithS3ResponseDto() {
//        // given
//        CompanyDetailsImpl companyDetails = new CompanyDetailsImpl(new Company(), new Company().getEmail());
//        S3ResponseDto s3ResponseDto = new S3ResponseDto(new S3Image());
//        s3ResponseDto.setUploadFileUrl("test");
//
//        Long studyId = 1L;
//        Study saveStudy = Study1.of(studyId);
//
//        given(studyRepository.save(any(Study.class))).willReturn(saveStudy);
//
//        // when
//        studyService.createStudy(requestDto, companyDetails, s3ResponseDto);
//
//        // then
//        // verify the save method is called with correct argument
//        verify(studyRepository, times(1)).save(any(Study.class));
//    }
//
//    @Test
//    @DisplayName("공고 작성 without image")
//    void testCreateStudyWithNullS3ResponseDto() {
//        // given
//        CompanyDetailsImpl companyDetails = new CompanyDetailsImpl(new Company(), new Company().getEmail());
//        given(studyRepository.save(any(Study.class))).willReturn(new Study());
//
//        // when
//        studyService.createStudy(requestDto, companyDetails, null);
//
//        // then
//        // verify the save method is called with correct argument
//        verify(studyRepository, times(1)).save(any(Study.class));
//    }
//
//    @Test
//    @DisplayName("공고 상세 조회")
//    void getDetailStudy() {
//        // given
//        Long studyId = 1L;
//        UserDetailsImpl userDetails = new UserDetailsImpl(new User(), "test@test.com");
//        Study savedStudy = Study1.of(studyId);
//        given(getStudyService.getStudy(studyId)).willReturn(savedStudy);
//        given(bookmarkService.checkBookmark(studyId,userDetails.getUser())).willReturn(true);
//
//        // when
//        StudyDetailResponseDto studyDetailResponseDto = studyService.getDetailStudy(studyId, userDetails);
//
//        // then
//        assertEquals(studyId, studyDetailResponseDto.getId());
//        assertTrue(studyDetailResponseDto.isIsbookmarked());
//    }
//
//    @Test
//    @DisplayName("공고 수정 - 이미지까지 수정할 경우")
//    void testUpdateStudyWithImage() {
//        // given
//        CompanyDetailsImpl companyDetails = new CompanyDetailsImpl(new Company(), new Company().getEmail());
//        Long studyId = 1L;
//        Study existingStudy = Study1.of(studyId);
//        given(getStudyService.getStudy(studyId)).willReturn(existingStudy);
//        given(studyRepository.findByIdAndCompany(existingStudy.getId(), companyDetails.getCompany())).willReturn(Optional.of(existingStudy));
////        given(studyService.isCompanyLogin(companyDetails)).willReturn(new Company());
//
//        S3ResponseDto s3ResponseDto = new S3ResponseDto(new S3Image());
//        s3ResponseDto.setOriginalFileName("test");
//        s3ResponseDto.setUploadFileUrl("testUploadFileUrl");
//        s3ResponseDto.setUploadFileName("testUploadFileName.jpg");
//        s3ResponseDto.setUploadFilePath("testUploadFilePath");
////        given(s3Service.getS3Image(existingStudy.getImageURL())).willReturn(new S3Image());
//        given(s3UploaderService.uploadFiles(eq("thumbnail"), any(MultipartFile.class))).willReturn(s3ResponseDto);
//        // when
//        studyService.updateStudy(studyId, requestDto, companyDetails);
//
//        // then
//        assertAll(() ->assertThat(existingStudy.getTitle()).isEqualTo(requestDto.getTitle()));
////        verify(getStudyService, times(1)).getStudy(existingStudy.getId());
////        verify(studyService, times(1)).isCompanyLogin(companyDetails);
////        verify(s3Service, times(1)).getS3Image(existingStudy.getImageURL());
////        verify(s3UploaderService, times(1)).uploadFiles(eq("thumbnail"), any(MultipartFile.class));
////        verify(studyRepository, times(1)).save(any(Study.class));
////        assertThat(existingStudy.getTitle()).isEqualTo(requestDto.getTitle());
////        assertThat(existingStudy.getStatus()).isEqualTo(studyService.setStatus(requestDto));
////        assertThat(existingStudy.getImageURL()).isEqualTo("testUploadFileUrl");
//    }
//
//    @Test
//    @DisplayName("공고 수정 - 이미지까지 수정하지 않을 경우")
//    void testUpdateStudyWithoutImage() {
//        // given
//        StudyRequestDto requestDto = StudyRequestDto.builder()
//                .title("Study Title")
//                .studyInfo("Study Info")
//                .studyPurpose("Study Purpose")
//                .studyAction("Study Action")
//                .subjectCount(10L)
//                .category(CategoryEnum.ONLINE)
//                .date(LocalDateTime.now())
//                .address("Study Address")
//                .pay(10000)
//                .subjectGender("Male")
//                .subjectMinAge(20)
//                .subjectMaxAge(30)
//                .repeatCount(5)
//                .endDate(LocalDateTime.now().plusDays(30))
//                .build();
//        CompanyDetailsImpl companyDetails = new CompanyDetailsImpl(new Company(), new Company().getEmail());
//        Long studyId = 1L;
//        Study existingStudy = Study1.of(studyId);
//        given(getStudyService.getStudy(studyId)).willReturn(existingStudy);
//        given(studyRepository.findByIdAndCompany(existingStudy.getId(), companyDetails.getCompany())).willReturn(Optional.of(existingStudy));
////        given(studyService.isCompanyLogin(companyDetails)).willReturn(new Company());
//
//        // when
//        studyService.updateStudy(studyId, requestDto, companyDetails);
//
//        // then
//        assertAll(() ->assertThat(existingStudy.getTitle()).isEqualTo(requestDto.getTitle()));
////        verify(getStudyService, times(1)).getStudy(existingStudy.getId());
////        verify(studyService, times(1)).isCompanyLogin(companyDetails);
////        verify(studyRepository, times(1)).save(any(Study.class));
////        assertThat(existingStudy.getTitle()).isEqualTo(requestDto.getTitle());
////        assertThat(existingStudy.getStatus()).isEqualTo(studyService.setStatus(requestDto));
////        assertThat(existingStudy.getImageURL()).isNull();
//    }
//
//    @Test
//    // checkRole
//    @DisplayName("공고 수정 - 공고 작성자가 아니면 예외 발생")
//    void updateStudy_NotAuthorException(){
//        // given
//        Long studyId = 1L;
//        CompanyDetailsImpl companyDetails = new CompanyDetailsImpl(new Company(), new Company().getEmail());
//        Study savedStudy = Study1.of(studyId);
//        given(getStudyService.getStudy(studyId)).willReturn(savedStudy);
//
//        // when & then
//        assertAll(() -> assertThatThrownBy(() -> studyService.updateStudy(studyId, requestDto, companyDetails)).isInstanceOf(
//                StudyException.class
//        ));
////        assertThrows(StudyException.class, () -> studyService.updateStudy(studyId, requestDto, companyDetails));
//    }
//
////    @Test
////    void updateStudy() {
////        // given
////        Long studyId = 1L;
////        CompanyDetailsImpl companyDetails = new CompanyDetailsImpl(new Company(), "test@test.com");
////        Study existingStudy = new Study(requestDto, StudyStatusEnum.ONGOING, companyDetails.getCompany(), null);
////        // getStudyService.getStudy() 메서드가 호출될 때 마다 existingStudy 객체가 반환됨
////        given(getStudyService.getStudy(studyId)).willReturn(existingStudy);
////        given(studyRepository.findByIdAndCompany(studyId, companyDetails.getCompany())).willReturn(Optional.of(existingStudy));
////
////        S3Image s3Image = S3Image.builder()
////                .originalFileName("test.jpg")
////                .uploadFileName("test_12345.jpg")
////                .uploadFilePath("/uploads")
////                .uploadFileUrl("https://example.com/test_12345.jpg")
////                .build();
////        given(s3Service.getS3Image(existingStudy.getImageURL())).willReturn(s3Image);
////
////        // when
////        studyService.updateStudy(studyId, requestDto, companyDetails);
////
////        // then
////        // verify that the study was updated correctly
////        Study updatedStudy = getStudyService.getStudy(studyId);
////        assertAll(
////                () -> assertThat(updatedStudy.getId()).isEqualTo(studyId),
////                () -> assertThat(updatedStudy.getTitle()).isEqualTo(requestDto.getTitle()),
////                () -> assertThat(updatedStudy.getImageURL()).isEqualTo(existingStudy.getImageURL()),
////                () -> assertThat(updatedStudy.getCompany()).isEqualTo(existingStudy.getCompany()),
////                () -> assertThat(updatedStudy.getStatus()).isEqualTo(StudyStatusEnum.ONGOING)
////        );
////
////        // verify that the S3UploaderService was called to delete the original image
////        verify(s3UploaderService, times(1)).deleteFile(s3Image.getId());
////
////        // verify that the S3UploaderService was not called to upload a new image
////        verify(s3UploaderService, never()).uploadFiles(anyString(), any(MultipartFile.class));
////
////        // verify that the save method was called to update the study
////        verify(studyRepository, times(1)).save(any(Study.class));
////    }
//
//    @Test
//    @DisplayName("공고 삭제")
//    void deleteStudy() {
//        // given
//        CompanyDetailsImpl companyDetails = new CompanyDetailsImpl(new Company(), new Company().getEmail());
//        Long studyId = 1L;
//        Study savedStudy = Study1.of(studyId);
//        given(getStudyService.getStudy(studyId)).willReturn(savedStudy);
//        given(studyRepository.findByIdAndCompany(savedStudy.getId(), companyDetails.getCompany())).willReturn(Optional.of(savedStudy));
//
//        // when
//        studyService.deleteStudy(studyId, companyDetails);
//
//        // then
//        verify(bookmarkService).deleteByStudyId(studyId);
//        verify(getStudyService).getStudy(studyId);
//        verify(studyRepository).deleteById(studyId);
////        verify(s3UploaderService).deleteFile(any());
//    }
//
//    @Test
//    @DisplayName("공고 삭제 - 작성자가 아니면 예외 발생")
//    void deleteStudy_NotAuthorException() {
//        // given
//        Long studyId = 1L;
//        CompanyDetailsImpl companyDetails = new CompanyDetailsImpl(new Company(), new Company().getEmail());
//        Study savedStudy = Study1.of(studyId);
//        given(getStudyService.getStudy(studyId)).willReturn(savedStudy);
//
//        // when & then
////        assertAll(() -> assertThatThrownBy(() -> studyService.deleteStudy(studyId, companyDetails)).isInstanceOf(
////                StudyException.class
////        ));
//        assertThrows(StudyException.class, () -> studyService.deleteStudy(studyId, companyDetails));
//    }
//
//    @Test
//    @DisplayName("기업별 공고 찾기")
//    void testFindAllCompanyStudy() {
//        // Given
//        Company company = new Company();
//        List<Study> studies = Arrays.asList(
//                Study1.of(1L),
//                Study2.of(2L)
//        );
//        when(studyRepository.findAllByCompany(company)).thenReturn(studies);
//
//        // When
//        List<Study> result = studyService.findAllCompanyStudy(company);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertEquals("Study1 Title", result.get(0).getTitle());
//        assertEquals("Study2 Title", result.get(1).getTitle());
//    }
//
//    @Test
//    @DisplayName("기업이 작성한 공고 찾기")
//    void findStudyFromCompany() {
//        // given
//        Long studyId = 1L;
//        Company company = new Company();
//        Study study = Study1.of(studyId);
//        given(studyRepository.findByIdAndCompany(studyId, company)).willReturn(Optional.of(study));
//
//        // when
//        Study result = studyService.findStudyFromCompany(studyId, company);
//
//        // then
//        assertNotNull(result);
//        assertEquals(studyId, result.getId());
//        assertEquals(company.getId(), result.getCompany().getId());
//        assertEquals("Study1 Title", result.getTitle());
//    }
//}
