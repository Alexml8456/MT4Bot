package com.alex.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImageOperations {

    @Autowired
    private DataHolder dataHolder;

    @Value("${mt4.files.folder}")
    private String mt4Folder;

    public void mergeImageFiles() {
        int rows = 4;
        int cols = 1;
        int chunks = rows * cols;
        int chunkWidth, chunkHeight;
        int type;


        File[] imgFiles = new File[chunks];
        List<String> result = dataHolder.getFileList().stream().filter(f -> f.endsWith(".gif")).collect(Collectors.toList());

        for (int i = 0; i < result.size(); i++) {
            imgFiles[i] = new File(result.get(i));
        }

        Arrays.sort(imgFiles, (o1, o2) -> {
            int n1 = Integer.valueOf(o1.getName().replaceFirst("[.][^.]+$", ""));
            int n2 = Integer.valueOf(o2.getName().replaceFirst("[.][^.]+$", ""));
            return n1 - n2;
        });
        try {
            //creating a bufferd image array from image files
            BufferedImage[] buffImages = new BufferedImage[chunks];
            for (int i = 0; i < chunks; i++) {
                buffImages[i] = ImageIO.read(imgFiles[i]);
            }
            type = buffImages[0].getType();
            chunkWidth = buffImages[0].getWidth();
            chunkHeight = buffImages[0].getHeight();

            //Initializing the final image
            BufferedImage finalImg = new BufferedImage(chunkWidth * cols, chunkHeight * rows, type);

            int num = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    finalImg.createGraphics().drawImage(buffImages[num], chunkWidth * j, chunkHeight * i, null);
                    num++;
                }
            }
            log.info("Image concatenated.....");

            ImageIO.write(finalImg, "jpg", new File(mt4Folder.concat("/ScreenShots/").concat("MT4.jpg")));
        } catch (IOException e) {
            log.error("Cannot write file- {}", e.getMessage());
        }


    }
}
