package me.ruana.dobbysshopapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.ruana.dobbysshopapp.dto.SocksDto;
import me.ruana.dobbysshopapp.model.Socks;
import me.ruana.dobbysshopapp.operations.SocksOperation;
import me.ruana.dobbysshopapp.service.FileService;
import me.ruana.dobbysshopapp.service.OperationsHistory;
import me.ruana.dobbysshopapp.service.StockService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/files")

public class FilesController {
    @Value("${path.to.file}")
    private String filePath;
    @Value("${name.of.file2}")
    private String operationsInStockFile;
    @Value("${name.of.file1}")
    private String fileNameSocks;

    FileService fileService;
    StockService stockService;
    OperationsHistory operationsHistory;

    public FilesController(FileService fileService, StockService stockService, OperationsHistory operationsHistory) {
        this.fileService = fileService;
        this.stockService = stockService;
        this.operationsHistory = operationsHistory;
    }


    //ЗАГРУЗКА ФАЙЛА:
    @GetMapping("/download/socks")
    @Operation(summary = "Загрузка файла с перечнем носков на складе с сервера",
            description = "Загружает файл с базой носков на складе с сервера на диск пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Файл найден и готов к загрузке. Нажмите [Download file]",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Socks.class)))}) // схема содержания загруженного файла
    })
    public ResponseEntity<InputStreamResource> downloadSocksFile() throws IOException {
        File socksFile = fileService.checkExistsSocksFile(); // проверяем, на месте ли файл

        InputStreamResource resource = new InputStreamResource((new FileInputStream(socksFile)));// формируем входной поток из файла

        return ResponseEntity.ok() //
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // заголовок скачивает по ссылке/
                .contentLength(socksFile.length())// заголовок для проверки соответствия размера скачанного файла размеру файла на сервере
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SocksShopFile.json\"") // заголовок для загрузки
                // файла с нужным именем
                .body(resource); // возвращаем файл в теле объекта resource
    }


    // ЗАГРУЗКА ФАЙЛА С НОСКАМИ НА СЕРВЕР:
    @PostMapping(value = "import/recipes", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузка файла с базой носков на сервер (с использованием библиотеки Apache Commons IO)",
            description = "Загружает файл с перечнем носков на сервер с диска пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Файл найден и готов к выгрузке. Нажмите [Download file]",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SocksDto.class)))}) // схема содержания загруженного файла
    })
    public ResponseEntity<Void> uploadRecipeDataFile(@RequestParam MultipartFile uploadedRecipesFile) throws IOException {
        if (fileService.uploadSocksFile(uploadedRecipesFile)) {
            stockService.readSocksMapFromFile();
            return ResponseEntity.ok()
                    .build(); // если файл скопирован успешно, сообщаем клиенту, что всё ОК}
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // или сообщаем об ошибке, если что-то пошло не так
                .build();
    }

    //ПРОСМОТР ФАЙЛА С ОПЕРАЦИЯМИ СКЛАДА:
    @GetMapping("/operations")

    @Operation(summary = "ПРОСМОТР ФАЙЛА С ОПЕРАЦИЯМИ СКЛАДА",
            description = "Выводит перечень всех операций с носками")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Список складских операций с носками",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SocksOperation.class)))})
    })

    public ResponseEntity<Object> getOperationsList() throws IOException {
        var operationsList = operationsHistory.readSocksOperationFromFile();
        return ResponseEntity.ok().body(operationsList);
    }


    //ЗАГРУЗКА ФАЙЛА С ОПЕРАЦИЯМИ:
    @GetMapping("/download/operations")
    @Operation(summary = "ЗАГРУЗКА СПИСКА СКЛАДСКИХ ОПЕРАЦИЙ",
            description = "Загружает файл со складскими операциями с сервера на диск пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Файл найден и готов к загрузке. Нажмите [Download file]",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SocksOperation.class)))}) // схема содержания загруженного файла
    })
    public ResponseEntity<InputStreamResource> downloadOperationsFile() throws IOException {
        File operationsFile = fileService.checkExistsFile(); // проверяем, на месте ли файл
        InputStreamResource resource = new InputStreamResource((new FileInputStream(operationsFile)));// формируем входной поток из файла
        return ResponseEntity.ok() //
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // заголовок скачивает по ссылке/
                .contentLength(operationsFile.length())// заголовок для проверки соответствия размера скачанного файла размеру файла на сервере
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"stockOperationsFile.json\"") // заголовок для загрузки
                // файла с нужным именем
                .body(resource); // возвращаем файл в теле объекта resource
    }

}


