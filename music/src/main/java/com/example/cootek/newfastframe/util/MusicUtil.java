package com.example.cootek.newfastframe.util;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.FileUtil;
import com.example.cootek.newfastframe.view.lrc.LrcRow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by COOTEK on 2017/8/10.
 */

public class MusicUtil {


    public static final Integer[] RANK_TYPE_LIST = new Integer[]{
            1, 2, 6, 7, 8, 9, 11, 14, 20, 21, 22, 23, 24, 25
    };
    public static final int FROM_RANK = 0;
    public static final String FROM = "from";
    public static final int FROM_ALBUM = 2;
    public static final String SHARED_PREFERENCES_NAME = "music";
    public static final String PLAY_MODE = "PLAY_MODE";
    public static final String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting/";
    public static final String DATA = "data";
    public static final int FROM_RECOMMEND = 1;
    public static final int FROM_BOTTOM_ALBUM = 3;
    public static final int BASE_TYPE_SEARCH_CONTENT = 1;
    public static final int FROM_SINGER = 4;
    public static final String ARTIST_ID = "ARTIST_ID";
    public static final int BASE_TYPE_ALBUM_CONTENT = 2;
    public static final int FROM_LOCAL = 1;
    public static final int FROM_RECENT = 2;

    public static Uri getAlbumArtUri(long paramInt) {
        return ContentUris.withAppendedId(Uri.parse("content://media/exjava.lang.Stringternal/audio/albumart"), paramInt);
    }


    public static String getLyricPath(long longId) {
        return FileUtil.getDefaultCacheFile(BaseApplication.getInstance()).getAbsolutePath() + "/music/lrc" + longId;
    }


    public static String getMusicLrcCacheDir() {
        return FileUtil.getDefaultCacheFile(BaseApplication.getInstance()).getAbsolutePath() + "/music/lrc";
    }


    public static String getMusicImageCacheDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/music/image";
    }


    public static List<LrcRow> parseLrcContent(File file) {
        List<LrcRow> rows = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is == null) {
                return null;
            }
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            rows = getLrcRows(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

    private static List<LrcRow> getLrcRows(String content) {

        if (TextUtils.isEmpty(content)) {
            return null;
        }
        BufferedReader br = new BufferedReader(new StringReader(content));

        List<LrcRow> lrcRows = new ArrayList<>();
        String lrcLine;
        try {
            while ((lrcLine = br.readLine()) != null) {
                List<LrcRow> rows = LrcRow.createRows(lrcLine);
                if (rows != null && rows.size() > 0) {
                    lrcRows.addAll(rows);
                }
            }
            Collections.sort(lrcRows);
            int len = lrcRows.size();
            for (int i = 0; i < len - 1; i++) {
                lrcRows.get(i).setTotalTime(lrcRows.get(i + 1).getTime() - lrcRows.get(i).getTime());
            }
            lrcRows.get(len - 1).setTotalTime(5000);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lrcRows;
    }

    public static String makeLrcTime(long milliSecs) {
        StringBuffer sb = new StringBuffer();
        long m = milliSecs / (60 * 1000);
        sb.append(m < 10 ? "0" + m : m);
        sb.append(":");
        long s = (milliSecs % (60 * 1000)) / 1000;
        sb.append(s < 10 ? "0" + s : s);
        String content = sb.toString();
        return content.equals("00:00") ? null : content;
    }

    public static String getRealUrl(String uri, int size) {
        int index = uri.lastIndexOf("@s_");
        if (index > 0) {
            uri = uri.substring(0, index);
            uri = uri + "@s_1,w_" + size + ",h_" + size;
        }
        return uri;
    }


}
