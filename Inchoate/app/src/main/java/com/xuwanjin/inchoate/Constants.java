package com.xuwanjin.inchoate;

/**
 * @author Matthew Xu
 */
public class Constants {
    public static final String ECONOMIST_RSS_URL = "https://www.economist.com/audio-edition-podcast/gjnM3ZCaCYmUq40rM%24uSaA/index.xml";
    public static final String COMMON_URL = "https://www.economist.com/graphql?version=v1&query=query";
    public static final String TODAY_SECTION_QUERY_URL = COMMON_URL + "%20TodaySectionQuery%20%7B%20canonical:%20canonical(ref:%20%22/content/8mmm7h9v7arvfpvn4n20hakmg4ugatur%22)%20%7B%20id%20type%20hasPart%20%7B%20parts%20%7B%20id%20headline%20type%20hasPart%20%7B%20parts%20%7B%20isPartOf%20%7B%20id%20context%20%7B%20title:%20headline%20flyTitle:%20subheadline%20rubric:%20description%20image%20%7B%20main%20%7B%20url%20%7B%20canonical%20%7D%20width%20height%20id%20%7D%20promo%20%7B%20url%20%7B%20canonical%20%7D%20width%20height%20id%20%7D%20%7D%20id%20%7D%20%7D%20...%20on%20Content%20%7B%20source:%20channel%20%7B%20id%20headline%20%7D%20id%20type%20byline%20key:%20tegID%20title:%20headline%20flyTitle:%20subheadline%20rubric:%20description%20published:%20datePublished%20lastModified:%20dateModified%20tegID%20author%20%7B%20headline%20id%20%7D%20publication%20%7B%20title:%20headline%20url%20%7B%20canonical%20%7D%20id%20%7D%20articleSection%20%7B%20internal%20%7B%20id%20sectionName:%20headline%20%7D%20%7D%20print%20%7B%20section%20%7B%20id%20tegID%20sectionName:%20headline%20%7D%20title:%20headline%20flyTitle:%20subheadline%20rubric:%20description%20%7D%20image%20%7B%20main%20%7B%20url%20%7B%20canonical%20%7D%20width%20height%20id%20%7D%20inline%20%7B%20url%20%7B%20canonical%20%7D%20width%20height%20id%20%7D%20%7D%20url%20%7B%20canonical%20%7D%20audio%20%7B%20main%20%7B%20id%20duration(format:%20%22seconds%22)%20url%20%7B%20canonical%20%7D%20source:%20channel%20%7B%20id%20headline%20%7D%20%7D%20%7D%20text(format:%20%22json%22)%20dateline%20%7D%20id%20%7D%20%7D%20%7D%20%7D%20%7D%7D&variables=%7B%7D\n";
    public static final String ARCHIVE_QUERY_URL = COMMON_URL + "%20ArchiveQuery(%20$size:%20Int)%20%7B%20section:%20canonical(ref:%20%22/content/d06tg8j85rifiq3oo544c6b9j61dno2n%22)%20%7B%20...ArchiveFragment_section_18PEfK%20id%20%7D%7Dfragment%20ArchiveFragment_section_18PEfK%20on%20Content%20%7B%20id%20type%20hasPart(from:%200,%20size:%20$size,%20sort:%20%22datePublished:desc%22)%20%7B%20parts%20%7B%20url%20%7B%20canonical%20%7D%20id%20type%20datePublished%20image%20%7B%20cover%20%7B%20headline%20width%20height%20url%20%7B%20canonical%20%7D%20regionsAllowed%20id%20%7D%20%7D%20%7D%20%7D%7D&variables=%7B%22size%22:null%7D\n";
    public static final String WEEK_FRAGMENT_COMMON_URL = COMMON_URL + "%20WeekFragmentQuery(%20$path:%20String!)%20%7B%20section:%20canonical(ref:%20$path)%20%7B%20...WeekFragment_section%20id%20%7D%7Dfragment%20WeekFragment_section%20on%20Content%20%7B%20id%20type%20dateModified%20datePublished%20hasPart(size:%20100,%20sort:%20%22publication.context.position%22)%20%7B%20parts%20%7B%20...%20on%20Content%20%7B%20source:%20channel%20%7B%20id%20headline%20%7D%20id%20type%20byline%20key:%20tegID%20title:%20headline%20flyTitle:%20subheadline%20rubric:%20description%20published:%20datePublished%20lastModified:%20dateModified%20tegID%20author%20%7B%20headline%20id%20%7D%20publication%20%7B%20title:%20headline%20url%20%7B%20canonical%20%7D%20id%20%7D%20articleSection%20%7B%20internal%20%7B%20id%20sectionName:%20headline%20%7D%20%7D%20print%20%7B%20section%20%7B%20id%20tegID%20sectionName:%20headline%20%7D%20title:%20headline%20flyTitle:%20subheadline%20rubric:%20description%20%7D%20image%20%7B%20main%20%7B%20url%20%7B%20canonical%20%7D%20width%20height%20id%20%7D%20inline%20%7B%20url%20%7B%20canonical%20%7D%20width%20height%20id%20%7D%20%7D%20url%20%7B%20canonical%20%7D%20audio%20%7B%20main%20%7B%20id%20duration(format:%20%22seconds%22)%20url%20%7B%20canonical%20%7D%20source:%20channel%20%7B%20id%20headline%20%7D%20%7D%20%7D%20text(format:%20%22json%22)%20dateline%20%7D%20id%20%7D%20%7D%20image%20%7B%20cover%20%7B%20width%20height%20headline%20url%20%7B%20canonical%20%7D%20regionsAllowed%20id%20%7D%20%7D%20url%20%7B%20canonical%20%7D%7D&variables=%7B%22path%22:%22/content/";
    public static final String TAIL = "%22%7D";
    public static final String WEEK_FRAGMENT_QUERY_URL_01 = WEEK_FRAGMENT_COMMON_URL + "stl9iqjg96dmv43dkqg2q2jfqh2870g0" + TAIL;
    public static final String WEEK_FRAGMENT_QUERY_05_30_URL = "4fjub8gf5d9c9nfb169062fdf114u3i3";
    public static final String ESPRESSO_SECTION_QUERY_URL = COMMON_URL + "%20EspressoSectionQuery(%20$espressoRef:%20String!)%20%7B%20espresso:%20canonical(ref:%20$espressoRef)%20%7B%20...EspressoFragment_espresso%20id%20%7D%7Dfragment%20EspressoFragment_espresso%20on%20Content%20%7B%20id%20type%20hasPart(size:%201,%20sort:%20%22datePublished:desc%22)%20%7B%20parts%20%7B%20id%20type%20hasPart(sort:%20%22publication.context.position:asc,datePublished:desc%22)%20%7B%20parts%20%7B%20...%20on%20Content%20%7B%20source:%20channel%20%7B%20id%20headline%20%7D%20id%20type%20byline%20key:%20tegID%20title:%20headline%20flyTitle:%20subheadline%20rubric:%20description%20published:%20datePublished%20lastModified:%20dateModified%20tegID%20author%20%7B%20headline%20id%20%7D%20publication%20%7B%20title:%20headline%20url%20%7B%20canonical%20%7D%20id%20%7D%20articleSection%20%7B%20internal%20%7B%20id%20sectionName:%20headline%20%7D%20%7D%20print%20%7B%20section%20%7B%20id%20tegID%20sectionName:%20headline%20%7D%20title:%20headline%20flyTitle:%20subheadline%20rubric:%20description%20%7D%20image%20%7B%20main%20%7B%20url%20%7B%20canonical%20%7D%20width%20height%20id%20%7D%20inline%20%7B%20url%20%7B%20canonical%20%7D%20width%20height%20id%20%7D%20%7D%20url%20%7B%20canonical%20%7D%20audio%20%7B%20main%20%7B%20id%20duration(format:%20%22seconds%22)%20url%20%7B%20canonical%20%7D%20source:%20channel%20%7B%20id%20headline%20%7D%20%7D%20%7D%20text(format:%20%22json%22)%20dateline%20%7D%20publication%20%7B%20datePublished%20regionsAllowed%20id%20%7D%20id%20%7D%20%7D%20%7D%20%7D%7D&variables=%7B%22espressoRef%22:%22/content/jakj5ed3rml75i8j0d5i74p8adf6eem4%22%7D\n";
    public static final String WEEK_INITIAL_QUERY_URL = COMMON_URL + "%20WeekInitialQuery%20%7B%20lastPrint:%20canonical(ref:%20%22/content/d06tg8j85rifiq3oo544c6b9j61dno2n%22)%20%7B%20id%20type%20hasPart(from:%200,%20size:%201,%20sort:%20%22datePublished:desc%22)%20%7B%20parts%20%7B%20id%20type%20datePublished%20%7D%20%7D%20%7D%7D&variables=%7B%7D\n";

