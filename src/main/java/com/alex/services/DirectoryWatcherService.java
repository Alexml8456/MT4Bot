package com.alex.services;

import com.alex.repository.SQLRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;

@Service
@Slf4j
public class DirectoryWatcherService {
    @Value("${mt4.files.folder}")
    private String mt4Folder;

    @Autowired
    private CSVOperations csvOperations;

    @Autowired
    private SQLRepository sqlRepository;

    @Autowired
    private CsvMetrics csvMetrics;

    public void processEvents() {
        log.info("Will start to listen changes in csv files ...");
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            log.error("Watch service got exception  - ", e);
        }
        Path path = Paths.get(mt4Folder);
        try {
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            log.error("Exception during register watch service  - ", e);
        }

        WatchKey key;
        try {
            if (watchService != null) {
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        final Path changed = (Path) event.context();
                        if (changed.toString().endsWith(".csv")) {
                            csvOperations.saveValuesToList(mt4Folder + "/" + event.context());
                            log.info(Arrays.toString(csvMetrics.getCsvList().get(0)));
                            try {
                                sqlRepository.insertValues();
                            } catch (
                                    DataIntegrityViolationException e
                                    ) {
                                log.warn("Some data may be missed during writing to DB!");
                            }
                            ;
                            csvMetrics.getCsvList().clear();
                        }
                    }
                    key.reset();
                }
            }
        } catch (InterruptedException e) {
            log.error("Exception during process event  - ", e);
        }
    }
}