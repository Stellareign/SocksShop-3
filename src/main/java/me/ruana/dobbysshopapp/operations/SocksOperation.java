package me.ruana.dobbysshopapp.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ruana.dobbysshopapp.model.Socks;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class SocksOperation {
    @SerializedName("Тип операции")
    @JsonProperty("Тип операции")
    private OperationsType operationsType;
    @SerializedName("Дата")
    @JsonProperty("Дата")
    private LocalDateTime dateTime;
    @SerializedName("Количество")
    @JsonProperty("Количество")
    private int quantity;
    @SerializedName("Носки")
    @JsonProperty("Носки")
    private Socks socks;


    @Override
    public String toString() {
        return operationsType + ": \n" +
                "Дата операции: " + dateTime + "\n" +
                "Количество: " + quantity + "пар(ы)" + "\n" +
                "Носки: " + socks;
    }
}
