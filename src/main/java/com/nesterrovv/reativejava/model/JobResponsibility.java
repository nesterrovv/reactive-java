package com.nesterrovv.reativejava.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponsibility {

    private String title;
    private String description;
    private int hoursPerWeek;
    private boolean isRemote;

}
