package my_sevev_pkg1.example.mysevev.ComparatorsCustom;


import my_sevev_pkg1.example.mysevev.Objects.Occasion;

import java.util.Comparator;

/**
 * latest occassions are first
 */
public class sortOccByLeastRece implements Comparator<Occasion> {

    @Override
    public int compare(Occasion o1, Occasion o2) {
        return o1.compareTo(o2);
    }
}
