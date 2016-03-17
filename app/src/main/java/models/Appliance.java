package models;

/**
 * Created by AKINDE-PETERS on 3/16/2016.
 */
public class Appliance {

    public String name, count, wattage, duration;

    public Appliance(String name,String count,String wattage,String duration){

        this.name = name;
        this.count = count;
        this.wattage = wattage;
        this.duration = duration;

    }


    //// getters ////

    public String getName(){
        return this.name;
    }

    public String getCount(){
        return this.count;
    }

    public String getWattage(){
        return this.wattage;
    }

    public String getDuration(){
        return this.duration;
    }

    //// setters ////

    public void setName(String value){
        this.name = value;
    }

    public void setCount(String value){
        this.count = value;
    }

    public void setWattage(String value){
        this.wattage = value;
    }

    public void setDuration(String value){
        this.duration = value;
    }



    public String toString(){
        return this.name;
    }

}

