package com.alex.services;

import com.alex.configuration.CurrencyProperties;
import com.alex.configuration.PeriodsProperties;
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
    private CurrencyProperties currency;

    @Autowired
    private DataHolder dataHolder;

    @Value("${mt4.files.folder}")
    private String mt4Folder;

    public void mergeImages() {
        currency.getPairs().stream()
                .forEach(pair -> {
                    mergeImageFiles(pair);
                });
        mergeImageFiles("Crypto");
    }

    private void mergeImageFiles(String pair) {
        String fileName;
        List<String> result;

        if (pair.equals("Crypto")) {
            fileName = "Crypto.png";
            result = dataHolder.getFileList().stream()
                    .filter(f -> f.endsWith(".png"))
                    .filter(f -> f.contains("BTC") || f.contains("ETH"))
                    .collect(Collectors.toList());
        } else {
            fileName = pair.concat(".png");
            result = dataHolder.getFileList().stream()
                    .filter(f -> f.endsWith(".gif"))
                    .filter(f -> f.contains(pair))
                    .collect(Collectors.toList());
        }

        int rows = result.size();
        int cols = 1;
        int chunks = rows * cols;
        int chunkWidth, chunkHeight;
        int type;

        File[] imgFiles = new File[chunks];

        for (int i = 0; i < result.size(); i++) {
            imgFiles[i] = new File(result.get(i));
        }

        sortInteger(imgFiles);

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
            log.info("Image for {} concatenated.....", pair);

            ImageIO.write(finalImg, "png", new File(mt4Folder.concat("/ScreenShots/").concat(fileName)));
        } catch (IOException e) {
            log.error("Cannot write file- {}", e.getMessage());
        }


    }

    private void sortInteger(File[] fileArray) {
        if (fileArray.length > 2) {
            Arrays.sort(fileArray, (o1, o2) -> {
                int n1 = Integer.valueOf(o1.getName().replaceFirst("[.][^.]+$", "").split("_")[1]);
                int n2 = Integer.valueOf(o2.getName().replaceFirst("[.][^.]+$", "").split("_")[1]);
                return n2 - n1;
            });
        }
    }

    private void sortString(File[] fileArray) {
        Arrays.sort(fileArray, (o1, o2) -> {
            String n1 = o1.getName().replaceFirst("[.][^.]+$", "");
            String n2 = o2.getName().replaceFirst("[.][^.]+$", "");
            return n2.compareTo(n1);
        });
    }

}
