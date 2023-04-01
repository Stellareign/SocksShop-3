package me.ruana.dobbysshopapp.service;

import me.ruana.dobbysshopapp.model.ColoursOfSocks;
import me.ruana.dobbysshopapp.model.SizesOfSocks;
import me.ruana.dobbysshopapp.model.Socks;

import java.util.Map;

public interface StockService {
    // ПРОСМОТР СПИСКА ВСЕХ НОСКОВ:
    Map<Socks, Integer> getSocksMapInStock();


    // СОХРАНЕНИЕ В ФАЙЛ:
    void saveSocksToFile();

    // КОЛИЧЕСТВО НОСКОВ ПО ПАРАМЕТРАМ:
    int getSocksByParam(ColoursOfSocks colour, SizesOfSocks size, int cottonMin, int cottonMax);

    // ОТПУСК НОСКОВ
    Map<Socks, Integer> extractSocksFromStock(SizesOfSocks size, ColoursOfSocks colour, int cotton, int exractSocksQuantity);

}