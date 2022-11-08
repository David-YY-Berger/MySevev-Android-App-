package my_sevev_pkg1.example.mysevev.ComparatorsCustom;

import my_sevev_pkg1.example.mysevev.Objects.Person;

import java.util.Comparator;
import java.util.Objects;

/**
 * sorts persons by least-recently-done first
 */
public class sortPersByLeastRece implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {

        if(Objects.isNull(p1.getLastOccasion()) && Objects.isNull(p2.getLastOccasion()))
            return 0;
        else if(Objects.isNull(p1.getLastOccasion()))
            return -1;
        else if(Objects.isNull(p2.getLastOccasion()))
            return 1;
        else //both are assigned..
            return p1.getLastOccasion()
                .compareTo(p2.getLastOccasion());
    }
}
