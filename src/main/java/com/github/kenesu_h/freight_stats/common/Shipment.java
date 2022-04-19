package com.github.kenesu_h.freight_stats.common;

import java.time.LocalDate;

public class Shipment {
    private int id;
    private TradeType tradeType;
    private int commodityId;
    private int transportMethodId;
    private int source;
    private int destination;
    private int value;
    private int weight;
    private int freightCharges;
    private DF df;
    private boolean containerized;
    private LocalDate date;

    public Shipment(
            int id, TradeType tradeType, int commodityId, int transportMethodId, int source, int destination, int value,
            int weight, int freightCharges, DF df, boolean containerized, LocalDate date
    ) {
        this.id = id;
        this.tradeType = tradeType;
        this.commodityId = commodityId;
        this.transportMethodId = transportMethodId;
        this.source = source;
        this.destination = destination;
        this.value = value;
        this.weight = weight;
        this.freightCharges = freightCharges;
        this.df = df;
        this.containerized = containerized;
        this.date = date;
    }

    public int getId() {
        return this.id;
    }

    public TradeType getTradeType() {
        return this.tradeType;
    }

    public int getCommodityId() {
        return this.commodityId;
    }

    public int getTransportMethodId() {
        return this.transportMethodId;
    }

    public int getSource() {
        return this.source;
    }

    public int getDestination() {
        return this.destination;
    }

    public int getValue() {
        return this.value;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getFreightCharges() {
        return this.freightCharges;
    }

    public DF getDF() {
        return this.df;
    }

    public boolean getContainerized() {
        return this.containerized;
    }

    public LocalDate getDate() {
        return this.date;
    }
}
