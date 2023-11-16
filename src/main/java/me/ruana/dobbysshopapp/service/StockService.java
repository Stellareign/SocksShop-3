package me.ruana.dobbysshopapp.service;

import me.ruana.dobbysshopapp.model.ColoursOfSocks;
import me.ruana.dobbysshopapp.model.SizesOfSocks;
import me.ruana.dobbysshopapp.model.Socks;

import java.io.IOException;
import java.util.Map;

public interface StockService {
    // ДОБАВЛЕНИЕ НОСКОВ:
    Map<Socks, Integer> addSocksInStock(SizesOfSocks size, ColoursOfSocks colour, int cotton, int addSocksQuantity) throws IOException;

    //  ДОБАВЛЕНИЕ НОСКОВ json:
    Map<Socks, Integer> addSocksToStockJson(Socks socks, int quantity);

    // ПРОСМОТР СПИСКА ВСЕХ НОСКОВ:
    Map<Socks, Integer> getSocksMapInStock() throws IOException;


    // СОХРАНЕНИЕ В ФАЙЛ:
   // void saveSocksToFile();

    // КОЛИЧЕСТВО НОСКОВ ПО ПАРАМЕТРАМ:
    int getSocksByParam(ColoursOfSocks colour, SizesOfSocks size, int cottonMin, int cottonMax) throws IOException;

    // ОТПУСК НОСКОВ
    Map<Socks, Integer> extractSocksFromStock(SizesOfSocks size, ColoursOfSocks colour, int cotton, int exractSocksQuantity) throws IOException;

    // СЧИТЫВАНИЕ МАПЫ ИЗ ФАЙЛА:
 //   void readRecipeFromFile();

  //  Map<String, Integer> readSocksMapFromFile(String filePath);

    // СЧИТЫВАНИЕ МАПЫ ИЗ ФАЙЛА:
 //   Map<Socks, Integer> readSocksMapFromFile();


    Map<Socks, Integer> readSocksMapFromFile() throws IOException;
}