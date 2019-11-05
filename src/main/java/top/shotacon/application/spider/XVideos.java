package top.shotacon.application.spider;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import top.shotacon.application.model.VideoInfo;
import top.shotacon.application.utils.UrlUtil;

public class XVideos {

    public static List<VideoInfo> doSpider(String text) {
        return parseHtml(UrlUtil.getVideoUrl(text));
    }
    
    
    private static List<VideoInfo> parseHtml(String html) {
        List<VideoInfo> resultList = new ArrayList<>();
        resultList.add(new VideoInfo("name", StringUtils.substringBetween(html, "html5player.setVideoTitle('", "');")));
        resultList.add(new VideoInfo("low", StringUtils.substringBetween(html, "html5player.setVideoUrlLow('", "');")));
        resultList.add(new VideoInfo("high", StringUtils.substringBetween(html, "html5player.setVideoUrlHigh('", "');")));
        resultList.add(new VideoInfo("thumb", StringUtils.substringBetween(html, "html5player.setThumbUrl('", "');")));
        
        return resultList;
    }

}
