package com.example.fogonstreet.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fogonstreet.data.PostClient;
import com.example.fogonstreet.model.update.Update;
import com.example.fogonstreet.model.update.UpdateResponse;

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

   MutableLiveData<Update> update1 = new MutableLiveData<>();
   MutableLiveData<List<UpdateResponse>> putMutableLiveData = new MutableLiveData<>();
   MutableLiveData<String> token= new MutableLiveData<>();
   MutableLiveData<Boolean> send = new MutableLiveData<Boolean>();

    //double latitude,longtiude;
    public void CallUpdate(){
        PostClient.getINSTANCE().CallUpdate(token.getValue(),update1.getValue()).enqueue(new Callback<List<UpdateResponse>>() {
            @Override
            public void onResponse(Call<List<UpdateResponse>> call, Response<List<UpdateResponse> >response) {
                Log.d("ReSponseAcceptCode",String.valueOf(response.code()));

            }

            @Override
            public void onFailure(Call<List<UpdateResponse>> call, Throwable t) {
                Log.d("ReSponseFail", String.valueOf(t.getMessage()));
            }
        });
    }
public void putUpdate(){
    Log.d("putUpdate","putUpdateExcute");
       Observable observable = Observable.interval(5,TimeUnit.SECONDS)
               .flatMap(n->PostClient.getINSTANCE().putUpdate(token.getValue(),update1.getValue()))
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread());
       io.reactivex.Observer<List<UpdateResponse>> observer = new io.reactivex.Observer<List<UpdateResponse>>(){

           @Override
           public void onSubscribe(Disposable d) {

           }

           @Override
           public void onNext(List<UpdateResponse> updateResponses) {
             putMutableLiveData.setValue(updateResponses);
            // Log.d("UpdateOnNext","updateOccur");
           }

           @Override
           public void onError(Throwable e) {
            Log.d("ErrorUpdated",e.getMessage().toString());
           }

           @Override
           public void onComplete() {
          Log.d("putUpdate","onComplete");
           }
       };
    observable.subscribe(observer);
}

//set Post1 of update
public void setValueUpdate(Update update){
        update1.setValue(update);
}
public void setToken(String Token){
    token.setValue(Token);
}
public MutableLiveData<List<UpdateResponse>> getPutMutableLiveData(){
    if(putMutableLiveData==null){
        putMutableLiveData = new MutableLiveData<List<UpdateResponse>>();
    }
    return putMutableLiveData;
}
public void setValueSend(boolean value){
        send.setValue(value);
}
public MutableLiveData<Boolean> getValueSend(){
        return send;
}
}
