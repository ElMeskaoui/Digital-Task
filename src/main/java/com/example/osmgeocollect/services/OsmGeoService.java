package com.example.osmgeocollect.services;

import com.example.osmgeocollect.models.Polygon;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OsmGeoService {

    private final String nominatimBaseUrl = "https://nominatim.openstreetmap.org";
    private JsonMapper jsonMapper;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public OsmGeoService(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public Object getOsm_Id(String cityName, String subpart) throws JsonProcessingException {

//        get the OSm id of the City
        JsonNode JsonNode = restTemplate.getForObject(nominatimBaseUrl + "/search.php?q=" + cityName +
                "&polygon_geojson=1&format=jsonv2", com.fasterxml.jackson.databind.JsonNode.class);
        JsonNode placeNode = JsonNode.get(0);
        long osmId = placeNode.get("osm_id").asLong();
        System.out.println("osm_id: " + osmId);
        System.out.println(subpart);

//        get Json Of subpart
        String subpartUrl = nominatimBaseUrl + "/search.php?q=" + subpart + "&polygon_geojson=1&format=jsonv2";
        JsonNode subpartJson = restTemplate.getForObject(subpartUrl, JsonNode.class);
        for (JsonNode node : subpartJson) {
            long sub_OsmID = node.findValue("osm_id").asLong();
            String categoryType = node.findValue("category").asText();
            System.out.println(node);
            String osmType = (node.findValue("osm_type").asText().equals("relation")) ? "R" : "N";
            System.out.println("osmType = " + osmType);
            JsonNode TryPlaces = restTemplate.getForObject(nominatimBaseUrl + "/details.php?osmtype=" + osmType +
                            "&osmid=" + sub_OsmID + "&class=" + categoryType + "&addressdetails=1&hierarchy=0&group_hierarchy=1&polygon_geojson=1&format=json",
                            JsonNode.class);
            JsonNode Addresses = TryPlaces.findValue("address");
//            verify if the subpar its a part of the City name
            for (JsonNode address : Addresses) {
                if (address.findValue("osm_id").asLong() == osmId) {
                    Polygon polygon = new Polygon();
                    polygon= objectMapper.readValue(TryPlaces.findValue("geometry").toString(),Polygon.class);
                    System.out.println(polygon);
                    polygon.setId(sub_OsmID);
                    return polygon;
                }
            }
        }
        return "City Not found";
    }
}
