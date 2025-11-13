package com.ateam.calmate.event.command.port;

import java.io.InputStream;

public interface FileStoragePort {
    record SavedFile(String relativePath, String storedFileName) {}
    SavedFile save(String directory, String originalFilename, String contentType, InputStream in, long size);
}