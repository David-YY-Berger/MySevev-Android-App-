package my_sevev_pkg1.example.mysevev.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Sevev {

    static Date currentDate;
    List<Occasion> lstOccasion = new LinkedList<>();
    List<Person> lstPerson = new LinkedList<>();


    //CTORS
    public Sevev() {
        initialize();
    }
    void initialize() {

        initializeShabats();
        Person.serialNumCouter = 0;
        Occasion.serialNumCounter = 0;
//        initializeExamplePersons();
    }
    void initializeShabats() {



        String hebrewParasha[] = {
                "בראשית", "נח", "לך-לך", "וירא", "חיי-שרה", "תולדות", "ויצא", "וישלח",
                "וישב", "מקץ", "ויגש", "ויחי"
                , "שמות", "וארא", "בא", "בשלח", "יתרו", "משפטים", "תרומה", "תצוה",
                "כי תשא", "ויקהל-פקודי",
                "ויקרא", "צו", "חול המועד", "שמיני", "תזריע-מצורע", "אחרי מות-קדושים", "אמור", "בהר-בחוקתי",
                "במדבר", "נשא", "בהעלותך", "שלח","קרח","חקת", "בלק" , "פנחס",  "מטות-מסעי",
                "דברים",       "ואתחנן", "עקב","ראה",  "שופטים",   "כי תצא", "כי תבוא", "נצבים-וילך", "ראש השנה", "האזינו"
        };
        String dateBegin = "2022-10-22";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //create instance of the Calendar class and set the date to the given date
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(dateBegin));
        } catch (ParseException e) {
            return;
        }

        for (int i = 0; i < hebrewParasha.length; i++) {
            try {
                lstOccasion.add(new Shabat(hebrewParasha[i],
                        sdf.parse(sdf.format(cal.getTime()))));
            } catch (ParseException e) {

            }
            cal.add(Calendar.DAY_OF_MONTH, Shabat.getFrequency());
        }
    }
    void initializeExamplePersons() {

        lstPerson.add(new Person("David Berger"));
        lstPerson.add(new Person("Sara Abrahams"));
        lstPerson.add(new Person("Aaron Cohen"));
        lstPerson.add(new Person("Miriam Levi"));
        lstPerson.add(new Person("Yisrael Shabat"));

    }

    public static Date getCurrentDate() {
        return currentDate;
    }

    public void updateCurrDate(){
        //called every time the app is opened
        currentDate = Calendar.getInstance().getTime();
    }

    public void reset() {
        lstOccasion = new LinkedList<>();
        lstPerson = new LinkedList<>();
        initialize();
    }

    //GETTERS
    public List<Occasion> getLstOccasion() {
        return lstOccasion;
    }
    public List<Person> getLstPerson() {
        return lstPerson;
    }
    /**
     * @return list of name+dates of all unnassigned occcasions,
     * that have not yet passed
     */
    public List<String> getLstUnassgndOccNames() {
        List<String> res = new LinkedList<>();
        for (Occasion occ : lstOccasion
        ) {
            if (!occ.isAssigned() && !occ.isAlreadyPassed())
                res.add(occ.getNameAndDate());
        }
        return res;
    }


    //CHANGING DATA
    public void assign(Occasion occasion, Person person) {
        if (occasion.isAssigned())
            throw new IllegalStateException("Occasion is already assigned!");

        occasion.assign(person);
        person.assign(occasion);
    }
    public void unAssign(Occasion occasion, int personId) {
        occasion.unAssign();

        Person personPtr = null;
        for (Person person : lstPerson
        ) {
            if (person.getSerialNum() == personId) {
                personPtr = person;
                break;
            }
        }
        if (Objects.isNull(personPtr))
            throw new IllegalArgumentException("person id is invalid!");

        personPtr.unassign(occasion);
    }
    public void addPerson(String personName){
        lstPerson.add(new Person(personName));
    }
    public void removePerson(int personId){
        Person personptr = null;
        for (Person person: lstPerson
             ) {
            if(person.getSerialNum() == personId){
                personptr = person;
                break;
            }
        }
        if(Objects.isNull(personptr))
            throw new IllegalArgumentException("Person id not found!");
        lstPerson.remove(personptr);

        //MUST REMOVE FROM OCCASION!
        for (Occasion occasion: lstOccasion
             ) {
            if(occasion.personId == personId)
                occasion.unAssign();
        }
    }
    public void removePerson(Person person){
        lstPerson.remove(person);
    }

    //GENERATING MESSAGES
    public String generateAskMsg(Person person, List<String> occNames) {
        if (occNames.size() == 0)
            throw new IllegalArgumentException("empty occasion list!");

        String res = "Hi " + person.getName() + "!\n";
        res += "Does " + occNames.get(0);
        if (occNames.size() > 1) {
            for (int i = 1; i < occNames.size(); i++) {
                res += ", or " + occNames.get(i);
            }
        }
        res += " work for you?\n";

        if (occNames.size() > 1) {
            res += "\n Please tell me all options that work :)\n";
        }
        return res;
    }
    public String generateRemindMsg(Person person, Occasion occasion) {
        return "Hi " + person.getName() + "!\n"
                + "Friendly reminder that you are signed up for " + occasion.getNameAndDate() + ".\n"
                + "Looking forward!\n";
    }
}


