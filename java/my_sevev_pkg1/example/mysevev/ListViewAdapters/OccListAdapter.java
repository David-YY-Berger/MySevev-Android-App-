package my_sevev_pkg1.example.mysevev.ListViewAdapters;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import my_sevev_pkg1.example.mysevev.Objects.Occasion;
import com.example.mysevev.R;

import java.util.Date;
import java.util.List;

/**
 * occasion listview adapter..
 */
public class OccListAdapter extends ArrayAdapter<Occasion> {

    //CONSTANTS:
    final int SELECTED_ITEM_COLOR = Color.rgb(128, 203, 196);
    final int BACKGROUND_COLOR = Color.rgb(220,220,220);
    final int EMPTY_POS = -1; //no item is selectd
    int selectedItemIndex = EMPTY_POS; //last selected item

    // FIELDS:
    Context myContext;
    int myResource;
    int posOfCurrentDate = -1;


    public OccListAdapter(@NonNull Context context, int resource,
                          @NonNull List<Occasion> objects) {
        super(context, resource, objects);
        myContext = context;
        myResource = resource;
    }

    public int getPosOfCurrentDate() {
        return posOfCurrentDate;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String date = getItem(position).getDateString();
        String person = getItem(position).getPersonName();

        LayoutInflater inflater = LayoutInflater.from(myContext);
        convertView = inflater.inflate(myResource, parent, false);

        //set specific parts of convert view:
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewName);
        TextView tvDate = (TextView) convertView.findViewById(R.id.textViewDate);
        TextView tvPerson = (TextView) convertView.findViewById(R.id.textViewPerson);

        if(position == selectedItemIndex)
            convertView.setBackgroundColor(SELECTED_ITEM_COLOR);
        else
            convertView.setBackgroundColor(BACKGROUND_COLOR);

        tvName.setText(name);
        tvDate.setText(date);
        tvPerson.setText(person);

        if(getItem(position).isAlreadyPassed()){ //occasion passed already...
            tvDate.setPaintFlags(tvDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvName.setPaintFlags(tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvPerson.setPaintFlags(tvPerson.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
        else {
            if(posOfCurrentDate == -1){
                posOfCurrentDate = position; //this is set once to get current position..
            }
        }

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
    public Occasion getSelectedItem(){
        if(!isItemSelected())
            return null;
        else
            return getItem(selectedItemIndex);
    }
    public void refresh(){
        selectedItemIndex = EMPTY_POS;
    }

}