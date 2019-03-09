package top.shotacon.application.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

import javafx.collections.FXCollections;
import top.shotacon.application.VideoInfo;

public class Pornhub {

    public static List<VideoInfo> doSpider(String basePath) throws Exception {

        String data = UrlUtil.getHtml(basePath);

        Pattern p = Pattern.compile("flashvars_.*;", Pattern.MULTILINE);
        Matcher matcher = p.matcher(data);
        if (!matcher.find()) {
            return FXCollections.observableArrayList();
        }

        String group = matcher.group();
        group = group.substring(group.indexOf("=") + 1, group.length() - 1);
        JSONObject json = JSONObject.parseObject(group);

        return handleJson(json);

//        StringBuffer sb = new StringBuffer();
//        sb.append("title: ").append(json.getString("video_title")).append("\n");
//        sb.append("duration: ").append(json.getInteger("video_duration")).append("\n");
//        sb.append("image: ").append(json.getString("image_url")).append("\n");
//        sb.append("link: ").append(json.getString("link_url")).append("\n");
//        json.getJSONArray("mediaDefinitions").forEach(o -> {
//            JSONObject obj = (JSONObject) o;
//            String vu = obj.getString("videoUrl");
//            if (!vu.isEmpty()) {
//                sb.append("quality" + obj.getString("quality") + ": ").append(vu).append("\n");
//            }
//        });
//
//        return sb.toString();
    }

    private static List<VideoInfo> handleJson(JSONObject json) {
        List<VideoInfo> resultList = new ArrayList<>();
        resultList.add(new VideoInfo("title", json.getString("video_title")));
        resultList.add(new VideoInfo("duration", json.getString("video_duration")));
        resultList.add(new VideoInfo("image", json.getString("image_url")));
        resultList.add(new VideoInfo("link", json.getString("link_url")));
        json.getJSONArray("mediaDefinitions").forEach(o -> {
            JSONObject obj = (JSONObject) o;
            String vu = obj.getString("videoUrl");
            if (!vu.isEmpty()) {
                resultList.add(new VideoInfo("quality" + obj.getString("quality"), vu));
            }
        });

        return resultList;
    }
}
