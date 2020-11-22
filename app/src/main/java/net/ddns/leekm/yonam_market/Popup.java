package net.ddns.leekm.yonam_market;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class Popup extends Activity {
    TextView textView;
    String type;
    String pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);


    //UI 객체생성
    textView = findViewById(R.id.text);

    //데이터 가져오기
    Intent intent = getIntent();
    String data = intent.getStringExtra("data");
    textView.setText(data);
    type = intent.getStringExtra("type");
    pos = intent.getStringExtra("pos");
    }


    //확인 버튼 클릭
    public void apply(View v){
        //데이터 전달하기
        Intent new_intent = new Intent();
        if(type.equals("mainBoard")){ // 게시물 삭제
            new_intent.putExtra("result", "Delete_Board");
            String url = "http://leekm.ddns.net:8080/yonam-market/market/deleteBoard.jsp";
            String parse_data = null;

            // AsyncTask를 통해 HttpURLConnection 수행.
            ContentValues contentValues = new ContentValues();
            contentValues.put("board_num", pos);

            NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
            try {
                parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
                Log.i("1",parse_data);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            Parse p = new Parse((AppData)getApplication() ,parse_data);
            if(p.getNotice().equals("success")){
                Log.i("게시글 삭제 완료","삭제완료");
            }
        }else if(type.equals("Comment")){ // 댓글 삭제
            new_intent.putExtra("result", "Comment");
            new_intent.putExtra("result", "Delete_Board");
            String url = "http://leekm.ddns.net:8080/yonam-market/market/deleteComment.jsp";
            String parse_data = null;

            // AsyncTask를 통해 HttpURLConnection 수행.
            ContentValues contentValues = new ContentValues();
            contentValues.put("comment_num", pos);

            NetworkTask networkTask = new NetworkTask(this, url, contentValues, (AppData)getApplication());
            try {
                parse_data =  networkTask.execute().get(); // get()함수를 이용해 작업결과를 불러올 수 있음.
                Log.i("1",parse_data);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            Parse p = new Parse((AppData)getApplication() ,parse_data);
            if(p.getNotice().equals("success")){
                Log.i("댓글 삭제 완료","삭제완료");
            }
        }


        setResult(RESULT_OK, new_intent);
        //액티비티(팝업) 닫기
        finish();
    }

    // 취소 버튼 클릭
    public void close(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
