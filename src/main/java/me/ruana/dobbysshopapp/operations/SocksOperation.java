package me.ruana.dobbysshopapp.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ruana.dobbysshopapp.model.Socks;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class SocksOperation {

    private OperationsType operationsType;

    private LocalDateTime dateTime;

    private int quantity;

    private Socks socks;


    @Override
    public String toString() {
        return operationsType + ": \n" +
                "Дата операции: " + dateTime + "\n" +
                "Количество: " + quantity + "пар(ы)" + "\n" +
                "Носки: " + socks;
    }
}
