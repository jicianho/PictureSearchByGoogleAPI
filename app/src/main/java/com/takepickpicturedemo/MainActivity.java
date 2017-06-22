package com.takepickpicturedemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private ImageView imageView;
    private TextView textView;
    private TextView textView2;
    private TextView textViewKcal;
    //    private TableLayout tableLayout;
    private GridView gridView;

    private File file;
    private static final int REQUEST_TAKE_PICTURE_SMALL = 0;
    private static final int REQUEST_TAKE_PICTURE_LARGE = 1;
    private static final int REQUEST_PICK_PICTURE = 2;

    final Bitmap[] Bmp_list = new Bitmap[100];  // "final" added
    svm_parameter _param;
    svm_problem _prob;
    String _model_file;
    String Gobal_pass_img_path;
    String fileName = getSDPath() + "/" + "caroies";
//svm_parameter：SVM的參數
//svm_problem：之後會儲存要被分析的資料
//_model_file：model檔案的路徑

    private static final String TAG = "MainActivity";

    Bitmap picture;
    Bitmap changeIm;
    Mat change_ori_Mat;
    private Scalar mBlobColorHsv;
    private Scalar mBlobColorRgba;
    private ColorBlobDetector mDetector;
    private Mat mSpectrum;
    private Size SPECTRUM_SIZE;
    private boolean mIsColorSelected = false;
    private Scalar CONTOUR_COLOR;
    int color_r = 1, color_g = 2, color_b = 3;
    public double r;
    public double g;
    public double b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.tvImageHW);
        textView2 = (TextView) findViewById(R.id.textViewRS);
        textViewKcal = (TextView) findViewById(R.id.textViewKcal);

        mBlobColorHsv = new Scalar(255);
        mBlobColorRgba = new Scalar(255);
        CONTOUR_COLOR = new Scalar(255, 0, 0, 255);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;//圖片寬高都為原來的二分之一，即圖片為原來的四分之一


    }
    //check opencv
    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    imageView.setOnTouchListener(MainActivity.this);

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onResume() {
        Log.i(TAG, "Called onResume");
        super.onResume();

        Log.i(TAG, "Trying to load OpenCV library");
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, this, mOpenCVCallBack)) {
            Log.e(TAG, "Cannot connect to OpenCV Manager");
        }
    }

    public void onTakePictureSmallClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (isIntentAvailable(this, intent)) {
            startActivityForResult(intent, REQUEST_TAKE_PICTURE_SMALL);
        } else {
            Toast.makeText(this, R.string.msg_NoCameraAppsFound,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onTakePictureLargeClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        file = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        if (isIntentAvailable(this, intent)) {
            startActivityForResult(intent, REQUEST_TAKE_PICTURE_LARGE);
        } else {
            Toast.makeText(this, R.string.msg_NoCameraAppsFound,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onPickPictureClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        file = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        startActivityForResult(intent, REQUEST_PICK_PICTURE);
    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    Uri uri;

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE_SMALL:
                    picture = (Bitmap) intent.getExtras().get("data");
                    changeIm = picture.copy(Bitmap.Config.ARGB_8888, true); //copy
                    int[][] imRed = new int[picture.getWidth()][picture.getHeight()];
                    int[][] imGreen = new int[picture.getWidth()][picture.getHeight()];
                    int[][] imBlue = new int[picture.getWidth()][picture.getHeight()];
                    int count = 0;
                    for (int x = 0; x < picture.getWidth(); x++) {
                        for (int y = 0; y < picture.getHeight(); y++) {
                            int color = picture.getPixel(x, y);

                            imRed[x][y] = Color.red(color);
                            imGreen[x][y] = Color.red(color);
                            imBlue[x][y] = Color.red(color);
                            count++;
                        }
                    }

                    // 灰階影像
                    change_ori_Mat = new Mat();
                    //convert bitmap to opencv Mat   bitmap轉成Mat
                    Utils.bitmapToMat(changeIm, change_ori_Mat);
                    Mat grayMat = new Mat();
                    //'將彩色image轉換成gray level image 並儲存倒grayMat中
                    Imgproc.cvtColor(change_ori_Mat, grayMat, Imgproc.COLOR_RGB2GRAY);
                    //創建一個gra level image
                    Bitmap grayBmp = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.RGB_565);
                    //將矩陣grayMat轉換為gray image
                    Utils.matToBitmap(grayMat, grayBmp);
                    imageView.setImageBitmap(grayBmp);
                    imageView.setOnTouchListener(MainActivity.this);
                    break;

                case REQUEST_TAKE_PICTURE_LARGE:
                    picture = BitmapFactory.decodeFile(file.getPath());
                    imageView.setImageBitmap(picture);
                    textView.setText("width:" + picture.getWidth() + "heigh:" + picture.getHeight());

                    changeIm = picture.copy(Bitmap.Config.ARGB_8888, true); //copy
                    change_ori_Mat = new Mat();
                    Utils.bitmapToMat(changeIm, change_ori_Mat);
                    imageView.setOnTouchListener(MainActivity.this);
                    break;

                case REQUEST_PICK_PICTURE:
                    uri = intent.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, columns,
                            null, null, null);
                    if (cursor.moveToFirst()) {
                        String imagePath = cursor.getString(0);
                        cursor.close();
                        picture = BitmapFactory.decodeFile(imagePath);
                        imageView.setImageBitmap(picture);
                        textView.setText("width:" + picture.getWidth() + "heigh:" + picture.getHeight());
                        picture = small(picture); //把圖片縮小
                        changeIm = picture.copy(Bitmap.Config.ARGB_8888, true); //copy
                        change_ori_Mat = new Mat();

                        String picture_base64 = bitmapToBase64(picture);
                        Utils.bitmapToMat(changeIm, change_ori_Mat);
                        imageView.setOnTouchListener(MainActivity.this);
                        new GetPredict().execute(picture_base64);
                    }
                    break;
            }
        }
    }

    private static Bitmap small(Bitmap bitmap) {

        Matrix matrix = new Matrix();
        int newWidth = 640;
        int newHeight = 480;
        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
        if (scaleWidth<640 ||scaleHeight<480){
            return bitmap;
        }
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //建立一個AsyncTask執行緒進行圖片讀取動作，並帶入圖片連結網址路徑
//        new GetPredict().execute("http://images.google.com/searchbyimage?image_url=http://weknowyourdreamz.com/images/orange/orange-04.jpg");
//        new GetPredict().execute("");


        int cols = change_ori_Mat.cols();
        int rows = change_ori_Mat.rows();

        int xOffset = (imageView.getWidth() - cols) / 2;
        int yOffset = (imageView.getHeight() - rows) / 2;

        int x = (int) event.getX() - xOffset;
        int y = (int) event.getY() - yOffset;

        Log.i(TAG, "Touch image coordinates: (" + x + ", " + y + ")");

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

        Rect touchedRect = new Rect();

        touchedRect.x = (x > 4) ? x - 4 : 0;
        touchedRect.y = (y > 4) ? y - 4 : 0;

        touchedRect.width = (x + 4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
        touchedRect.height = (y + 4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

        Mat touchedRegionRgba = change_ori_Mat.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width * touchedRect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;

        mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

        Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");

        r = (int) mBlobColorRgba.val[0];
        g = (int) mBlobColorRgba.val[1];
        b = (int) mBlobColorRgba.val[2];

        mDetector = new ColorBlobDetector();
        mDetector.setHsvColor(mBlobColorHsv);

        mSpectrum = new Mat();
        SPECTRUM_SIZE = new Size(200, 64);
        Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE);

        mIsColorSelected = true;
        if (mIsColorSelected) {
            mDetector.process(change_ori_Mat);
            List<MatOfPoint> contours = mDetector.getContours();

            Bitmap ResultBmp = BitmapFactory.decodeFile(file.getPath());
            ;

            Log.e(TAG, "Contours count: " + contours.size());
            Imgproc.drawContours(change_ori_Mat, contours, -1, CONTOUR_COLOR);

            Mat view2 = new Mat();
            //convert bitmap to opencv Mat   bitmap轉成Mat

            for (int a = 0; a < changeIm.getHeight(); a++) {
                for (int b = 0; b < changeIm.getWidth(); b++) {
                    changeIm.setPixel(b, a, Color.WHITE);
                }
            }

            Utils.bitmapToMat(changeIm, view2);
            Imgproc.drawContours(view2, contours, -1, CONTOUR_COLOR);

            ArrayList<Point> list_all_point = new ArrayList<Point>();
            Converters.Mat_to_vector_Point(contours.get(0), list_all_point);

            Utils.matToBitmap(view2, changeIm);
            Mat colorLabel = change_ori_Mat.submat(4, 68, 4, 68);
            colorLabel.setTo(mBlobColorRgba);

            Mat spectrumLabel = change_ori_Mat.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
            mSpectrum.copyTo(spectrumLabel);

            Utils.matToBitmap(change_ori_Mat, ResultBmp);
            imageView.setImageBitmap(ResultBmp);

//            if(!ResultBmp.isRecycled() ){
//                ResultBmp.recycle();   //回收圖片所占的記憶體
//                System.gc();  //提醒系統及時回收
//            }

        }
        touchedRegionRgba.release();
        touchedRegionHsv.release();

        return false; // don't need subsequent touch events
    }

    protected void prediction() {


        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        try {
            for (int i = 1; i < 10; i++) {

//                Map<String, Object> item = new HashMap<String, Object>();
//                ex_img = BitmapFactory.decodeFile(img_adress[index[i]], options);//依照距離輸出圖片
//                item.put("image", ex_img);
//                item.put("text", img_Distance[i]);
//                items.add(item);

            }
        } catch (Exception e) {
            // Log.e("log_tag", e.toString());
        }

        gridView = (GridView) findViewById(R.id.main_page_gridview);
        gridView.setNumColumns(3);


        // set adapter
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.grid_item, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});

        // 顯示Bitmap
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                boolean isBitmap = false;

                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageBitmap((Bitmap) data);

                    isBitmap = true;
                }

                return isBitmap;
            }
        });

        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判斷sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//獲取跟目錄
        }
        return sdDir.toString();
    }

    private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }

    private class GetPredict extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(MainActivity.this);
            progressBar.setMessage("圖片解析中...視網路情況而定約需數十秒...");
            progressBar.setCancelable(false);
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.show();
            //初始化進度條並設定樣式及顯示的資訊。

        }

        @Override
        protected String doInBackground(String... params) {
            int progress = 0;

            publishProgress(progress += 10);
            String title_word = "";
            String link = "http://www.google.com/";
            String[] titlelist = new String[100];
            String[] link_list=new String[100];
//            String[] link_list = new String[100];
            Bitmap bmp ;
            try {

                try{
                    JSONObject JObj_all = new JSONObject();
                    JSONObject JObj_data = new JSONObject();
                    String post_info = getImgurContent("41debcab6785c2e",params[0]);

                    JObj_all = new JSONObject(post_info);

                    String data = JObj_all.getString("data");
                    JObj_data = new JSONObject(data);
                    link = JObj_data.getString("link");

                    Log.e("getImgurContent", post_info + "link ====" + link);
                }catch (Exception e){
                    Log.e("getImgurContent", e.toString());
                }

                String searchlink ="http://images.google.com/searchbyimage?image_url=";
                String syn_searchlink = searchlink + link;
                URL url = new URL(syn_searchlink);
                publishProgress(progress += 15);
                Document xmlDoc = Jsoup.parse(url, 3000); //使用Jsoup jar 去解析
                publishProgress(progress += 15);

                Elements title = xmlDoc.select("a");
                int comp = -1;
                int count = 0;
                int title_lenth = title.size();

                for (int i = 0; i < title_lenth; i++) {
                    titlelist[i] = title.get(i).toString();
                    comp =titlelist[i].indexOf("_gUb");
                    if ( comp != -1) {
                        title_word = title.get(i).text();
                        break;
                    } else
                        title_word = "Please try the other picures";
                }

                title = xmlDoc.select("div[class=rg_meta]");

                title_lenth = title.size();

                for (int i = 0; i < title_lenth; i++) {
                    link_list[count] = title.get(i).text();
                    try{
                        JSONObject JObj_link ;

                        JObj_link = new JSONObject(link_list[count]);

                        link_list[count] = JObj_link.getString("ou");

                        Log.e("ou Content", link_list[count]);
                    }catch (Exception e){
                        Log.e("ou Content", e.toString());
                    }
                        System.out.print(titlelist[count]);
                    count++;
                }

                        getUrlPic(link_list);

                publishProgress(progress += 15);

                //注意: 因為有好多個td 我想要取得的是<td>樂</td> 是第2個td 所以填get(1)
            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            publishProgress(progress = 100);

            return title_word;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //執行中 可以在這邊告知使用者進度
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            //取得更新的進度
        }

        @Override
        protected void onPostExecute(String string) {
            //執行後 完成背景任務
            super.onPostExecute(string);
            double calorie = 0.0;
            String final_predict = "預測結果為：" + string;
            textView.setText(final_predict);

            CalorieComparison cc = new CalorieComparison();
            try {
                calorie = cc.matching_database(string);
            }catch(Exception e){
                Log.e("calorie",e.toString());
            }
            final_predict = "卡路里為：" + String.valueOf(calorie) + "KCal/100g";
            textViewKcal.setText(final_predict);

            String[] test=new String[2];
            setBmp(test);
            progressBar.dismiss();
            //當完成的時候，把進度條消失
        }
    }


    public String getImgurContent(String clientID, String pic_base64) throws Exception {
        URL url;
        url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String data = URLEncoder.encode("image", "UTF-8") + "="
                + URLEncoder.encode(pic_base64, "UTF-8");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        conn.connect();
        StringBuilder stb = new StringBuilder();
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        wr.close();
        rd.close();


        return stb.toString();
    }

    public synchronized void getUrlPic(String[] url) {

        Bitmap webImg = null;
        for(int i=0 ; i<url.length;i++) {
            try {
                URL imgUrl = new URL(url[i]);
                HttpURLConnection httpURLConnection
                        = (HttpURLConnection) imgUrl.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                int length = (int) httpURLConnection.getContentLength();
                int tmpLength = 512;
                int readLen = 0, desPos = 0;
                byte[] img = new byte[length];
                byte[] tmp = new byte[tmpLength];
                if (length != -1) {
                    while ((readLen = inputStream.read(tmp)) > 0) {
                        System.arraycopy(tmp, 0, img, desPos, readLen);
                        desPos += readLen;
                    }
                    webImg = BitmapFactory.decodeByteArray(img, 0, img.length);
                    if (desPos != length) {
                        throw new IOException("Only read" + desPos + "bytes");
                    }
                }
                httpURLConnection.disconnect();
            } catch (IOException e) {
                Log.e("IOException", e.toString());
            }
            Bmp_list[i] = webImg;
        }

    }

    public void setBmp (String[] string){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;//圖片寬高都為原來的二分之一，即圖片為原來的四分之一
        Bitmap bitmap;

        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        try {
            for (int i = 0; i < 9; i++) {

                Map<String, Object> item = new HashMap<String, Object>();
//                bitmap = getUrlPic(string[i]);
                item.put("image", Bmp_list[i]);
                item.put("text", "test");
                items.add(item);
                System.out.print("success put " + String.valueOf(i));

            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }



        gridView = (GridView) findViewById(R.id.main_page_gridview);
        gridView.setNumColumns(3);


        // set adapter
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.grid_item, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});

        // 顯示Bitmap
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                boolean isBitmap = false;

                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageBitmap((Bitmap) data);

                    isBitmap = true;
                }

                return isBitmap;
            }
        });

        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }



}