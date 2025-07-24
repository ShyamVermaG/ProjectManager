package com.example.projectmanager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import org.json.JSONObject;

import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
//import org.json.parser.*;

public class MainActivity extends AppCompatActivity {

    public static final String KeyAddStringg = "<<<img>>>";

    public static final String JSON_STRING = "{}";

    public int projNo = -1, verNo = -1, topicNo = -1, dataNo = -1;
    public JSONObject projNoJSON, verNoJSON, topicNoJSON, dataNoJSON;

    public int cur_por = 0;
    public JSONObject cur_porObj;


    LinearLayout headingCon, con;
    TextView heading;
    JSONObject mainObj = new JSONObject();


    EditText inputText;
    LinearLayout addDataCon;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addDataCon = findViewById(R.id.addDataCon);
        inputText = findViewById(R.id.text);
        con = findViewById(R.id.con);
        headingCon = findViewById(R.id.headingCon);
        heading = findViewById(R.id.heading);


        //default initiallization
        cur_por = 0;
        cur_porObj = new JSONObject();


//        //get project
//
//        try {
//            JSONObject emp = (new JSONObject(JSON_STRING)).getJSONObject("employee");
//
//            //initiallize mainObj;
//            mainObj = emp;
//
//            showAllProjects(emp);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        open();
    }

    //this is for showing all Portions
    private void showAllProjects(JSONObject obj) {

        con.removeAllViews();
        headingCon.setVisibility(View.VISIBLE);
        heading.setText("Projects:");

        //setting up for project level
        cur_por = 0;
        cur_porObj = obj;

        JSONObject singleP = new JSONObject();
        try {
            int i = 0;
            while (!obj.isNull(String.valueOf(i))) {


                singleP = obj.getJSONObject(String.valueOf(i));


                //escape condition data not found
                if (singleP.isNull(KeyCon.project.name)) {
                    i++;
                    continue;
                }


                final View view = View.inflate(this, R.layout.single_project, null);

                ImageView imgV = view.findViewById(R.id.projectImg);
                TextView nameV = view.findViewById(R.id.name);
                TextView titleV = view.findViewById(R.id.title);
                TextView startV = view.findViewById(R.id.startDate);
                TextView endV = view.findViewById(R.id.endDate);


                nameV.setText(singleP.getString(KeyCon.project.name));
                titleV.setText(singleP.getString(KeyCon.project.problem));
//                singleP.getString(KeyCon.project.img);
                startV.setText(singleP.getString(KeyCon.project.strtDate));
                endV.setText(singleP.getString(KeyCon.project.endDate));




//                if (singleP.get(KeyCon.project.status).equals(KeyCon.project.value.Completed)) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        view.setBackground(getDrawable(R.drawable.green_status));
//                    }
//                } else if (singleP.get(KeyCon.project.status).equals(KeyCon.project.value.Working)) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        view.setBackground(getDrawable(R.drawable.yellow_status));
//                    }
//                } else {
//
//                }

//                singleP.getString(KeyCon.project.status);
//                singleP.getString(KeyCon.project.text);


//                here is for status showing
//                if(singleP.get(KeyCon.project.status).equals(KeyCon.project.value.Completed)){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        view.setBackground(getDrawable(R.drawable.green_status));
//                    }
//                }else if(singleP.get(KeyCon.project.status).equals(KeyCon.project.value.Working)){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        view.setBackground(getDrawable(R.drawable.yellow_status));
//                    }
//                }


                JSONObject finalSingleP = singleP;
                int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        projNo = finalI;
                        projNoJSON = finalSingleP;

                        //this is for onclick listener
                        showAllVersion(finalSingleP);
                    }
                });

                //for erase data of perticular obj
                JSONObject finalSingleP1 = singleP;
                int finalI1 = i;
                int finalI2 = i;
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        //for uting status

                        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        View mview = getLayoutInflater().inflate(R.layout.status_updater, null);


                        Button Completed = mview.findViewById(R.id.completed);
                        Button Updation = mview.findViewById(R.id.updationAvl);
                        Button Working = mview.findViewById(R.id.working);

                        Button ViewV = mview.findViewById(R.id.view);
                        Button deleteV = mview.findViewById(R.id.delete);

                        alert.setView(mview);

                        final AlertDialog alertDialog = alert.create();
                        alertDialog.setCanceledOnTouchOutside(true);

                        Completed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //for updating status
                                try {
                                    finalSingleP.put(KeyCon.project.status, KeyCon.project.value.Completed);
                                    mainObj.put(String.valueOf(finalI), finalSingleP);
//                            con.removeAllViews();
                                    showAllProjects(mainObj);
                                    alertDialog.dismiss();
                                    showAllProjects(mainObj);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Updation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //for updating status
                                try {
                                    finalSingleP.put(KeyCon.project.status, KeyCon.project.value.UpdationAvailable);
                                    mainObj.put(String.valueOf(finalI), finalSingleP);
//                            con.removeAllViews();
                                    showAllProjects(mainObj);
                                    alertDialog.dismiss();
                                    showAllProjects(mainObj);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Working.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //for updating status
                                try {
                                    finalSingleP.put(KeyCon.project.status, KeyCon.project.value.Working);
                                    mainObj.put(String.valueOf(finalI), finalSingleP);
//                            con.removeAllViews();
                                    showAllProjects(mainObj);
                                    alertDialog.dismiss();

                                    showAllProjects(mainObj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        deleteV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                for removing
                                try {
                                    mainObj.put(String.valueOf(finalI), " ");
                                    showAllProjects(mainObj);
                                    alertDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        ViewV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                alertDialog.dismiss();

                                //for viewing page

                                Intent intent = new Intent(getApplicationContext(), ProjectView.class);
                                intent.setData(Uri.parse(String.valueOf(finalI2)));
//                        intent.putData();
                                startActivity(intent);

                            }
                        });

                        alertDialog.show();

                        return false;
                    }
                });

                con.addView(view);
