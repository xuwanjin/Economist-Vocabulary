package com.xuwanjin.inchoate.ui.bookmark;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.xuwanjin.inchoate.InchoateApp;
import com.xuwanjin.inchoate.R;
import com.xuwanjin.inchoate.database.dao.InchoateDBHelper;
import com.xuwanjin.inchoate.model.Article;
import com.xuwanjin.inchoate.model.ArticleDao;
import com.xuwanjin.inchoate.ui.BaseFragment;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Matthew Xu
 */
public class BookmarkFragment extends BaseFragment<BookmarkAdapter, BookmarkItemDecoration, List<Article>, LinearLayoutManager> {
    private static final String TAG = "BookmarkFragment";
    private TextView mTextView;
    private Disposable mDisposable;
    private static List<Article> sArticleList = new ArrayList<>();

    @Override
    protected void initView(View view) {
        mRecyclerView = view.findViewById(R.id.bookmark_recyclerView);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mTextView = view.findViewById(R.id.bookmark_title);
        mBaseAdapter = new BookmarkAdapter(getContext(), initFakeData());
        mRecyclerView.setAdapter(mBaseAdapter);
    }

    @Override
    protected List<Article> initFakeData() {
        List<Article> articleList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Article article = new Article();
            article.flyTitle = "";
            article.title = "";
            article.headline = "Matthew";
            article.articleUrl = "";
            article.imageUrl = "";
            articleList.add(article);
        }
        return articleList;
    }

    @Override
    public void loadData() {
        if (sArticleList.size() > 0) {
            updateFragmentContent(sArticleList);
        }
        loadBookmarkData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_bookmark;
    }


    @Override
    protected List<Article> fetchDataFromDBOrNetwork() {
        List<Article> articleList;
//        InchoateDBHelper helper = InchoateDBHelper.getInstance(getContext());
        QueryBuilder<Article> bookmarkArticleQuery = ((InchoateApp)getContext()
                .getApplicationContext())
                .getDaoSession()
                .getArticleDao()
                .queryBuilder()
                .where(ArticleDao.Properties.IsBookmark.eq(Boolean.TRUE));
//        articleList = helper.queryBookmarkedArticle();
        articleList = bookmarkArticleQuery.list();
        Log.d(TAG, "fetchDataFromDBOrNetwork: articleList.size = " + articleList.size());
//        helper.close();
        return articleList;
    }

    private void loadBookmarkData() {
        mDisposable = Single.create(new SingleOnSubscribe<List<Article>>() {
            @Override
            public void subscribe(SingleEmitter<List<Article>> emitter) throws Exception {
                sArticleList = fetchDataFromDBOrNetwork();
                Log.d(TAG, "loadBookmarkData: sArticleList.size() = " + sArticleList.size());
                emitter.onSuccess(sArticleList);
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, "loadBookmarkData: accept: fetch data from db or network get wrong.");
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(List<Article> articles) throws Exception {
                        Log.d(TAG, "loadBookmarkData: accept: articles.size() = " + articles.size());
                        if (articles.size() > 0) {
                            updateFragmentContent(articles);
                        }
                    }
                });

    }

    private void updateFragmentContent(List<Article> articleList) {
        Log.d(TAG, "updateFragmentContent: articleList = " + articleList.size());
        mRecyclerView.addItemDecoration(new BookmarkItemDecoration(getContext(), mRecyclerView));
        mBaseAdapter.updateData(articleList);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
