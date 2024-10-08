package com.nesterrovv.reativejava;

import com.nesterrovv.reativejava.timer.StatisticsCalculatorTimer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Main {

    public static final int SMALL_COLLECTION_SIZE = 5000;
    public static final int MEDIUM_COLLECTION_SIZE = 50000;
    public static final int LARGE_COLLECTION_SIZE = 250000;

    private Main() {}

    public static void main(String[] args) {
        log.info("--- SMALL COLLECTION ---");
        StatisticsCalculatorTimer.calculate(SMALL_COLLECTION_SIZE);
        log.info("--- MEDIUM COLLECTION ---");
        StatisticsCalculatorTimer.calculate(MEDIUM_COLLECTION_SIZE);
        log.info("--- LARGE COLLECTION ---");
        StatisticsCalculatorTimer.calculate(LARGE_COLLECTION_SIZE);
        log.info("Test of develop branch");
    }

}
