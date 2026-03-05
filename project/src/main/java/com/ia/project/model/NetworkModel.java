package com.ia.project.model;

import lombok.Data;

@Data
public class NetworkModel {
    
    private String lossFunction;

    private String modelMetrics;

    private LayersModel layers;

    private SumaryModel sumary;

}
