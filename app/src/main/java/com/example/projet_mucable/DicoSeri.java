package com.example.projet_mucable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DicoSeri implements Serializable {

    private Map<String,String> map;

    public DicoSeri(){
        this.map =  new HashMap<String,String>();
    }

    public void put(String k, String v){
        map.put(k, v);
    }

    public String get(String k){
        return (String)map.get(k);
    }

    public Map getMap(){
        return map;
    }

}
