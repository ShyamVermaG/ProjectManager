package com.example.projectmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProjectView extends AppCompatActivity {

    ImageView imgV, futurGoalV;
    TextView strDateV, lastDateV, nameV, problemV, statusV, textV;
    LinearLayout futurGoalCon;

    String  projNo;
    JSONObject project;
    JSONObject mainObj;
    ArrayList<String> futureGoal=new ArrayList<>();

    String strtDate,lastDate,name,problem,status,text,img;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        project=new JSONObject();

        open();



        futurGoalV =findViewById(R.id.addfutuGoal);
        imgV =findViewById(R.id.img);
        strDateV =findViewById(R.id.strtDate);
        lastDateV =findViewById(R.id.endDate);
        nameV =findViewById(R.id.name);
        problemV =findViewById(R.id.problem);
        statusV =findViewById(R.id.status);
        textV =findViewById(R.id.text);


        Intent intent=getIntent();
        String data= String.valueOf(intent.getData());
//        Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
//
//        ArrayList<String> list=new ArrayList<>();
//        list.addAll(Arrays.asList(data.split("<>")));
//
//        Toast.makeText(this,list.get(1),Toast.LENGTH_SHORT).show();
        projNo= data;

        try {
            project = mainObj.getJSONObject(projNo);



            name=project.getString(KeyCon.project.name);
            strtDate=project.getString(KeyCon.project.strtDate);
            lastDate=project.getString(KeyCon.project.endDate);
            problem=project.getString(KeyCon.project.problem);
            text=project.getString(KeyCon.project.text);
            status=project.getString(KeyCon.project.status);
            img=project.getString(KeyCon.project.img);


            nameV.setText(name);
            strDateV.setText(strtDate);
            lastDateV.setText(lastDate);
            problemV.setText(problem);
            textV.setText(text);
            switch (status) {
                case "0" :
                    statusV.setText("Completed");
                    break;
                    case "1" :
                    statusV.setText("Working");
                    break;
                case "2" :
                    statusV.setText("Updation Avail");
                    break;

            }
            statusV.setText(status);


            String Aim=project.getString(KeyCon.project.futureGoal);
            futureGoal.addAll(Arrays.asList(Aim.split("<>")));

            //here add view to the main Container

            for(int i=0;i<futureGoal.size();i++){
                final View view = View.inflate(this, R.layout.single_goal, null);

                TextView no=view.findViewById(R.id.no);
                TextView text=view.findViewById(R.id.goalTxt);
                no.setText(""+i+1);
                text.setText(futureGoal.get(i));

                int finalI = i;
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        futureGoal.remove(finalI);
                        futurGoalCon.removeView(view);
                        return false;
                    }
                });
                futurGoalCon.addView(view);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        futurGoalV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //take text
                //for taking Version

                final AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                View mview = getLayoutInflater().inflate(R.layout.input_text, null);

                TextInputLayout nameV = mview.findViewById(R.id.text);
//            final TextInputLayout problemV=mview.findViewById(R.id.problem);

                TextView headV = mview.findViewById(R.id.heading);

                Button btn_cancel = mview.findViewById(R.id.cancelBtn);
                Button btn_ok = mview.findViewById(R.id.okBtn);


                headV.setText("Input Goal Text");
                alert.setView(mview);

                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        futureGoal.add(nameV.getEditText().getText().toString());

                        //show view
                        final View viewS = View.inflate(getApplicationContext(), R.layout.single_goal, null);

                        int i=futureGoal.size()-1;
                        TextView no=view.findViewById(R.id.no);
                        TextView text=view.findViewById(R.id.goalTxt);
                        no.setText(""+i);
                        text.setText(nameV.getEditText().getText().toString());

                        view.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                futureGoal.remove(i);
                                futurGoalCon.removeView(view);
                                return false;
                            }
                        });
                        futurGoalCon.addView(view);
                    }
                });


            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void open() {

        //getting data to build
        File d = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(d, "AllProject.txt");
        StringBuilder build = new StringBuilder();
//        Toast.makeText(this, "read succe", Toast.LENGTH_SHORT).show();

        try {
            FileReader reader = new FileReader(file);
            int ch;
            while ((ch = reader.read()) != -1)
                build.append((char) ch);
            String tamp = build.toString();
            mainObj = (new JSONObject(tamp));

            Toast.makeText(this, "read succe", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void addOneMore(View view){

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StringBuilder str=new StringBuilder();

        for(String a:futureGoal){
            str.append(a);
            str.append("<>");
        }


        try {
            project.put(KeyCon.project.status,status);
            project.put(KeyCon.project.problem,problem);
            project.put(KeyCon.project.text,text);
            project.put(KeyCon.project.name,name);
            project.put(KeyCon.project.endDate,lastDate);
            project.put(KeyCon.project.strtDate,strtDate);
            project.put(KeyCon.project.img,img);
            project.put(KeyCon.project.futureGoal,str.toString());


            mainObj.put(String.valueOf(projNo),project);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //here we store it to the main file

        write();


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void write() {

        File d = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(d, "AllProject.txt");
        FileWriter write = null;
        try {
            write = new FileWriter(file);
            write.append(mainObj.toString());
            write.close();
            Toast.makeText(this, "write sucess", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}