package com.horrorcore.eva_social.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HashtagExtractor {
    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#(\\w+)");

    public List<String> extractHashtags(String text) {
        List<String> hashtags = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return hashtags;
        }

        Matcher matcher = HASHTAG_PATTERN.matcher(text);
        while (matcher.find()) {
            String tag = matcher.group(1).toLowerCase();
            if (!hashtags.contains(tag)) {
                hashtags.add(tag);
            }
        }
        return hashtags;
    }
}
