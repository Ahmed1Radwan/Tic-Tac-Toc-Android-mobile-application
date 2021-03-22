package com.ahmedhamdy.tic_tac_toe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    ListView lvDashboard;
    Button newuserButton;
    EditText newuserET;
    EditText ET2;

    DashboardObject object;
    List<DashboardObject> selectedObject;
    AlertDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        lvDashboard = findViewById(R.id.lvDashboard);
        ET2 = findViewById(R.id.ET2);
        ET2.setKeyListener(null);

        newuserButton = findViewById(R.id.newuserButton);
        newuserET = findViewById(R.id.newuserET);
        object = new DashboardObject();
        selectedObject = new ArrayList<>();

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DashboardActivity.this);
        alertBuilder.setMessage("Delete record for this name");
        alertBuilder.setTitle("Alert !");
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DashboardDatabase.getInstance(DashboardActivity.this).dashboardDAO().delete(selectedObject.get(0));
                List<DashboardObject> allRows = DashboardDatabase.getInstance(DashboardActivity.this).dashboardDAO().getAllRows();
                DashboardAdapter adapter = new DashboardAdapter(DashboardActivity.this, allRows);
                lvDashboard.setAdapter(adapter);
                dialog.cancel();
                //finish();
            }
        });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert = alertBuilder.create();

        List<DashboardObject> allRows = DashboardDatabase.getInstance(this).dashboardDAO().getAllRows();
        DashboardAdapter adapter = new DashboardAdapter(this, allRows);
        lvDashboard.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        List<DashboardObject> s = null;
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cm_menu, menu);
        return true;
    }

    public void DeleteAll(MenuItem item) {
        DashboardDatabase.getInstance(this).dashboardDAO().deleteAll();
        List<DashboardObject> allRows = DashboardDatabase.getInstance(this).dashboardDAO().getAllRows();
        DashboardAdapter adapter = new DashboardAdapter(this, allRows);
        lvDashboard.setAdapter(adapter);
        /*
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
         */
    }

    class DashboardAdapter extends ArrayAdapter<DashboardObject>{

        public DashboardAdapter(@NonNull Context context, List<DashboardObject> input) {
            super(context, 0,input);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            viewHolder holder;
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.custom_dashboard, parent, false);
                holder = new viewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (viewHolder) convertView.getTag();

            }

            holder.cm_name.setText(getItem(position).Name);
            holder.cm_score.setText(getItem(position).Score+"");
            holder.cm_gamed.setText(getItem(position).PlayedGames+"");

            return convertView;
        }

        class viewHolder{
            TextView cm_name;
            TextView cm_score;
            TextView cm_gamed;
            public viewHolder(View convertView){
                cm_name = convertView.findViewById(R.id.cm_name);
                cm_score = convertView.findViewById(R.id.cm_score);
                cm_gamed = convertView.findViewById(R.id.cm_gamed);
            }
        }
    }
    public void newUserClicked(View view) {

        if(newuserET.getText().toString().isEmpty()) return;

        selectedObject = DashboardDatabase.getInstance(this).dashboardDAO().checkIfNameFound(newuserET.getText().toString());
        if(selectedObject.size() > 0){
            excuteDialog();
        }else{
            object.Score = 0;
            object.PlayedGames = 0;
            object.Name = newuserET.getText().toString();
            DashboardDatabase.getInstance(this).dashboardDAO().insert(object);
            newuserET.setText("");
            List<DashboardObject> allRows = DashboardDatabase.getInstance(this).dashboardDAO().getAllRows();
            DashboardAdapter adapter = new DashboardAdapter(this, allRows);
            lvDashboard.setAdapter(adapter);
        }

    }
    private void excuteDialog(){
        alert.show();
    }
}