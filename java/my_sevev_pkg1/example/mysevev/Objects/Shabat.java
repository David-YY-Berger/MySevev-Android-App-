package my_sevev_pkg1.example.mysevev.Objects;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

public class Shabat extends Occasion{



    public Shabat(String name, Date date){
        super(name, date); //to assign occId..
        frequency = 7; //static field (7 days)
    }

    @NonNull
    @Override
    public String toString() {
        return name;
//        String res =  name + " ";
//        if(isAssigned())
//            res += person.name;
//        else
//            res += "XX";
//        return res;
    }


}
