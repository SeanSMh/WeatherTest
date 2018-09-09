package com.example.sean.weathertest_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private EditText city;
    private Button query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        query = (Button) findViewById(R.id.query);  //获取布局控件
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        city = (EditText) findViewById(R.id.city);

        query.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.query){
           sendRequestWithOkHttp();
        }
    }

    public void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String city_1 = city.getText().toString();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://apicloud.mob.com/v1/weather/query?key=27bdcfa312de0&city=" + city_1 +"&province=河南")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(final String jsonData) {
        Gson gson = new Gson();
        JavaRootBean javaRootBean =gson.fromJson(jsonData,JavaRootBean.class);
        List<Result> listResult = javaRootBean.getResult();
        for (int i= 0; i < listResult.size(); i++) {
            final String data = listResult.get(i).getDate();
            final String airCondition = listResult.get(i).getAirCondition();
            final String coldIndex = listResult.get(i).getColdIndex();
            final String tem = listResult.get(i).getTemperature();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //List<JavaRootBean> javaRootBeanList = gson.fromJson(jsonData,new TypeToken<List<JavaRootBean>>(){}.getType());
                    text1.setText(data);
                    text2.setText(airCondition);
                    text3.setText(coldIndex);
                    text4.setText(tem);
                   /* text2.setText(javaRootBean.getResult().getCity());
                    text3.setText(javaRootBean.getResult().getAirCondition());
                    text4.setText(javaRootBean.getResult().getColdIndex());*/

                }
            });
        }
    }
}
