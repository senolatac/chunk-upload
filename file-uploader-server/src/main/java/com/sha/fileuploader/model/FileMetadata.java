package com.sha.fileuploader.model;

import com.sha.fileuploader.utils.EncryptionUtil;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "file_metadata")
public class FileMetadata implements IEncryption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "hash")
    private String hash;

    @Column(name = "name")
    private String name;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "size")
    private Long size;

    @Column(name = "upload_id")
    private String uploadId;

    @Column(name = "screencast_path")
    private String screencastPath;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "duration")
    private String duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_status")
    private FileStatus fileStatus;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getScreencastPath() {
        return screencastPath;
    }

    public void setScreencastPath(String screencastPath) {
        this.screencastPath = screencastPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetadata that = (FileMetadata) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(hash, that.hash) &&
                Objects.equals(name, that.name) &&
                Objects.equals(mimeType, that.mimeType) &&
                Objects.equals(size, that.size) &&
                Objects.equals(uploadId, that.uploadId) &&
                Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hash, name, mimeType, size, uploadId, createdTime);
    }

    @Override
    public String getEncryptPath() {
        if(this.filePath == null){
            return null;
        }
        return EncryptionUtil.encrypt(this.filePath);
    }

    @Override
    public String getEncryptScreen() {
        if(this.screencastPath == null){
            return null;
        }
        return EncryptionUtil.encrypt(this.screencastPath);
    }
}
