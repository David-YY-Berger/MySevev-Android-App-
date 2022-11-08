package my_sevev_pkg1.example.mysevev.Objects;

import my_sevev_pkg1.example.mysevev.ComparatorsCustom.sortOccByMostRece;
import my_sevev_pkg1.example.mysevev.MyEnums.PersonPreference;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Person {

    static int serialNumCouter = 0;

    String name;
    List<Occasion> allOccasionsLst = new LinkedList<>();
    int serialNum; //used to label each person (so easy to find..)
    PersonPreference pref;
    String email;
    String phoneNumber;


    public List<Occasion> getAllOccasionsLst() {
        return allOccasionsLst;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public Person(String _name){
        name = _name;
        pref = PersonPreference.NEUTRAL;
        if(serialNumCouter == Integer.MAX_VALUE-1)
            serialNumCouter = 0;
        serialNum = serialNumCouter++;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Person setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Person setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }



    //implement build pattern
    public Person setPref(PersonPreference pref) {
        this.pref = pref;
        return this;
    }

    public String getName() {
        return name;
    }

    public PersonPreference getPref() {
        return pref;
    }

    public String getLastOccasName() {
        if(noOccasions())
            return "no occasions";
        else {
            Collections.sort(allOccasionsLst, new sortOccByMostRece());
            return getLastOccasion().getName();
        }
    }


    public boolean noOccasions() {
        return allOccasionsLst.isEmpty();
    }
    public Occasion getLastOccasion(){
        if(noOccasions())
            return null;
        else{
            Collections.sort(allOccasionsLst, new sortOccByMostRece());
            return allOccasionsLst.get(0);
        }
    }

    /**
     * assigns the given occasion to this person. does not update in the occassion..
     * @param occasion
     */
    public void assign(Occasion occasion){
        if(noOccasions()){
            allOccasionsLst.add(occasion);
        }
        else { //this occasion is later..
           int max_size = 10;
           if(allOccasionsLst.size()>= max_size){
               Collections.sort(allOccasionsLst, new sortOccByMostRece());
               allOccasionsLst.remove(max_size-1);
           }
           allOccasionsLst.add(occasion);
        }
    }
    public void unassign(Occasion occasion){

        Occasion occPtr = null;
        for (Occasion occ: allOccasionsLst
             ) {
            if(occ.occId == occasion.occId){
                occPtr = occ;
                break;
            }
        }
        if(Objects.isNull(occPtr))
            throw new IllegalArgumentException("occasion id is not found!");

        allOccasionsLst.remove(occPtr);
    }



}
