package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AKINDE-PETERS on 3/16/2016.
 */
public class Appliance implements Parcelable {

    public String name, count, wattage, duration;

    public Appliance(String name,String count,String wattage,String duration){

        this.name = name;
        this.count = count;
        this.wattage = wattage;
        this.duration = duration;

    }


    //// getters ////

    protected Appliance(Parcel in) {
        name = in.readString();
        count = in.readString();
        wattage = in.readString();
        duration = in.readString();
    }

    public static final Creator<Appliance> CREATOR = new Creator<Appliance>() {
        @Override
        public Appliance createFromParcel(Parcel in) {
            return new Appliance(in);
        }

        @Override
        public Appliance[] newArray(int size) {
            return new Appliance[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(count);
        parcel.writeString(wattage);
        parcel.writeString(duration);
    }
}

