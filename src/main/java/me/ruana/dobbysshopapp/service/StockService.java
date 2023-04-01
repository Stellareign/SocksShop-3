package me.ruana.dobbysshopapp.service;

import me.ruana.dobbysshopapp.model.ColoursOfSocks;
import me.ruana.dobbysshopapp.model.SizesOfSocks;
import me.ruana.dobbysshopapp.model.Socks;

import java.util.Map;

public interface StockService {
    // ДОБАВЛЕНИЕ НОСКОВ:
    Map<Socks, Integer> addSocksInStock(SizesOfSocks size, ColoursOfSocks colour, int cotton, int addSocksQuantity);

    //  ДОБАВЛЕНИЕ НОСКОВ json:
    Map<Socks, Integer> addSocksToStockJson(Socks socks, int quantity);

    // ПРОСМОТР СПИСКА ВСЕХ НОСКОВ:
    Map<Socks, Integer> getSocksMapInStock();


    // СОХРАНЕНИЕ В ФАЙЛ:
    void saveSocksToFile();

    // КОЛИЧЕСТВО НОСКОВ ПО ПАРАМЕТРАМ:
    int getSocksByParam(ColoursOfSocks colour, SizesOfSocks size, int cottonMin, int cottonMax);

    // ОТПУСК НОСКОВ
    Map<Socks, Integer> extractSocksFromStock(SizesOfSocks size, ColoursOfSocks colour, int cotton, int exractSocksQuantity);

}