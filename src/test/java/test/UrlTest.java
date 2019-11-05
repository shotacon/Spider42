package test;

import top.shotacon.application.utils.UrlUtil;

public class UrlTest {

    static String url = "https://www.xvideos.com/video40576299/_";
    public static void main(String[] args) {
        String videoUrl = UrlUtil.getVideoUrl(url);
        
        System.out.println(videoUrl);
    }
}
