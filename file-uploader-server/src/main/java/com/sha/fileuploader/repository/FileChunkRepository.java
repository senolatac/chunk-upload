package com.sha.fileuploader.repository;

import com.sha.fileuploader.model.FileChunk;

public interface FileChunkRepository extends IGenericDao<FileChunk> {
    FileChunk findLastChunk(String hash);
}
