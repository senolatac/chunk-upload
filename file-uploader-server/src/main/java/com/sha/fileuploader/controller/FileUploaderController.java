package com.sha.fileuploader.controller;

import com.google.gson.Gson;
import com.sha.fileuploader.dto.StringResponse;
import com.sha.fileuploader.model.*;
import com.sha.fileuploader.service.FileStorageService;
import com.sha.fileuploader.service.FileUploadService;
import com.sha.fileuploader.utils.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class FileUploaderController {

    Pattern contentRangePattern = Pattern.compile("bytes ([0-9]+)-([0-9]+)/([0-9]+)");
    Pattern authorizationPattern = Pattern.compile("Bearer ([A-Za-z0-9]+)");

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileUploadService fileUploadService;



    @RequestMapping(value = "/notify-completed", method = RequestMethod.POST)
    public FileMetadata notifyCompleted(@RequestBody CompleteFileInfo fileInfo){
        FileMetadata fileMetadata = fileUploadService.findByHash(fileInfo.getHash());
        if(!FileStatus.COMPLETED.equals(fileMetadata.getFileStatus())){
            FileMetadata mergeMetadata = fileStorageService.mergeFile(fileInfo);
            fileMetadata.setScreencastPath(mergeMetadata.getScreencastPath());
            fileMetadata.setDuration(mergeMetadata.getDuration());
            fileMetadata.setFileStatus(FileStatus.COMPLETED);
            fileUploadService.updateFile(fileMetadata);
        }
        return fileMetadata;
    }

    @PutMapping(value = "/ngx-upload/{uploadId}")
    public ResponseEntity<StringResponse> ngxUpload(@PathVariable String uploadId, final HttpServletRequest request)
            throws Exception{
        ContentRange contentRange = ServletUtil.getContentRange(contentRangePattern, request);
        FileMetadata fileMetadata = fileUploadService.findByUniqueId(uploadId);
        String fileName = fileStorageService.storeFile(fileMetadata, request.getInputStream(),
                contentRange.getCurrentChunk());
        if(contentRange.getEndByte()!=0 && contentRange.getEndByte()>=contentRange.getSize()){
            return new ResponseEntity<>(ServletUtil.createRange(Long.toString(contentRange.getEndByte()-1)),
                    HttpStatus.OK);
        }
        FileChunk fileChunk = new FileChunk();
        fileChunk.setMetadata(fileMetadata);
        fileChunk.setChunkNo(contentRange.getCurrentChunk());
        fileChunk.setCreatedTime(LocalDateTime.now());
        fileChunk.setStartByte(contentRange.getStartByte());
        fileChunk.setEndByte(contentRange.getEndByte());
        fileUploadService.saveChunk(fileChunk);
        return new ResponseEntity<>(
                ServletUtil.createRange(Long.toString(contentRange.getEndByte()-1)),
                HttpStatus.FOUND
        );
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, headers = "content-type!=multipart/form-data")
    public ResponseEntity<StringResponse> uploadChunk(final HttpServletRequest request,
                                                      final HttpServletResponse response) throws Exception{
        String token = ServletUtil.getAuthorization(authorizationPattern,request);
        FileMetadata metadata = new Gson().fromJson(request.getReader(), FileMetadata.class);
        if(metadata.getHash()==null){
            metadata.setHash(token);
        }
        FileMetadata exisFileMetadata = fileUploadService.findByHash(metadata.getHash());
        if(exisFileMetadata!=null && FileStatus.COMPLETED.equals(exisFileMetadata.getFileStatus())){
            return new ResponseEntity<>(
                    ServletUtil.createLocationAndRange(Long.toString(exisFileMetadata.getSize()),
                            "ngx-upload/"+metadata.getUniqueId()),
              HttpStatus.IM_USED

            );
        }else if (exisFileMetadata!=null){
            FileChunk fileChunk = fileUploadService.findLastChunk(metadata.getHash());
            return new ResponseEntity<>(
                    ServletUtil.createLocationAndRange(Long.toString(fileChunk.getEndByte() - 1),
                            "ngx-upload/"+metadata.getUniqueId()),
                    HttpStatus.ALREADY_REPORTED
            );
        }
        metadata.setCreatedTime(LocalDateTime.now());
        metadata.setUniqueId(UUID.randomUUID().toString());
        metadata.setFileStatus(FileStatus.UPLOADING);
        fileUploadService.saveFile(metadata);
        return new ResponseEntity<>(new StringResponse("ngx-upload"),
                ServletUtil.createLocation("ngx-upload/"+metadata.getUniqueId()), HttpStatus.OK);

    }

}
