package com.example.osmgeocollect.controllers;

import com.example.osmgeocollect.models.Polygon;
import com.example.osmgeocollect.services.OsmGeoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class OsmGeoController {
    private final OsmGeoService osmGeoService;

    public OsmGeoController(OsmGeoService osmGeoService) {
        this.osmGeoService = osmGeoService;
    }

    @GetMapping("/polygon")
    Object getPolygons(@RequestParam(name = "city-name") String cityName,
                        @RequestParam(name = "sub-ports") String subpart) throws JsonProcessingException {
        return osmGeoService.getOsm_Id(cityName,subpart);
    }
}
