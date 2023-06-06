package com.example.myrealm;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.engine.CropEngine;
import com.luck.picture.lib.engine.CropFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private Button btnCamera;
    private Button btnGallery;
    private ImageView ivImage;

    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ivImage = findViewById(R.id.ivImage);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission(CAMERA)) {
                    ActivityCompat.requestPermissions(ImageActivity.this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
                } else {
                    //already granted
                    openCamera();
                }
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission(READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(ImageActivity.this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PICK_REQUEST);
                } else {
                    //already granted
                    chooseAlbum();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(ImageActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();

                openCamera();
            } else {
                Toast.makeText(ImageActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_REQUEST) {
            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(ImageActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();

                chooseAlbum();
            } else {
                Toast.makeText(ImageActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void chooseAlbum() {
//        PictureSelector.create(this)
//                .openGallery(SelectMimeType.ofImage())
//                .setImageEngine(GlideEngine.createGlideEngine())
//                .forResult(new OnResultCallbackListener<LocalMedia>() {
//                    @Override
//                    public void onResult(ArrayList<LocalMedia> result) {
//                        String imgPath;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                            imgPath = result.get(0).getRealPath();
//                        } else {
//                            imgPath = result.get(0).getPath();
//                        }
//
//                        if (imgPath != null) {
//                            File file = new File(imgPath);
////                            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
//                            Glide.with(ImageActivity.this).load(file).into(ivImage);
////                            Bitmap bitmap = WonderfulBitmapUtils.zoomBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), ivImage.getWidth(), ivImage.getHeight());
////                            ivImage.setImageBitmap(bitmap);
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });

        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setMaxSelectNum(1)
                .setCropEngine(new CropFileEngine() {
                    @Override
                    public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {

                        UCrop uCrop = UCrop.of(srcUri, destinationUri, dataSource);
                        uCrop.setImageEngine(new UCropImageEngine() {
                            @Override
                            public void loadImage(Context context, String url, ImageView imageView) {
                                Glide.with(context).load(url).into(imageView);
                            }

                            @Override
                            public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
//                                Glide.with(context).load(url).into(imageView);
                            }
                        });
                        UCrop.Options options = new UCrop.Options();
                        options.setToolbarTitle("Crop Image UI");
                        options.setHideBottomControls(false);
                        uCrop.withOptions(options);
                        uCrop.withAspectRatio(16, 12);
                        uCrop.start(fragment.requireActivity(), fragment, requestCode);
                    }
                })
                .forResult(PICK_REQUEST);

    }

    private void openCamera() {
//        PictureSelector.create(this)
//                .openCamera(SelectMimeType.ofImage())
//                .forResult(new OnResultCallbackListener<LocalMedia>() {
//                    @Override
//                    public void onResult(ArrayList<LocalMedia> result) {
//                        String imgPath;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                            imgPath = result.get(0).getRealPath();
//                        } else {
//                            imgPath = result.get(0).getPath();
//                        }
//
//                        if (imgPath != null) {
//                            File file = new File(imgPath);
////                            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
//                            Glide.with(ImageActivity.this).load(file).into(ivImage);
////                            Bitmap bitmap = WonderfulBitmapUtils.zoomBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), ivImage.getWidth(), ivImage.getHeight());
////                            ivImage.setImageBitmap(bitmap);
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });

        PictureSelector.create(this)
                .openCamera(SelectMimeType.ofImage())
//                .setCropEngine(new CropFileEngine() {
//                    @Override
//                    public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
//
//                        UCrop uCrop = UCrop.of(srcUri,destinationUri,dataSource);
//                        uCrop.setImageEngine(new UCropImageEngine() {
//                            @Override
//                            public void loadImage(Context context, String url, ImageView imageView) {
//                                Glide.with(context).load(url).into(imageView);
//                            }
//
//                            @Override
//                            public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
////                                Glide.with(context).load(url).into(imageView);
//                            }
//                        });
//                        UCrop.Options options = new UCrop.Options();
//                        options.setToolbarTitle("Crop Camera UI");
//                        options.setHideBottomControls(false);
//                        uCrop.withOptions(options);
//                        uCrop.withAspectRatio(16,12);
//                        uCrop.start(fragment.requireActivity(),fragment,requestCode);
//                    }
//                })
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        String imgPath;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            imgPath = result.get(0).getRealPath();
                        } else {
                            imgPath = result.get(0).getPath();
                        }

                        if (imgPath != null) {
                            File file = new File(imgPath);
//                            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
//                            Glide.with(ImageActivity.this).load(file).into(ivImage);
//                            Bitmap bitmap = WonderfulBitmapUtils.zoomBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), ivImage.getWidth(), ivImage.getHeight());
//                            ivImage.setImageBitmap(bitmap);
                            Uri originUri = getImageUri(file, true);

                            Uri destinationUri = getImageUri(file, false);

                            if(originUri == null || destinationUri == null) return;

                            UCrop.Options options = new UCrop.Options();
                            options.setToolbarTitle("Crop Camera UI");
                            options.setHideBottomControls(false);
                            UCrop.of(originUri,destinationUri)
                                    .withMaxResultSize(1080,800)
                                    .withAspectRatio(16,12)
                                    .withOptions(options)
                                    .start(ImageActivity.this,CAMERA_REQUEST);
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    private Uri getImageUri(File imgFile, boolean isOrigin) {
        Uri destinationUri = null;
        String authority = BuildConfig.APPLICATION_ID;

        if (isOrigin) {
            if(imgFile != null){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    destinationUri = WonderfulFileUtils.getUriForFile(ImageActivity.this, imgFile);
                } else {
                    destinationUri = Uri.fromFile(imgFile);
                }
            }
        } else {
            String imageFileName = "Edited_" + System.currentTimeMillis() + ".jpg";
            File cropFile = WonderfulFileUtils.getCacheSaveFile(ImageActivity.this, imageFileName);

            if (cropFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            destinationUri = FileProvider.getUriForFile(MainActivity.this, authority+".provider", cropFile);
                    destinationUri = WonderfulFileUtils.getUriForFile(ImageActivity.this, cropFile);
                } else {
                    destinationUri = Uri.fromFile(cropFile);
                }
            }
        }

        return destinationUri;
    }

    private boolean checkPermission(String value) {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), value);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_REQUEST:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri imgUri = UCrop.getOutput(data);

                        List<LocalMedia> medias = PictureSelector.obtainSelectorList(data);
                        String path = medias.get(0).getCutPath();

                        if (path == null) return;

                        File file = new File(path);

                        Glide.with(ImageActivity.this).load(file).into(ivImage);
                    }
                }
                break;
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri imgUri = UCrop.getOutput(data);

//                        List<LocalMedia> medias = PictureSelector.obtainSelectorList(data);
//                        String path = medias.get(0).getCutPath();

                        if (imgUri == null) return;

                        File file = new File(imgUri.getPath());

                        Glide.with(ImageActivity.this).load(file).into(ivImage);
                    }
                }
                break;

        }
    }
}