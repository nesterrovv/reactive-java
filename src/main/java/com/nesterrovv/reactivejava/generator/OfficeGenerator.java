package com.nesterrovv.reactivejava.generator;

import com.nesterrovv.reactivejava.model.Colleague;
import com.nesterrovv.reactivejava.model.Office;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OfficeGenerator {

    private static final Random RANDOM = new Random();

    final static int MAX_EMPLOYEES = 10;
    final static int MIN_EMPLOYEES = 0;
    final static List<String> ADDRESSES = Arrays.asList("61 Pushkin St., Saint Petersburg, 630000, Georgia",
            "20 Nevsky Prospect, Nizhny Novgorod, 620000, Kazakhstan",
            "19 Nevsky Prospect, Moscow, 101000, Belarus",
            "14 Pushkin St., Moscow, 603000, Russia",
            "15 Sverdlov St., Ekaterinburg, 630000, Georgia",
            "10 Lenina St., Nizhny Novgorod, 630000, Georgia",
            "55 Sverdlov St., Novosibirsk, 620000, Russia",
            "32 Ligovsky Prospect, Kazan, 190000, Belarus",
            "14 Pushkin St., Nizhny Novgorod, 630000, Georgia",
            "43 Tverskaya St., Nizhny Novgorod, 420000, Belarus");

    private OfficeGenerator() {}

    public static Office generate(){
        Office office = new Office();
        office.setAddress(ADDRESSES.get(RANDOM.nextInt(ADDRESSES.size())));
        office.setColleagues(ColleagueGenerator.generateColleagues(RANDOM.nextInt(MIN_EMPLOYEES, MAX_EMPLOYEES)));
        return office;
    }

    public static List<Office> generate(int count) {
        List<Office> Offices = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Offices.add(generate());
        }
        return Offices;
    }
}
