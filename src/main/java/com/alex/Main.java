package com.alex;

public class Main {
    public static void main(String[] args) {
        System.out.println(conditionSecondToSell());
        System.out.println(conditionSecondToBuy());
    }


    private static boolean conditionSecondToSell() {
        double test = 0.09;
        return test <= 0.0999 && test >= -0.0999;
    }

    private static boolean conditionSecondToBuy() {
        double test = 0.0933;
        return test <= 0.0999 && test >= -0.0999;
    }
}