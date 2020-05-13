package com.xuwanjin.inchoate;

import android.util.Log;

import androidx.navigation.NavController;

import com.xuwanjin.inchoate.model.Article;
import com.xuwanjin.inchoate.model.Issue;
import com.xuwanjin.inchoate.model.Paragraph;
import com.xuwanjin.inchoate.model.archive.CoverContent;
import com.xuwanjin.inchoate.model.week.WeekFragment;
import com.xuwanjin.inchoate.model.week.WeekPart;
import com.xuwanjin.inchoate.model.week.WeekSection;
import com.xuwanjin.inchoate.model.week.WeekText;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.xuwanjin.inchoate.InchoateApplication.sArticleLinkedHashMap;


public class Utils {
    public static void navigationController(NavController navController, int resId) {
        if (navController != null) {
            navController.navigate(resId);
        }
    }

    public static List<Article> getWholeArticle(WeekFragment weekFragment) {
        List<Article> allArticleList = new ArrayList<>();
        List<WeekPart> weekPartList = weekFragment.data.section.hasPart.parts;

        //1.  weekPartList 是获取了一周的所有文章的json 数据
        for (WeekPart weekPart : weekPartList) {
            // weekPartList.get(0).text  获取 text 部分的 json 数据
//            List<WeekText> weekTextList = weekPartList.get(0).text;
//            weekTextList.get(0)  获取第一层数据

//          weekText.type == p 的表示段落
//          WeekText weekText = weekTextList.get(0);  // 获取的
            //2. weekPart.text 获取的是一篇文章的所有的 json 数据
            String audioUrl = "";
            float audioDuration = 0;
            String imageUrl = "";
            if (weekPart.audio.main != null) {
                audioUrl = weekPart.audio.main.url.canonical;
                audioDuration = weekPart.audio.main.duration;
            }
            if (weekPart.image != null && weekPart.image.main != null) {
                imageUrl = weekPart.image.main.url.canonical;
            }
            String articleSection = weekPart.print.section.sectionName;
            String articleUrl = weekPart.url.canonical;

            Article article = new Article();
            article.audioUrl = audioUrl;
            article.audioDuration = audioDuration;
            article.imageUrl = imageUrl;
            article.section = articleSection;
            article.title = weekPart.print.title;
            article.flyTitle = weekPart.print.flyTitle;
            article.articleUrl = articleUrl;
            article.mainArticleImage = imageUrl;
            article.date = Utils.digitalDateSwitchToEnglishFormat(weekPart.published.substring(0, 10));
            article.paragraphList = new ArrayList<>();
            StringBuilder articleBuilder = new StringBuilder();
            for (WeekText weekText0 : weekPart.text) {
                // weekText0.children 第一个 children列表里data 字段, 合并成一个段落
                StringBuilder paragraphBuilder = new StringBuilder();
                // 3. 获取段落的所有数据
                for (WeekText weekText1 : weekText0.children) {
                    if (weekText1.children != null) {
                        List<WeekText> children = weekText1.children;
                        for (WeekText w : children) {
                            paragraphBuilder.append(w.data);
                        }
                    } else { //(weekText1.children == null)
                        if (weekText1.type.equals("text")) {
                            paragraphBuilder.append(weekText1.data);
                        }
                    }
                }
                Paragraph paragraph = getParagraph(paragraphBuilder);

                if (paragraph != null) {
                    paragraph.articleName = article.title;
                    article.paragraphList.add(paragraph);
                }
                articleBuilder.append(paragraphBuilder);
            }
            article.content = articleBuilder.toString();
            allArticleList.add(article);
        }
        return allArticleList;
    }

    public static Paragraph getParagraph(StringBuilder paragraphBuilder) {
        Paragraph paragraph = new Paragraph();
        if (paragraphBuilder != null && !paragraphBuilder.toString().trim().isEmpty()) {
            paragraph.paragraph = paragraphBuilder.toString();
            return paragraph;
        }
        return null;
    }

