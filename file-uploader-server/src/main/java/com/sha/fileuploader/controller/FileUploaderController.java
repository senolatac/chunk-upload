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


}
