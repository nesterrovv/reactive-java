package com.nesterrovv.reactivejava.model;

import java.time.Instant;

public record PassportData(
        int series,
        int number,
        String issuedBy,
        Instant issueDate
) {}
