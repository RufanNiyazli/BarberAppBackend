package com.project.barberreservation.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileStorageUtil {

    @Value("${upload.dir.profile}")
    private String profileDir;

    @Value("${upload.dir.gallery}")
    private String galleryDir;

    private final long MAX_FILE_SIZE = 250L * 1024 * 1024;

    public String saveProfilePhoto(MultipartFile file) throws IOException {
        validateFileSize(file);
        return saveFile(file, profileDir);
    }

    public String saveGalleryPhotos(MultipartFile file) throws IOException {
        validateFileSize(file);
        return saveFile(file, galleryDir);
    }

    private void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds 250MB limit :" + file.getOriginalFilename());
        }
    }

    private String saveFile(MultipartFile file, String dir) throws IOException {
        String fileExtension = getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID() + "." + fileExtension;
        Path filePath = Paths.get(dir + newFileName);
        Files.createDirectories(filePath.getParent());
        file.transferTo(filePath.toFile());
        return filePath.toString();
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
