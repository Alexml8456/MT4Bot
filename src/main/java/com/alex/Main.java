package com.alex;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args)  {
        List<Object> csvList = new ArrayList<>();
        Object[] tmp = {"test", 5, 234.43, 5};
        csvList.addAll(Arrays.asList(tmp));
        System.out.println(csvList);

    }

}