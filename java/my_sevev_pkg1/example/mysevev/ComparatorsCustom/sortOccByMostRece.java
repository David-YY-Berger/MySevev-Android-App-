package my_sevev_pkg1.example.mysevev.ComparatorsCustom;

import java.util.Comparator;

import my_sevev_pkg1.example.mysevev.Objects.Occasion;

public class sortOccByMostRece implements Comparator<Occasion> {

    //reverses the order!
    @Override
    public int compare(Occasion o1, Occasion o2) {
        return o2.compareTo(o1);
    }
}
