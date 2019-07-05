/*
 * Copyright (C) 2014 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>
 *
 * This file is part of Amaze File Manager.
 *
 * Amaze File Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.whty.eschoolbag.filemanager.util;


import com.whty.eschoolbag.filemanager.bean.FileInfo;

import java.io.File;
import java.util.Comparator;

public class FileListSorter implements Comparator<FileInfo> {


    //文件夹在前面
    private int dirsOnTop = 0;
    private int asc = 1;
    private int sort = 0;

    public FileListSorter(int dir, int sort, int asc) {
        this.dirsOnTop = dir;
        this.asc = asc;
        this.sort = sort;
    }


    public FileListSorter(int sort, int asc) {
        this.asc = asc;
        this.sort = sort;
    }

    private boolean isDirectory(File file) {
        return file.isDirectory();
    }

    /**
     * Compares two elements and return negative, zero and positive integer if first argument is
     * less than, equal to or greater than second
     */
    @Override
    public int compare(FileInfo info1, FileInfo info2) {

        File file1 = new File(info1.getFilePath());
        File file2 = new File(info2.getFilePath());

        if (dirsOnTop == 0) {
            if (isDirectory(file1) && !isDirectory(file2)) {
                return -1;


            } else if (isDirectory(file2) && !isDirectory(file1)) {
                return 1;
            }
        } else if (dirsOnTop == 1) {
            if (isDirectory(file1) && !isDirectory(file2)) {

                return 1;
            } else if (isDirectory(file2) && !isDirectory(file1)) {
                return -1;
            }
        }

        if (sort == 0) {

            // sort by type
            if (!file1.isDirectory() && !file2.isDirectory()) {

                final String ext_a = getExtension(file1.getName());
                final String ext_b = getExtension(file2.getName());


                final int res = asc * ext_a.compareTo(ext_b);
                if (res == 0) {
                    return asc * file1.getName().compareToIgnoreCase(file2.getName());
                }
                return res;
            } else {
                return asc * file1.getName().compareToIgnoreCase(file2.getName());
            }


        } else if (sort == 1) {

            // sort by name
            return asc * file1.getName().compareToIgnoreCase(file2.getName());


        } else if (sort == 2) {

            // sort by size
            if (!file1.isDirectory() && !file2.isDirectory()) {

                return asc * Long.valueOf(file1.length()).compareTo(file2.length());
            } else {

                return asc * file1.getName().compareToIgnoreCase(file2.getName());
            }

        } else if (sort == 3) {

            // sort by last modified
            return asc * Long.valueOf(file1.lastModified()).compareTo(file2.lastModified());
        }
        return 0;

    }

    private static String getExtension(String a) {
        return a.substring(a.lastIndexOf(".") + 1).toLowerCase();
    }

}
