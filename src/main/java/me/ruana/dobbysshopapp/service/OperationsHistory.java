package me.ruana.dobbysshopapp.service;

import me.ruana.dobbysshopapp.operations.SocksOperation;

import java.io.IOException;
import java.util.Map;

public interface OperationsHistory {
    Map<Integer, SocksOperation> addOperation(SocksOperation socksOperation) throws IOException;


    void removeOperation(int id) throws IOException;

    Map<Integer, SocksOperation> getOperationsList() throws IOException;

    // СОХРАНЕНИЕ ОПЕРАЦИИ В ФАЙЛ:
    void saveSocksOperationMapToFile(Map<Integer, SocksOperation> operationMap1) throws IOException;

    // ЧТЕНИЕ МАПЫ С ОПЕРАЦТЯМИ ИЗ ФАЙЛА:
    Map<Integer, SocksOperation> readSocksOperationFromFile() throws IOException;
}