package com.rfcontrols.wms.service.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ItemLocationTrackerEvent {
    private String epc;
    private double x;
    private double y;
    private double z;
    private String zoneName;
    private Instant updateTime;
}
