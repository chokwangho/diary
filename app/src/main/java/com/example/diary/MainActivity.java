package com.example.diary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class MainActivity extends Activity {
   //View
    TextView mText;
    TextView mPickDate;
    Button saveBtn;
    EditText editDiary;
    String FileName;
    DatePicker datepicker;
    private int mYear;
    private int mMonth;
    private int mDay;


    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //날짜
            mPickDate =(TextView)findViewById(R.id.date);
            mPickDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Dialog datepicker = new DatePickerDialog(MainActivity.this, dateSetListener,mYear,mMonth,mDay);
                    datepicker.show();
                }
            });

            editDiary = (EditText) findViewById(R.id.edit);
            saveBtn = (Button) findViewById(R.id.save);
        //sd카드 경로
        final String SDpath= Environment.getExternalStorageDirectory().getAbsolutePath();
        final File myDiary = new File(SDpath +"/myDiary");
        final String path= "mnt/sdcard/myDiary/";
        // 현재날짜
            Calendar cal = Calendar.getInstance();
             mYear = cal.get(Calendar.YEAR);
             mMonth = cal.get(Calendar.MONTH);
             mDay = cal.get(Calendar.DAY_OF_MONTH);
            int year = mYear;
            int monthOfYear = mMonth;
            int dayOfMonth = mDay;
            updateDisplay(); //다이얼로그 보여주기

            FileName = Integer.toString(year) + "_"
                    + Integer.toString(monthOfYear + 1) + "_" + Integer.toString(dayOfMonth) + ".txt";
            String str = readDiary(FileName);
            editDiary.setText(str);
            saveBtn.setEnabled(true);


        // mydiary폴더 만들기 , 그리고  경로에다가 저장하기
            saveBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {if(!myDiary.exists()){
                        myDiary.mkdir();
                    }
                        final File file = new File(path+FileName);
                        FileOutputStream outFs = new FileOutputStream(file);
                        //FileOutputStream outFs =
                        //        openFileOutput(FileName, Context.MODE_WORLD_WRITEABLE);
                        String str = editDiary.getText().toString();
                        outFs.write(str.getBytes());
                        outFs.flush();
                        outFs.close();
                        Toast.makeText(getApplicationContext(),
                                FileName + "이 저장됨", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                    }
                }
            });
        }
    String readDiary(String fName) //일기 읽어오기
    {
        String diaryStr = null;
        FileInputStream inFs;
        try{
            inFs = openFileInput(fName);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
            saveBtn.setText("수정하기");
        } catch(IOException e){
            editDiary.setHint("일기 없음");
            saveBtn.setText("저장");
        }
        return diaryStr;
    }
    private void updateDisplay(){
        mText.setText(new StringBuilder().append(mYear).append(",").append(mMonth+1).append(",").append(mDay));
    }
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            mYear=year;
            mMonth=monthOfYear;
            mDay=dayOfMonth;
            updateDisplay();
           }
    };

    /*public void mOnClick(View v){ //날짜
                Refresh();
                      }
*/



    /*void Refresh(){ //날짜
        StringBuilder time = new StringBuilder();
        Calendar cal= new GregorianCalendar();
        time.append(String.format("%d년 %d월 %d일\n",cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH)));
        TextView date= (TextView)findViewById(R.id.date);
        date.setText(time.toString());
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
