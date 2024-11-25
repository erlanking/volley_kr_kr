package com.example.volley_kr;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Button button;
    RecyclerView recyclerView;
    PostAdapter adapter;
    List<Post> posts = new ArrayList<>();

    private static final String TAG = "taggg";
    private String url = "https://jsonplaceholder.typicode.com/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PostAdapter(posts, new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                // Показать детали элемента
                showDetails(post);
            }
        });
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        posts.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postJson = response.getJSONObject(i);
                                Post post = new Post(postJson.getInt("id"), postJson.getString("title"), postJson.getString("body"));
                                posts.add(post);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            Log.e(TAG, "Error parsing data: " + ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void showDetails(Post post) {
        String details = "ID: " + post.getId() + "\nTitle: " + post.getTitle() + "\nBody: " + post.getBody();
        new AlertDialog.Builder(this)
                .setTitle("Post Details")
                .setMessage(details)
                .setPositiveButton("OK", null)
                .show();
    }
}
