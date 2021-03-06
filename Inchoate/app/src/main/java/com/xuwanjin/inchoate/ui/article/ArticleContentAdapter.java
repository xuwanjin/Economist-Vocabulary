package com.xuwanjin.inchoate.ui.article;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xuwanjin.inchoate.R;
import com.xuwanjin.inchoate.model.Article;
import com.xuwanjin.inchoate.model.Paragraph;
import com.xuwanjin.inchoate.ui.BaseAdapter;

import java.util.List;

/**
 * @author Matthew Xu
 */
public class ArticleContentAdapter extends BaseAdapter<ArticleParagraphViewHolder, Paragraph> {
    public static final String TAG = "ArticleContentAdapter";
    private Article mArticle;

    public ArticleContentAdapter(Context context, List<Paragraph> paragraphList) {
        super(context, paragraphList);
    }

    public void setArticle(Article article) {
        this.mArticle = article;
    }

    @Override
    protected int getLayoutItemResId() {
        return R.layout.article_content_item;
    }

    @Override
    protected ArticleParagraphViewHolder getViewHolder(View view, boolean isHeaderOrFooter) {
        Log.d(TAG, "getViewHolder: isHeaderOrFooter = " + isHeaderOrFooter);
        return new ArticleParagraphViewHolder(view, mContext, mArticle, isHeaderOrFooter);
    }

    @Override
    public String getGroupName(int position) {
        return null;
    }

    @Override
    protected void onBindViewHolderImpl(@NonNull ArticleParagraphViewHolder holder, int position, Paragraph paragraph){
        holder.paragraphTextView.setText(paragraph.paragraph, TextView.BufferType.SPANNABLE);
        holder.setCurrentParagraph(paragraph);
    }

    @Override
    public boolean isItemHeader(int position) {
        return false;
    }
}
