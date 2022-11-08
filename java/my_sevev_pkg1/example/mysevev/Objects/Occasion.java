package my_sevev_pkg1.example.mysevev.Objects;

import java.util.Calendar;
import java.util.Date;

public class Occasion implements Comparable {

    final static int UNASSIGNED_PERS_ID = -1;
    final static String UNASSIGNED_PERS_NAME = "Not Assigned";
    static int serialNumCounter = 0;
    static  int frequency; //how many days between occasions

    String name;
    //int seqNum; //sequential number
    int personId = UNASSIGNED_PERS_ID; //person responsible
    int occId;
    String personName = UNASSIGNED_PERS_NAME;
    Date date;
    boolean alreadyPassed;

    public Occasion(String name, Date date){
        if(serialNumCounter == Integer.MAX_VALUE-1)
            serialNumCounter = 0;
        this.name = name;
        this.date = date;
        this.alreadyPassed = date.before(Calendar.getInstance().getTime()); //if already passted
        occId = serialNumCounter++;
    }


    public static int getFrequency() {
        return frequency;
    }
    public int getPersonId() {
        return personId;
    }
    public String getPersonName() {
        return personName;
    }
    public String getName() {
        return name;
    }

    public boolean isAssigned(){
        return !(personId == UNASSIGNED_PERS_ID);
    }
    public boolean isAlreadyPassed() {
        return this.date.before(Calendar.getInstance().getTime());}
    public String getNameAndDate(){
        return "parashat " + getName() + ", on (" + getDateString() + ")";
    }
    public Date getDate() {
        return date;
    }
    public String getDateString(){

        Calendar calBuffer = Calendar.getInstance();
        calBuffer.setTime(date);
        return calBuffer.get(Calendar.DAY_OF_MONTH) + "." + (
                calBuffer.get(Calendar.MONTH) + 1); // we add 1 bec months are zero indexed

    }

    /**
     * sorts LEAST-RECENT FIRST!
     * if this is greater, return 1, else if other is greater, return -1
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {

        Occasion other = (Occasion) o;
        if(!this.isAssigned() && !other.isAssigned())
            return 0;
        else if(!this.isAssigned())
            return 1;
        else if(!other.isAssigned())
            return -1;
        else //both are assigned..
            return this.date.compareTo(other.date);
    }

    /**
     * assigns person to this shabat. does not update person...
     * @param person
     */
    public void assign(Person person){
        personId = person.getSerialNum();
        personName = person.getName();
    }
    public void unAssign(){
        personId = UNASSIGNED_PERS_ID;
        personName = UNASSIGNED_PERS_NAME;
    }
}
