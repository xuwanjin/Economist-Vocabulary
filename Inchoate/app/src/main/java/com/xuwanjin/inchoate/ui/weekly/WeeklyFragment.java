package com.xuwanjin.inchoate.ui.weekly;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.xuwanjin.inchoate.Constants;
import com.xuwanjin.inchoate.InchoateActivity;
import com.xuwanjin.inchoate.InchoateApplication;
import com.xuwanjin.inchoate.R;
import com.xuwanjin.inchoate.Utils;
import com.xuwanjin.inchoate.model.Article;
import com.xuwanjin.inchoate.model.ArticleCategorySection;
import com.xuwanjin.inchoate.model.Issue;
import com.xuwanjin.inchoate.model.week.WeekFragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.xuwanjin.inchoate.Utils.getIssue;
import static com.xuwanjin.inchoate.Utils.getWholeArticle;

public class WeeklyFragment extends Fragment {
    public static final String TAG = "WeekFragment";
    RecyclerView issueContentRecyclerView;
    private View mSectionHeaderView;
    private View mFooterView;
    private TextView previousEdition;
    List<Article> mArticlesList;
    FloatingActionButton mFab;
    TextView downloadAudio;
    TextView streamAudio;
    TextView issueDate;
    TextView magazineHeadline;
    ImageView magazineCover;
    WeeklyAdapter mWeeklyAdapter;
    StickHeaderDecoration mStickHeaderDecoration;
    public static final int FETCH_DATA_AND_NOTIFY_MSG = 1000;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == FETCH_DATA_AND_NOTIFY_MSG) {
                if (mWeeklyAdapter != null) {
                    mWeeklyAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);
        issueContentRecyclerView = view.findViewById(R.id.issue_content_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        issueContentRecyclerView.setLayoutManager(linearLayoutManager);
        // 这种 header 的出现, 他会 inflate 在 RecyclerView 的上面, 这个时候, 画第一个 item 的 header,
        //也会出现在 RecyclerView 的上面, 但是他会出现, HeaderView 的上面
        mSectionHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.weekly_section_header, issueContentRecyclerView, false);
        previousEdition = mSectionHeaderView.findViewById(R.id.previous_edition);
        downloadAudio = mSectionHeaderView.findViewById(R.id.download_audio);
        streamAudio = mSectionHeaderView.findViewById(R.id.stream_audio);
        issueDate = mSectionHeaderView.findViewById(R.id.issue_date);
        magazineCover = mSectionHeaderView.findViewById(R.id.magazine_cover);
        magazineHeadline = mSectionHeaderView.findViewById(R.id.magazine_headline);
        previousEdition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: previousEdition = " + previousEdition);
                Utils.navigationController(InchoateApplication.NAVIGATION_CONTROLLER, R.id.navigation_previous_edition);
            }
        });
        mArticlesList = initData(new ArrayList<Article>());
        mWeeklyAdapter = new WeeklyAdapter(mArticlesList, getContext(), this);
        issueContentRecyclerView.setAdapter(mWeeklyAdapter);
        mWeeklyAdapter.setHeaderView(mSectionHeaderView);
        mStickHeaderDecoration = new StickHeaderDecoration(issueContentRecyclerView, getContext());
        issueContentRecyclerView.addItemDecoration(mStickHeaderDecoration);
        mFab = view.findViewById(R.id.issue_category_fab);
        mFab.setFocusable(true);
        mFab.setClickable(true);
        mFab.setVisibility(View.VISIBLE);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.navigationController(InchoateApplication.NAVIGATION_CONTROLLER, R.id.navigation_float_action);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private List<Article> initData(List<Article> articles) {
        List<Issue> issueList = InchoateApplication.getNewestIssueCache();
        final Issue issue;
        if (issueList != null && issueList.size() > 0) {
            issue = issueList.get(0);
            articles = issue.containArticle;
            Log.d(TAG, "onResponse: use the cache ");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: issue.coverImageUrl = " + issue.coverImageUrl);
                    Glide.with(mSectionHeaderView)
                            .load(issue.coverImageUrl)
                            .addListener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.d(TAG, "onLoadFailed: ");
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    Log.d(TAG, "onResourceReady: ");
                                    return false;
                                }
                            })
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.magazine_cover)
                            .into(magazineCover);
                    issueDate.setText(issue.issueDate);
                    magazineHeadline.setText(issue.headline);
                }
            });
        } else {
            for (int i = 0; i < 82; i++) {
                Article article = new Article();
                article.summary = "heeeeeeeeee" + i;
                article.section = "Matthew, helllo";
                article.headline = "Matthew = " + ArticleCategorySection.BRIEFING;
                articles.add(article);
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    parseJsonDataFromAsset();
                }
            });
        }
        return articles;
    }

    public void parseJsonDataFromAsset() {
        Gson gson = new Gson()
                .newBuilder()
                .setFieldNamingStrategy(new FieldNamingStrategy() {
                    @Override
                    public String translateName(Field f) {
                        String name = f.getName();
                        if (name.contains("-")) {
                            return name.replaceAll("-", "");
                        }
                        return name;
                    }
                }) // setFieldNamingPolicy 有什么区别
                .create();

        InputStream jsonStream = getContext().getResources().openRawResource(R.raw.week_fragment_query);
        InputStreamReader reader = new InputStreamReader(jsonStream);
        WeekFragment weekFragment = gson.fromJson(reader, WeekFragment.class);
        final Issue issue = getIssue(weekFragment);
        Glide.with(mSectionHeaderView)
                .load(issue.coverImageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d(TAG, "onLoadFailed: ");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "onResourceReady: ");
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.magazine_cover)
                .into(magazineCover);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                issueDate.setText(issue.issueDate);
                magazineHeadline.setText(issue.headline);

            }
        });
        Log.d(TAG, "parseJsonDataFromAsset: issue.containArticle.get(0) = " + issue.containArticle.get(0));
        InchoateApplication.setNewestIssueCache(issue);
//        mArticlesList.clear();
        mArticlesList = issue.containArticle;
        mHandler.sendEmptyMessage(FETCH_DATA_AND_NOTIFY_MSG);

    }

    public void weekFragmentTest() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constants.WEEK_FRAGMENT_QUERY_URL)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "weekFragmentTest: onFailure: e = " + e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonResult = response.body().string();
//                Log.d(TAG, "onResponse: jsonResult: jsonResult = " + jsonResult);
                Gson gson = new Gson()
                        .newBuilder()
                        .setFieldNamingStrategy(new FieldNamingStrategy() {
                            @Override
                            public String translateName(Field f) {
                                String name = f.getName();
                                if (name.contains("-")) {
                                    return name.replaceAll("-", "");
                                }
                                return name;
                            }
                        }) // setFieldNamingPolicy 有什么区别
                        .create();
                WeekFragment weekFragment = gson.fromJson(jsonResult, WeekFragment.class);
                Log.d(TAG, "onResponse: FETCH_DATA_AND_NOTIFY_MSG = ");
                Issue issue = getIssue(weekFragment);
                InchoateApplication.setNewestIssueCache(issue);
                Log.d(TAG, "onResponse: fetch from economist.com ");
                mArticlesList = issue.containArticle;
//                Log.d(TAG, "onResponse: mArticlesList = " + mArticlesList.get(0));
                mHandler.sendEmptyMessage(FETCH_DATA_AND_NOTIFY_MSG);
            }
        });
    }

}
