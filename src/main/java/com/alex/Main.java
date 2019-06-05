package com.alex;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String test = new File("/home/alexml/.wine/drive_c/Program Files (x86)/ForexClub MT4/MQL4/Files/ETHUSD_15.csv").getName();
        System.out.println(test.replaceFirst("[.][^.]+$", ""));
    }
}
