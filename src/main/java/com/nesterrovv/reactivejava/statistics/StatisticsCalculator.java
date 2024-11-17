package com.nesterrovv.reactivejava.statistics;

import com.nesterrovv.reactivejava.model.Colleague;
import com.nesterrovv.reactivejava.model.Company;
import com.nesterrovv.reactivejava.model.JobResponsibility;
import com.nesterrovv.reactivejava.model.Office;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public final class StatisticsCalculator {

    public static final String COMPANY = "Company: ";
    public static final String RESPONSIBILITY = "Responsibility: ";
    public static final String PERCENTAGE = "Percentage: ";

    public static final double TO_PERCENTS = 100.0;

    private StatisticsCalculator() {}

    // 3.1 Iterative approach
    @SuppressWarnings("NestedForDepth")
    public static void calculateStatisticsIterative(List<Company> companies) {
        Map<String, List<JobResponsibility>> responsibilitiesByCompany = new HashMap<>();

        for (Company company : companies) {
            String companyName = company.getTitle();
            if (!responsibilitiesByCompany.containsKey(companyName)) {
                responsibilitiesByCompany.put(companyName, new ArrayList<>());
            }

            for (Office office : company.getOffices()) {
                for (Colleague colleague : office.getColleagues()) {
                    for (JobResponsibility jobResponsibility : colleague.getJobResponsibilities()) {
                        responsibilitiesByCompany.get(companyName).add(jobResponsibility);
                    }
                }
            }
        }
        for (Map.Entry<String, List<JobResponsibility>> entry : responsibilitiesByCompany.entrySet()) {
            Map<String, Double> responsibilitiesPercentage = calculatePercentsForCompany(entry);
            for (Map.Entry<String, Double> percentageEntry : responsibilitiesPercentage.entrySet()) {
                log.info(
                        RESPONSIBILITY
                        + percentageEntry.getKey()
                        + ", " + PERCENTAGE + percentageEntry.getValue() + "%"
                );
            }
        }
    }

    private static @NotNull Map<String, Double> calculatePercentsForCompany(
            Map.Entry<String, List<JobResponsibility>> entry
    ) {
        List<JobResponsibility> responsibilities = entry.getValue();
        long totalResponsibilities = responsibilities.size();
        Map<String, Long> responsibilitiesCount = new HashMap<>();
        for (JobResponsibility jobResponsibility : responsibilities) {
            String title = jobResponsibility.getTitle();
            responsibilitiesCount.put(title, responsibilitiesCount.getOrDefault(title, 0L) + 1);
        }
        Map<String, Double> responsibilitiesPercentage = new HashMap<>();
        for (Map.Entry<String, Long> countEntry : responsibilitiesCount.entrySet()) {
            String title = countEntry.getKey();
            long count = countEntry.getValue();
            double percentage = (count * TO_PERCENTS) / totalResponsibilities;
            responsibilitiesPercentage.put(title, percentage);
        }
        return responsibilitiesPercentage;
    }

    // 3.2 Stream API with standard collectors
    public static void calculateStatisticsWithStreams(List<Company> companies) {
        Map<String, List<JobResponsibility>> responsibilitiesByCompany = companies.stream()
                .flatMap(company -> company.getOffices().stream()
                        .flatMap(office -> office.getColleagues().stream()
                                .flatMap(colleague -> colleague.getJobResponsibilities().stream()
                                        .map(jobResponsibility
                                                -> new AbstractMap.SimpleEntry<>(company.getTitle(), jobResponsibility))
                                )
                        )
                )
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList()))
                );

        responsibilitiesByCompany.entrySet().stream()
                .map(entry -> {
                    String companyName = entry.getKey();
                    List<JobResponsibility> responsibilities = entry.getValue();
                    long totalResponsibilities = responsibilities.size();

                    Map<String, Long> responsibilitiesCount = responsibilities.stream()
                            .collect(Collectors.groupingBy(JobResponsibility::getTitle, Collectors.counting()));

                    Map<String, Double> responsibilitiesPercentage = responsibilitiesCount.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> (e.getValue() * TO_PERCENTS) / totalResponsibilities
                            ));

                    log.info(COMPANY + companyName);
                    responsibilitiesPercentage.entrySet().stream()
                            .map(e -> RESPONSIBILITY + e.getKey() + ", " + PERCENTAGE + e.getValue() + "%")
                            .forEach(log::info);

                    return null;
                })
                .toArray();
    }

    // 3.3 Custom collector
    public static void calculateStatisticsWithCustomCollector(List<Company> companies) {
        Map<String, Map<String, Double>> statistics = companies.stream().collect(StatisticsCollector.collect());
        statistics.entrySet().stream()
                .map(entry -> {
                    String companyName = entry.getKey();
                    Map<String, Double> statistic = entry.getValue();

                    Stream.concat(
                            Stream.of(COMPANY + companyName),
                            statistic.entrySet().stream()
                                    .map(e -> RESPONSIBILITY + e.getKey() + ", " + PERCENTAGE + e.getValue() + "%")
                    ).toArray();

                    return null;
                })
                .toArray();
    }

}
