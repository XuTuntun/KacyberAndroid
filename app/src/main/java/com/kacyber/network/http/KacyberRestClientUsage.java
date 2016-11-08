package com.kacyber.network.http;

import android.app.usage.ConfigurationStats;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
//import com.facebook.login.LoginBehavior;
import com.kacyber.Utils.Constants;
import com.kacyber.Utils.PackageInfoUtil;
import com.kacyber.Utils.Util;
import com.kacyber.adapter.SortModelCities;
import com.kacyber.event.AllCategoryEvent;
import com.kacyber.event.AllCitiesEvent;
import com.kacyber.event.CategoryMerchantListEvent;
import com.kacyber.event.ConversationListEvent;
import com.kacyber.event.DealMerchantListEvent;
import com.kacyber.event.FriendSearchResult;
import com.kacyber.event.JMessage;
import com.kacyber.event.LoginSuccessEvent;
import com.kacyber.event.MainMerchantListEvent;
import com.kacyber.event.MerchantDetailEvent;
import com.kacyber.event.SendMessageSuccess;
import com.kacyber.model.Category;
import com.kacyber.model.Contact;
import com.kacyber.model.Conversation;
import com.kacyber.model.Deal;
import com.kacyber.model.EventItem;
import com.kacyber.model.DealMerchant;
import com.kacyber.model.LoginSuccessModel;
import com.kacyber.model.MerchantDetail;
import com.kacyber.model.TrendingItem;
import com.kacyber.model.User;
import com.kacyber.model.UserProfile;
import com.kacyber.network.service.NetStatus;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class KacyberRestClientUsage {

    public static String TAG = KacyberRestClientUsage.class.getName();

    private static KacyberRestClientUsage singleton;

    private KacyberRestClientUsage() {

//        musicRequestCallback = RunsicService.getInstance();
    }

    public synchronized static KacyberRestClientUsage getInstance() {
        if (singleton == null)
            singleton = new KacyberRestClientUsage();
        return singleton;
    }

    public void setTokenHeader(String token) {
        KacyberRestClient.setTokenHeader(token);
    }

    public void setAppkeyHeader() {
        KacyberRestClient.setAppkeyHeader();
    }

    public void getDiscoverMainPage() {
        setAppkeyHeader();

        Log.e(TAG, "get Discover Main Page");

        KacyberRestClient.get(Constants.DISCOVER_MAIN, null, new HttpResponseHandler() {

            @Override
            public void onSuccess(byte[] responseBytes) {

                String responseBody = null;
                ArrayList<DealMerchant> dealMerchants = new ArrayList<DealMerchant>();
                ArrayList<TrendingItem> trendingItems = new ArrayList<TrendingItem>();
                ArrayList<EventItem> eventItems = new ArrayList<EventItem>();
                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    JSONArray trendingsJSONArray = jsonData.getJSONArray("trendingItems");
                    for (int i=0; i < 2; i++) {
                        TrendingItem trendingItem = new TrendingItem();
                        trendingItem.initWithJSONObject(trendingsJSONArray.getJSONObject(i));
                        trendingItems.add(trendingItem);
                    }
                    JSONArray eventitemsJSONArray = jsonData.getJSONArray("eventItems");
                    for (int i=0; i < eventitemsJSONArray.length(); i++) {
                        EventItem eventItem = new EventItem();
                        eventItem.initWithJSONObject(eventitemsJSONArray.getJSONObject(i));
                        eventItems.add(eventItem);
                    }
                    JSONArray merchantsJSON = jsonData.getJSONArray("recommendItems");
                    for (int i = 0; i < merchantsJSON.length(); i++) {
                        DealMerchant dealMerchant = new DealMerchant();
                        dealMerchant.initWithJSONObject(merchantsJSON.getJSONObject(i));
                        dealMerchants.add(dealMerchant);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new MainMerchantListEvent(dealMerchants, trendingItems, eventItems));
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    public void getDiscoverMainPageByLatLng(double longitude, double latitude) {

        setAppkeyHeader();

        Log.e(TAG, "get Discover Main Page");

        AndroidRequestParams params = new AndroidRequestParams();
        params.put("lng", longitude);
        params.put("lat", latitude);
        KacyberRestClient.get(Constants.DISCOVER_MAIN, params, new HttpResponseHandler() {

            @Override
            public void onSuccess(byte[] responseBytes) {

                String responseBody = null;
                ArrayList<DealMerchant> dealMerchants = new ArrayList<DealMerchant>();
                ArrayList<TrendingItem> trendingItems = new ArrayList<TrendingItem>();
                ArrayList<EventItem> eventItems = new ArrayList<EventItem>();
                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    JSONArray merchantsJSON = jsonData.getJSONArray("recommendItems");
                    for (int i = 0; i < merchantsJSON.length(); i++) {
                        DealMerchant dealMerchant = new DealMerchant();
                        dealMerchant.initWithJSONObject(merchantsJSON.getJSONObject(i));
                        dealMerchants.add(dealMerchant);
                    }
                    JSONArray trendingsJSONArray = jsonData.getJSONArray("trendingItems");
                    for (int i=0; i < 2; i++) {
                        TrendingItem trendingItem = new TrendingItem();
                        trendingItem.initWithJSONObject(trendingsJSONArray.getJSONObject(i));
                        trendingItems.add(trendingItem);
                    }
                    JSONArray eventitemsJSONArray = jsonData.getJSONArray("eventItems");
                    for (int i=0; i < 2; i++) {
                        EventItem eventItem = new EventItem();
                        eventItem.initWithJSONObject(eventitemsJSONArray.getJSONObject(i));
                        eventItems.add(eventItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new MainMerchantListEvent(dealMerchants, trendingItems, eventItems));
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    public void facebookLogin(String accessToken) {
        setAppkeyHeader();

        AndroidRequestParams params = new AndroidRequestParams();
        params.put("accessToken", accessToken);
        KacyberRestClient.post(Constants.FACEBOOK_LOGIN_SMS, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;


                Log.e(TAG, "facebook Login result is " +responseBody);
                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    Log.e(TAG, "" + responseBody);
                    String resultCode = new JSONObject(responseBody).getString("msg");
                    if (resultCode.equals("SUCCESS")) {

                        LoginSuccessModel loginSuccessModel = new LoginSuccessModel();
                        JSONObject jsonObject = new JSONObject(responseBody).getJSONObject("data");
                        loginSuccessModel.initWithJSONObject(jsonObject);

                        EventBus.getDefault().post(new LoginSuccessEvent(loginSuccessModel));

                    } else {
                        LoginSuccessModel loginSuccessModel = new LoginSuccessModel();
                        EventBus.getDefault().post(new LoginSuccessEvent(loginSuccessModel));
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    public void getCategory(int categoryId, String lat, String lng, String sortBy, String filters) {
        setAppkeyHeader();

        Log.e(TAG, "getCategory by categoryID =======" + categoryId);
        AndroidRequestParams params = new AndroidRequestParams();
        params.put("categoryId", categoryId);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("orderBy", sortBy);
        params.put("filters", filters);

        KacyberRestClient.get(Constants.CATEGORY_SEARCH, params, new HttpResponseHandler() {

            @Override
            public void onSuccess(byte[] responseBytes) {

                String responseBody = null;

                ArrayList<DealMerchant> categoryMerchantList = new ArrayList<DealMerchant>();

                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    Log.e(TAG, "" + responseBody);

                    JSONObject jsonObject = new JSONObject(responseBody).getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("merchants");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        DealMerchant dealMerchant = new DealMerchant();
                        dealMerchant.initWithJSONObject(jsonArray.getJSONObject(i));
                        categoryMerchantList.add(dealMerchant);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                EventBus.getDefault().post(new CategoryMerchantListEvent(categoryMerchantList));

            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    public void getAllCities() {
        setAppkeyHeader();

        KacyberRestClient.get(Constants.ALL_CITIES, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                ArrayList<SortModelCities> modelCities = new ArrayList<SortModelCities>();

                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject jsonObject = new JSONObject(responseBody).getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("cities");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        SortModelCities sortModelCity = new SortModelCities();
                        sortModelCity.initWithJSONObject(jsonArray.getJSONObject(i));
                        modelCities.add(sortModelCity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                EventBus.getDefault().post(new AllCitiesEvent(modelCities));
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    public void getOverseaCities() {
        setAppkeyHeader();

        KacyberRestClient.get(Constants.OVERSEA_CITIES, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                ArrayList<SortModelCities> modelCities = new ArrayList<SortModelCities>();

                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject jsonObject = new JSONObject(responseBody).getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("cities");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        SortModelCities sortModelCity = new SortModelCities();
                        sortModelCity.initWithJSONObject(jsonArray.getJSONObject(i));
                        modelCities.add(sortModelCity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                EventBus.getDefault().post(new AllCitiesEvent(modelCities));
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    public void getCategoryList() {
        setAppkeyHeader();

        KacyberRestClient.get(Constants.CATEGORY_LIST, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                ArrayList<Category> categoryList = new ArrayList<Category>();

                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONArray jsonArray = new JSONObject(responseBody).getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.e(TAG, "======================== category list first loop " + i);
                        Category category = new Category();
                        category.initWithJSONObject(jsonArray.getJSONObject(i));
                        if (category.parentId == 0) {
                            Log.e(TAG, "======================== category list second loop " + i);
                            categoryList.add(category);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                EventBus.getDefault().post(new AllCategoryEvent(categoryList));
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    public void getMerchantsBySuperDeal() {
        setAppkeyHeader();

        Log.e(TAG, "get merchants by super deal");

        KacyberRestClient.get(Constants.DEAL_MERCHANT_LIST, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                ArrayList<DealMerchant> dealMerchantList = new ArrayList<DealMerchant>();
                String superDealImage = new String();


                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject jsonObject = new JSONObject(responseBody).getJSONObject("data");
                    superDealImage = jsonObject.getString("imageURL");
                    JSONArray jsonArray = jsonObject.getJSONArray("merchants");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        DealMerchant dealMerchant = new DealMerchant();
                        dealMerchant.initWithJSONObject(jsonArray.getJSONObject(i));
                        dealMerchantList.add(dealMerchant);
                    }

//                    Realm realm = Realm.getDefaultInstance();
//                    realm.createOrUpdateAllFromJson(DealMerchant.class, jsonArray);
//                    realm.commitTransaction();
//                    Log.e(TAG, "" + realm.where(DealMerchant.class).findAll().size());
                } catch (Exception e) {

                    e.printStackTrace();
                }
                EventBus.getDefault().post(new DealMerchantListEvent(superDealImage, dealMerchantList));
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    public void getMerchantDetailById(long merchantId) {
        setAppkeyHeader();

        Log.e(TAG, "GET_MERCHANT_BY_ID ");

        KacyberRestClient.getWithoutParams(Constants.GET_MERCHANT_BY_ID + merchantId, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                MerchantDetail merchantDetail = new MerchantDetail();

                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject jsonObject = new JSONObject(responseBody).getJSONObject("data");
                    merchantDetail.initWithJSONObject(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new MerchantDetailEvent(merchantDetail));
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

//    public void getFavMusic(String token) {
//        setTokenHeader(token);
//        Log.e(TAG, "getFavMusic token is " + token);
//        KacyberRestClient.get(Constants.USER_FAV_MUSIC, null, new HttpResponseHandler() {
//            @Override
//            public void onSuccess(byte[] responseBytes) {
//                String responseBody = null;
//                try {
//                    responseBody = new String(responseBytes, Constants.CHARSET);
//                    JSONObject jsonObject = new JSONObject(responseBody).getJSONObject("data");
//
//                    Log.e(TAG, "response array is " + jsonObject);
//
//                    JSONArray hotDeals = jsonObject.getJSONArray("hotDeals");
//                    Realm realm = Realm.getDefaultInstance();
//                    realm.beginTransaction();
//                    realm.createOrUpdateAllFromJson(Deal.class, hotDeals);
//                    realm.commitTransaction();
//
//                    JSONArray trendingItems = jsonObject.getJSONArray("trendingItems");
//                    realm.beginTransaction();
//                    realm.createOrUpdateAllFromJson(TrendingItem.class, trendingItems);
//
//                    JSONArray recommendItems = jsonObject.getJSONArray("recommendItems");
//                    realm.beginTransaction();
//                    realm.createOrUpdateAllFromJson(EventItem.class, recommendItems);
//                    realm.getSchema();
////                    if (jsonArray!=null) {
////                        ArrayList<Music> musicArrayList = new ArrayList<Music>();
////                        for (int i=0; i < jsonArray.length(); i++) {
////                            Music music = new Music();
////                            music.initWithJSONObject(jsonArray.getJSONObject(i));
////                            musicArrayList.add(music);
////                        }
////                        Log.e(TAG, "FavMusicResult posted");
////                        EventBus.getDefault().post(new FavMusicListEvent(musicArrayList));
////                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int errorCode) {
//
//            }
//        });
//    }

    public void setLikeMusic(String token, String musicID) {
        setTokenHeader(token);
        AndroidRequestParams params = new AndroidRequestParams();
        Log.e(TAG, "setFavMusic token is " + token + "MUSIC_ID is " + musicID);
        KacyberRestClient.post(Constants.USER_LIKE_MUSIC + "/" + musicID + "/like", params, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                try {
                    responseBody = new String(responseBytes);
                    JSONObject jsonObject = new JSONObject(responseBody);

                    if (Util.DEBUG) {
                        Log.e(TAG, "responseBody is " + jsonObject);
                        Log.e(TAG, "jsonArray is " + jsonObject);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode) {
                Log.e(TAG, "" + errorCode);

            }
        });
    }

    public void setUnLikeMusic(String token, String musicID) {
        setTokenHeader(token);
        AndroidRequestParams params = new AndroidRequestParams();
        Log.e(TAG, "setFavMusic token is " + token + "MUSIC_ID is " + musicID);
        KacyberRestClient.post(Constants.USER_LIKE_MUSIC + "/" + musicID + "/unlike", params, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                try {
                    responseBody = new String(responseBytes);
                    JSONObject jsonObject = new JSONObject(responseBody);

                    if (Util.DEBUG) {
                        Log.e(TAG, "responseBody is " + jsonObject);
                        Log.e(TAG, "jsonArray is " + jsonObject);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode) {
                Log.e(TAG, "" + errorCode);

            }
        });
    }

    public void getPlayListGroup(final String codeOrPlayListID) {
        KacyberRestClient.get(Constants.PLAYLIST_GROUP + codeOrPlayListID, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                try {
                    String responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    if (Util.DEBUG) {
                        Log.e(TAG, "responseBody is " + responseBody);
                        Log.e(TAG, "jsonArray is " + jsonArray);
                    }


                    if (jsonArray != null) {

//                        ArrayList<PlayListEntity> listGroupResult = new ArrayList<PlayListEntity>();
//                        for (int i=0; i < jsonArray.length(); i++) {
//                            PlayListEntity playListEntity = new PlayListEntity();
//                            playListEntity.initWithJSONObject(jsonArray.getJSONObject(i));
//                            listGroupResult.add(playListEntity);
////                            Play music = new Music();
////                            music.initWithJSONObject(jsonArray.getJSONObject(i));
//                        }
//                        if (codeOrPlayListID.equals("featured")) {
//                            EventBus.getDefault().post(new HotListGroupEvent(listGroupResult));
//                        } else {
//                            EventBus.getDefault().post(new RunListGroupEvent(listGroupResult));
//                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorCode) {
                Log.e(TAG, "PLAYLISTONTEMPO ERROR CODE is " + errorCode);
            }
        });
    }

    public void getTempoMusic(int tempo) {
        AndroidRequestParams params = new AndroidRequestParams();
        params.put("tempo", tempo);

        KacyberRestClient.get(Constants.PLAY_ON_TEMPO, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                try {
                    String responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject jsonObject = new JSONObject(responseBody);

                    if (Util.DEBUG) {
                        Log.e(TAG, "responseBody is " + jsonObject);
                        Log.e(TAG, "jsonArray is " + jsonObject);
                    }


                    if (jsonObject != null) {

//                            Music music = new Music();
//                            music.initWithJSONObject(jsonObject);
//                            RunsicService.getInstance().playOnTempoCallback(music);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorCode) {
                Log.e(TAG, "PLAYLISTONTEMPO ERROR CODE is " + errorCode);
            }
        });

    }


//    public void getPGCList() {
//
//
//        KacyberRestClient.get(Constants.PGC_MUSIC_LIST, null, new HttpResponseHandler() {
//            @Override
//            public void onSuccess(byte[] responseBytes) {
//                try {
//                    String responseBody = new String (responseBytes, Constants.CHARSET);
//                    JSONArray jsonArray = new JSONArray(responseBody);
//                    if (Util.DEBUG) {
//                        Log.e(TAG, "responseBody is " + responseBody);
//                        Log.e(TAG, "jsonArray is "+ jsonArray);
//                    }
//
//
//
//                    if (jsonArray!=null) {
//
//                        for (int i=0; i < jsonArray.length(); i++) {
//                            Music music = new Music();
//                            music.initWithJSONObject(jsonArray.getJSONObject(i));
//                            RunsicService.getInstance().addPGCList(music);
//                        }
//                    }
//
//                    RunsicService.getInstance().setPGCListChange();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
//            }
//
//            @Override
//            public void onFailure(int errorCode) {
//                Log.e(TAG, "GET PGCLIST FAILURE");
//                Log.e(TAG, "ERROR CODE " + errorCode);
//            }
//        });
//    }


    public void getTempoList(int tempo) {
        AndroidRequestParams params = new AndroidRequestParams();
        params.put("tempo", tempo);

        KacyberRestClient.get(Constants.PLAY_LIST_ON_TEMPO, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                try {
                    String responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONArray jsonArray = new JSONArray(responseBody);
                    if (Util.DEBUG) {
                        Log.e(TAG, "responseBody is " + responseBody);
                        Log.e(TAG, "jsonArray is " + jsonArray);
                    }

                    if (jsonArray != null) {
//                        ArrayList<Music> musicArrayList = new ArrayList<Music>();
//                        for (int i=0; i < jsonArray.length(); i++) {
//                            Music music = new Music();
//                            music.initWithJSONObject(jsonArray.getJSONObject(i));
//                            if (i == 0) {
//                                EventBus.getDefault().post(new CurrentMusicEvent(music));
//                            }
//                            musicArrayList.add(music);
//                        }
//                        EventBus.getDefault().post(new TempoListResult(musicArrayList));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorCode) {
                Log.e(TAG, "PLAYLISTONTEMPO ERROR CODE is " + errorCode);
            }
        });

    }

    public void getMoreTempoList(int tempo) {
        AndroidRequestParams params = new AndroidRequestParams();
        params.put("tempo", tempo);

        KacyberRestClient.get(Constants.PLAY_LIST_ON_TEMPO, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                try {
                    String responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONArray jsonArray = new JSONArray(responseBody);
                    if (Util.DEBUG) {
                        Log.e(TAG, "responseBody is " + responseBody);
                        Log.e(TAG, "jsonArray is " + jsonArray);
                    }


                    if (jsonArray != null) {
//                        for (int i=0; i < jsonArray.length(); i++) {
//                            Music music = new Music();
//                            music.initWithJSONObject(jsonArray.getJSONObject(i));
//                            RunsicService.getInstance().addCurrentList(music);
//                        }
                    }

//                    musicRequestCallback.onPlayonTempoMoreListCallback();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorCode) {
                Log.e(TAG, "PLAYLISTONTEMPO ERROR CODE is " + errorCode);
            }
        });
    }

    public void getEventList() {
        KacyberRestClient.getWithoutParams(Constants.EVENT_LIST_URL, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                try {
                    String responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONArray jsonArray = new JSONArray(responseBody);

                    if (Util.DEBUG) {
                        Log.e(TAG, "getEventList " + "responseBody is " + responseBody);
                        Log.e(TAG, "getEventList " + "jsonArray is " + jsonArray);
                    }

//                    ArrayList<Event> eventList = new ArrayList<Event>();

                    if (jsonArray != null) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            Event event = new Event();
//                            event.initWithJSONObject(jsonArray.getJSONObject(i));
//                            eventList.add(event);
//                        }

                    }

//                    EventBus.getDefault().post(new EventRequestEvent(eventList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode) {
                Log.e(TAG, "request url is " + Constants.EVENT_LIST_URL);
                Log.e(TAG, "getEventList ERROR CODE is " + errorCode);
            }
        });
    }


    public void addUser() {

        SharedPreferences sPreferences = Util.getUserPreferences();
        int userId = sPreferences.getInt(Constants.USER_INFO_USERID, 0);
        // userId = 0;
        if (userId == 0) {

            String pCode = sPreferences.getString(Constants.DEVICE_ID, "");
            // 去注册 用户
            AndroidRequestParams params = new AndroidRequestParams();
            params.put("action", "adduser");

            HashMap<String, String> userMap = new HashMap<String, String>();
            userMap.put("pc", pCode);
            String userString = JSON.toJSONString(userMap);

            params.put("user", userString);

            KacyberRestClient.post(Constants.SERVER_IP, params, new HttpResponseHandler() {

                @Override
                public void onSuccess(byte[] responseBytes) {
                    int uid = 0;
                    try {
                        String responseBody = new String(responseBytes, Constants.CHARSET);
                        JSONObject obj = new JSONObject(responseBody);
                        uid = Integer.parseInt(obj.getString("uid"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // 保存uid
                    SharedPreferences sPreferences = Util.getUserPreferences();

                    Editor editor = sPreferences.edit();
                    editor.putInt(Constants.USER_INFO_USERID, uid);
                    editor.commit();
                }

                @Override
                public void onFailure(int errorCode) {
                }

            });

        }

    }

    /**
     * @param obj
     */
    public void createUser(Object obj) {
        AndroidRequestParams params = new AndroidRequestParams();
//        params.put("action", "adduser");
        HashMap<String, Object> userInfo = (HashMap) obj;
        Log.e(TAG, "post userInfo is " + userInfo);

//        HashMap<String, String> userMap = new HashMap<String, String>();
//        userMap.put("pc", pCode);
//        String userString = JSON.toJSONString(userMap);

        params.put("access_token", Constants.access_token);
        params.put("openid", userInfo.get("openid").toString());
        params.put("unionid", userInfo.get("unionid").toString());
        params.put("type", "wechat");
        params.put("nickname", userInfo.get("nickname").toString());
        params.put("gender", userInfo.get("sex").toString());
        params.put("avatar", userInfo.get("headimgurl").toString());
        params.put("from", "android");

        KacyberRestClient.post(Constants.CREATE_USER, params, new HttpResponseHandler() {

            @Override
            public void onSuccess(byte[] responseBytes) {
                int uid = 0;
                try {
                    String responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String token = jsonObject.getString("token");

                    SharedPreferences sharedPreferences = Util.getUserPreferences();
                    Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.commit();

//                    String responseBody = new String(responseBytes, Constants.CHARSET);
//                    JSONObject obj = new JSONObject(responseBody);
//                    uid = Integer.parseInt(obj.getString("uid"));
//                    ServiceLauncher.saveUidAndPCode(uid + "", Util.deviceId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 保存uid
//                SharedPreferences sPreferences = Util.getUserPreferences();
//
//                Editor editor = sPreferences.edit();
//                editor.putInt(Constants.USER_INFO_USERID, uid);
//                editor.commit();
            }

            @Override
            public void onFailure(int errorCode) {
                Log.e(TAG, "errorCode is " + errorCode);
            }

        });
    }


    public void saveSportToServer(String token, String base64String) {
        setTokenHeader(token);
        AndroidRequestParams params = new AndroidRequestParams();
        params.put("data", base64String);
        Log.e(TAG, "saveSportToServer token is " + token);
        KacyberRestClient.post(Constants.USER_SPORT_SAVE, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                try {
                    responseBody = new String(responseBytes);
                    JSONObject jsonObject = new JSONObject(responseBody);

                    if (Util.DEBUG) {
                        Log.e(TAG, "responseBody is " + jsonObject);
                        Log.e(TAG, "jsonArray is " + jsonObject);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode) {
                Log.e(TAG, "" + errorCode);

            }
        });
    }

    /**
     * 获取用户信息
     *
     * @param sh
     */

    /**
     * 本地获取用户数据
     *
     * @return
     */
    public Map<String, String> getUserInfoFromLocal() {

        SharedPreferences sPreferences = Util.getUserPreferences();
        // 性别 体重 身高 生日 手机push_token
        String gender = sPreferences.getString(Constants.USER_INFO_GENDER, "m");
        String weight = "" + sPreferences.getFloat(Constants.USER_INFO_WEIGHT, 70);
        String height = "" + sPreferences.getFloat(Constants.USER_INFO_HEIGHT, 1.72f) * 100;
        String birthdate = "" + sPreferences.getFloat(Constants.USER_INFO_BIRTHDAY, 1980);

        String mobilPushToken = sPreferences.getString(Constants.MOBILE_PUSH_TOKEN, "");

        String nickname = sPreferences.getString(Constants.USER_INFO_NICKNAME, "");
        String avatarurl = sPreferences.getString(Constants.USER_INFO_AVATARURL, "");

        HashMap<String, String> userMap = new HashMap<String, String>();
        userMap.put("nick_name", nickname);
        userMap.put("avatar_url", "" + avatarurl);
        userMap.put("gender", "" + gender);
        userMap.put("birthdate", birthdate);
        userMap.put("weight", weight);
        userMap.put("height", height);
        userMap.put("mobile_type", "" + 1);
        userMap.put("mobile_version", "" + PackageInfoUtil.getVersionCode(Util.context()));
        userMap.put("mobile_push_token", mobilPushToken);

        return userMap;
    }

    public void justice() {

        KacyberRestClient.get(Constants.JUSTICE, null, new HttpResponseHandler() {

            @Override
            public void onSuccess(byte[] responseBytes) {

                String responseBody = null;

                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String justice = jsonObject.getString("msg");
                    if (!justice.equals("paid")) {
                        EventBus.getDefault().post(new JMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });

    }

    public void getConversationList() {
        setAppkeyHeader();

        KacyberRestClient.get(Constants.CONVERSATION_LIST, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                Realm realm = Realm.getDefaultInstance();

                try {
                    responseBody = new String (responseBytes, Constants.CHARSET);
                    final JSONObject json = new JSONObject(responseBody);
                    Log.e(TAG, "Conversation JSON is " + json);
                    final JSONArray jsonArray = json.getJSONArray("data");
                    for (int i = 0; i< jsonArray.length(); i++) {
                        realm.beginTransaction();
                        Log.e(TAG, "json array " + "i is " + jsonArray.getJSONObject(i));
                        if (realm.where(Conversation.class).equalTo("id", jsonArray.getJSONObject(i).getLong("id")).findAll().size()==0) {
                            realm.createObjectFromJson(Conversation.class, jsonArray.getJSONObject(i));
                        }
                        realm.commitTransaction();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                realm.close();

                EventBus.getDefault().post(new ConversationListEvent());
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    public void getContactList() {
        setAppkeyHeader();

        KacyberRestClient.get(Constants.CONTACT_LIST, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                Realm realm = Realm.getDefaultInstance();
                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    Log.e(TAG, "responseBody is " + responseBody);
                    JSONObject jsonObject = new JSONObject(responseBody);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i ++) {
                        realm.beginTransaction();
                        if (realm.where(Contact.class).equalTo("id", jsonArray.getJSONObject(i).getLong("id")).findAll().size()==0) {
                            realm.createObjectFromJson(Contact.class, jsonArray.getJSONObject(i));
                        }
                        realm.commitTransaction();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                realm.close();

            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    public boolean sendMessage(Context context, HttpEntity httpEntity, String contentType) {
        setAppkeyHeader();
        boolean result;
        KacyberRestClient.postBody(context, Constants.SEND_MESSAGE, httpEntity, contentType, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                try {
                    responseBody = new String (responseBytes, Constants.CHARSET);
                    Log.e(TAG, "responseBody is " + responseBody);
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getString("msg").equals("SUCESS")) {
//                        EventBus.getDefault().post(new SendMessageSuccess());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });


        return true;
    }

    public void searchFriends(String keyword) {
        setAppkeyHeader();

        AndroidRequestParams params = new AndroidRequestParams();
        params.put("keyword", keyword);

        KacyberRestClient.get(Constants.SEARCH_FRIENDS, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;
                User user = new User();
                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject json = new JSONObject(responseBody);
                    JSONObject json1 = json.getJSONObject("data").getJSONArray("items").getJSONObject(0);
                    user.initWithJSONObject(json1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new FriendSearchResult(user));
            }

            @Override
            public void onFailure(int errorCode) {
                Log.e(TAG, "ERROR CODE is " + errorCode);
            }
        });
    }

    public void addFriend(long targetUserId, String message) {
        setAppkeyHeader();

        AndroidRequestParams params = new AndroidRequestParams();
        params.put("targetUserId", targetUserId);
        params.put("message", message);

        KacyberRestClient.get(Constants.ADD_FRIEND, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;

                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    Log.e(TAG, "add Friend response is " + responseBody);
                    JSONObject jsonObject = new JSONObject(responseBody);
                    long conversationId = jsonObject.getLong("data");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    public void getUserById(long userId) {
        setAppkeyHeader();

        AndroidRequestParams params = new AndroidRequestParams();
        params.put("userId", userId);

        KacyberRestClient.get(Constants.GET_USER_BY_ID, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;

                Realm realm = Realm.getDefaultInstance();
                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    realm.beginTransaction();
                    realm.createObjectFromJson(UserProfile.class, jsonData);
                    realm.commitTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                realm.close();
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }




    public void acceptFriendRequest(long requestId) {
        setAppkeyHeader();

        AndroidRequestParams params = new AndroidRequestParams();
        params.put("requestId", requestId);

        KacyberRestClient.get(Constants.ACCEPT_FRIEND, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(byte[] responseBytes) {
                String responseBody = null;

                try {
                    responseBody = new String(responseBytes, Constants.CHARSET);
                    Log.e(TAG, "add Friend response is " + responseBody);
                    JSONObject jsonObject = new JSONObject(responseBody);
                    long conversationId = jsonObject.getLong("data");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }




    private enum TypeInfo {
        Tencent, Sinaweibo, Wandoujia
    }


    public synchronized void setInstallSource() {
        int currentVersionNumber = 0;
        String channel = "friends";
        String deviceId = "";
        try {
            Context context = Util.context();

            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            channel = ai.metaData.getString("UMENG_CHANNEL");

            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = TelephonyMgr.getDeviceId(); // Requires

            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionNumber = pinfo.versionCode;
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
        }

        if (currentVersionNumber == 0)
            return;

        SharedPreferences sPreferences = Util.getUserPreferences();

        String source = sPreferences.getString(Constants.INSTALL_SOURCE, "");

        if (Util.isEmpty(source)) { // 之前已经拼凑source成功,直接开始传输

            int guide = sPreferences.getInt(Constants.USER_INFO_GUIDE, 0);
            int versionNumber = sPreferences.getInt(Constants.LEDONGLI_VERSION_NUMBER, currentVersionNumber + 1);

            // 已经上传过版本信息,直接退出
            if (versionNumber == currentVersionNumber)
                return;

            // 升级用户
            if (currentVersionNumber > versionNumber || guide == 0) {
                source += "update@@@";
            } else { // 新用户
                source += "new@@@";
            }

//            source += Date.now().getTime() + "@@@" + channel + "@@@" + currentVersionNumber;
            // 本地保存source和更新本地版本号信息
            Editor editor = sPreferences.edit();
//            editor.putLong(Constants.INSTALL_TIME, Date.now().getTime());
            editor.putString(Constants.INSTALL_SOURCE, source);
            editor.putInt(Constants.LEDONGLI_VERSION_NUMBER, currentVersionNumber);
            editor.commit();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("source", "" + source);
            jsonObject.put("deviceId", "" + deviceId);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        // 网络判断
        if (!(NetStatus.isNetworkEnable())) {
            return;
        }

        AndroidRequestParams params = new AndroidRequestParams();
        params.put("action", "userstatistics");
        params.put("cmd", "setinstallsource");
        params.put("ctx", jsonObject.toString());

        KacyberRestClient.post(Constants.SERVER_IP, params, new HttpResponseHandler() {

            @Override
            public void onSuccess(byte[] responseBytes) {
                String succesString = "";
                try {
                    String responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject object = new JSONObject(responseBody);
                    succesString = object.getString("status");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (succesString.equals("OK")) {
                    // 上传成功后,恢复source的本地
                    Util.getUserPreferences().edit().putString(Constants.INSTALL_SOURCE, "").commit();
                }
            }

            @Override
            public void onFailure(int errorCode) {
                if (Util.DEBUG)
                    Log.d(TAG, "error");
            }
        });
    }

    public void uploadDeviceInfo(String info) {
        SharedPreferences sPreferences = Util.getUserPreferences();
        // boolean uploadDeviceStatus =
        // sPreferences.getBoolean(Constants.USER_DEVICE_INFO_STATUS, false);
        //
        // if (uploadDeviceStatus) {
        // return;
        // }

        int userId = sPreferences.getInt(Constants.USER_INFO_USERID, 0);
        // userId = 0;
        if (userId == 0) {
            addUser();
        }

        String pCode = sPreferences.getString(Constants.DEVICE_ID, "");
        // 去注册 用户
        AndroidRequestParams params = new AndroidRequestParams();
        params.put("action", "updatedeviceinfo");
        params.put("pc", pCode);
        params.put("uid", "" + userId);
        params.put("info", info);

        // 网络判断
        if (!(NetStatus.isNetworkEnable())) {
            return;
        }

        KacyberRestClient.post(Constants.SERVER_IP, params, new HttpResponseHandler() {

            @Override
            public void onSuccess(byte[] responseBytes) {
                String succesString = "";
                try {
                    String responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject object = new JSONObject(responseBody);
                    succesString = object.getString("status");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (succesString.equals("OK")) {
                    // 保存上传状态
                    // SharedPreferences sPreferences =
                    // Util.getUserPreferences();
                    //
                    // Editor editor = sPreferences.edit();
                    // editor.putBoolean(Constants.USER_DEVICE_INFO_STATUS,
                    // true);
                    // editor.commit();
                }
            }

            @Override
            public void onFailure(int errorCode) {
                Log.d(TAG, "error");
            }
        });
    }

    public void updateinfo() {

        SharedPreferences sPreferences = Util.getUserPreferences();
        int userId = sPreferences.getInt(Constants.USER_INFO_USERID, 0);

        if (userId == 0) {
            return;
        }

        boolean needUpdateInfo = sPreferences.getBoolean(Constants.UPDATE_INFO_KEY, false);
        if (!needUpdateInfo) {
            return;
        }

        String pCode = sPreferences.getString(Constants.DEVICE_ID, "");
        String gender = sPreferences.getString(Constants.USER_INFO_GENDER, "f");
        String weight = "" + sPreferences.getFloat(Constants.USER_INFO_WEIGHT, 70);
        String height = "" + sPreferences.getFloat(Constants.USER_INFO_HEIGHT, 1.72f) * 100;
        String birthdate = "" + sPreferences.getFloat(Constants.USER_INFO_BIRTHDAY, 1980);

        String nickname = sPreferences.getString(Constants.USER_INFO_NICKNAME, "");
        String avatarurl = sPreferences.getString(Constants.USER_INFO_AVATARURL, "");

        String mobilPushToken = sPreferences.getString(Constants.MOBILE_PUSH_TOKEN, "");

        AndroidRequestParams params = new AndroidRequestParams();
        params.put("action", "updateinfo");
        params.put("uid", "" + userId);
        params.put("pc", "" + pCode);

        HashMap<String, String> userMap = new HashMap<String, String>();
        userMap.put("nick_name", nickname);
        userMap.put("avatar_url", "" + avatarurl);
        userMap.put("gender", "" + gender);
        userMap.put("birthdate", birthdate);
        userMap.put("weight", weight);
        userMap.put("height", height);
        // userMap.put("IOS_APN_token", "");//TODO 上传ios token
        userMap.put("mobile_type", "" + 1);
        userMap.put("mobile_version", "" + PackageInfoUtil.getVersionCode(Util.context()));
        userMap.put("mobile_push_token", mobilPushToken);
        String userString = JSON.toJSONString(userMap);
        params.put("user", userString);

        KacyberRestClient.post(Constants.SERVER_IP, params, new HttpResponseHandler() {

            @Override
            public void onSuccess(byte[] responseBytes) {
                String statusString = "";
                try {
                    String responseBody = new String(responseBytes, Constants.CHARSET);
                    JSONObject obj = new JSONObject(responseBody);
                    statusString = obj.getString("status");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (!statusString.equals("OK")) {
                    return;
                }

                SharedPreferences sPreferences = Util.getUserPreferences();
                Editor editor = sPreferences.edit();
                editor.putBoolean(Constants.UPDATE_INFO_KEY, false);
                editor.commit();
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }


    public boolean isUidChanged = false;

    /**
     * 修改Uid的状态
     *
     * @param uid
     */
    public void setIsUidChanged(int uid) {
        // 保存uid
        SharedPreferences sPreferences = Util.getUserPreferences();
        int oldUid = sPreferences.getInt(Constants.USER_INFO_USERID, 0);

        if (oldUid != uid)
            isUidChanged = true;

        Editor editor = sPreferences.edit();
        editor.putInt(Constants.USER_INFO_USERID, uid);
        editor.commit();
    }

}
