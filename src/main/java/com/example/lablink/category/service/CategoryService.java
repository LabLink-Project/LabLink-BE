package com.example.lablink.category.service;

import com.example.lablink.category.entity.Category;
import com.example.lablink.category.exception.CategoryErrorCode;
import com.example.lablink.category.exception.CategoryException;
import com.example.lablink.category.repository.CategoryRepository;
import com.example.lablink.study.dto.requestDto.StudyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void saveCategory(StudyRequestDto studyRequestDto, Long studyId){
        categoryRepository.save(new Category(studyRequestDto, studyId));
    }

    public Category getCategory(Long studyId) {
        return categoryRepository.findByStudyId(studyId).orElseThrow(
                ()-> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND)
        );
    }
}
