package com.nesterrovv.reativejava.statistics;

import com.nesterrovv.reativejava.model.Color;
import java.util.Map;

class Statistics {

    double averageAge;
    Map<Color, Double> hairColorPercentage;
    Map<Color, Double> eyeColorPercentage;
    String mostPopularIssuedBy;
    double averageWorkload;

    @Override
    public String toString() {
        return "Statistics{"
                + "averageAge=" + averageAge
                + ", hairColorPercentage=" + hairColorPercentage
                + ", eyeColorPercentage=" + eyeColorPercentage
                + ", mostPopularIssuedBy='" + mostPopularIssuedBy
                + '\''
                + ", averageWorkload=" + averageWorkload
                + '}';
    }
}
