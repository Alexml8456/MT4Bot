package com.alex;


import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        String[] files = {"ETHUSD_240.gif", "ETHUSD_60.gif", "BTCUSD_240.gif", "BTCUSD_60.gif"};

        Arrays.sort(files, (o1, o2) -> {
            String n1 = o1.replaceFirst("[.][^.]+$", "");
            String n2 = o2.replaceFirst("[.][^.]+$", "");
            return n2.compareTo(n1);
        });

        System.out.println(Arrays.toString(files));

    }

}