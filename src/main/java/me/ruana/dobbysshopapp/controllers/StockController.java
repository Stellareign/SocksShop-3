package me.ruana.dobbysshopapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.ruana.dobbysshopapp.exceptions.InvalidRequestException;
import me.ruana.dobbysshopapp.exceptions.InvalidResponseStatusException;
import me.ruana.dobbysshopapp.exceptions.NotFoundException;
import me.ruana.dobbysshopapp.model.ColoursOfSocks;
import me.ruana.dobbysshopapp.model.SizesOfSocks;
import me.ruana.dobbysshopapp.model.Socks;
import me.ruana.dobbysshopapp.service.StockServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {
    private final StockServiceImpl stockService;

    public StockController(StockServiceImpl stockService) {
        this.stockService = stockService;
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidException(InvalidRequestException invalidRequestException) {
        return ResponseEntity.badRequest().body(invalidRequestException.getMessage());
    }

    @ExceptionHandler(InvalidResponseStatusException.class)
    public ResponseEntity<String> handleInvalidException(InvalidResponseStatusException invalidResponseStatusException) {
        return ResponseEntity.badRequest().body(invalidResponseStatusException.getMessage());
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleInvalidException(NotFoundException notFoundException) {
        return ResponseEntity.badRequest().body(notFoundException.getMessage());
    }

    // ПОЛУЧЕНИЕ СПИСКА НОСКОВ:
    @GetMapping//
    @Operation(summary = "ПРОСМОТР ВСЕХ НОСКОВ НА СКЛАДЕ",
            description = "Выводит перечень всех носков, имеющихся на складе")
    @ApiResponses(value = {                                                     // нужно понимание!
            @ApiResponse(responseCode = "200",
                    description = "Носки в наличии",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Socks.class)))})
    })
    public ResponseEntity<Object> getSocksList() {
        var socksInStock = stockService.getSocksMapInStock();
        return ResponseEntity.ok().body(socksInStock);
    }

    // ДОБАВЛЕНИЕ НОСКОВ:
    @PostMapping //
    @Operation(summary = "ПРИХОД НОСКОВ НА СКЛАД",
            description = "Выбрать соответствующие добавляемым носкам параметры: размер, цвет, содержание хлопка и количество пар")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Добавить носки на склад",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Socks.class)))})
    })
    public ResponseEntity<?> addSocks(@RequestParam SizesOfSocks size, ColoursOfSocks colour, int cotton, Integer quantity) {
        var socks1 = stockService.addSocksInStock(size, colour, cotton, quantity);
        return ResponseEntity.ok(socks1);
    }

    // ДОБАВЛЕНИЕ НОСКОВ ЧЕРЕЗ JSON:
    @PostMapping
    @Operation(summary = "ПРИХОД НОСКОВ НА СКЛАД - через JSON",
            description = "Выбрать соответствующие добавляемым носкам параметры: размер, цвет, содержание хлопка и количество пар")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Добавить носки на склад",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Socks.class)))})
    })
    public ResponseEntity<?> addSocksJson(@RequestBody Socks socks,int quantity) {
        var socks1 = stockService.addSocksToStockJson(socks, quantity);
        return ResponseEntity.ok(socks1);
    }

    // СПИСОК НОСКОВ ПО ВСЕМ ПАРАМЕТРАМ:
    @GetMapping("/socks")
    @Operation(summary = "ЗАПРОС КОЛИЧЕСТВА НОСКОВ С УКАЗАННЫМИ ПАРАМЕТРАМИ",
            description = "Указать размер, цвет, min и max содержание хлопка")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Количество указанных носков",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Socks.class)))})
    })
    public ResponseEntity<Object> getSocksByParameter(SizesOfSocks size, ColoursOfSocks colour, int cottonMin, int cottonMax) {
        var count = stockService.getSocksByParam(colour, size, cottonMin, cottonMax);
        if (count > 0) {
            return ResponseEntity.ok().body(count);
        } else {
            return new ResponseEntity<>("Указанные носки отсутствуют на складе", HttpStatus.NOT_FOUND);
        }
    }

    //ОТПУСК НОСКОВ СО СКЛАДА:
    @PutMapping
    @Operation(summary = "СПИСАНИЕ ПРОДАННЫХ НОСКОВ СО СКЛАДА",
            description = "Укажите параметры носков для списания: размер, цвет, содержание хлопка и количество пар.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Списаны указанные носки в заданном количестве",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Socks.class)))}),
            @ApiResponse(
                    responseCode = "400", description = "Ошибка в параметрах запроса"
            ),
            @ApiResponse(
                    responseCode = "404", description = "Неверный URL или команда"
            ),
            @ApiResponse(
                    responseCode = "500", description = "Извините, при выполнении запроса произошла ошибка на сервере"
            )
    })
    public void exportSocksFromStock(@RequestParam SizesOfSocks sizes, ColoursOfSocks colours,
                                     int cotton, int quantity) {
        stockService.extractSocksFromStock(sizes, colours, cotton, quantity);
    }

    // СПИСАНИЕ БРАКА:
    @DeleteMapping
    @Operation(summary = "СПИСАНИЕ БРАКОВАННЫХ НОСКОВ СО СКЛАДА",
            description = "Укажите параметры носков для списания: размер, цвет, содержание хлопка и количество пар.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Списаны указанные носки в заданном количестве",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Socks.class)))}),
            @ApiResponse(
                    responseCode = "400", description = "Ошибка в параметрах запроса"
            ),
            @ApiResponse(
                    responseCode = "404", description = "Неверный URL или команда"
            ),
            @ApiResponse(
                    responseCode = "500", description = "Извините, при выполнении запроса произошла ошибка на сервере"
            )
    })
    public void deleteDefectiveSocksFromStock(@RequestParam SizesOfSocks sizes, ColoursOfSocks colours,
                                              int cotton, int quantity) {
        stockService.extractSocksFromStock(sizes, colours, cotton, quantity);
    }

}
