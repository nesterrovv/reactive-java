package com.nesterrovv.reactivejava.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    private String title;
    private List<Office> offices;
}
