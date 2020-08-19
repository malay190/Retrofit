package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.texView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getPost();
        //getComments();
    }

    private void getPost(){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");

       // Call<List<Post>> call = jsonPlaceHolderApi.getPosts(4, "id", "desc");
       // Call<List<Post>> call = jsonPlaceHolderApi.getPosts(2,4, "id", "desc");
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,3,4}, "id", "desc");
        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);

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

    private void getComments(){

        Call<List<Comment>> call = jsonPlaceHolderApi.getComments(3);
        //Call<List<Comment>> call = jsonPlaceHolderApi.getComments(posts/3/comments); for @url


        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){
                    textView.setText("Code:" + response.code());
                    return;
                }

                List<Comment> comments = response.body();

                for(Comment comment: comments){
                    String content = "postId: " + comment.getPostId() + "\n" +
                            "id: " + comment.getId() + "\n" +
                            "name: " + comment.getName() + "\n" +
                            "email: " + comment.getEmail() + "\n" +
                            "body:" + comment.getText();
                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textView.setText(t.getMessage());

            }
        });


    }
}
