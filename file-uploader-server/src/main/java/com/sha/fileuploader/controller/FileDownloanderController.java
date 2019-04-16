package com.sha.fileuploader.controller;

import com.sha.fileuploader.service.FileStorageService;
import com.sha.fileuploader.utils.EncryptionUtil;
import com.sha.fileuploader.utils.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileDownloanderController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        String decryptPath = EncryptionUtil.decrypt(fileName);
        Resource resource = fileStorageService.loadFileAsResource(decryptPath);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            //logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/displayFile/{fileName:.+}")
    public ResponseEntity<Void> displayFile(@PathVariable String filename, HttpServletRequest request,
                                            HttpServletResponse response){
        String decryptPath  = EncryptionUtil.decrypt(filename);
        Resource resource = fileStorageService.loadFileAsResource(decryptPath);
        String contentType = null;
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (IOException ex){
            ex.printStackTrace();
        }
        if(contentType == null){
            contentType = "application/octet-stream";
        }
        ServletUtil.sendFile(request, response, decryptPath, contentType);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
