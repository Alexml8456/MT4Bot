package com.alex.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
@Slf4j
public class DirectoryWatcherService {
    @Value("${mt4.files.folder}")
    private String mt4Folder;

    public void processEvents(){
        log.info("Will start to listen changes in csv files ...");
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            log.error("Watch service got exception  - ", e);
        }
        Path path = Paths.get("/home/alexml/Downloads/CSV-Data");
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
                            log.info("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
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
