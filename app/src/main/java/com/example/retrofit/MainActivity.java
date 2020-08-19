package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.texView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        //Asynchronously send the request and notify callback of its response or
        // if an error occurred talking to the server, creating the request, or processing the response.
        call.enqueue(new Callback<List<Post>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()){
                    textView.setText("Code:" + response.code());// HTTP status code.
                    return;
                }

                List<Post> posts = response.body(); //The deserialize response body of a successful response.

                for( Post post:posts){

                    String content = "Id: " + post.getId() + "\n" +
                            "User Id: " + post.getUserId() + "\n" +
                            "Title: " + post.getTitle() + "\n" +
                            "Body: " + post.getText() + "\n";
                    textView.append(content);
                }


            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textView.setText(t.getMessage());
                //Returns the detail message string of this throwable.

            }
        });
    }
}
