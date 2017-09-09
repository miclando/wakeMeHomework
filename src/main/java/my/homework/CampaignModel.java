package my.homework;

import java.util.Map;

public class CampaignModel {
    private String Id  ;
    private String name;
    private String data;
    private Map<CapCount,Integer> cap;

    public CampaignModel(String id, String name, String data, Map<CapCount, Integer> cap) {
        Id = id;
        this.name = name;
        this.data = data;
        this.cap = cap;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public int getMaxCountPerUser(){
        return this.cap.get(CapCount.MAX_COUNT_PER_USER);
    }

    public int getMaxCount(){
        return this.cap.get(CapCount.MAX_COUNT);
    }

    enum CapCount{
        MAX_COUNT_PER_USER,
        MAX_COUNT;
    }
}
