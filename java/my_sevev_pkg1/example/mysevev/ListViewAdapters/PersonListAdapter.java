package my_sevev_pkg1.example.mysevev.ListViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import my_sevev_pkg1.example.mysevev.ComparatorsCustom.sortPersByLeastRece;
import my_sevev_pkg1.example.mysevev.Objects.Person;
import com.example.mysevev.R;

import java.util.List;

public class PersonListAdapter extends ArrayAdapter<Person> {

    Context myContext;
    int myResource;

    final int SELECTED_ITEM_COLOR = Color.rgb(128, 203, 196);
    final int BACKGROUND_COLOR = Color.rgb(220,220,220);
    final int EMPTY_POS = -1; //no item is selectd
    int selectedItemIndex = EMPTY_POS; //last selected item


    public PersonListAdapter(@NonNull Context context, int resource, @NonNull List<Person> objects) {
        super(context, resource, objects);
        myContext = context;
        myResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String occasion= getItem(position).getLastOccasName();
//        PersonPreference pref = getItem(position).getPref();

        LayoutInflater inflater = LayoutInflater.from(myContext);
        convertView = inflater.inflate(myResource, parent, false);

        //set specific parts of convert view:
        TextView tvName = (TextView) convertView.findViewById(R.id.persTVName);
        TextView tvOccasion = (TextView) convertView.findViewById(R.id.persTVOccasion);
//        View rectangle = (View) convertView.findViewById(R.id.rectangle);

        if(position == selectedItemIndex)
            convertView.setBackgroundColor(SELECTED_ITEM_COLOR);
        else
            convertView.setBackgroundColor(BACKGROUND_COLOR);

        tvName.setText(name);
        tvOccasion.setText(occasion);
//        rectangle.setBackgroundColor(Color.BLUE);

        return convertView;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public boolean isItemSelected(){
        if(selectedItemIndex == EMPTY_POS)
            return false;
        else
            return true;
    }
    public Person getSelectedItem(){
        if(!isItemSelected())
            return null;
        else
            return getItem(selectedItemIndex);
    }

    //called after assigned, or unassignment
    public void refresh(){
        selectedItemIndex = EMPTY_POS;
        this.sort(new sortPersByLeastRece());
    }

    //end of class
}
