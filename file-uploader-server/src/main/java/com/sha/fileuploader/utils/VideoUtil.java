package com.sha.fileuploader.utils;

import com.sha.fileuploader.model.FileMetadata;
import org.bytedeco.javacv.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


public class VideoUtil {

    private static final String DOT = ".";

    public static String extension(String fileName) {
        fileName = fileName.replace("__thumb", "").replace("jpg_", "jpg").replace("png_", "png").replace("jpeg_", "jpeg");
        final int indexOfExtension = fileName.lastIndexOf(DOT);
        if(indexOfExtension >= 0) {
            return fileName.substring(indexOfExtension + 1, fileName.length());
        }
        else {
            return null;
        }
    }

    public static String createPrePath(Date date){
        return TimeUtil.extractYear(date) + File.separator + TimeUtil.extractMonth(date) + File.separator + TimeUtil.extractDay(date) + File.separator;
    }

    public static void saveFileToSystem(String folderPath, String prePath, String nameOnDisk, BufferedImage uploadFile){
        try{
            final File folder = new File(folderPath + File.separator +prePath);
            if(!folder.exists()) {
                if(!folder.mkdirs()) {
                    throw new Exception("File is not created. " + folderPath);
                }
            }
            ImageIO.write(uploadFile, extension(nameOnDisk),
                    new FileOutputStream(folderPath + File.separator + prePath + nameOnDisk));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateFileName(String fileName){
        String randomName = UUID.randomUUID().toString();
        if(extension(fileName)!=null){
            return randomName + DOT + extension(fileName);
        }else{
            return null;
        }
    }
}
