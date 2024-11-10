package com.nesterrovv.reactivejava.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Office {

    private String address;
    private List<Colleague> colleagues;
    private Company company;

}
