package com.whty.eschoolbag.filemanager.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tianqs on 2018/2/1.
 */

public class Utils {
    public static String images = Environment.getExternalStorageDirectory() + "/interactiveroom/images/";
    private final static String TAG = Utils.class.getSimpleName();

    public static int getDismension(Context context, int id) {
        return (int) context.getResources().getDimension(id);
    }

    public static int getDimensionPixelSize(Context context, int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

    public static Bitmap getBmpFromLocal(String filePath) {
        Bitmap bmp = null;
        try {
//            InputStream is = new FileInputStream(new File(filePath));
            bmp = BitmapFactory.decodeFile(filePath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bmp;
    }

    public static String write2Sd(String dirs, String fileName, byte[] bytes) {
        String filePath = null;
        File file = new File(dirs);
        if (!file.exists()) {
            file.mkdirs();
        }

        FileOutputStream fos = null;
        File f = new File(dirs, fileName);
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            fos = new FileOutputStream(f);
            fos.write(bytes);// 写入文件内容
            fos.flush();
            filePath = f.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("文件创建失败");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.err.println("文件流关闭失败");
                }
            }
        }

        return filePath;
    }

    public static String saveBitmap(String dirs, String fileName, Bitmap bmp) {
        String path = null;
        File file = new File(dirs);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        File f = new File(dirs, fileName);
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            fileOutputStream = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            path = f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
        return path;

    }

    public static float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public static Rect calculateTapArea(float x, float y, float coefficient, int width, int height) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / width * 2000 - 1000);
        int centerY = (int) (y / height * 2000 - 1000);

        int halfAreaSize = areaSize / 2;
        RectF rectF = new RectF(clamp(centerX - halfAreaSize, -1000, 1000)
                , clamp(centerY - halfAreaSize, -1000, 1000)
                , clamp(centerX + halfAreaSize, -1000, 1000)
                , clamp(centerY + halfAreaSize, -1000, 1000));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    public static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    public static void handleZoom(boolean isZoomIn, Camera camera) {
        Camera.Parameters params = camera.getParameters();
        if (params.isZoomSupported()) {
            int maxZoom = params.getMaxZoom();
            Log.v(TAG, "maxZoom = " + maxZoom);
            int zoom = params.getZoom();
            Log.v(TAG, "zoom = " + zoom);
            if (isZoomIn) {
                if (zoom >= maxZoom) {
                    return;
                }
                zoom++;
            } else if (zoom > 0) {
                if (zoom <= 1) {
                    return;
                }
                zoom--;
            }
            params.setZoom(zoom);
            camera.setParameters(params);
        } else {
            Log.i(TAG, "zoom not supported");
        }
    }

    public static Bitmap zoomInImage(Bitmap bmp, float w, float h) {
        if(bmp.getWidth() <= w && bmp.getHeight() <= h) {
            return bmp;
        }

        float rateWH = w / h;
        float rateWHBmp = (float) bmp.getWidth() / bmp.getHeight();
        float scaleRate;
        if (rateWHBmp >= rateWH) {
            scaleRate = w / bmp.getWidth();
        } else {
            scaleRate = h / bmp.getHeight();
        }

//        LogUtil.v("Utils","scaleRate = " + scaleRate);

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scaleRate, scaleRate);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
                matrix, true);
        bmp.recycle();
//        LogUtil.v("Utils","w = " + resizedBitmap.getWidth() + ",h = " + resizedBitmap.getHeight() + ",bytes count = " + resizedBitmap.getByteCount());
        return resizedBitmap;
    }

    /**
     * 检查app是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkAppInstalled(Context context, String packageName) {
        boolean isAppInstalled;
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            isAppInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            isAppInstalled = false;
        }
        return isAppInstalled;
    }



}
