/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.whty.eschoolbag.filemanager;

import android.Manifest;
import android.os.Environment;

public interface Config {


    String ClASS_FILE= Environment.getExternalStorageDirectory() + "/interactiveroom" +"/文件传输";


    String downloadpath1 = Environment.getExternalStorageDirectory() + "/eschoolHomework/download";
    String downloadpath2 = Environment.getExternalStorageDirectory() + "/eschoolbag_download";

    String ROOT_PATH = "/";

    String SDCARD_PATH = ROOT_PATH + "sdcard";

    String[] requestPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    String BUCKET_SCREENSHOT = "Screenshots";
    String BUCKET_CAMERA = "Camera";
    String TYPE_IMAGE = "Image";
    String TYPE_VIDEO = "Video";
    String TYPE_AUDIO = "Audio";
    String TYPE_DOC = "Document";

    public static final String TYPE_CLASS = "class";
    public static final String TYPE_FAVORITE = "favorite";
    public static final String TYPE_DOWNLOAD = "download";
    public static final String TYPE_CLEAN = "clean";

}
