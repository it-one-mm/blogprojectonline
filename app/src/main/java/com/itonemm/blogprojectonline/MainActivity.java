package com.itonemm.blogprojectonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView list=findViewById(R.id.news);
        String url="https://newsapi.org/v2/top-headlines?country=us&apiKey=165cb5e082f34bc98b730a7aae8cbc4c";

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request=new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{

                            ArrayList<NewsModel> newsModels=new ArrayList<NewsModel>();
                            JSONArray myarticles=response.getJSONArray("articles");
                            for(int i=0;i<myarticles.length();i++)
                            {

                                JSONObject obj=myarticles.getJSONObject(i);
                                NewsModel model=new NewsModel();
                                model.author=obj.getString("author");
                                model.imageUrl=obj.getString("urlToImage");
                                model.title=obj.getString("title");
                                model.newsUrl=obj.getString("url");
                                model.publishedDate=obj.getString("publishedAt");
                                newsModels.add(model);
                            }


                            NewsAdpater adpater=new NewsAdpater(newsModels,getApplicationContext(),getParent());

                            list.setAdapter(adpater);
                            LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                            list.setLayoutManager(lm);
                            progressBar.setVisibility(View.GONE);
                            list.setVisibility(View.VISIBLE);
                        }
                        catch (Exception ex)
                        {
                            Log.e("Error", ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        queue.add(request);


    }
}
