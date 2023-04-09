package me.ruana.dobbysshopapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ruana.dobbysshopapp.exceptions.InvalidRequestException;
import me.ruana.dobbysshopapp.exceptions.NotFoundException;
import me.ruana.dobbysshopapp.model.ColoursOfSocks;
import me.ruana.dobbysshopapp.model.SizesOfSocks;
import me.ruana.dobbysshopapp.model.Socks;
import me.ruana.dobbysshopapp.operations.OperationsType;
import me.ruana.dobbysshopapp.operations.SocksOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockServiceImpl implements StockService {
    @Value("${path.to.file}")
    private String filePath;
    @Value("${name.of.file1}")
    private String fileNameSocks;

    private static Map<Socks, Integer> socksMap = new HashMap<>();

    //СЧИТЫВАНИЕ ФАЙЛА С ДИСКА:
    @PostConstruct
    private void foo() throws IOException {
        File file = new File(filePath, fileNameSocks);
        if (file.exists())
            readSocksMapFromFile();
    }

    private FileService fileService;
    private OperationsHistory operationsHistory;

    public StockServiceImpl(FileService fileService, OperationsHistory operationsHistory) {
        this.fileService = fileService;
        this.operationsHistory = operationsHistory;
    }

    // ДОБАВЛЕНИЕ НОСКОВ:
    @Override
    public Map<Socks, Integer> addSocksInStock(SizesOfSocks size, ColoursOfSocks colour, int cotton, int addSocksQuantity) throws IOException {
        File file = new File(filePath + "/" + fileNameSocks);
        Socks socks = new Socks(size, colour, cotton);
        SocksOperation socksOperation = new SocksOperation(OperationsType.PUT_SOCKS, LocalDateTime.now(), addSocksQuantity, socks);
        if (file.exists() && file.length() == 0) {
            socksMap.put(socks, addSocksQuantity);
            saveSocksMapToFile(socksMap);
        } else {
            Map<Socks, Integer> socksMap1 = readSocksMapFromFile();
            int allSocksQuantity = 0;
            checkRequest(socks);
            if (socksMap1.containsKey(socks) && addSocksQuantity > 0) {
                allSocksQuantity = socksMap1.get(socks) + addSocksQuantity;
                socksMap1.put(socks, allSocksQuantity);
                saveSocksMapToFile(socksMap1);
            } else if (addSocksQuantity <= 0) {
                throw new InvalidRequestException("Количество добавляемых носков не должно быть меньше или равно 0");
            } else socksMap1.put(socks, addSocksQuantity);
            saveSocksMapToFile(socksMap1);
        }
        return readSocksMapFromFile();
    }

    //  ДОБАВЛЕНИЕ НОСКОВ через json:
    @Override
    public Map<Socks, Integer> addSocksToStockJson(Socks socks, int quantity) {
        int allSocksQuantity = 0;
        if (quantity <= 0) {
            throw new InvalidRequestException("Количество добавляемых носков не должно быть меньше или равно 0");
        }
        checkRequest(socks);
        if (socksMap.containsKey(socks)) {
            allSocksQuantity = socksMap.get(socks) + quantity;
            socksMap.put(socks, allSocksQuantity);
            saveSocksMapToFile(socksMap);
        } else socksMap.put(socks, quantity);
        saveSocksMapToFile(socksMap);
        return socksMap;
    }

    // ПРОСМОТР СПИСКА ВСЕХ НОСКОВ:
    @Override
    public Map<Socks, Integer> getSocksMapInStock() throws IOException {
        Map<Socks, Integer> socksMap1 = readSocksMapFromFile();
        //  socksMap = socksMap1;
        if (socksMap1.isEmpty() || socksMap1 == null) {
            throw new NotFoundException("Склад пуст");
        } else
            return socksMap1;
    }


    public void saveSocksMapToFile(Map<Socks, Integer> socksMap1) {
        String filePath1 = filePath + "/" + fileNameSocks;
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath1);
        try {
            mapper.writeValue(file, socksMap1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // КОЛИЧЕСТВО НОСКОВ ПО ПАРАМЕТРАМ:
    @Override
    public int getSocksByParam(ColoursOfSocks colour, SizesOfSocks size, int cottonMin, int cottonMax) throws IOException {
        Map<Socks, Integer> socksMap1 = readSocksMapFromFile();
        if (cottonMax < cottonMin) {
            throw new InvalidRequestException("Min количество хлопка не может быть больше max.");
        }
        ObjectUtils.isNotEmpty(socksMap1);
        if (socksMap1.isEmpty()) {
            throw new NotFoundException("Склад пустой.");
        }
        int count = 0;
        for (Map.Entry<Socks, Integer> entry : socksMap1.entrySet()) {
            if (entry.getKey().getColourOfSocks() == colour &&
                    entry.getKey().getSizesOfSocks() == size &&
                    entry.getKey().getCottonContent() < cottonMax &&
                    entry.getKey().getCottonContent() >= cottonMin) {
                count += entry.getValue();
                checkRequest(entry.getKey());
            }
        }
        return count;
    }

    // ОТПУСК носков со склада и СПИСАНИЕ бракованных НОСКОВ:
    @Override
    public Map<Socks, Integer> extractSocksFromStock(SizesOfSocks size, ColoursOfSocks colour, int cotton, int exractSocksQuantity) throws IOException {
        int allSocksQuantity = 0;
        Map<Socks, Integer> socksMap1 = readSocksMapFromFile();
        Socks socks = new Socks(size, colour, cotton);
        if (socksMap1.isEmpty() || !socksMap1.containsKey(socks)) {
            throw new NotFoundException("Склад пуст или указанных носков нет на складе.");
        }
        checkRequest(socks);
        if (socksMap1 != null && socksMap1.containsKey(socks)) {
            if (socksMap1.get(socks) > exractSocksQuantity) {
                allSocksQuantity = socksMap1.get(socks) - exractSocksQuantity;
                socksMap1.put(socks, allSocksQuantity);
                saveSocksMapToFile(socksMap1);
            } else if (exractSocksQuantity < 0) {
                throw new InvalidRequestException("Количество носков для списания не может быть меньше нуля.");
            } else if (socksMap1.get(socks) < exractSocksQuantity) {
                throw new NotFoundException("Указанных носков на складе недостаточно для списания.");
            }
        }
        return socksMap1;
    }


    // ПРОВЕРКА ЗАПРОСА ДЛЯ ОБРАБОТКИ ИСКЛЮЧЕНИЯ:
    private void checkRequest(Socks socks) {
        if (socks.getColourOfSocks() == null || socks.getSizesOfSocks() == null) {
            throw new InvalidRequestException("Заполните все необходимые поля");
        }
        if (socks.getCottonContent() < 0 || socks.getCottonContent() > 100) {
            throw new InvalidRequestException("Содержание хлопка не может быть больше 100 % или меньше 0 %.");
        }
    }

    // СЧИТЫВАНИЕ МАПЫ ИЗ ФАЙЛА:
    @Override
    public Map<Socks, Integer> readSocksMapFromFile() throws IOException {
        File file = new File(filePath + "/" + fileNameSocks);
        if (!file.exists()) {
            file.createNewFile();
        }
        if (file.length() == 0) {
            saveSocksMapToFile(socksMap);
        }
        String jsonFilePath = filePath + "/" + fileNameSocks;
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Integer>> typeRef = new TypeReference<Map<String, Integer>>() {
        };

        Map<String, Integer> stringMap = objectMapper.readValue(new File(jsonFilePath), typeRef);
        Map<Socks, Integer> socksMap1 = new HashMap<>();
        for (String key : stringMap.keySet()) {
            String[] parts = key.split(", ");
            String size = parts[0].substring(parts[0].indexOf(": ") + 2);
            String color = parts[1].substring(parts[1].indexOf(": ") + 2);
            int cottonPart = 0;
            if (parts[2].contains(": ")) {
                String[] cottonPartParts = parts[2].split(": ");
                if (cottonPartParts.length > 1) {
                    cottonPart = Integer.parseInt(cottonPartParts[1]);
                }
            }

            int quantity = stringMap.get(key);
            Socks socks = new Socks(SizesOfSocks.valueOf(size), ColoursOfSocks.valueOf(color), cottonPart);
            socksMap1.put(socks, quantity);
        }
        return socksMap1;
    }
}