    public static final String PENDING_DOWNLOAD_ISSUE_DATE = "download_issue_date";
    public static final String DOWNLOAD_ARTICLE = "download_article";

    public static final int WEEKLY_PLAYING_SOURCE = 1;
    public static final int BOOKMARK_PLAYING_SOURCE = 2;
    public static final int TODAY_PLAYING_SOURCE = 3;
    public static final int ARTICLE_DETAIL_PLAYING_SOURCE = 4;


    public static final String INCHOATE_PREFERENCE_FILE_NAME = "inchoate";
    // rewind_to_previous_article forward_to_next_article forward_to_next_paragraph rewind_to_previous_paragraph
    public static final String REWIND_OR_FORWARD_PREFERENCE = "rewind_or_forward";
    public static final String REWIND_BY_SECONDS_PREFERENCE = "rewind_by_seconds";
    public static final String FORWARD_BY_SECONDS_PREFERENCE = "forward_by_seconds";
    public static final String REWIND_PARAGRAPH_PREFERENCE = "rewind_by_paragraph";
    public static final String FORWARD_PARAGRAPH_PREFERENCE = "forward_by_paragraph";

    public static final String SKIP_DURATION_PREFERENCE = "skip_duration";
    public static final int SKIP_DURATION_10_SECONDS_PREFERENCE = 10000;
    public static final int SKIP_DURATION_20_SECONDS_PREFERENCE = 20000;
    public static final int SKIP_DURATION_30_SECONDS_PREFERENCE = 30000;
    public static final int SKIP_DURATION_DEFAULT_VALUE_PREFERENCE = SKIP_DURATION_30_SECONDS_PREFERENCE;

    public static final String NEWEST_ISSUE_DATE = "2020-05-30";
    public static final String CURRENT_DISPLAY_ISSUE_URL_ID = "current_display_issue_url_id";

}
