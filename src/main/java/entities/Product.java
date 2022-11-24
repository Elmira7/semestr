package entities;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Product {
    private Long id;
    private String name;
    private String pathImage;
    private Integer price;
    private Map<String, String> feature;
    private String description;
    private Category category;
    private Integer count;

    public static String featureToString(Map<String, String> features){
        String feature = "";
        for(Map.Entry<String, String> feat: features.entrySet()){
            feature += feat.getKey() + "@" + feat.getValue() + "&&";
        }
        return feature;
    }
    public static Map<String, String> featureToMap(String feature){
        Map<String, String> features = new HashMap<>();
        if (feature != null && !feature.equals("")) {
            String[] featureMassive = feature.split("&&");
            String[] entity;
            for (String feat : featureMassive) {
                entity = feat.split("@");
                features.put(entity[0], entity[1]);
            }
        }
        return features;
    }
}
