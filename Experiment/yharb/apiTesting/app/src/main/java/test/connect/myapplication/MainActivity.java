package test.connect.myapplication;

import static test.connect.myapplication.api.ApiClientFactory.GetPhotoApi;
import static test.connect.myapplication.api.ApiClientFactory.GetPostApi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import test.connect.myapplication.api.SlimCallback;
import test.connect.myapplication.model.Photo;
import test.connect.myapplication.model.Post;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView apiText1 = findViewById(R.id.activity_main_testView1);


        //Gets first photo from remote website and prints the values on display
        
//        GetPhotoApi().getFirstPhoto().enqueue(new SlimCallback<Photo>(responsePhoto -> {
//            apiText1.setText(responsePhoto.printable());
//        }));


    }
}


