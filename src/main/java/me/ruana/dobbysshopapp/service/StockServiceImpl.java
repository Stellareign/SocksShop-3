package me.ruana.dobbysshopapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ruana.dobbysshopapp.exceptions.InvalidRequestException;
import me.ruana.dobbysshopapp.exceptions.NotFoundException;
import me.ruana.dobbysshopapp.model.ColoursOfSocks;
import me.ruana.dobbysshopapp.model.SizesOfSocks;
import me.ruana.dobbysshopapp.model.Socks;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StockServiceImpl implements StockService {
    @Value("${path.to.file}")
    private String filePath;
    @Value("${name.of.file}")
    private String fileName;

    private static Map<Socks, Integer> socksMap = new HashMap<>();
    private FileService fileService;

    // ДОБАВЛЕНИЕ НОСКОВ:
    @Override
    public Map<Socks, Integer> addSocksInStock(SizesOfSocks size, ColoursOfSocks colour, int cotton, int addSocksQuantity) {
        Socks socks = new Socks(size, colour, cotton);
        int allSocksQuantity = 0;
        checkRequest(socks);
        if (socksMap.containsKey(socks) && addSocksQuantity > 0) {
            allSocksQuantity = socksMap.get(socks) + addSocksQuantity;
            socksMap.put(socks, allSocksQuantity);
        } else if (addSocksQuantity <= 0) {
            throw new InvalidRequestException("Количество добавляемых носков не должно быть меньше или равно 0");
        } else socksMap.put(socks, addSocksQuantity);
        return socksMap;
    }

    //  ДОБАВЛЕНИЕ НОСКОВ json:
    @Override
    public Map<Socks, Integer> addSocksToStockJson(Socks socks, int quantity) {
        int allSocksQuantity = 0;
        if (quantity <= 0) {
            throw new InvalidRequestException("Количество добавляемых носков не должно быть меньше или равно 0");}
        checkRequest(socks);
        if (socksMap.containsKey(socks)) {
            allSocksQuantity = socksMap.get(socks) + quantity;
            socksMap.put(socks, allSocksQuantity);
        } else {
            socksMap.put(socks, quantity);
        } return socksMap;
    }


    // ПРОСМОТР СПИСКА ВСЕХ НОСКОВ:
    @Override
    public Map<Socks, Integer> getSocksMapInStock() {
        if (socksMap.isEmpty()) {
            throw new InvalidRequestException("Склад пуст");
        } else return socksMap;
    }

    // СОХРАНЕНИЕ В ФАЙЛ:
    @Override
    public void saveSocksToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socksMap);
            fileService.saveSocksToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // КОЛИЧЕСТВО НОСКОВ ПО ПАРАМЕТРАМ:
    @Override
    public int getSocksByParam(ColoursOfSocks colour, SizesOfSocks size, int cottonMin, int cottonMax) {
        if (cottonMax < cottonMin) {
            throw new InvalidRequestException("Min количество хлопка не может быть больше max.");
        }
        ObjectUtils.isNotEmpty(socksMap);
        if (socksMap.isEmpty()) {
            throw new NotFoundException("Склад пустой.");
        }
        int count = 0;
        for (Map.Entry<Socks, Integer> entry : socksMap.entrySet()) {
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

    // ОТПУСК и СПИСАНИЕ бракованных НОСКОВ:
    @Override
    public Map<Socks, Integer> extractSocksFromStock(SizesOfSocks size, ColoursOfSocks colour, int cotton, int exractSocksQuantity) {
        Socks socks = new Socks(size, colour, cotton);
        int allSocksQuantity = 0;
        if (socksMap.isEmpty() || !socksMap.containsKey(socks)) {
            throw new NotFoundException("Склад пуст или указанных носков нет на складе.");
        }
        checkRequest(socks);
        if (socksMap != null && socksMap.containsKey(socks)) {
            if (socksMap.get(socks) > exractSocksQuantity) {
                allSocksQuantity = socksMap.get(socks) - exractSocksQuantity;
                socksMap.put(socks, allSocksQuantity);

            } else if (exractSocksQuantity < 0) {
                throw new InvalidRequestException("Количество носков для списания не может быть меньше нуля.");
            } else if (socksMap.get(socks) < exractSocksQuantity) {
                throw new NotFoundException("Указанных носков на складе недостаточно для списания.");
            }
        }
        return socksMap;
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
}
