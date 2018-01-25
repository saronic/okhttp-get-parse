package fei.li.okhttpget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import fei.li.okhttpget.Pojo.Person;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView mInfoTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfoTV = (TextView) findViewById(R.id.textView);

        Button okGetBtn = (Button) findViewById(R.id.get_btn);
        okGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://192.168.31.139:3000/")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String s = response.body().string();
                            Log.d(TAG, "run: response: " + s);
                            updateUi(s);
                            //parseXmlWithPull(s);
                            //parseXmlWithSAX(s);
                           //parseJsonWithJsonObject(s);
                            //parseJsonWithGson(s);
                            //parseJsonArrayWithGson(s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void updateUi(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInfoTV.setText(mInfoTV.getText() + "\n" + s);
            }
        });
    }

    private void parseJsonArrayWithGson(String s) {
        Gson gson = new Gson();
        List<Person> persons = gson.fromJson(s, new TypeToken<List<Person>>() {
        }.getType());
        for (Person person : persons) {
            Log.d(TAG, "age: " + person.getAge() + " name: " + person.getName());
        }
    }

    private void parseJsonWithGson(String s) {
        Gson gson = new Gson();
        Person person = gson.fromJson(s, Person.class);
        Log.d(TAG, "name: " + person.getName());
        Log.d(TAG, "age: " + person.getAge());
    }


    private void parseJsonWithJsonObject(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            String name = jsonObject.getString("name");
            Log.d(TAG, "name: " + name);
            int age = jsonObject.getInt("age");
            Log.d(TAG, "age: " + age);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /*private void parseJsonWithJsonObject(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            String city = jsonObject.getString("city");
            Log.d(TAG, "city: " + city);
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            for(int i = 0; i< jsonArray.length();i++) {
                JSONObject jsonTemp = jsonArray.getJSONObject(i);
                String date = jsonTemp.getString("date");
                int temp = jsonTemp.getInt("temp");
                Log.d(TAG, "date: " + date + ", temp: " + temp);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/


    private void parseXmlWithSAX(String s) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            ContentHandler handler = new fei.li.okhttpget.xml.ContentHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new StringReader(s)));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void parseXmlWithPull(String s) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(s));
            int eventType = xmlPullParser.getEventType();
            String city = "";
            String wendu = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("city".equals(nodeName)) {
                            city = xmlPullParser.nextText();
                        } else if ("wendu".equals(nodeName)) {
                            wendu = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("resp".equals(nodeName)) {
                            Log.d(TAG, "parseXmlWithPull: city: " + city);
                            Log.d(TAG, "parseXmlWithPull: wendu: " + wendu);
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
