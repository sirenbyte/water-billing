package org.example.waterbilling.model.enumr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Statuses {
    WAIT("В ожиданий"),
    SUCCESS( "Успешно"),
    REJECT( "Отказ");

    private String description;

    Statuses(String description) {
        this.description = description;
    }

    public static List<Map> getAll(){
        List<Map> mapList = new ArrayList<>();
        for (Statuses m: Statuses.values()){
            Map<String,Object> map = new HashMap<>();
            map.put("code",m);
            map.put("description",m.description);
            mapList.add(map);
        }
        return mapList;
    }
}
