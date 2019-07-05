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

package com.whty.eschoolbag.filemanager.bean;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.whty.eschoolbag.filemanager.util.MimeType;

import java.io.File;
import java.io.Serializable;

public class FileInfo implements Serializable , MultiItemEntity {

    public String fileName;
    public Uri uri;
    public String filePath;

    public long fileSize;

    public boolean IsDir;

    public int Count;

    public long ModifiedDate;

    public boolean Selected;

    public boolean canRead;

    public boolean canWrite;

    public boolean isHidden;

    public String mimeType;

    public String mediaType;

    public long dbId; // id in the database, if is from database

    public long duration; // only for video, in ms


    public FileInfo() {
    }

    public FileInfo(long id, String mimeType, long size, long duration, String name) {
        this.dbId = id;
        this.mimeType = mimeType;
        this.fileName = name;
        Uri contentUri;
        if (isImage()) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (isVideo()) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else {
            // ?
            contentUri = MediaStore.Files.getContentUri("external");
        }
        this.uri = ContentUris.withAppendedId(contentUri, id);
        this.fileSize = size;
        this.duration = duration;
    }


    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isImage() {
        return MimeType.isImage(mimeType);
    }

    public boolean isGif() {
        return MimeType.isGif(mimeType);
    }

    public boolean isVideo() {
        return MimeType.isVideo(mimeType);
    }


    public boolean isAudio() {
        return MimeType.isAudio(mimeType);
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Uri getUri() {
        return uri;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isDir() {
        return IsDir;
    }

    public void setDir(boolean dir) {
        IsDir = dir;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public long getModifiedDate() {
        if (ModifiedDate != 0) {
            return ModifiedDate;
        } else {
            return new File(filePath).lastModified();
        }
    }

    public void setModifiedDate(long modifiedDate) {
        ModifiedDate = modifiedDate;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public String getMimeType() {
        return mimeType;
    }

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public long getDuration() {
        return duration;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }



    @Override
    public int getItemType() {
        return 0;
    }
}
