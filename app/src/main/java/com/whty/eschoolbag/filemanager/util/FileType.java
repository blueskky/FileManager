package com.whty.eschoolbag.filemanager.util;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/7/3 14:36
 * description:
 */
public class FileType {


    public static final String[][] MATCH_ARRAY={
            //{后缀名，    文件类型}
            {".3gp",    "video/3gpp"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp4",    "video/mp4"},
            {".mpe",    "video/mpeg"},
            {".mpeg",    "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",    "video/mp4"},


            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mpga",    "audio/mpeg"},
            {".ogg",    "audio/ogg"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".rmvb",    "audio/x-pn-realaudio"},

            {".bmp",      "image/bmp"},
            {".gif",    "image/gif"},
            {".jpeg",    "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".png",    "image/png"},


            {".apk",    "application/vnd.android.package-archive"},
            {".bin",    "application/octet-stream"},

            {".c",        "text/plain"},
            {".class",    "application/octet-stream"},
            {".conf",    "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".docx",    "application/msword"},
            {".xls",    "application/msword"},
            {".xlsx",    "application/msword"},
            {".exe",    "application/octet-stream"},

            {".gtar",    "application/x-gtar"},
            {".gz",        "application/x-gzip"},
            {".h",        "text/plain"},
            {".htm",    "text/html"},
            {".html",    "text/html"},
            {".jar",    "application/java-archive"},
            {".java",    "text/plain"},

            {".js",        "application/x-javascript"},
            {".log",    "text/plain"},

            {".mpc",    "application/vnd.mpohun.certificate"},


            {".msg",    "application/vnd.ms-outlook"},
            {".pdf",    "application/pdf"},

            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".prop",    "text/plain"},
            {".rar",    "application/x-rar-compressed"},
            {".rc",        "text/plain"},

            {".rtf",    "application/rtf"},
            {".sh",        "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},

            {".wps",    "application/vnd.ms-works"},
            {".xml",    "text/plain"},
            {".z",        "application/x-compress"},
            {".zip",    "application/zip"},
            {"",        "*/*"}
    };

}
