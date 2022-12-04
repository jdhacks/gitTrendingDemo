package com.github.display.retrofitservice;


import com.github.display.models.GithubPublicRepos;
import com.github.display.models.GithubSearch;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("search/repositories?q=created:\">2022-09-18\"&sort:updated&order=desc&sort:stars&order=desc&since=daily")
    Call<GithubSearch> GetReposSearchList();

    @GET("repositories")
    Call<ArrayList<GithubPublicRepos>> GetReposList();

  }