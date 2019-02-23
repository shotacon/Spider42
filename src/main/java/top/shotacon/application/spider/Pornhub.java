package top.shotacon.application.spider;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

public class Pornhub {

    public static String doSpider(String basePath) throws Exception {

        String data = UrlUtil.getHtml(basePath);

        Pattern p = Pattern.compile("flashvars_.*;", Pattern.MULTILINE);
        Matcher matcher = p.matcher(data);
        if (!matcher.find()) {
            return "";
        }

        String group = matcher.group();
        group = group.substring(group.indexOf("=") + 1, group.length() - 1);
        JSONObject json = JSONObject.parseObject(group);

        StringBuffer sb = new StringBuffer();
        sb.append("title: ").append(json.getString("video_title")).append("\n");
        sb.append("duration: ").append(json.getInteger("video_duration")).append("\n");
        sb.append("image: ").append(json.getString("image_url")).append("\n");
        sb.append("link: ").append(json.getString("link_url")).append("\n");
        json.getJSONArray("mediaDefinitions").forEach(o -> {
            JSONObject obj = (JSONObject) o;
            String vu = obj.getString("videoUrl");
            if (!vu.isEmpty()) {
                sb.append("quality" + obj.getString("quality") + ": ").append(vu).append("\n");
            }
        });

        return sb.toString();
    }
}
