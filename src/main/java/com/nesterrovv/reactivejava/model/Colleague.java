package com.nesterrovv.reactivejava.model;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colleague {

    private int age;
    private String name;
    private Instant birthTime;
    private Color eyeColor;
    private Color hairColor;
    private PassportData passportData;
    private List<JobResponsibility> jobResponsibilities;

}
