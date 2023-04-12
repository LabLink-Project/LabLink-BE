package com.example.lablink.study.dto.responseDto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ZSetOperations;

@Getter
@Setter
public class SearchRankResponseDto {
    private String rankKeyword;
    private Double score;

    public static SearchRankResponseDto convertToResponseRankingDto(ZSetOperations.TypedTuple<String> stringTypedTuple) {
        SearchRankResponseDto searchRankResponseDto = new SearchRankResponseDto();
        searchRankResponseDto.setRankKeyword(stringTypedTuple.getValue());
        searchRankResponseDto.setScore(stringTypedTuple.getScore());
        return searchRankResponseDto;
    }
}
