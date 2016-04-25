package models;

/**
 * Created by AKINDE-PETERS on 4/17/2016.
 */
public class Home {

    public String name, appliances, location;

    public Home(String name, String appliances, String location){

        this.name = name;
        this.appliances = appliances;
        this.location = location;

    }


    //// getters ////

    public String getName(){
        return this.name;
    }

    public String getAppliances(){
        return this.appliances;
    }

    public String getLocation(){
        return this.location;
    }

    //// setters ////

    public void setName(String value){
        this.name = value;
    }

    public void setAppliances(String value){
        this.appliances = value;
    }

    public void setLocation(String value){
        this.location = value;
    }



    public String toString(){
        return this.name;
    }

}
