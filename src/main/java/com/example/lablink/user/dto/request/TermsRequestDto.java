package com.example.lablink.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TermsRequestDto {

    private boolean marketingTerm;
    private boolean NotificationTerm;
}
