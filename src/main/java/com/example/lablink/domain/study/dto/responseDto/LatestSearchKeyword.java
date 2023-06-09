package com.example.lablink.domain.study.dto.responseDto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ZSetOperations;

@Getter
@Setter
public class LatestSearchKeyword {
    private String latestKeyword;

    public static LatestSearchKeyword convertToLatestSearchKeyword(ZSetOperations.TypedTuple<String> stringTypedTuple) {
        LatestSearchKeyword latestSearchKeyword = new LatestSearchKeyword();
        latestSearchKeyword.setLatestKeyword(stringTypedTuple.getValue());
        return latestSearchKeyword;
    }
}
