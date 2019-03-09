package com.sha.fileuploader.service;

import com.sha.fileuploader.model.FileChunk;
import com.sha.fileuploader.model.FileMetadata;
import com.sha.fileuploader.repository.FileChunkRepository;
import com.sha.fileuploader.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements  FileUploadService{

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    private FileChunkRepository fileChunkRepository;

    @Override
    public void saveFile(FileMetadata fileMetadata){
        this.fileMetadataRepository.save(fileMetadata);
    }

    @Override
    public void updateFile(FileMetadata fileMetadata){
        this.fileMetadataRepository.update(fileMetadata);
    }

    @Override
    public FileMetadata findByHash(String hash){
        return this.fileMetadataRepository.findByHash(hash);
    }

    @Override
    public FileMetadata findByUniqueId(String uniqueId){
        return this.fileMetadataRepository.findByUniqueId(uniqueId);
    }

    @Override
    public void saveChunk(FileChunk fileChunk){
        this.fileChunkRepository.save(fileChunk);
    }

    @Override
    public FileChunk findLastChunk(String hash){
        return this.fileChunkRepository.findLastChunk(hash);
    }
}
