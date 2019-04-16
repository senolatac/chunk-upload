package com.sha.fileuploader.service;

import com.sha.fileuploader.config.FileStorageProperties;
import com.sha.fileuploader.exception.FileStorageException;
import com.sha.fileuploader.exception.MyFileNotFoundException;
import com.sha.fileuploader.model.CompleteFileInfo;
import com.sha.fileuploader.model.FileMetadata;
import com.sha.fileuploader.utils.VideoUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public FileMetadata mergeFile(final CompleteFileInfo fileInfo){
        FileMetadata metadata = new FileMetadata();
        ///ngx-upload/fdsd84u349823432
        String uniqueFileName = fileInfo.getName().substring(fileInfo.getName().lastIndexOf("/")+1);
        metadata.setUniqueId(uniqueFileName);
        File splitFiles = new File(this.fileStorageLocation.toAbsolutePath().toString() + File.separator + "splits"
        + File.separator + uniqueFileName + File.separator);
        try{
            if(splitFiles.exists()){
                File[] files = splitFiles.getAbsoluteFile().listFiles();
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                if(files.length!=0){
                    String joinFileName = Arrays.asList(files).get(0).getName();
                    String pureFileName = joinFileName.substring(0, joinFileName.lastIndexOf("."));
                    File fileJoinPath = new File(this.fileStorageLocation.toAbsolutePath().toString() + File.separator
                    + "joins" + File.separator + pureFileName);
                    if(!fileJoinPath.exists()){
                        fileJoinPath.mkdirs();
                    }
                    OutputStream outputStream = new FileOutputStream(fileJoinPath.getAbsolutePath() + File.separator +
                            joinFileName, true);
                    for(File file : files){
                        InputStream inputStream = new FileInputStream(file);
                        byte[] fileBytes = new byte[(int)file.length()];
                        int bytesRead = inputStream.read(fileBytes, 0, (int) file.length());
                        assert (bytesRead == fileBytes.length);
                        assert (bytesRead == (int)file.length());
                        outputStream.write(fileBytes);
                        outputStream.flush();
                        fileBytes = null;
                        inputStream.close();
                        inputStream = null;
                    }
                    metadata.setFilePath(fileJoinPath.getAbsolutePath() + File.separator + joinFileName);
                    FileMetadata screePath = VideoUtil.getScreenCastFromVideo(fileJoinPath.getAbsolutePath(),
                            fileJoinPath.getAbsolutePath() + File.separator + joinFileName);
                    metadata.setScreencastPath(fileJoinPath.getAbsolutePath() + File.separator +
                            screePath.getScreencastPath());
                    metadata.setDuration(screePath.getDuration());
                    outputStream.close();
                    FileUtils.deleteDirectory(new File(splitFiles.getAbsolutePath()));
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return metadata;
    }

    public String storeFile(FileMetadata fileMetadata, InputStream stream, int currentChunk){
        String pureFileName = fileMetadata.getName().substring(0, fileMetadata.getName().lastIndexOf("."));
        File splitFile = new File(this.fileStorageLocation.toAbsolutePath().toString() + File.separator + "splits"
                + File.separator + fileMetadata.getUniqueId());
        if(!splitFile.exists()){
            splitFile.mkdirs();
        }

        String fileCount = String.format("%04d", currentChunk); //0001
        String fileName = fileCount + "_" + StringUtils.cleanPath(fileMetadata.getName());
        String chunkFileName = splitFile.getAbsolutePath() + File.separator + fileName;
        try{
            Path targetLocation = Paths.get(chunkFileName).toAbsolutePath().normalize();
            Files.copy(stream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return chunkFileName;
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return pureFileName;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
