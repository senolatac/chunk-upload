package com.sha.fileuploader.model;

import lombok.Data;

@Data
public class ContentRange {
    int currentChunk;
    int totalChunk;
    long startByte;
    long endByte;
    long size;

}
