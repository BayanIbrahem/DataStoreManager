package com.example.android.studio.datastoremanager;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class DataStoreManager{
  private static RxDataStore<Preferences> dataStore;
  private static DataStoreManager instance;
  private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  
  private DataStoreManager(RxDataStore<Preferences> dataStore){
    this.dataStore = dataStore;
  }
  public static DataStoreManager getInstance(Application application, final String DATASTORE_TAG){
    if(instance == null){
      instance = new DataStoreManager(
          new RxPreferenceDataStoreBuilder(
              application.getApplicationContext(),
              DATASTORE_TAG
          ).build()
      );
    }
    return instance;
  }
  
  public int readInteger(final String KEY_NAME, final int DEFAULT_VALUE){
    final Preferences.Key<Integer> INT_KEY = PreferencesKeys.intKey(KEY_NAME);
    Flowable<Integer> flowable = dataStore.data().map((preferences -> {
      Integer value = preferences.get(INT_KEY);
      return (value==null)?DEFAULT_VALUE:value;
    }));
    int value = flowable.blockingFirst();
    return value;
  }
  public boolean readBoolean(final String KEY_NAME, final boolean DEFAULT_VALUE){
    final Preferences.Key<Boolean> BOOLEAN_KEY = PreferencesKeys.booleanKey(KEY_NAME);
    Flowable<Boolean> flowable = dataStore.data().map((preferences -> {
      Boolean value = preferences.get(BOOLEAN_KEY);
      return (value==null)?DEFAULT_VALUE:value;
    }));
    boolean value = flowable.blockingFirst();
    return value;
  }
  public double readDouble(final String KEY_NAME, final double DEFAULT_VALUE){
    final Preferences.Key<Double> DOUBLE_KEY = PreferencesKeys.doubleKey(KEY_NAME);
    Flowable<Double> flowable = dataStore.data().map((preferences -> {
      Double value = preferences.get(DOUBLE_KEY);
      return (value==null)?DEFAULT_VALUE:value;
    }));
    double value = flowable.blockingFirst();
    return value;
  }
  public String readString(final String KEY_NAME, final String DEFAULT_VALUE){
    final Preferences.Key<String> STRING_KEY = PreferencesKeys.stringKey(KEY_NAME);
    Flowable<String> flowable = dataStore.data().map((preferences -> {
      String value = preferences.get(STRING_KEY);
      return (value==null)?DEFAULT_VALUE:value;
    }));
    String value = flowable.blockingFirst();
    return value;
  }
  
  public void writeInteger(String INT_KEY, Integer int_value){
    executorService.submit(()-> writingIntegerSteps(INT_KEY, int_value));
  }
  private void writingIntegerSteps(String INT_KEY, Integer int_value){
    Preferences.Key<Integer> key = PreferencesKeys.intKey(INT_KEY);
    Single<Preferences> updateResult = dataStore.updateDataAsync(inputPreferences->{
      MutablePreferences mutablePreferences = inputPreferences.toMutablePreferences();
      mutablePreferences.set(key, int_value);//TODO: set default value
      return Single.just(mutablePreferences);
    });
    updateResult.subscribe();
  }
  
  public void writeBoolean(String BOOL_KEY, Boolean bool_value){
    executorService.submit(()-> writingBooleanSteps(BOOL_KEY, bool_value));
  }
  private void writingBooleanSteps(String BOOL_KEY, Boolean bool_value){
    Preferences.Key<Boolean> key = PreferencesKeys.booleanKey(BOOL_KEY);
    Single<Preferences> updateResult = dataStore.updateDataAsync(inputPreferences->{
      MutablePreferences mutablePreferences = inputPreferences.toMutablePreferences();
      mutablePreferences.set(key, bool_value);//TODO: set default value
      return Single.just(mutablePreferences);
    });
    updateResult.subscribe();
  }
  
  public void writeDouble(String DOUBLE_KEY, Double double_value){
    executorService.submit(()-> writingDoubleSteps(DOUBLE_KEY, double_value));
  }
  private void writingDoubleSteps(String INT_KEY, Double double_value){
    Preferences.Key<Double> key = PreferencesKeys.doubleKey(INT_KEY);
    Single<Preferences> updateResult = dataStore.updateDataAsync(inputPreferences->{
      MutablePreferences mutablePreferences = inputPreferences.toMutablePreferences();
      mutablePreferences.set(key, double_value);//TODO: set default value
      return Single.just(mutablePreferences);
    });
    updateResult.subscribe();
  }
  
  public void writeString(String STRING_KEY, String string_value){
    executorService.submit(()-> writingStringSteps(STRING_KEY, string_value));
  }
  private void writingStringSteps(String STRING_KEY, String string_value){
    Preferences.Key<String> key = PreferencesKeys.stringKey(STRING_KEY);
    Single<Preferences> updateResult = dataStore.updateDataAsync(inputPreferences->{
      MutablePreferences mutablePreferences = inputPreferences.toMutablePreferences();
      mutablePreferences.set(key, string_value);//TODO: set default value
      return Single.just(mutablePreferences);
    });
    updateResult.subscribe();
  }
  @Override
  protected void finalize() throws Throwable{
    if(executorService != null && !executorService.isShutdown()){
      executorService.shutdown();
    }
    super.finalize();
  }
}

