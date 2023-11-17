package com.example.osmgeocollect.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class Polygon {

    private Long Id;
    private String type;
    private List<List<List<Double>> > coordinates;
}
