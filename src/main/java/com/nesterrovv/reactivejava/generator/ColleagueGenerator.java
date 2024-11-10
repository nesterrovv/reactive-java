package com.nesterrovv.reactivejava.generator;

import com.nesterrovv.reactivejava.model.Colleague;
import com.nesterrovv.reactivejava.model.Color;
import com.nesterrovv.reactivejava.model.JobResponsibility;
import com.nesterrovv.reactivejava.model.Office;
import com.nesterrovv.reactivejava.model.PassportData;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class ColleagueGenerator {

    public static final int MIN_JOBS_NUMBER = 1;
    public static final int MAX_JOBS_NUMBER = 3;
    public static final int MAXIMAL_AGE_BOUND = 43;
    public static final int MINIMAL_AGE_BOUND = 18;
    public static final long DAYS_IN_YEAR = 365L;
    public static final int MAX_PASSPORT_AGE_YEARS = 20;
    public static final int MIN_PASSPORT_AGE_YEARS = 1;
    public static final int MIN_PASSPORT_NUMBER_BOUND = 100000;
    public static final int MAX_PASSPORT_NUMBER_BOUND = 900000;
    public static final int MIN_PASSPORT_SERIES_BOUND = 1000;
    public static final int MAX_PASSPORT_SERIES_BOUND = 9000;

    private ColleagueGenerator() {}

    private static final Random RANDOM = new Random();
    private static final List<String> NAMES = Arrays.asList("Ivan", "Alexey", "Maria", "Olga", "Dmitry", "Anna");
    private static final List<String> ISSUED_BY = Arrays.asList("Moscow", "St. Petersburg", "Murmansk", "Arkhangelsk");
    private static final List<JobResponsibility> RESPONSIBILITIES = Arrays.asList(
            new JobResponsibility("Develop Software", "Write and maintain code for projects", 40, false),
            new JobResponsibility("Test Software", "Perform testing and debugging", 30, true),
            new JobResponsibility("Manage Projects", "Coordinate team efforts and ensure deadlines are met", 20, false),
            new JobResponsibility("Design UI/UX", "Create user-friendly designs and layouts", 25, true),
            new JobResponsibility("Write Documentation", "Document code and project processes", 15, true),
            new JobResponsibility("Customer Support", "Assist customers with product-related issues", 35, false),
            new JobResponsibility("Conduct Training", "Train new employees and conduct workshops", 10, false),
            new JobResponsibility("Analyze Data", "Perform data analysis to support decision-making", 20, true)
    );

    public static Colleague generateColleague(Office office) {
        int age = RANDOM.nextInt(MAXIMAL_AGE_BOUND) + MINIMAL_AGE_BOUND; // Random age between 18 and 60
        String name = NAMES.get(RANDOM.nextInt(NAMES.size()));
        Instant birthTime = Instant.now().minus(age * DAYS_IN_YEAR, ChronoUnit.DAYS);
        Color eyeColor = Color.values()[RANDOM.nextInt(Color.values().length)];
        Color hairColor = Color.values()[RANDOM.nextInt(Color.values().length)];
        PassportData passportData = new PassportData(
                MIN_PASSPORT_SERIES_BOUND + RANDOM.nextInt(MAX_PASSPORT_SERIES_BOUND), // 4-digit series
                MIN_PASSPORT_NUMBER_BOUND + RANDOM.nextInt(MAX_PASSPORT_NUMBER_BOUND), // 6-digit number
                ISSUED_BY.get(RANDOM.nextInt(ISSUED_BY.size())),
                LocalDate.now().minus(RANDOM.nextInt(MAX_PASSPORT_AGE_YEARS)
                        + MIN_PASSPORT_AGE_YEARS, ChronoUnit.YEARS).atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
        List<JobResponsibility> jobResponsibilities = new ArrayList<>();
        int numResponsibilities = RANDOM.nextInt(MAX_JOBS_NUMBER) + MIN_JOBS_NUMBER;
        // Random number of responsibilities between 1 and 3
        for (int i = 0; i < numResponsibilities; i++) {
            jobResponsibilities.add(RESPONSIBILITIES.get(RANDOM.nextInt(RESPONSIBILITIES.size())));
        }
        return new Colleague(age, name, birthTime, eyeColor, hairColor, passportData, jobResponsibilities, office);
    }

    public static List<Colleague> generateColleagues(int count, Office office) {
        List<Colleague> colleagues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            colleagues.add(generateColleague(office));
        }
        return colleagues;
    }

}
