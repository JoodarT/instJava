package com.example.instajava.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * Сохранение загруженного файла изображения на диск.
     * Генерирует уникальное имя файла (например, через UUID), чтобы избежать перезаписи файлов с одинаковыми именами.
     *
     * @param file файл из формы загрузки (MultipartFile).
     * @return относительный веб-путь или сгенерированное имя сохраненного файла для записи в БД.
     * @throws IllegalArgumentException если файл пустой или не является допустимым изображением.
     */
    String saveFile(MultipartFile file);

    /**
     * Физическое удаление файла изображения с диска.
     * Вызывается при удалении публикации.
     *
     * @param filePath путь к файлу или имя файла, который нужно удалить.
     */
    void deleteFile(String filePath);
}