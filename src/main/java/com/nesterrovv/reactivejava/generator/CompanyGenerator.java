package com.nesterrovv.reactivejava.generator;

import com.nesterrovv.reactivejava.model.Company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CompanyGenerator {
    private static final Random RANDOM = new Random();

    final static int MIN_COUNT_OFFICES = 1;
    final static int MAX_COUNT_OFFICES = 5;
    final static List<String> TITLES = Arrays.asList("Blue Solutions",
            "NextGen Enterprises",
            "Quantum Networks",
            "Alpha Technologies",
            "Tech Networks",
            "Blue Systems",
            "NextGen Technologies",
            "Quantum Holdings",
            "Innovative Enterprises",
            "Innovative Technologies");

    private CompanyGenerator() {}

    public static Company generate() {
        Company company = new Company();
        company.setTitle(TITLES.get(RANDOM.nextInt(TITLES.size())));
        company.setOffices(OfficeGenerator.generate(RANDOM.nextInt(MIN_COUNT_OFFICES, MAX_COUNT_OFFICES)));
        return company;
    }

    public static List<Company> generate(int count) {
        List<Company> companies = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            companies.add(generate());
        }
        return companies;
    }
}
