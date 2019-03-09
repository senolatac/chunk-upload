package com.sha.fileuploader.service;

import com.sha.fileuploader.model.FileChunk;
import com.sha.fileuploader.model.FileMetadata;

public interface FileUploadService {
    void saveFile(FileMetadata fileMetadata);

    void updateFile(FileMetadata fileMetadata);

    FileMetadata findByHash(String hash);

    FileMetadata findByUniqueId(String uniqueId);

    void saveChunk(FileChunk fileChunk);

    FileChunk findLastChunk(String hash);
}
