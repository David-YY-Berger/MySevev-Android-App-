package my_sevev_pkg1.example.mysevev.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import my_sevev_pkg1.example.mysevev.Objects.Sevev;
import my_sevev_pkg1.example.mysevev.Objects.Person;
import com.example.mysevev.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import my_sevev_pkg1.example.mysevev.ListViewAdapters.PersonListAdapter;
import my_sevev_pkg1.example.mysevev.ListViewAdapters.OccListAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREF_NAME = "shared preferences";
    public static final String SHARED_PREF_KEY_NAME = "sevevKey";

    ArrayList<Sevev> sevevLst;
    final int sevevPlace = 0;

    ListView occLstView;//occasion
    ListView personListView;

    OccListAdapter occLstAdapter;
    PersonListAdapter persLstAdapter;

    Button butAssign;
    Button butUnassign;
    Button butAsk;
    Button butRemind;
    Button butAddPerson;
    Button butRemovePerson;

    List<String> selectedOccs = new ArrayList<>();//used by "Ask" button, for dialogs to select items
    String personInputName = "";
    View.OnClickListener clkListComingSoon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        //ASSIGN, SET LISTVIEWS & ADAPTERS:
        occLstView = (ListView) findViewById(R.id.OccasionListView);
        occLstAdapter = new OccListAdapter(this, R.layout.activity_occasion_list_view,
                sevevLst.get(sevevPlace).getLstOccasion());
        occLstView.setAdapter(occLstAdapter);

        personListView = (ListView) findViewById(R.id.personListView);
        persLstAdapter = new PersonListAdapter(this, R.layout.activity_person_list_view,
                sevevLst.get(sevevPlace).getLstPerson());
        personListView.setAdapter(persLstAdapter);

        occLstView.setSelection(occLstAdapter.getPosOfCurrentDate()); // current shabat...

        occLstView.setOnItemClickListener((parent, view, position, id) -> {
            occLstAdapter.setSelectedItemIndex(position);
            occLstAdapter.notifyDataSetChanged();
        });
        personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                persLstAdapter.setSelectedItemIndex(position);
                persLstAdapter.notifyDataSetChanged();
            }
        });

        //ASSIGN BUTTONS:
        butAssign = findViewById(R.id.butAssign);
        butUnassign = (Button) findViewById(R.id.butUnassign);
        butAsk = (Button) findViewById(R.id.butAsk);
        butRemind = (Button) findViewById(R.id.butRemind);
        butAddPerson = (Button) findViewById(R.id.butAddPerson);
        butRemovePerson = (Button) findViewById(R.id.butRemovePerson);

        clkListComingSoon = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayComingSoon(v);
            }
        };

        //ASSIGN BUTTON CALLBACKS
        butAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!occLstAdapter.isItemSelected()){
                    displayErrMsg(v, "Please select a Shabat!");
                    return;
                }
                else if(!persLstAdapter.isItemSelected()){
                    displayErrMsg(v, "Please select a Person!");
                }
                else {
                    if(occLstAdapter.getSelectedItem().isAssigned()){
                        displayErrMsg(v, "Occasion is already assigned! You must unassign it");
                        return;
                    }
                    sevevLst.get(sevevPlace).assign(occLstAdapter.getSelectedItem(), persLstAdapter.getSelectedItem());
                    refresh();
                }

            }
        });
        butUnassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!occLstAdapter.isItemSelected()){
                    displayErrMsg(v, "Please select an Ocassion to unnasign");
                    refresh();
                    return;
                }
                else if (!occLstAdapter.getSelectedItem().isAssigned()) {
                    displayErrMsg(v, "Occasion not assigned!");
                    refresh();
                    return;
                }
                else {
                        sevevLst.get(sevevPlace).unAssign(occLstAdapter.getSelectedItem(),
                                occLstAdapter.getSelectedItem().getPersonId());
                        refresh();
                }


            }
        });
        butRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(!persLstAdapter.isItemSelected()){
                    displayErrMsg(v, "Please select a person!\n");
                    return;
                }
                Person person = persLstAdapter.getSelectedItem();
                if(person.noOccasions()){
                    displayErrMsg(v, "This person is not signed up for anything!");
                    return;
                }

                //launch new activity!
                String msgToSend = sevevLst.get(sevevPlace).generateRemindMsg(person, person.getLastOccasion());
                Intent intentMsgActiv = SendMsgActivity.genIntent(MainActivity.this,
                        msgToSend);
                startActivity(intentMsgActiv);
                refresh();
            }
        });
        butAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!persLstAdapter.isItemSelected()){
                    displayErrMsg(v, "Please Select a person to ask!");
                    return;
                }

                final ArrayAdapter<String> unassgndOccAdapt = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_multichoice);
                for (String s: sevevLst.get(sevevPlace).getLstUnassgndOccNames()
                     ) {
                    unassgndOccAdapt.add(s);
                }

                //start
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Select which Shabats to ask:-")
                        .setAdapter(unassgndOccAdapt, null)
                        .setPositiveButton("Ask", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(selectedOccs.size() == 0){
                                    //no item selected..
                                    displayErrMsg(v, "please select at least one Shabat!");
                                    return;
                                }
                                String msgToSend = sevevLst.get(sevevPlace).generateAskMsg(persLstAdapter.getSelectedItem(),
                                        selectedOccs);
                                Intent intentMsgActiv = SendMsgActivity.genIntent(MainActivity.this,
                                       msgToSend);
                                startActivity(intentMsgActiv);
                                selectedOccs.clear();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedOccs.clear();
                                dialog.dismiss();
                            }
                        })
                        .create();

                dialog.getListView().setItemsCanFocus(false);
                dialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                dialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // Manage selected items here
                        CheckedTextView textView = (CheckedTextView) view;

                        if(textView.isChecked()) {
                            selectedOccs.add(unassgndOccAdapt.getItem(position));
                        } else {
                            selectedOccs.remove(unassgndOccAdapt.getItem(position));
                        }
                    }
                });
                dialog.show();
            }
        });
        butAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Please enter the person's name:");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected;
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("Add Person", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        personInputName = input.getText().toString();
                        if(personInputName == ""){
                            displayErrMsg(v, "please type a name!");
                            return;
                        }
                        sevevLst.get(sevevPlace).addPerson(personInputName);
                        personInputName = "";
                        refresh();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        butRemovePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!persLstAdapter.isItemSelected()){
                    displayErrMsg(v, "Please Select a person to remove!");
                    return;
                }
                sevevLst.get(sevevPlace).removePerson(persLstAdapter.getSelectedItem().getSerialNum());
                refresh();
            }
        });
        refresh();
    }

    private void displayComingSoon(View view) {

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Coming Soon!");
        alertDialog.setMessage("We will develop this feature and update our app soon!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        refresh();
    }
    private void displayErrMsg(View view, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    private void refresh(){

        occLstAdapter.refresh();
        persLstAdapter.refresh();

        runOnUiThread(new Runnable() {
            public void run() {
                persLstAdapter.notifyDataSetChanged();
                occLstAdapter.notifyDataSetChanged();
            }
        });
        occLstView.setSelection(occLstAdapter.getPosOfCurrentDate()); // current shabat...

        saveData();
    }


    /**
     * called every refresh
     */
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sevevLst);
        editor.putString(SHARED_PREF_KEY_NAME, json);
        editor.apply();
    }

    /**
     * called once at the beginning
     */
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREF_KEY_NAME, null);
        Type type = new TypeToken<ArrayList<Sevev>>() {}.getType();
        sevevLst = gson.fromJson(json, type);

        if (sevevLst == null) {
            sevevLst = new ArrayList<>();
            sevevLst.add(new Sevev()); //initializes the occasions..
        }
        sevevLst.get(sevevPlace).updateCurrDate(); //update to today (called once every time app is opened)
//        sevevLst.get(sevevPlace).reset(); //<-- uncomment to reset the data..
        saveData();
    }




//end of class
}