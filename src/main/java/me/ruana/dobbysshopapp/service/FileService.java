package me.ruana.dobbysshopapp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class FileService {


    // ПРОВЕРКА НАЛИЧИЯ ФАЙЛА ПЕРЕД ЗАГРУЗКОЙ НА СЕРВЕР:
    public abstract File checkExistsSocksFile() throws IOException;

    public abstract File getDataSocksFile();

    // ПОЛУЧЕНИЕ ФАЙЛА С ОПЕРАЦИЯМИ ПО НОСКАМ С СЕРВЕРА
    public abstract File getDataSocksOperationsFile();

    // ЗАМЕНА ФАЙЛА НА СЕРВЕРЕ
    public abstract boolean uploadSocksFile(MultipartFile uploadSocksFile);

    //ОЧИСТКА ФАЙЛА НА СЕРВЕРЕ ПЕРЕД ЗАМЕНОЙ:
    public abstract boolean cleanSocksFile();

    // ЧТЕНИЕ ФАЙЛА С ОПЕРАЦИЯМИ:
    public abstract String readOperationsLMapFromFile() throws IOException;


    public abstract File checkExistsFile() throws IOException;
}
