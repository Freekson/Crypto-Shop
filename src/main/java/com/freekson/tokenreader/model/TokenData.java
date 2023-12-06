package com.freekson.tokenreader.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenData {

    private String id;
    private String symbol;
    private String name;
    private String nameid;
    private int rank;
    @JsonProperty("price_usd")
    private String priceUSD;
    @JsonProperty("percent_change_24h")
    private String percentChange24h;
    @JsonProperty("percent_change_1h")
    private String percentChange1h;
    @JsonProperty("percent_change_7d")
    private String percentChange7d;
    @JsonProperty("price_btc")
    private String priceBTC;
    @JsonProperty("market_cap_usd")
    private String marketCapUSD;
    private double volume24;
    @JsonProperty("volume24a")
    private double volume24a;
    private String csupply;
    private String tsupply;
    private String msupply;
}
