package android.com.sqliteapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText firstName, lastName;
    ListView listView;
    Button add, delete;

    Profile profile;
    List<Profile> allUser;
    ArrayList<String> userNames;
    MySqliteHandler mySqliteHandler;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = (EditText) findViewById(R.id.efirstName);
        lastName = (EditText) findViewById(R.id.eLastName);
        listView = (ListView) findViewById(R.id.listView);
        add = (Button) findViewById(R.id.btnAdd);
        delete = (Button) findViewById(R.id.btnDelete);

        add.setOnClickListener(MainActivity.this);
        delete.setOnClickListener(MainActivity.this);

        mySqliteHandler = new MySqliteHandler(MainActivity.this);
        allUser = mySqliteHandler.getAllUser();
        userNames = new ArrayList<>();

        if (allUser.size() > 0) {

            for (int i = 0; i < allUser.size(); i++) {

                Profile profile = allUser.get(i);
                userNames.add(profile.getFirstName() + "-" + profile.getLastName());
            }
        }


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userNames);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnAdd:

                profile = new Profile(firstName.getText().toString(), lastName.getText().toString());

                if (firstName.getText().toString().matches("") || lastName.getText().toString().matches("")) {
                    return;
                }
                profile = new Profile(firstName.getText().toString(), lastName.getText().toString());

                allUser.add(profile);
                userNames.add(profile.getFirstName() + " - " + profile.getLastName());
                firstName.setText("");
                lastName.setText("");

                break;

            case R.id.btnDelete:

                if (allUser.size() > 0) {
                    userNames.remove(0);
                    mySqliteHandler.deleteUser(allUser.get(0));
                    allUser.remove(0);
                } else {
                    return;
                }

                break;
        }

        arrayAdapter.notifyDataSetChanged();
    }
}
