package me.ruana.dobbysshopapp.service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl extends FileService {
    @Value("${path.to.file}")
    private String filePath;
    @Value("${name.of.file1}")
    private String fileNameSocks;
    @Value("${name.of.file2}")
    private String operationsInStockFile;
//    @PostConstruct
//    private void foo() throws IOException {
//        checkExistsFile(filePath + "/" + operationsInStockFile);
//        checkExistsFile(filePath + "/" + fileNameSocks);
//    }

    // ПРОВЕРКА НАЛИЧИЯ ФАЙЛА ПЕРЕД ЗАГРУЗКОЙ НА СЕРВЕР:
    @Override
    public File checkExistsSocksFile() throws IOException {
        File socksFile = getDataSocksFile();
        if (socksFile.exists()) { // проверяем, существует ли файл
            return socksFile;
        } else {socksFile.createNewFile();
        }
        return socksFile;
    }


    // ПОЛУЧЕНИЕ ФАЙЛА С НОСКАМИ С СЕРВЕРА
    @Override
    public File getDataSocksFile() {
        return new File(filePath + "/" + fileNameSocks); //возвращает файл с указанным именем по указанному адресу.
    }

    // ПОЛУЧЕНИЕ ФАЙЛА С ОПЕРАЦИЯМИ ПО НОСКАМ С СЕРВЕРА
    @Override
    public File getDataSocksOperationsFile() {
        return new File(filePath + "/" + operationsInStockFile); //возвращает файл с указанным именем по указанному адресу.
    }


    // ЗАМЕНА ФАЙЛА НА СЕРВЕРЕ
    @Override
    public boolean uploadSocksFile(MultipartFile uploadSocksFile) {
        cleanSocksFile(); // очищаем файл на сервере для перезаписи
        File socksFile = getDataSocksFile(); // получаем объект File из методом getDataFileRecipes() из fileService
        try (FileOutputStream fos = new FileOutputStream(socksFile)) { // Открываем поток для записи в файл с помощью FileOutputStream
            IOUtils.copy(uploadSocksFile.getInputStream(), fos); // Копируем содержимое загруженного файла в
            // файл на сервере с помощью метода copy() из библиотеки Apache Commons IO.
            uploadSocksFile.getResource();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //ОЧИСТКА ФАЙЛА НА СЕРВЕРЕ ПЕРЕД ЗАМЕНОЙ:
    @Override
    public boolean cleanSocksFile() {
        try {
            Path path = Path.of(filePath, fileNameSocks);
            Files.deleteIfExists(path); // удалить, если существует
            Files.createFile(path); // создание пустого файла
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ЧТЕНИЕ ФАЙЛА С ОПЕРАЦИЯМИ:
    @Override
    public String readOperationsLMapFromFile() throws IOException {
        File file = new File(filePath + "/" + operationsInStockFile);
        checkExistsFile();
        try {
//            if (file.length() != 0) {
            return Files.readString(Path.of(filePath, operationsInStockFile));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public File checkExistsFile() throws IOException {
        File operationsFile = getDataSocksOperationsFile();
        if (operationsFile.exists()) { // проверяем, существует ли файл
            return operationsFile;
        }else {
            operationsFile.createNewFile();
        } return operationsFile;
    }


}





