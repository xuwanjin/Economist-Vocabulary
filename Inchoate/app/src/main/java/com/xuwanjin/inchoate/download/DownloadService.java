package com.xuwanjin.inchoate.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.xuwanjin.inchoate.Constants;
import com.xuwanjin.inchoate.database.dao.InchoateDBHelper;
import com.xuwanjin.inchoate.model.Article;
import com.xuwanjin.inchoate.model.Issue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/*
    输入 issue 下载整个期刊
    输入 Article 下载一篇文章
    下载每篇文章的音频文件
    下载完成后更新数据库字段

 */
public class DownloadService extends Service {
    public static final String TAG = "DownloadService";
    public static final int DOWNLOAD_TAG = 12;
    private final IBinder mBinder = new LocalBinder();
    private Issue mIssue = new Issue();
    private List<Article> mDownloadArticle = new ArrayList<>();
    public static int percent = 0;
    public ArrayMap<String, Boolean> mLocalAudioUrlMap = new ArrayMap<>();
    final Runnable mDownloadRunnable = new Runnable() {
        @Override
        public void run() {
            ArrayList<Article> audioArticle = new ArrayList<>();
            for (Article article : mDownloadArticle) {
                if (article.audioUrl != null
                        && !article.audioUrl.trim().equals("")) {
                    audioArticle.add(article);
                }
            }

            File commonFile = getBaseContext().getExternalCacheDirs()[0];

            /*
                issueDate/Section/article_title
                N 个     article_audio_url
             */
            final String issueDate = mIssue.issueDate;
            DownloadTask[] downloadTasks = new DownloadTask[audioArticle.size()];
            for (int i = 0; i < audioArticle.size(); i++) {
                Article article = audioArticle.get(i);
                String section = article.section;
                String audioFile = commonFile.getAbsolutePath() + "/" + issueDate + "/" + section;
                String noSpacePath = audioFile.replace(" ", "_");
                String noSpaceName = article.title.replace(" ", "_") + ".mp3";
                DownloadTask task =
                        new DownloadTask
                                .Builder(article.audioUrl, noSpacePath, noSpaceName)
                                .build()
                                .addTag(DOWNLOAD_TAG, article);
                downloadTasks[i] = task;
                String fullPath = noSpacePath + "/" + noSpaceName;
                mLocalAudioUrlMap.put(fullPath, false);
            }
            DownloadTask.enqueue(downloadTasks, downloadListener);
        }
    };
    private static final ExecutorService mExecutorService = Executors.newFixedThreadPool(1);

    public class LocalBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        startDownloadRunnable(intent);
        return mBinder;
    }

    public void startDownloadRunnable(Intent intent) {
        String formatIssueDate = intent.getStringExtra(Constants.PENDING_DOWNLOAD_ISSUE_DATE);
        InchoateDBHelper helper = InchoateDBHelper.getInstance(getApplicationContext());
        mIssue = helper.queryIssueByFormatIssueDate(formatIssueDate).get(0);
//        helper.close();
        Article article = intent.getParcelableExtra(Constants.DOWNLOAD_ARTICLE);
        if (mIssue != null && article == null) {
            mDownloadArticle = mIssue.containArticle;
        }
        if (mIssue == null && article != null) {
            mDownloadArticle.add(article);
        }
        if (mIssue != null && article != null) {
            mDownloadArticle = mIssue.containArticle;
        }
        mExecutorService.submit(mDownloadRunnable);
        if (mIssue == null && article == null) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public float getDownloadPercent() {
        if (mLocalAudioUrlMap == null || !(mLocalAudioUrlMap.size() > 0)) {
            return 0;
        }
        int count = 0;
        File audioFile;
        for (int i = 0; i < mLocalAudioUrlMap.size(); i++) {
            String audioPath = mLocalAudioUrlMap.keyAt(i);
            audioFile = new File(audioPath);
            if (audioFile.exists() && audioFile.isFile()) {
                count++;
            }
        }
        float percent = (float) ((count * 100.0) / mLocalAudioUrlMap.size());
        Log.d(TAG, "getDownloadPercent: percent = " + percent);
        return percent;
    }

    final DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void taskStart(@NonNull DownloadTask task) {
            Log.d(TAG, "taskStart: task.getFilename = " + task.getFilename());

        }

        @Override
        public void connectTrialStart(@NonNull DownloadTask task, @NonNull Map<String, List<String>> requestHeaderFields) {

        }

        @Override
        public void connectTrialEnd(@NonNull DownloadTask task, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {

        }

        @Override
        public void downloadFromBeginning(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @NonNull ResumeFailedCause cause) {

        }

        @Override
        public void downloadFromBreakpoint(@NonNull DownloadTask task, @NonNull BreakpointInfo info) {

        }

        @Override
        public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {

        }

        @Override
        public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {

        }

        @Override
        public void fetchStart(@NonNull DownloadTask task, int blockIndex, long contentLength) {

        }

        @Override
        public void fetchProgress(@NonNull DownloadTask task, int blockIndex, long increaseBytes) {

        }

        @Override
        public void fetchEnd(@NonNull DownloadTask task, int blockIndex, long contentLength) {

        }

        @Override
        public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
            String localeAudioUrl = task.getParentFile().getAbsolutePath() + "/" + task.getFilename();
            Article article = (Article) task.getTag(DOWNLOAD_TAG);
            article.localeAudioUrl = localeAudioUrl;
            Log.d(TAG, "taskEnd: localeAudioUrl = " + localeAudioUrl);
            InchoateDBHelper helper = InchoateDBHelper.getInstance(getApplication());
            helper.updateArticleAudioLocaleUrl(article, article.date);
//            helper.close();
            mLocalAudioUrlMap.put(localeAudioUrl, true);
        }
    };
}
