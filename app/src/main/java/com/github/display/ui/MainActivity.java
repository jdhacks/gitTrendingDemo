package com.github.display.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.room.Room;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.github.display.R;
import com.github.display.Utility.Constants;
import com.github.display.Utility.Utility;
import com.github.display.adapters.GitRepoAdapter;
import com.github.display.databinding.ActivityMainBinding;
import com.github.display.models.GithubSearch;
import com.github.display.models.Item;
import com.github.display.retrofitservice.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    GitRepoAdapter gitRepAdapter;
    public static ArrayList<Item> repolist;
    public static ArrayList<Item> repoSearchlist;
    Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
         View v= binding.getRoot();
         setContentView(v);


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!s.isEmpty() && s!=null) {
                    findUserByName(s);
                }else{
                    gitRepAdapter = new GitRepoAdapter(repolist, MainActivity.this);
                    binding.rvRepos.setHasFixedSize(true);
                    binding.rvRepos.setAdapter(gitRepAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.isEmpty() && s!=null) {
                    findUserByName(s);
                }else{
                    gitRepAdapter = new GitRepoAdapter(repolist, MainActivity.this);
                    binding.rvRepos.setHasFixedSize(true);
                    binding.rvRepos.setAdapter(gitRepAdapter);
                }
                return false;
            }
        });
        if(repolist!=null && repolist.size()>0){
            gitRepAdapter = new GitRepoAdapter(repolist, MainActivity.this);
            binding.rvRepos.setHasFixedSize(true);
            binding.rvRepos.setAdapter(gitRepAdapter);
        }else {
            getReposList();
        }
    }

    public void findUserByName(String name) {
        repoSearchlist = new ArrayList<>();
        // go through list of members and compare name with given name
        for (Item user : repolist) {
            if (user.getOwner().getLogin().contains(name)) {
                repoSearchlist.add(user); // return member when name found
            }
        }
       // return repoSearchlist; // return null when no member with given name could be found
        if(repoSearchlist!=null && repoSearchlist.size()>0){
            gitRepAdapter = new GitRepoAdapter(repoSearchlist, MainActivity.this);
            binding.rvRepos.setHasFixedSize(true);
            binding.rvRepos.setAdapter(gitRepAdapter);
        }else{
            gitRepAdapter = new GitRepoAdapter(repolist, MainActivity.this);
            binding.rvRepos.setHasFixedSize(true);
            binding.rvRepos.setAdapter(gitRepAdapter);
        }
    }


    //new oneshot  AWS
    private void getReposList() {
        String datem = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateOutput = format.format(date);
        if (binding.shimmer.getVisibility() == View.GONE) {
            binding.shimmer.startShimmer();
            binding.shimmer.setVisibility(View.VISIBLE);
            //Utility.showProgress(getActivity());
            binding.rvRepos.setVisibility(View.GONE);
        }
        //  Utility.showProgress(this);
        // https://api.github.com/search/repositories?q=created:">2022-09-18"&sort:updated&order=desc&sort:stars&order=desc&since=weekly
        String Query="created:'>"+dateOutput+"'&sort:updated&order=desc&sort:stars&order=desc&since=weekly";
        Call<GithubSearch> call = RetrofitClient.getInstance(Constants.BASE_URL).getMyApi().GetReposSearchList();
        call.request().url();
        call.enqueue(new Callback<GithubSearch>() {
            @Override
            public void onResponse(@NonNull Call<GithubSearch> call, @NonNull Response<GithubSearch> response) {
                Log.d("onResponse", response.code() + " " + response.body());
                Log.d("onResponseoneshot", "onResponseoneshot" + response);
                Log.d("okhttp", "oneshot: response" + response.body());

               // Utility.cancelProgress();
               if(response.body()!=null &&  response.body().getItems()!=null && response.body().getItems().size()>0) {
                   Log.d("okhttp", "oneshot: response" + response.body().getItems().size());
                   repolist= new ArrayList<>();
                   repolist.addAll(response.body().getItems());
                   gitRepAdapter = new GitRepoAdapter(repolist, MainActivity.this);
                   binding.rvRepos.setHasFixedSize(true);
                   binding.rvRepos.setAdapter(gitRepAdapter);
                   binding.shimmer.stopShimmer();
                   binding.shimmer.setVisibility(View.GONE);
                   binding.rvRepos.setVisibility(View.VISIBLE);

                   //  userDao.insertAll(repolist);
                   //   gitRepAdapter.setProductsListByCategories(response.body().getItems());
                }else{
                   binding.shimmer.stopShimmer();
                   binding.shimmer.setVisibility(View.GONE);
                   binding.rvRepos.setVisibility(View.VISIBLE);

               }
            }

            @Override
            public void onFailure(@NonNull Call<GithubSearch> call, @NonNull Throwable t) {
                Log.d("onResponseoneshot", "onResponseoneshotfail");
                if (!Utility.isNetworkAvailable(getApplicationContext())) {
                    Utility.nointernetAlertDialog(MainActivity.this, getString(R.string.no_internet), false, () -> {

                    });
                } else {
                    Utility.alertDialog(MainActivity.this, t.getMessage(), false);

                }
                binding.shimmer.stopShimmer();
                binding.shimmer.setVisibility(View.GONE);

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        state = binding.rvRepos.getLayoutManager().onSaveInstanceState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.rvRepos.getLayoutManager().onRestoreInstanceState(state);
    }
}