//                Toast.makeText(this,""+i,Toast.LENGTH_SHORT).show();
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showAllVersion(JSONObject obj) {
        con.removeAllViews();
        headingCon.setVisibility(View.VISIBLE);
        heading.setText("Versions:");

        //setting up for project level
        cur_por = 1;
        cur_porObj = obj;

        JSONObject singleP = new JSONObject();
        try {
            int i = 0;
            while (!obj.isNull(String.valueOf(i))) {
                singleP = obj.getJSONObject(String.valueOf(i));

                //escape condition data not found
                if (singleP.isNull(KeyCon.project.version.name)) {
                    i++;
                    continue;
                }


                final View view = View.inflate(this, R.layout.single_version, null);

//                ImageView imgV=view.findViewById(R.id.);
                TextView nameV = view.findViewById(R.id.name);
//                TextView titleV = view.findViewById(R.id.title);
                TextView startV = view.findViewById(R.id.strtDate);
                TextView endV = view.findViewById(R.id.endDate);

                nameV.setText(singleP.getString(KeyCon.project.version.name));
                startV.setText(singleP.getString(KeyCon.project.version.strtDate));
                endV.setText(singleP.getString(KeyCon.project.version.lastDate));
//                if (singleP.getString(KeyCon.project.version.status).equals(KeyCon.project.version.value.Working)){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        view.setBackground(getDrawable(R.color.yellow));
//                    }
//            }
//                else if(singleP.getString(KeyCon.project.version.status).equals(KeyCon.project.version.value.Completed))
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            view.setBackground(getDrawable(R.color.green));
//                        }
//
//
//

                if (singleP.get(KeyCon.project.version.status).equals(KeyCon.project.version.value.Completed)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.setBackground(getDrawable(R.drawable.green_status));
                    }
                } else if (singleP.get(KeyCon.project.version.status).equals(KeyCon.project.version.value.Working)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.setBackground(getDrawable(R.drawable.yellow_status));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.setBackground(getDrawable(R.drawable.red_status));
                    }
                }

                JSONObject finalSingleP = singleP;
                int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        verNo = finalI;
                        verNoJSON = finalSingleP;
                        //this is for onclick listener
                        showAllTopic(finalSingleP);
                    }
                });
                //for erase data of perticular obj
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {


                        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        View mview = getLayoutInflater().inflate(R.layout.status_updater, null);


                        Button Completed = mview.findViewById(R.id.completed);
                        Button Updation = mview.findViewById(R.id.updationAvl);
                        Button Working = mview.findViewById(R.id.working);
                        Button delte = mview.findViewById(R.id.delete);

                        alert.setView(mview);

                        final AlertDialog alertDialog = alert.create();
                        alertDialog.setCanceledOnTouchOutside(true);

                        Completed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //for updating status
                                try {
                                    finalSingleP.put(KeyCon.project.version.status, KeyCon.project.version.value.Completed);
                                    projNoJSON.put(String.valueOf(finalI), finalSingleP);
                                    mainObj.put(String.valueOf(projNo), projNoJSON);
                                    alertDialog.dismiss();
                                    showAllVersion(projNoJSON);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Updation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //for updating status
                                try {
                                    finalSingleP.put(KeyCon.project.version.status, KeyCon.project.version.value.UpdationAvailable);
                                    projNoJSON.put(String.valueOf(finalI), finalSingleP);
                                    mainObj.put(String.valueOf(projNo), projNoJSON);
                                    alertDialog.dismiss();
                                    showAllVersion(projNoJSON);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Working.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //for updating status
                                try {
                                    finalSingleP.put(KeyCon.project.version.status, KeyCon.project.version.value.Working);
                                    projNoJSON.put(String.valueOf(finalI), finalSingleP);
                                    mainObj.put(String.valueOf(projNo), projNoJSON);
                                    alertDialog.dismiss();

                                    showAllVersion(projNoJSON);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                        delte.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                for removing
                                try {
                                    alertDialog.dismiss();
                                    projNoJSON.put(String.valueOf(finalI), " ");
                                    mainObj.put(String.valueOf(projNo), projNoJSON);
                                    showAllVersion(projNoJSON);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        alertDialog.show();


                        return false;
                    }
                });

                //this is for onclick listener
//                showAllTopic(singleP);
                con.addView(view);
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showAllTopic(JSONObject obj) {
        con.removeAllViews();
        headingCon.setVisibility(View.VISIBLE);
        heading.setText("Topics:");

        //setting up for project level
        cur_por = 2;
        cur_porObj = obj;

        JSONObject singleP = new JSONObject();
        try {
            int i = 0;
            while (!obj.isNull(String.valueOf(i))) {
                singleP = obj.getJSONObject(String.valueOf(i));

                //escape condition data not found
                if (singleP.isNull(KeyCon.project.version.topic.name)) {
                    i++;
                    continue;
                }


                String name = singleP.getString(KeyCon.project.version.topic.name);
                String date = singleP.getString(KeyCon.project.version.topic.date);
                singleP.getString(KeyCon.project.version.topic.status);

                final View view = View.inflate(this, R.layout.single_topic, null);

                TextView text = view.findViewById(R.id.text);
                TextView no = view.findViewById(R.id.no);

                text.setText(name);
                no.setText(i + 1 + "");

                JSONObject finalSingleP = singleP;
                int finalI = i;


                //here is for status showing
                if (singleP.get(KeyCon.project.version.topic.status).equals(KeyCon.project.version.topic.value.Working)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.setBackground(getDrawable(R.drawable.yellow_status));
                    }
                } else if (singleP.get(KeyCon.project.version.topic.status).equals(KeyCon.project.version.topic.value.Verified)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.setBackground(getDrawable(R.drawable.light_red_status));
                    }
                } else if (singleP.get(KeyCon.project.version.topic.status).equals(KeyCon.project.version.topic.value.Cancelled)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.setBackground(getDrawable(R.drawable.red_status));
                    }
                } else if (singleP.get(KeyCon.project.version.topic.status).equals(KeyCon.project.version.topic.value.Completed)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.setBackground(getDrawable(R.drawable.green_status));
                    }
                }


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        topicNo = finalI;
                        topicNoJSON = finalSingleP;

                        //this is for onclick listener
                        showAllData(finalSingleP);
                    }
                });
                //for erase data of perticular obj
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {


                        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        View mview = getLayoutInflater().inflate(R.layout.update_status_topic, null);


                        Button created = mview.findViewById(R.id.created);
                        Button Working = mview.findViewById(R.id.working);
                        Button Cacncelled = mview.findViewById(R.id.cancelled);
                        Button verified = mview.findViewById(R.id.verified);
                        Button Completed = mview.findViewById(R.id.completed);
                        Button Delete = mview.findViewById(R.id.delete);

                        alert.setView(mview);

                        final AlertDialog alertDialog = alert.create();
                        alertDialog.setCanceledOnTouchOutside(true);

                        created.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //for updating status
                                try {
                                    finalSingleP.put(KeyCon.project.version.topic.status, KeyCon.project.version.topic.value.Created);
                                    verNoJSON.put(String.valueOf(finalI), finalSingleP);
                                    projNoJSON.put(String.valueOf(verNo), verNoJSON);
                                    mainObj.put(String.valueOf(projNo), projNoJSON);
                                    showAllTopic(verNoJSON);

                                    alertDialog.dismiss();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Working.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //for updating status
                                try {
                                    finalSingleP.put(KeyCon.project.version.topic.status, KeyCon.project.version.topic.value.Working);
                                    verNoJSON.put(String.valueOf(finalI), finalSingleP);
                                    projNoJSON.put(String.valueOf(verNo), verNoJSON);
                                    mainObj.put(String.valueOf(projNo), projNoJSON);
                                    showAllTopic(verNoJSON);

                                    alertDialog.dismiss();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Cacncelled.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //for updating status
                                try {
                                    finalSingleP.put(KeyCon.project.version.topic.status, KeyCon.project.version.topic.value.Cancelled);
                                    verNoJSON.put(String.valueOf(finalI), finalSingleP);
                                    projNoJSON.put(String.valueOf(verNo), verNoJSON);
                                    mainObj.put(String.valueOf(projNo), projNoJSON);
                                    showAllTopic(verNoJSON);

                                    alertDialog.dismiss();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        verified.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //for updating status
                                try {
                                    finalSingleP.put(KeyCon.project.version.topic.status, KeyCon.project.version.topic.value.Verified);
                                    verNoJSON.put(String.valueOf(finalI), finalSingleP);
                                    projNoJSON.put(String.valueOf(verNo), verNoJSON);
                                    mainObj.put(String.valueOf(projNo), projNoJSON);
                                    showAllTopic(verNoJSON);

                                    alertDialog.dismiss();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Completed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //for updating status
                                try {
                                    finalSingleP.put(KeyCon.project.version.topic.status, KeyCon.project.version.topic.value.Completed);
                                    verNoJSON.put(String.valueOf(finalI), finalSingleP);
                                    projNoJSON.put(String.valueOf(verNo), verNoJSON);
                                    mainObj.put(String.valueOf(projNo), projNoJSON);
                                    showAllTopic(verNoJSON);

                                    alertDialog.dismiss();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    verNoJSON.put(String.valueOf(finalI), " ");
                                    projNoJSON.put(String.valueOf(verNo), verNoJSON);
                                    mainObj.put(String.valueOf(projNo), projNoJSON);
                                    showAllTopic(verNoJSON);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                        alertDialog.show();


                        return false;
                    }
                });

                con.addView(view);

                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showAllData(JSONObject obj) {

        addDataCon.setVisibility(View.VISIBLE);

        con.removeAllViews();
        headingCon.setVisibility(View.VISIBLE);
        heading.setText("Datas:");

        //setting up for project level
        cur_por = 3;
        cur_porObj = obj;

        JSONObject singleP = new JSONObject();
        try {
            int i = 0;
            while (!obj.isNull(String.valueOf(i))) {
                singleP = obj.getJSONObject(String.valueOf(i));

                //escape condition data not found
                if (singleP.isNull(KeyCon.project.version.topic.data.data)) {
                    i++;
                    continue;
                }


                String data = singleP.getString(KeyCon.project.version.topic.data.data);
                String date = singleP.getString(KeyCon.project.version.topic.data.date);

                //if it contains image
                if (data.contains(KeyAddStringg)) {
                    i++;
                    continue;
                }


                final View view = View.inflate(this, R.layout.single_text, null);

                TextView text = view.findViewById(R.id.text);
                TextView dateV = view.findViewById(R.id.date);

                text.setText(data);
                dateV.setText(date);


                //for erase data of perticular obj
                int finalI = i;
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        try {
                            topicNoJSON.put(String.valueOf(finalI), " ");
                            verNoJSON.put(String.valueOf(topicNo), topicNoJSON);
                            projNoJSON.put(String.valueOf(verNo), verNoJSON);
                            mainObj.put(String.valueOf(projNo), projNoJSON);
                            showAllData(topicNoJSON);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });

                con.addView(view);

                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //<------------     this is for creating all Portions  ------------------->

    private JSONObject createProject(String name, String problem, String img, String strtDate, String endDate, String status, String text) {
        JSONObject em = new JSONObject();
        try {
            em.put(KeyCon.project.name, name);
            em.put(KeyCon.project.problem, problem);
            em.put(KeyCon.project.img, img);
            em.put(KeyCon.project.strtDate, strtDate);
            em.put(KeyCon.project.endDate, endDate);
            em.put(KeyCon.project.status, status);
            em.put(KeyCon.project.text, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return em;
    }

    private JSONObject createVersion(String name, String strtDate, String lastDate, String status) {
        JSONObject em = new JSONObject();
        try {
            em.put(KeyCon.project.version.name, name);
            em.put(KeyCon.project.version.strtDate, strtDate);
            em.put(KeyCon.project.version.lastDate, lastDate);
            em.put(KeyCon.project.version.status, status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return em;
    }

    private JSONObject createTopic(String name, String date, String status) {
        JSONObject em = new JSONObject();
        try {
            em.put(KeyCon.project.version.topic.name, name);
            em.put(KeyCon.project.version.topic.date, date);
            em.put(KeyCon.project.version.topic.status, status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return em;
    }

    private JSONObject createData(String data, String date) {
        JSONObject em = new JSONObject();
        try {
            em.put(KeyCon.project.version.topic.data.data, data);
            em.put(KeyCon.project.version.topic.data.date, date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return em;
    }


    //this is for adding one more data
    public void addOneMore(View view) {


        //for project level
        if (cur_por == 0) {


            //for taking project

            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            View mview = getLayoutInflater().inflate(R.layout.input_project, null);

            final TextInputLayout nameV = mview.findViewById(R.id.name);
            final TextInputLayout problemV = mview.findViewById(R.id.problem);

            Button btn_cancel = mview.findViewById(R.id.cancelBtn);
            Button btn_ok = mview.findViewById(R.id.okBtn);

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
                public void onClick(View v) {
                    //for project
                    String name = "Project", problem = "hee", text = "text", img = "", strtDate = "", lastDate = "";

                    name = nameV.getEditText().getText().toString();
                    problem = problemV.getEditText().getText().toString();

                    Calendar calendar = Calendar.getInstance();
                    strtDate = DateFormat.getDateInstance().format(calendar.getTime());

                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
                    lastDate = DateFormat.getDateInstance().format(calendar.getTime());


                    int i = 0;
                    while (!cur_porObj.isNull(String.valueOf(i))) {

                        try {

                            cur_porObj.getJSONObject(String.valueOf(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            break;
                        }
                        i++;
                    }


                    //add the new data on i positions

                    try {
                        JSONObject singleP = new JSONObject();
                        singleP.put(KeyCon.project.name, name);
                        singleP.put(KeyCon.project.strtDate, strtDate);

                        singleP.put(KeyCon.project.endDate, lastDate);
                        singleP.put(KeyCon.project.img, img);
                        singleP.put(KeyCon.project.problem, problem);
                        singleP.put(KeyCon.project.text, text);
                        singleP.put(KeyCon.project.status, KeyCon.project.value.UpdationAvailable);

//                cur_porObj.put(String.valueOf(i), singleP);
                        //adding to the main obj
                        mainObj.put(String.valueOf(i), singleP);

                        showAllProjects(mainObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    alertDialog.dismiss();

                }
            });

            alertDialog.show();


        }
        //this is for versions
        else if (cur_por == 1) {
            //add the new data on i positions

            //for taking Version

            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            View mview = getLayoutInflater().inflate(R.layout.input_text, null);

            TextInputLayout nameV = mview.findViewById(R.id.text);
//            final TextInputLayout problemV=mview.findViewById(R.id.problem);

            TextView headV = mview.findViewById(R.id.heading);

            Button btn_cancel = mview.findViewById(R.id.cancelBtn);
            Button btn_ok = mview.findViewById(R.id.okBtn);


            headV.setText("Input Version Name");
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
                public void onClick(View v) {
                    //for project
                    String name = "Versions", strtDate = "dfsd", lastDate = "df", status = "sdfsd";

                    name = nameV.getEditText().getText().toString();
//                    problem=problemV.getEditText().getText().toString();

                    Calendar calendar = Calendar.getInstance();
                    strtDate = DateFormat.getDateInstance().format(calendar.getTime());

                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
                    lastDate = DateFormat.getDateInstance().format(calendar.getTime());

                    status = KeyCon.project.version.value.UpdationAvailable;


                    int i = 0;
                    while (!cur_porObj.isNull(String.valueOf(i))) {

                        i++;
//                try {
//
//                    cur_porObj.getJSONObject(String.valueOf(i));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    break;
//                }
                    }


                    try {
                        JSONObject singleP = new JSONObject();
                        singleP.put(KeyCon.project.version.name, name);
                        singleP.put(KeyCon.project.version.strtDate, strtDate);

                        singleP.put(KeyCon.project.version.lastDate, lastDate);
                        singleP.put(KeyCon.project.version.status, status);

                        //adding to the main obj
//                verNoJSON.put(String.valueOf(i),singleP);
                        projNoJSON.put(String.valueOf(i), singleP);
                        mainObj.put(String.valueOf(projNo), projNoJSON);

                        showAllVersion(projNoJSON);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    alertDialog.dismiss();

                }
            });

            alertDialog.show();


        }
        //this is for topics
        else if (cur_por == 2) {


            //for taking topics

            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            View mview = getLayoutInflater().inflate(R.layout.input_text, null);

            final TextInputLayout nameV = mview.findViewById(R.id.text);
//            final TextInputLayout problemV=mview.findViewById(R.id.problem);

            TextView headV = mview.findViewById(R.id.heading);

            Button btn_cancel = mview.findViewById(R.id.cancelBtn);
            Button btn_ok = mview.findViewById(R.id.okBtn);


            headV.setText("Input Topic Name");
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
                public void onClick(View v) {
                    //for topics
                    String name = "topics", status = "sdfsd", date = "dsdf";

                    name = nameV.getEditText().getText().toString();
//                    problem=problemV.getEditText().getText().toString();

                    Calendar calendar = Calendar.getInstance();
                    date = DateFormat.getDateInstance().format(calendar.getTime());


                    status = KeyCon.project.version.topic.value.Created;


                    //add the new data on i positions
                    int i = 0;
                    while (!cur_porObj.isNull(String.valueOf(i))) {

//                try {
//
//                    cur_porObj.getJSONObject(String.valueOf(i));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    break;
//                }
                        i++;
                    }


                    try {
                        JSONObject singleP = new JSONObject();
                        singleP.put(KeyCon.project.version.topic.name, name);
                        singleP.put(KeyCon.project.version.topic.status, status);

                        singleP.put(KeyCon.project.version.topic.date, date);

//                cur_porObj.put(String.valueOf(i), singleP);
                        //adding to the main obj
                        verNoJSON.put(String.valueOf(i), singleP);
                        projNoJSON.put(String.valueOf(verNo), verNoJSON);
                        mainObj.put(String.valueOf(projNo), projNoJSON);
                        showAllTopic(verNoJSON);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    alertDialog.dismiss();

                }
            });

            alertDialog.show();


        }
        //this is for datas
        else if (cur_por == 3) {
            //this data will add by text input botttom
        }
//        Toast.makeText(this, ""+mainObj.toString(), Toast.LENGTH_SHORT).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        write();
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {

        switch (cur_por) {
            case 0: {
                //store data to the file back
                write();
                super.onBackPressed();
                break;
            }
            case 1: {
                cur_por = 0;
//                cur_porObj=projNoJSON;
//                try {
//                    mainObj.get(String.valueOf(projNo));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                showAllProjects(mainObj);
                break;
            }
            case 2: {
                cur_por = 1;
//                cur_porObj=verNoJSON;
                showAllVersion(projNoJSON);
//                Toast.makeText(this,topicNo+"",Toast.LENGTH_SHORT).show();
                break;
            }
            case 3: {
                cur_por = 2;
//                cur_porObj=topicNoJSON;
                showAllTopic(verNoJSON);
                addDataCon.setVisibility(View.GONE);
//                Toast.makeText(this,topicNo+"",Toast.LENGTH_SHORT).show();
                break;
            }

        }


    }

    public void addImage(View view) {
        //        verify current position for data only
        if (cur_por == 3) {
            selectImg();
        }
    }

    public void selectImg() {
        //two permission are required for taking image
        //read, write
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);

        } else {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 2);
        }
        //imageNum increased when we get an image


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent dataG) {
        super.onActivityResult(requestCode, resultCode, dataG);

        if (requestCode == 2 && resultCode == RESULT_OK && dataG != null) {
            Uri imageUri = dataG.getData();
//            data.

            //here we compress the Image
            try {

                //this is for orgImg
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                String txt = Base64.encodeToString(bytes, Base64.DEFAULT);


                //here we adding data to data
                //add the new data on i positions
                int i = 0;
                while (!cur_porObj.isNull(String.valueOf(i))) {

//                try {
//
//                    cur_porObj.getJSONObject(String.valueOf(i));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    break;
//                }
                    i++;

                }
                String data = "data", date = "dasdf";

                data = txt;
                data += KeyAddStringg;
                date = String.valueOf(System.currentTimeMillis());

                if (!data.isEmpty()) {
                    try {
                        JSONObject singleP = new JSONObject();
                        singleP.put(KeyCon.project.version.topic.data.data, data);
                        singleP.put(KeyCon.project.version.topic.data.date, date);


                        topicNoJSON.put(String.valueOf(i), singleP);
                        verNoJSON.put(String.valueOf(topicNo), topicNoJSON);
                        projNoJSON.put(String.valueOf(verNo), verNoJSON);
                        mainObj.put(String.valueOf(projNo), projNoJSON);

                        showAllData(topicNoJSON);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


//                imageUri = Base64.decode(txt, Base64.DEFAULT);
//                orgImg = txt;
//            }//
//                //this is for compression
//                Bitmap imageBitmap = SiliCompressor.with(this).getCompressBitmap(String.valueOf(imageUri), false);
//                //this is for converting bitmap into String
//                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), imageBitmap, "val", null);
////                this is for converting String into Uri
//                imageUri = Uri.parse(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

////            store data in
//            userImgF= String.valueOf(imageUri);

            //here we add img to display
//            img.setImageURI(imageUri);

        }

    }


    public void addData(View view) {
//        verify current position for data only
        if (cur_por == 3) {
            //add the new data on i positions
            int i = 0;
            while (!cur_porObj.isNull(String.valueOf(i))) {

//                try {
//
//                    cur_porObj.getJSONObject(String.valueOf(i));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    break;
//                }
                i++;

            }
            String data = "data", date = "dasdf";

            data = inputText.getEditableText().toString();
            inputText.setText("");
            date = String.valueOf(System.currentTimeMillis());

            if (!data.isEmpty()) {
                try {
                    JSONObject singleP = new JSONObject();
                    singleP.put(KeyCon.project.version.topic.data.data, data);
                    singleP.put(KeyCon.project.version.topic.data.date, date);


                    topicNoJSON.put(String.valueOf(i), singleP);
                    verNoJSON.put(String.valueOf(topicNo), topicNoJSON);
                    projNoJSON.put(String.valueOf(verNo), verNoJSON);
                    mainObj.put(String.valueOf(projNo), projNoJSON);

                    showAllData(topicNoJSON);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }

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
        showAllProjects(mainObj);

//
//        try {
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
////        String tagS[] = tamp.split("\n");
//
//
////        ArrayList<String> tags = new ArrayList<>();
////        tags.addAll(Arrays.asList(tagS));
//
//        File d = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
//        File file = new File(d, "AllProject.txt");
//        FileReader write = null;
//        try {
//            write = new FileReader(file);
//            String data= String.valueOf(write.read());
//            try {
//                mainObj= (new JSONObject(data)).getJSONObject("employee");
//                Toast.makeText(this, "Read sucess", Toast.LENGTH_SHORT).show();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            write.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addDataToMainFile(View view) {
        File d = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(d, "AllProjectSecure.txt");
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