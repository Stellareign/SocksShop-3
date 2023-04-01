package me.ruana.dobbysshopapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    // ПОЛУЧЕНИЕ СПИСКА НОСКОВ:
    @GetMapping//
    @Operation(summary = "Просмотр всех носков на складе",
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
    @Operation(summary = "Добавление носков на склад",
            description = "Выбрать соответствующие добавляемым носкам параметры: размер, цвет, содержание хлопка и количество пар")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "200",
                   description = "Добавить носки на склад",
                   content = {@Content(mediaType = "application/json",
                           array = @ArraySchema(schema = @Schema(implementation = Socks.class)))})
   })
    public ResponseEntity<?> addSocks (@RequestBody Socks socks, @RequestParam SizesOfSocks size, ColoursOfSocks colour, int cotton, Integer quantity) {
                var socks1 = stockService.addSocksInStock(size, colour,cotton, quantity);
        return ResponseEntity.ok(socks1);
    }

    // СПИСОК НОСКОВ ПО ВСЕМ ПАРАМЕТРАМ:
    @GetMapping("/socksParam")
    @Operation(summary = "Указать параеметры носков, которые нужно подобрать",
            description = "Показывает количество носков с одинаковыми параметрами на складе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Количество указанных носков",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Socks.class)))})
    })
    public ResponseEntity<Object> getSocksByParameter(ColoursOfSocks colour, SizesOfSocks size, int cottonMin, int cottonMax){
        var count = stockService.getSocksByParam(colour, size, cottonMin, cottonMax);
        if (count > 0) {
            return ResponseEntity.ok().body(count);
        } else {
            return new ResponseEntity<>("Указанные носки отсутствуют на складе", HttpStatus.BAD_REQUEST);
        }
    }

    //ОТПУСК НОСКОВ СО СКЛАДА:
//    @PutMapping("/export")
//    @Operation(summary = "Указать параеметры носков, которые нужно подобрать",
//            description = "Укажите параемтры носков для списания: размер, цвет, содержание хлопка и количество пар.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200",
//                    description = "Количество указанных носков",
//                    content = {@Content(mediaType = "application/json",
//                            array = @ArraySchema(schema = @Schema(implementation = Socks.class)))}),
//            @ApiResponse(
//                    responseCode = "400", description = "Ошибка в параметрах запроса"
//            ),
//            @ApiResponse(
//                    responseCode = "404", description = "Неверный URL или команда"
//            ),
//            @ApiResponse(
//                    responseCode = "500", description = "Извините, при выполнении запроса произошла ошибка на сервере"
//            )
//    })
//    public ResponseEntity<?> exportSocksFromStock() {
//
//    }

}
