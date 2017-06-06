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

public class mypage extends AppCompatActivity {

    String kakao_email_z;
    String for_url_z = "http://ec2-52-24-158-191.us-west-2.compute.amazonaws.com/";
    TextView mypage_z;
    TextView mypage_crown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        mypage_z = (TextView) findViewById(R.id.textView_num);
        mypage_crown = (TextView) findViewById(R.id.textView_mypage);

        Intent intent = getIntent();
        kakao_email_z = intent.getStringExtra("zzkakao");

        String mypage_1 = for_url_z + "chance?e=" + kakao_email_z;
        String mypage_2 = for_url_z + "winlist";
        new HttpAsyncTask1().execute(mypage_1);
        new HttpAsyncTask2().execute(mypage_2);

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public class HttpAsyncTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            /*Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_SHORT).show();*/
            String remain_z = "남은 조르기 횟수 : " + result ;
            mypage_z.setText(remain_z);
        }
    }

    public class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            /*Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_SHORT).show();*/

            String result_z = result;
            String[] mypage_m = result_z.split("\\}");
            // mypage_m[0]=명예의전당 1번상품 당첨자

            String[] crown_1 = mypage_m[0].split(",");
            String crown_z1 = (crown_1[0].substring(11)).replace("\"", "");
            String n_crown_z1 = crown_z1.substring(3);
            String p_num1 = crown_1[1].substring(12);

            String[] crown_2 = mypage_m[1].split(",");
            String crown_z2 = (crown_2[1].substring(10)).replace("\"", "");
            String n_crown_z2 = crown_z2.substring(3);
            String p_num2 = crown_2[2].substring(12);

            String[] crown_3 = mypage_m[2].split(",");
            String crown_z3 = (crown_3[1].substring(10)).replace("\"", "");
            String n_crown_z3 = crown_z3.substring(3);
            String p_num3 = crown_3[2].substring(12);

            String final_z = p_num1 + "번 당첨 : ***" + n_crown_z1 + "님\n" +
                    p_num2 + "번 당첨 : ***" + n_crown_z2 + "님\n" +
                    p_num3 + "번 당첨 : ***" + n_crown_z3 + "님\n";
            mypage_crown.setText(final_z);
        }
    }


};