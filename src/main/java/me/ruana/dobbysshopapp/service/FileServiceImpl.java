package me.ruana.dobbysshopapp.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
@Service
public class FileServiceImpl implements FileService {
   @Value("${path.to.file}")
    private String filePath;
    @Value("${name.of.file}")
    private String fileNameSocks;

    @Override
    public boolean saveSocksToFile(String json) {
        try {
            Files.writeString(Path.of(filePath, fileNameSocks), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
