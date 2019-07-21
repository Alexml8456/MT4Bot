package com.alex.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileOperations {

    @Value("${mt4.files.folder}")
    private String mt4Folder;

    private final DataHolder dataHolder;

    public FileOperations(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    public void saveFilesToList() {
        try (Stream<Path> walk = Files.walk(Paths.get(mt4Folder))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(Path::toString).collect(Collectors.toList());

            if (result.size() > 0) {
                result.forEach(dataHolder::addFile);
            }

        } catch (IOException e) {
            log.error("Cannot read files from folder - " + e.getMessage(), e);
        }
        //log.info("List of files has been saved!");
    }
}
