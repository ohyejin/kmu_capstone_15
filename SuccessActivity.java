package ilco.cap.junghee.zorzima;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.Session;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.lang.System.exit;

public class SuccessActivity extends AppCompatActivity {

    final private UnityAdsListener unityAdsListener = new UnityAdsListener();

    TextView now_z1;

    String kakao_email;
    String for_url = "http://ec2-52-24-158-191.us-west-2.compute.amazonaws.com/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list01);


        if (UnityAds.isReady("skip")) {
                UnityAds.show(this, "skip");
            }
        UnityAds.initialize(this, "1406957", unityAdsListener, false);


        /* kakao parsing */
        Intent intent = getIntent();
        String kakao_id = intent.getStringExtra("zkakao");

        String kakao_id_parse = kakao_id.substring(12);
        String[] kakao_send = kakao_id_parse.split(",");
        // kakao_send[0]=nickname. kakao_send[1]=email, kakao_send[4]=profile_image

        String kakao_nickname = (kakao_send[0].substring(9)).replace("'", "");
        kakao_email = (kakao_send[1].substring(7)).replace("'", "");
        String kakao_profile_img = (kakao_send[4].substring(18)).replace("'", "");

        String kakao_send_user = for_url + "login?n=" + kakao_nickname + "&e=" + kakao_email; //카카오 nicknamm
        new HttpAsyncTask().execute(kakao_send_user);


        now_z1 = (TextView) findViewById(R.id.textView_num2);


        ActionBar actionBar = getSupportActionBar();

        //ActionBar가 Tab를 보여줄 수 있는 모양이 되도록 설정
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //ActionBar에 추가 될 Tab 참조변수
        ActionBar.Tab tab = null;

        //첫번째 Tab 객체 생성 및 ActionBar에 추가하기
        tab = actionBar.newTab();
        tab.setText("상품1");
        tab.setTabListener((ActionBar.TabListener) listener);
        actionBar.addTab(tab);

        //두번째 Tab 객체 생성 및 ActionBar에 추가하기
        tab = actionBar.newTab();
        tab.setText("상품2");
        tab.setTabListener((ActionBar.TabListener) listener);
        actionBar.addTab(tab);

        //세번째 Tab 객체 생성 및 ActionBar에 추가하기
        tab = actionBar.newTab();
        tab.setText("상품3");
        tab.setTabListener((ActionBar.TabListener) listener);
        actionBar.addTab(tab);

        getSupportActionBar().setTitle("조르지마");
    }

    //Tab의 선택변화를 인지하는 Listener 객체 생성
    ActionBar.TabListener listener = new ActionBar.TabListener() {


        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            //선택된 Tab객체의 위치값(왼족 처음부터 0,1,2....순으로 됨)
            int position = tab.getPosition();

            switch (position) {
                case 0: //가장 왼쪽 Tab 선택

                    setContentView(R.layout.activity_list01);
                    String t1_2 = for_url + "zorgi?p_number=1";
                    new HttpAsyncTask1().execute(t1_2);

                    break;

                case 1:
                    setContentView(R.layout.activity_list02);

                    String t2_2 = for_url + "zorgi?p_number=2";
                    new HttpAsyncTask1().execute(t2_2);

                    break;

                case 2: //세번째 Tab 선택
                    setContentView(R.layout.activity_list03);

                    String t3_2 = for_url + "zorgi?p_number=3";
                    new HttpAsyncTask1().execute(t3_2);

                    break;

            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 메뉴의 항목을 선택(클릭)했을 때 호출되는 콜백메서드

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_logout:
                Toast.makeText(getApplicationContext(), "로그아웃 성공",
                        Toast.LENGTH_SHORT).show();
                String log_out = for_url + "logout?e=" + kakao_email;
                new HttpAsyncTask4().execute(log_out);
                Intent intent1 = new Intent(SuccessActivity.this, MainActivity.class);
                startActivity(intent1);
                return true;

            case R.id.menu_a:
                Toast.makeText(getApplicationContext(), "마이페이지",
                        Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(SuccessActivity.this, mypage.class);
                intent2.putExtra("zzkakao", kakao_email);
                startActivity(intent2);
                return true;

            case R.id.menu_b:
                Toast.makeText(getApplicationContext(), "꼭 읽어주세요!",
                        Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(SuccessActivity.this, tutorial.class);
                startActivity(intent3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ad_Click1(View view) {
        if (UnityAds.isReady("rewardedVideo")) {
            UnityAds.show(this, "rewardedVideo");
        }
        UnityAds.initialize(this, "1406957", unityAdsListener, false);

        String test = kakao_email;

        String p1_1 = for_url + "btn?p_number=1&e=" + test;
        new HttpAsyncTask3().execute(p1_1);

    }

    public void ad_Click2(View view) {
        if (UnityAds.isReady("rewardedVideo")) {
            UnityAds.show(this, "rewardedVideo");
        }
        UnityAds.initialize(this, "1406957", unityAdsListener, false);

        String test = kakao_email;

        String p2_1 = for_url + "btn?p_number=2&e=" + test;
        new HttpAsyncTask3().execute(p2_1);
    }

    public void ad_Click3(View view) {
        if (UnityAds.isReady("rewardedVideo")) {
            UnityAds.show(this, "rewardedVideo");
        }
        UnityAds.initialize(this, "1406957", unityAdsListener, false);

        String test = kakao_email;

        String p3_1 = for_url + "btn?p_number=3&e=" + test;
        new HttpAsyncTask3().execute(p3_1);
    }

    public class UnityAdsListener implements IUnityAdsListener {

        @Override
        public void onUnityAdsReady(String s) {

            final Button mButton = (Button) findViewById(R.id.ad_button);
            //mButton.setText("show ads");
        }

        @Override
        public void onUnityAdsStart(String s) {

        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {

        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {

        }
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_SHORT).show();

        }
    }

    private class HttpAsyncTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            /*now_z1.setText(result);*/
            String z_toast = "현재 " + result + "명이 조르기중!";
            Toast.makeText(getApplicationContext(), z_toast,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class HttpAsyncTask3 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class HttpAsyncTask4 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        }
    }


};
