package com.xuwanjin.inchoate.model.today;

import com.xuwanjin.inchoate.model.ArticleSection;
import com.xuwanjin.inchoate.model.Publication;
import com.xuwanjin.inchoate.model.common.EconomistUrl;
import com.xuwanjin.inchoate.model.common.Source;
import com.xuwanjin.inchoate.model.week.Print;
import com.xuwanjin.inchoate.model.week.WeekAudio;
import com.xuwanjin.inchoate.model.week.WeekAuthor;
import com.xuwanjin.inchoate.model.week.WeekImage;
import com.xuwanjin.inchoate.model.week.WeekText;

import java.util.List;

public class TodaySecondParts {
    public List<IsPartOf> isPartOf;
    public Source source;
    public String id;
    public String[] type;
    public String byline;
    public String key;
    public String title;
    public String flyTitle;
    public String rubric;
    public String published;
    public String lastModified;
    public String tegID;
    public WeekAuthor[] author;
    public List<Publication> publication;
    public ArticleSection articleSection;
    public Print print;
    public WeekImage image;
    public EconomistUrl url;
    public WeekAudio audio;
    public List<WeekText> text;
    public String dateline;

}
