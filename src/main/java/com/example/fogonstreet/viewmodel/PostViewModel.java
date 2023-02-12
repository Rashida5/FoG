package com.example.fogonstreet.viewmodel;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fogonstreet.data.PostClient;
import com.example.fogonstreet.model.Post;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewModel extends ViewModel  {
    MutableLiveData<List<Post>> postsMutableLiveData= new MutableLiveData<>();
    MutableLiveData<String> posts=new MutableLiveData<>();
    MutableLiveData<String> email=new MutableLiveData<>();
    LiveData<String> _email=email;
    MutableLiveData<Post> post1= new MutableLiveData<>();
    MutableLiveData<List<Post>> patchMutableLiveData= new MutableLiveData<>();
    double latitude,longtiude;

    public void getPosts(){
        PostClient.getINSTANCE().getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("msg", String.valueOf(response.body()));
                postsMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                posts.setValue("errr");
            }
        });
    }
    public void setEmail(String Email){
        email.setValue("car33@gmail.com");
    }

    public void DeleteMessage(String email){
        Observable observable=PostClient.getINSTANCE().deletePost(email)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
        Observer <Post> observer=new Observer<Post>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Post post) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void getNearby(String str){
      /*  Observable observable=PostClient.getINSTANCE().getNearby()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());*/
        Observable observable=Observable.interval(10, TimeUnit.SECONDS)
                .flatMap(n->PostClient.getINSTANCE().getNearby(str))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observer<List<Post>> observer = new Observer<List<Post>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<Post> posts) {
                postsMutableLiveData.setValue(posts);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("Msg_Error","onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);

    }

    public void patchPost() {
        Observable observable=Observable.interval(5, TimeUnit.SECONDS)
                .flatMap(n-> PostClient.getINSTANCE().patchPost(post1.getValue().getEmail(), post1.getValue()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        io.reactivex.Observer<List<Post>> observer = new io.reactivex.Observer<List<Post>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<Post> posts) {
                patchMutableLiveData.setValue(posts);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("Msg_Error_No_Modify","onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
    }
public void setValuePost(Post post){
        post1.setValue(post);
}
}
