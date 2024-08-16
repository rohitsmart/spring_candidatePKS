package com.candidate.pks.auth.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Designation {
    BACKEND_DEV("Back-end Developer"),
    FULLSTACK_DEV("Fullstack Developer"),
    FRONTEND_DEV("Front-end Developer"),
    JUNIOR_HR("Junior HR"),
    SENIOR_HR("Senior HR"),
    CEO("Chief Executive Officer"),
    SOFTWARE_ENGINEER("Software Engineer"),
    NATIVE_DEV("Native Developer"),
    FLUTTER_DEV("Flutter Developer");

    private final String designationName;

}
