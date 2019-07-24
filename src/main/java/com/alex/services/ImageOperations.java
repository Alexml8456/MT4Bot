package com.alex.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service
public class ImageOperations {

    @Autowired
    private DataHolder dataHolder;

    @Value("${mt4.files.folder}")
    private String mt4Folder;

    public void mergeImageFiles(){
        int rows = 2;
        int cols = 2;
        int chunks = rows * cols;
        File[] imgFiles = new File[chunks];

        dataHolder.getFileList().stream().filter(f -> f.endsWith(".gif")).forEach((file) -> {

        });





    }
}
