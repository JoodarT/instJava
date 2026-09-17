package com.example.instajava.service.impl;

import com.example.instajava.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            if (!Files.exists(this.rootLocation)) {
                Files.createDirectories(this.rootLocation);
                log.info("Директория для загрузок создана: {}", this.rootLocation);
            }
        } catch (IOException e) {
            log.error("Не удалось создать директорию для загрузки файлов: {}", this.rootLocation, e);
            throw new RuntimeException("Не удалось инициализировать хранилище файлов", e);
        }
    }

    @Override
    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String newFilename = UUID.randomUUID() + extension;
        Path destinationFile = this.rootLocation.resolve(newFilename).normalize();

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            log.info("Файл успешно сохранен: {}", destinationFile);
            return "/uploads/" + newFilename;
        } catch (IOException e) {
            log.error("Ошибка при сохранении файла: {}", newFilename, e);
            throw new RuntimeException("Не удалось сохранить файл", e);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }

        String filename = Paths.get(filePath).getFileName().toString();
        Path file = this.rootLocation.resolve(filename).normalize();

        try {
            boolean deleted = Files.deleteIfExists(file);
            if (deleted) {
                log.info("Файл удален с диска: {}", file);
            } else {
                log.warn("Файл для удаления не найден: {}", file);
            }
        } catch (IOException e) {
            log.error("Ошибка при физическом удалении файла: {}", file, e);
        }
    }
}