    public static Issue getIssue(WeekFragment weekFragment) {
        Issue issue = new Issue();
        WeekSection weekSection = weekFragment.data.section;
        CoverContent coverContent = weekSection.image.cover.get(0);
        issue.headline = coverContent.headline;
        issue.issueUrl = weekSection.url.canonical;
        issue.coverImageUrl = coverContent.url.canonical;
        issue.containArticle = getWholeArticle(weekFragment);
        issue.issueDate = Utils.digitalDateSwitchToEnglishFormat(weekSection.datePublished.substring(0, 10));
        List<String> sectionList = new ArrayList<>();
        for (Article a : issue.containArticle) {
            if (!sectionList.contains(a.section)) {
                sectionList.add(a.section);
            }
        }
        issue.categorySection = sectionList;
        sArticleLinkedHashMap = getArticleListBySection(issue);
        return issue;
    }
    public static LinkedHashMap<String, List<Article>> getArticleListBySection(Issue issue) {
        LinkedHashMap<String, List<Article>> articleLinkedHashMap= new LinkedHashMap<>();
        List<Article> articleList = issue.containArticle;
        List<Article> list = new ArrayList<>();
        for (Article article : articleList) {
            if (articleLinkedHashMap.get(article.section) == null){
                list = new ArrayList<>();
            }else {
                list = articleLinkedHashMap.get(article.section);
            }
            list.add(article);
            articleLinkedHashMap.put(article.section, list);
        }
        return articleLinkedHashMap;
    }
    public static int getArticleSumBySection(int sectionPosition) {
        LinkedHashMap<String, List<Article>> maps = sArticleLinkedHashMap;
        int i = 0;
        int sum = 0;
        for(Iterator pairs = maps.entrySet().iterator(); pairs.hasNext() && i < sectionPosition; i++) {
            Map.Entry pair = (Map.Entry) pairs.next();
            List<Article> articleList = (List<Article>) pair.getValue();
            sum += articleList.size();
        }
        return sum;
    }



    public static String getDurationFormat(float duration) {
        int minute = (int) (duration / 60);  // 63
        int seconds = (int) (duration % 60);
        if (minute < 10 && seconds < 10) {
            return "0" + minute + ":" + "0" + seconds;
        }
        if (minute < 10 && seconds > 10) {
            return "0" + minute + ":" + seconds;
        }
        if (minute > 10 && seconds > 10) {
            return  minute + ":" + seconds;
        }
        if (minute > 10 && seconds < 10) {
            return minute + ":" + "0:" + seconds;
        }
        return null;
    }
    public static String digitalDateSwitchToEnglishFormat(String dateString) {
        Log.d("Matthew", "digitalDateSwitchToEnglishFormat: dateString = " +dateString);
        if (dateString.trim().equals("")){
            return null;
        }
        //2020-05-09 ====>  May 9th 2020
        String[] dateArray = dateString.split("-");
        String year = dateArray[0];
        String day ;
        switch (dateArray[2]) {
            case "01":
                day = "1st";
                break;
            case "02":
                day = "2nd";
                break;
            case "03":
                day = "3rd";
                break;
            case "21":
                day = "21st";
                break;
            case "22":
                day = "22nd";
                break;
            case "23":
                day = "23rd";
                break;
            case "31":
                day = "31st";
                break;
            default:
                String trimDay;
                if (dateArray[2].startsWith("0")){
                    trimDay = dateArray[2].substring(1,2);
                    day = trimDay+"th";
                }else {
                    day = dateArray[2] + "th";
                }
                break;
        }

        String month ;
        switch (dateArray[1]) {
            case "01":
                month = "Jan";
                break;
            case "02":
                month = "Feb";
                break;
            case "03":
                month = "Mar";
                break;
            case "04":
                month = "Apr";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "Jun";
                break;
            case "07":
                month = "Jul";
                break;
            case "08":
                month = "Aug";
                break;
            case "09":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            case "12":
                month = "Dec";
                break;
            default:
                month = "";
                break;
        }

        return month + " " + day + " " + year;
    }

}
