package com.sha.fileuploader.repository;

import com.sha.fileuploader.model.FileMetadata;

public interface FileMetadataRepository extends IGenericDao<FileMetadata> {
    FileMetadata findByHash(String hash);

    FileMetadata findByUniqueId(String uniqueId);
}
