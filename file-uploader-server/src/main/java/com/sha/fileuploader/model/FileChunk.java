package com.sha.fileuploader.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "file_chunk")
public class FileChunk implements IModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metadata_id")
    private FileMetadata metadata;

    @Column(name = "start_byte")
    private Long startByte;

    @Column(name = "end_byte")
    private Long endByte;

    @Column(name = "chunk_no")
    private int chunkNo;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(FileMetadata metadata) {
        this.metadata = metadata;
    }

    public Long getStartByte() {
        return startByte;
    }

    public void setStartByte(Long startByte) {
        this.startByte = startByte;
    }

    public Long getEndByte() {
        return endByte;
    }

    public void setEndByte(Long endByte) {
        this.endByte = endByte;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public void setChunkNo(int chunkNo) {
        this.chunkNo = chunkNo;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileChunk fileChunk = (FileChunk) o;
        return chunkNo == fileChunk.chunkNo &&
                Objects.equals(id, fileChunk.id) &&
                Objects.equals(startByte, fileChunk.startByte) &&
                Objects.equals(endByte, fileChunk.endByte) &&
                Objects.equals(createdTime, fileChunk.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startByte, endByte, chunkNo, createdTime);
    }
}
