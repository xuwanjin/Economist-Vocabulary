package com.xuwanjin.inchoate.ui.weekly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xuwanjin.inchoate.InchoateApp;
import com.xuwanjin.inchoate.R;
import com.xuwanjin.inchoate.Utils;
import com.xuwanjin.inchoate.database.dao.InchoateDBHelper;
import com.xuwanjin.inchoate.model.Article;

import java.util.ArrayList;
import java.util.List;

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.ViewHolder>
        implements StickHeaderDecoration.StickHeaderInterface {
    private Context mContext;
    public List<Article> mArticleList = new ArrayList<>();
    private Fragment mFragment;
    private View mHeaderView;
    private View mFooterView;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_NORMAL = 2;

    public WeeklyAdapter(Context context, Fragment fragment) {
        mContext = context;
        mFragment = fragment;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public boolean isFirstItem(int position) {
        if (mHeaderView != null && position == 1) {
            return true;
        } else if (mHeaderView == null && position == 0) {
            return true;
        }
        return false;
    }

    public boolean isHasHeader() {
        return mHeaderView != null;
    }

    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setFooterView(View footerView) {
        this.mFooterView = footerView;
    }

    @NonNull
    @Override
    public WeeklyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = mHeaderView;
                break;
            case TYPE_FOOTER:
                view = mFooterView;
                break;
            case TYPE_NORMAL:
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.weekly_content_list, parent, false);
                break;
        }

        return new WeeklyAdapter.ViewHolder(view);
    }

    public void updateData(List<Article> articleList) {
        mArticleList.clear();
        mArticleList.addAll(articleList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyAdapter.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_NORMAL) {
            //  mArticleList.get(position) 会出现第一个 item 不显示的状况
            Article article = mArticleList.get(position - 1);
            Glide.with(mContext)
                    .load(article.mainArticleImage)
                    .error(R.mipmap.the_economist)
                    .placeholder(R.mipmap.the_economist)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.article_image);
            holder.articleTitle.setText(article.title);
            holder.articleFlyTitle.setText(article.flyTitle);
            holder.dateAndReadTime.setText(position - 1 + " min read");
            View.OnClickListener viewArticleOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (article == null
                            || article.title == null
                            || article.title.equals("")
                            || article.paragraphList == null) {
                        return;
                    }
                    InchoateApp.setDisplayArticleCache(article);
                    Utils.navigationController(
                            InchoateApp.NAVIGATION_CONTROLLER, R.id.navigation_article);
                }

            };
            holder.titleAndMainImage.setOnClickListener(viewArticleOnClickListener);

            Glide.with(mContext)
                    .load(article.isBookmark ? R.mipmap.bookmark_black : R.mipmap.bookmark_white)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.bookmark_white)
                    .into(holder.bookmark);

            holder.bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (article.isBookmark) {
                        article.isBookmark = false;
                    } else {
                        article.isBookmark = true;
                    }
                    Glide.with(mContext)
                            .load(article.isBookmark ? R.mipmap.bookmark_black : R.mipmap.bookmark_white)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.bookmark_white)
                            .into(holder.bookmark);

                    InchoateDBHelper dbHelper = InchoateDBHelper.getInstance(mContext);
                    dbHelper.setBookmarkStatus(article, article.isBookmark);
                    dbHelper.close();
                }
            });
        }
    }

    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        // position 为零, 同时 mHeaderView 不为空, 那么第一个应该是 TYPE_HEADER
        if (position == 0) {
            if (mHeaderView != null) {
                return TYPE_HEADER;
            }
        }
        // 最后一个
        if (position == getItemCount() - 1) {
            if (mFooterView != null) {
                //最后一个,应该加载Footer
                return TYPE_FOOTER;
            } else {
                return TYPE_NORMAL;
            }
        }
        return TYPE_NORMAL;
    }


    @Override
    public int getItemCount() {
        if ((mHeaderView != null && mFooterView == null) ||
                mHeaderView == null && mFooterView != null) {
            return mArticleList == null ? 0 : mArticleList.size() + 1;
        }
        if (mHeaderView != null && mFooterView != null) {
            return mArticleList == null ? 0 : mArticleList.size() + 2;
        }
        return mArticleList == null ? 0 : mArticleList.size();
    }

    public String getGroupName(int position) {
        return mArticleList.get(position).section;
    }

    // 判断当前的 position 对应的 item1 是否是相应的组的第一项
    @Override
    public boolean isItemHeader(int position) {
        // position == 1 ,以及之后的才是 item1, item2, item3, item3
        if (position == 1) {
            return true;
        }
        // position == 0 ,是inflater 进去的 HeaderView,
        // HeaderView 上面不能画一个 ItemHeaderView, 所以, 返回的是 false
        // 因为第一项是 HeaderView
        if (position == 0) {
            return false;
        }
        if (position == getItemCount() - 1) {
            return false;
        }

        // 因为我们有一个 HeaderView, 这个 Position 是
        // RecyclerView 里的是 List.size() +1 项, 为了数据对应. 这里的需要  position -2
        String lastGroupName = mArticleList.get(position - 2).section;
        String currentGroupName = mArticleList.get(position - 1).section;
        if (lastGroupName.equals(currentGroupName)) {
            return false;
        }
        return true;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle;
        TextView articleFlyTitle;
        ImageView article_image;
        TextView dateAndReadTime;
        ImageView bookmark;
        View titleAndMainImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                return;
            }
            titleAndMainImage = itemView.findViewById(R.id.title_and_main_image);
            articleTitle = itemView.findViewById(R.id.article_title);
            articleFlyTitle = itemView.findViewById(R.id.article_fly_title);
            article_image = itemView.findViewById(R.id.article_image);
            dateAndReadTime = itemView.findViewById(R.id.date_and_read_time);
            bookmark = itemView.findViewById(R.id.bookmark);
        }

    }
}
