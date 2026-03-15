package com.example.board.post.commons.utils;

import java.util.Map;

public class BearerHeaderUtils {
    private BearerHeaderUtils() {}

    public static String computeWWWAuthenticateHeaderValue(Map<String, String> parameters) {
        var wwwAuthenticate = new StringBuilder();
        wwwAuthenticate.append("Bearer");
        if(!parameters.isEmpty()) {
            wwwAuthenticate.append(" ");
            var i = 0;
            for(var entry : parameters.entrySet()) {
                wwwAuthenticate.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
                if(i != parameters.size() - 1) {
                    wwwAuthenticate.append(", ");
                }
                i++;
            }
        }
        return wwwAuthenticate.toString();
    }
}
