package com.task.taskshopping.api;


import com.task.taskshopping.model.Product;
import com.task.taskshopping.model.Productlist;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by KATHIR on 07-06-2020
 */
public interface Apiinterface {

    @GET("/v2/5def7b172f000063008e0aa2/")
    Call<Productlist> getprouctlist();
    @GET("/v2/5def7b172f000063008e0aa2/")
    Call<Product> getprouct();
}
