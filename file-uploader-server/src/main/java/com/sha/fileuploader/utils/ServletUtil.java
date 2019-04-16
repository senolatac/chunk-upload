package com.sha.fileuploader.utils;

import com.sha.fileuploader.model.ContentRange;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServletUtil {

    public static HttpHeaders createRange(String range){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.RANGE);
        headers.add(HttpHeaders.RANGE, range);
        return headers;
    }

    public static HttpHeaders createLocation(String location){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION);
        headers.add(HttpHeaders.LOCATION, location);
        return headers;
    }

    public static HttpHeaders createLocationAndRange(String range, String location){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION + "," + HttpHeaders.RANGE);
        headers.add(HttpHeaders.LOCATION, location);
        headers.add(HttpHeaders.RANGE, range);
        return headers;
    }

    public static String getAuthorization(Pattern authorizationPattern, final HttpServletRequest request){
        Matcher matcher = authorizationPattern.matcher(request.getHeader(HttpHeaders.AUTHORIZATION));
        String token = null;
        if(matcher.matches()){
            token = matcher.group( 1 );
        }
        return token;
    }

    public static ContentRange getContentRange(Pattern contentRangePattern, final HttpServletRequest request){
        ContentRange contentRange = new ContentRange();
        Matcher matcher = contentRangePattern.matcher(request.getHeader(HttpHeaders.CONTENT_RANGE));
        int currentChunk = 0;
        long end = 0, start = 0, length = 0, chunk = 0;
        if(matcher.matches()){
            start = Long.parseLong( matcher.group( 1 ) );
            end = Long.parseLong( matcher.group( 2 ) ) + 1L;
            length = Long.parseLong( matcher.group( 3 ) );
            chunk = end -start;
            currentChunk = (int) (start / (chunk));
        }
        contentRange.setCurrentChunk(currentChunk);
        contentRange.setStartByte(start);
        contentRange.setEndByte(end);
        contentRange.setSize(length);
        contentRange.setTotalChunk((int)(length / chunk) + 1);
        return contentRange;
    }

    public static void sendFile(final HttpServletRequest req, final HttpServletResponse res, final String discName,
                                final String mimeType) {
        OutputStream out = null;
        FileInputStream in = null;
        try {
            final File file = new File(discName);
            if(!file.exists()) {
                System.out.println("File is not exist.");
                return;
            }

            res.setContentType(mimeType);
            res.setContentLength(Long.valueOf(file.length()).intValue());
            res.setHeader("Content-Disposition", "inline; filename=\"" + discName + "\"");
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            res.setDateHeader("Expires", 0); // Proxies.
            out = res.getOutputStream();
            in = new FileInputStream(file);
            int byteCount = -1;
            final int oneKb = 8096;
            final byte[] bytes = new byte[oneKb];
            while((byteCount = in.read(bytes)) != -1) {
                out.write(bytes, 0, byteCount);
            }
        }
        catch(final IOException e) {
            //e.printStackTrace();
        }
        finally {
            if(out != null) {
                try {
                    out.close();
                }
                catch(final IOException e) {
                    //e.printStackTrace();
                }
            }
            if(in != null) {
                try {
                    in.close();
                }
                catch(final IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }

}
