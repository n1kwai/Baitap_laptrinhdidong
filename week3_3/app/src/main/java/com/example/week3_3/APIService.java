package com.example.week3_3;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import com.example.week3_3.Category;

public interface APIService {
    @GET("categories.php")
    Call<List<Category>> getCategoryAll();
}