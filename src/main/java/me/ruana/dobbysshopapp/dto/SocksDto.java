package me.ruana.dobbysshopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.ruana.dobbysshopapp.model.Socks;


@Data
@AllArgsConstructor
public class SocksDto {
    @SerializedName("Носки")
    @JsonProperty("Носки")
    private Socks socks;
    @SerializedName("Количество на складе")
    @JsonProperty("Количество на складе")
     private int quantity;



    public static SocksDto from(Socks socks, int quantity) {
        return new SocksDto(socks, quantity);
    }
}
