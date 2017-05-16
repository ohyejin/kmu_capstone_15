package com.example.junghee.zorzima;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;


public class SuccessActivity extends AppCompatActivity {
    //  Toolbar toolbar;
    final private UnityAdsListener unityAdsListener = new UnityAdsListener();

    boolean bLog = false; // false : 로그아웃 상태

    EditText etResponse;
    TextView tvIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list01);

        //Activity 객체는 이미 ActionBar를 가지고 있으므로

        //이미 존해하는 ActionBar 객체를 얻어오기.(API 10버전 이상에서 사용가능)

        ActionBar actionBar = getSupportActionBar();


        //ActionBar가 Tab를 보여줄 수 있는 모양이 되도록 설정

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        //ActionBar에 추가 될 Tab 참조변수

        ActionBar.Tab tab = null;


        //첫번째 Tab 객체 생성 및 ActionBar에 추가하기

        tab = actionBar.newTab(); //ActionBar에 붇는 Tab객체 생성

        tab.setText("상품1");    //Tab에 보여지는 글씨

        //Tab의 선택이 변경되는 것을 인지하는 TabListener 설정(아래쪽 객체 생성 코드 참고)

        tab.setTabListener((ActionBar.TabListener) listener);

        //ActionBar에 Tab 추가

        actionBar.addTab(tab);


        //두번째 Tab 객체 생성 및 ActionBar에 추가하기

        tab = actionBar.newTab();//ActionBar에 붇는 Tab객체 생성

        tab.setText("상품2");   //Tab에 보여지는 글씨

        //Tab의 선택이 변경되는 것을 인지하는 TabListener 설정(아래쪽 객체 생성 코드 참고)

        tab.setTabListener((ActionBar.TabListener) listener);

        //ActionBar에 Tab 추가

        actionBar.addTab(tab);


        //세번째 Tab 객체 생성 및 ActionBar에 추가하기

        tab = actionBar.newTab(); //ActionBar에 붇는 Tab객체 생성

        tab.setText("상품3");   //Tab에 보여지는 글씨

        //Tab의 선택이 변경되는 것을 인지하는 TabListener 설정(아래쪽 객체 생성 코드 참고)

        tab.setTabListener((ActionBar.TabListener) listener);

        //ActionBar에 Tab 추가

        actionBar.addTab(tab);


        tab = actionBar.newTab(); //ActionBar에 붇는 Tab객체 생성

        tab.setText("상품4");    //Tab에 보여지는 글씨

        //Tab의 선택이 변경되는 것을 인지하는 TabListener 설정(아래쪽 객체 생성 코드 참고)

        tab.setTabListener((ActionBar.TabListener) listener);

        //ActionBar에 Tab 추가

        actionBar.addTab(tab);





        //  toolbar = (Toolbar) findViewById(R.id.my_toolbar); //툴바설정
        //    toolbar.setTitleTextColor(Color.parseColor("#000000"));
        //  setSupportActionBar(toolbar);//액션바와 같게 만들어줌
        getSupportActionBar().setTitle("조르지마");
    }

    //Tab의 선택변화를 인지하는 Listener 객체 생성

    //(Button의 onClickListner 처럼 생각하시면 됩니다.)

    ActionBar.TabListener listener = new ActionBar.TabListener() {


        //Tab의 선택이 벗어날 때 호출

        //첫번째 파라미터 : 선택에서 벗어나는 Tab 객체

        //두번째 파라미터 : Tab에 해당하는 View를 Fragment로 만들때 사용하는 트랜젝션.(여기서는 사용X)

        @Override

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            // TODO Auto-generated method stub


        }


        //Tab이 선택될 때 호출

        //첫번째 파라미터 : 선택된 Tab 객체

        //두번째 파라미터 : Tab에 해당하는 View를 Fragment로 만들때 사용하는 트랜젝션.(여기서는 사용X)

        @Override

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

            // TODO Auto-generated method stub


            //선택된 Tab객체의 위치값(왼족 처음부터 0,1,2....순으로 됨)

            int position = tab.getPosition();


            switch (position) {

                case 0: //가장 왼쪽 Tab 선택(Analog)


                    //MainActivity가 보여 줄 View를

                    //res폴더>>layout폴더>>layout_tab_0.xml 로 설정

                    setContentView(R.layout.activity_list01);

                    break;


                case 1: //두번째 Tab 선택(Digital)


                    //MainActivity가 보여 줄 View를

                    //res폴더>>layout폴더>>layout_tab_1.xml 로 설정

                    setContentView(R.layout.activity_list02);

                    break;


                case 2: //세번째 Tab 선택(Calendar)


                    //MainActivity가 보여 줄 View를

                    //res폴더>>layout폴더>>layout_tab_2.xml 로 설정

                    setContentView(R.layout.activity_list03);

                    break;

                case 3:
                    setContentView(R.layout.activity_list04);
                    break;

            }


        }


        //Tab이 재 선택될 때 호출

        //첫번째 파라미터 : 재 선택된 Tab 객체

        //두번째 파라미터 : Tab에 해당하는 View를 Fragment로 만들때 사용하는 트랜젝션.(여기서는 사용X)

        @Override

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            // TODO Auto-generated method stub

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d("test", "onCreateOptionsMenu - 최초 메뉴키를 눌렀을 때 호출됨");
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("test", "onPrepareOptionsMenu - 옵션메뉴가 " +
                "화면에 보여질때 마다 호출됨");
        if (bLog) { // 로그인 한 상태: 로그인은 안보이게, 로그아웃은 보이게
            menu.getItem(0).setEnabled(true);
            menu.getItem(1).setEnabled(false);
        } else { // 로그 아웃 한 상태 : 로그인 보이게, 로그아웃은 안보이게
            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(true);
        }

        bLog = !bLog;   // 값을 반대로 바꿈

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 메뉴의 항목을 선택(클릭)했을 때 호출되는 콜백메서드
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("test", "onOptionsItemSelected - 메뉴항목을 클릭했을 때 호출됨");

        int id = item.getItemId();


        switch (id) {
            case R.id.menu_login:
                Toast.makeText(getApplicationContext(), "로그인 메뉴 클릭",
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_logout:
                Toast.makeText(getApplicationContext(), "로그아웃 메뉴 클릭",
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_a:
                Toast.makeText(getApplicationContext(), "마이페이지",
                        Toast.LENGTH_SHORT).show();
                setContentView(R.layout.mypage);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }






    public void ad_Click(View view) {
        if (UnityAds.isReady("rewardedVideo")) {
            UnityAds.show(this, "rewardedVideo");
        }
        UnityAds.initialize(this, "1406957", unityAdsListener, false);
    }

    private class UnityAdsListener implements IUnityAdsListener {

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

};
