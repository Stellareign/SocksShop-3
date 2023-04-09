package me.ruana.dobbysshopapp.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import me.ruana.dobbysshopapp.model.ColoursOfSocks;
import me.ruana.dobbysshopapp.model.SizesOfSocks;
import me.ruana.dobbysshopapp.model.Socks;
import me.ruana.dobbysshopapp.operations.SocksOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class OperationsHistoryImpl implements OperationsHistory {
    @Value("${path.to.file}")
    private String filePath;
    @Value("${name.of.file2}")
    private String operationsInStockFile;
    private static Map<Integer, SocksOperation> socksOperationMap = new HashMap<>();
    private FileService fileService;

    public OperationsHistoryImpl(FileService fileService) {
        this.fileService = fileService;
    }


    // ДОБАВЛЕНИЕ ОПЕРАЦИИ В СПИСОК И СОХРАНЕНИЕ В ФАЙЛ
    @Override
    public Map<Integer, SocksOperation> addOperation(SocksOperation socksOperation) throws IOException {
        Map<Integer, SocksOperation> socksOperationMap1 = readSocksOperationFromFile();
        int operationId = 0;
        if (socksOperationMap1 != null && !socksOperationMap1.isEmpty()) {
            operationId = Collections.max(socksOperationMap1.keySet()) + 1;
            socksOperationMap1.put(operationId, socksOperation);
            saveSocksOperationMapToFile(socksOperationMap1);
            return socksOperationMap1;
        } else {
            socksOperationMap.put(operationId, socksOperation);
            saveSocksOperationMapToFile(socksOperationMap);
            return socksOperationMap;
        }
    }

    // УДАЛЕНИЕ ОПЕРАЦИИ ИЗ ФАЙЛА ПО ID:
    @Override
    public void removeOperation(int socksOperationId) throws IOException {
        Map<Integer, SocksOperation> socksOperationMap1 = readSocksOperationFromFile();
        SocksOperation socksOperation = socksOperationMap1.get(socksOperationId);
        if (socksOperation != null) {
            socksOperationMap1.remove(socksOperationId);
            saveSocksOperationMapToFile(socksOperationMap1);
        }
    }

    // ПРОСМОТР СПИСКА ОПЕРАЦИЙ:
    @Override
    public Map<Integer, SocksOperation> getOperationsList() throws IOException {
        Map<Integer, SocksOperation> socksOperationMap1 = readSocksOperationFromFile();
        return socksOperationMap1;
    }


    // СОХРАНЕНИЕ ОПЕРАЦИИ В ФАЙЛ:
    @Override
    public void saveSocksOperationMapToFile(Map<Integer, SocksOperation> operationMap1) {
        String filePath1 = filePath + "/" + operationsInStockFile;
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        var module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException { // обработка local date
                gen.writeString(value.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")));
            }
        });
        mapper.registerModule(module);
        File file = new File(filePath1);
        try {
            mapper.writeValue(file, operationMap1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ЧТЕНИЕ МАПЫ С ОПЕРАЦИЯМИ ИЗ ФАЙЛА:
    @Override
    public Map<Integer, SocksOperation> readSocksOperationFromFile() {
        File file = new File(filePath + "/" + operationsInStockFile);
        if (file.length() == 0) {
            saveSocksOperationMapToFile(socksOperationMap);
            return socksOperationMap;
        } else {
            try {
                String json = fileService.readOperationsLMapFromFile();
                ObjectMapper mapper = JsonMapper.builder() // добавили модуль для чтения дата(после добавления зависимости)
                        .addModule(new JavaTimeModule())
                        .build();
                SimpleModule module = new SimpleModule();
                module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonParser p, DeserializationContext cntxt) throws IOException, JsonProcessingException {
                        return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
                    }
                });
                module.addDeserializer(Socks.class, new JsonDeserializer<Socks>() { // десериализация класса носков
                    @Override
                    public Socks deserialize(JsonParser p, DeserializationContext cntxt) throws IOException {
                        ObjectCodec codec = p.getCodec();
                        JsonNode node = codec.readTree(p);
                        SizesOfSocks size = SizesOfSocks.valueOf(node.get("Размер").asText());
                        ColoursOfSocks color = ColoursOfSocks.valueOf(node.get("Цвет").asText());
                        int cottonPart = node.get("Содержание хлопка").asInt();
                        return new Socks(size, color, cottonPart);
                    }
                });
                mapper.registerModule(module);

                Map<Integer, SocksOperation> operationMap1 = mapper.readValue(json, new TypeReference<Map<Integer, SocksOperation>>() {
                });
                return operationMap1;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}


