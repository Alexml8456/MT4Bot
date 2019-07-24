package com.alex;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        saveFilesToList();
    }

    public static void saveFilesToList() {
        try (Stream<Path> walk = Files.walk(Paths.get("/home/alexml/.wine/drive_c/Program Files (x86)/ForexClub MT4/MQL4/Files"))) {

            List<String> result = walk.filter(f -> f.getFileName().toString().endsWith(".gif"))
                    .map(Path::toString).collect(Collectors.toList());


            int rows = 2;   //we assume the no. of rows and cols are known and each chunk has equal width and height
            int cols = 2;
            int chunks = rows * cols;

            int chunkWidth, chunkHeight;
            int type;
            //fetching image files
            File[] imgFiles = new File[chunks];
            for (int i = 0; i < result.size(); i++) {
                imgFiles[i] = new File(result.get(i));
            }

            Arrays.sort(imgFiles, (o1, o2) -> {
                int n1 = Integer.valueOf(o1.getName().replaceFirst("[.][^.]+$", ""));
                int n2 = Integer.valueOf(o2.getName().replaceFirst("[.][^.]+$", ""));
                return n1 - n2;
            });

            //creating a bufferd image array from image files
            BufferedImage[] buffImages = new BufferedImage[chunks];
            for (int i = 0; i < chunks; i++) {
                buffImages[i] = ImageIO.read(imgFiles[i]);
            }
            type = buffImages[0].getType();
            chunkWidth = buffImages[0].getWidth();
            chunkHeight = buffImages[0].getHeight();

            //Initializing the final image
            BufferedImage finalImg = new BufferedImage(chunkWidth*cols, chunkHeight*rows, type);

            int num = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    finalImg.createGraphics().drawImage(buffImages[num], chunkWidth * j, chunkHeight * i, null);
                    num++;
                }
            }
            System.out.println("Image concatenated.....");
            ImageIO.write(finalImg, "png", new File("/home/alexml/.wine/drive_c/Program Files (x86)/ForexClub MT4/MQL4/Files/ScreenShots/MT4.png"));



        } catch (IOException e) {
            System.out.println(e);
        }
    }
}