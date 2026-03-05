package com.ia.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LayersModel {

    private String name;
    private String type;
    private String outputShape;
    private String activation;
}
