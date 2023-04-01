package me.ruana.dobbysshopapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ruana.dobbysshopapp.model.ColoursOfSocks;
import me.ruana.dobbysshopapp.model.SizesOfSocks;
import me.ruana.dobbysshopapp.model.Socks;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockServiceImpl implements StockService {
    @Value("${path.to.file}")
    private String filePath;
    @Value("${name.of.file}")
    private String fileName;

    private static Map<Socks, Integer> socksMap = new HashMap<>();
    private static int addSocksQuantity = 0;
    private static int allSocksQuantity = 0;
    private FileService fileService;

    // ДОБАВЛЕНИЕ НОСКОВ:
    public Map<Socks, Integer> addSocksInStock(SizesOfSocks size, ColoursOfSocks colour, int cotton, int addSocksQuantity) {
//        String size1 = size.name();
//        String colour1 = colour.name();
//        String cotton1 = cotton.name();
        // for (Map.Entry<Socks, Integer> entry : socksMap.entrySet()) {
        //            if (entry.getKey().getColourOfSocks().name().equals(colour1) &&
//                    entry.getKey().getSizesOfSocks().name().equals(size1) &&
//                    entry.getKey().getCottonContent().name().equals(cotton1)) {
//        SizesOfSocks size2 = SizesOfSocks.valueOf(size1);
//        CottonContentInSocks cotton2 = CottonContentInSocks.valueOf(cotton1);
//        ColoursOfSocks colour2 = ColoursOfSocks.valueOf(colour1);
        Socks socks = new Socks(size, colour, cotton);
        if (socksMap.containsKey(socks)) {
            allSocksQuantity = socksMap.get(socks).intValue() + addSocksQuantity;
            socksMap.put(socks, allSocksQuantity);
            saveSocksToFile();
        } else socksMap.put(socks, addSocksQuantity);
        saveSocksToFile();
        return socksMap;
    }


    // ПРОСМОТР СПИСКА ВСЕХ НОСКОВ:
    @Override
    public Map<Socks, Integer> getSocksMapInStock() {
        if (socksMap.isEmpty()) {
            throw new ResolutionException("Склад пуст");
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
        ObjectUtils.isNotEmpty(socksMap);
        int count = 0;
        for (Map.Entry<Socks, Integer> entry : socksMap.entrySet()) {
            if (entry.getKey().getColourOfSocks() == colour &&
                    entry.getKey().getSizesOfSocks() == size &&
                    //entry.getKey().getCottonContent() == cotton)
                    entry.getKey().getCottonContent() < cottonMax &&
                    entry.getKey().getCottonContent() >= cottonMin) {
                count += entry.getValue();
            }
        }
        return count;
    }

    // ОТПУСК НОСКОВ
    @Override
    public Map<Socks, Integer> extractSocksFromStock(SizesOfSocks size, ColoursOfSocks colour, int cotton, int exractSocksQuantity) {
        Socks socks = new Socks(size, colour, cotton);
        if (socksMap.containsKey(socks)) {
            if (socksMap.get(socks).intValue() > exractSocksQuantity) {
                allSocksQuantity = socksMap.get(socks).intValue() - exractSocksQuantity;
                socksMap.put(socks, allSocksQuantity);
                saveSocksToFile();
            }
        } else
            throw new IllegalArgumentException("Указанных носков на складе недостаточно для списания: " + socksMap.get(socks).intValue() + "пар(ы)");
        return socksMap;
    }
}

