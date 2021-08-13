package online.rkmhikai.ui.authentication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import online.rkmhikai.R;
import online.rkmhikai.config.RequestSingletonVolley;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.URLs;
import online.rkmhikai.library.VolleyMultipartRequest;


public class UploadImageFragment extends Fragment {

    ImageView pPhoto;
    Button btnUpload, btnSelect;
    private int STORAGE_PERMISSION_CODE = 1;
    CardView cardSelect,cardUpload;
    ProgressBar uploadProgress;



    public UploadImageFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_upload_image, container, false);
        pPhoto = v.findViewById(R.id.ivPPhoto);
        btnUpload = v.findViewById(R.id.btnUpload);
        btnSelect = v.findViewById(R.id.btnSelect);
        cardSelect=v.findViewById(R.id.cardSelect);
        cardUpload=v.findViewById(R.id.crdUpload);
        uploadProgress = v.findViewById(R.id.uploadProgress);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfileAccount();
                //Progress bar show
                //hide Upload Button
            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    CropImage.startPickImageActivity(getContext(), UploadImageFragment.this);
                    Log.d("Crop Image", getActivity().toString());

                } else {

                    requestStoragePermission();

                }

            }
        });
        cardSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    CropImage.startPickImageActivity(getContext(), UploadImageFragment.this);
                    Log.d("Crop Image", getActivity().toString());

                } else {

                    requestStoragePermission();
                }
            }
        });
        cardUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    CropImage.startPickImageActivity(getContext(), UploadImageFragment.this);
                    Log.d("Crop Image", getActivity().toString());

                } else {

                    requestStoragePermission();
                }
            }
        });
        return v;
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("OnActivity Called", String.valueOf(requestCode));
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {


            Log.d("RequestCode", String.valueOf(requestCode));
            Log.d("Result_ok", String.valueOf(requestCode));

            if (resultCode == getActivity().RESULT_OK) {
                Log.d("Fired", String.valueOf(requestCode));
                Uri path = CropImage.getPickImageResultUri(requireContext(), data);
                startCrop(path);

            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                cardSelect.setVisibility(View.GONE);
                cardUpload.setVisibility(View.VISIBLE);
                Log.d("ForGlide", result.getUri().toString());
                Glide.with(this).load(result.getUri()).override(379, 413).into(pPhoto);

            }
        }
    }

    private void startCrop(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(getContext(), this);
    }


    private void saveProfileAccount() {
        // loading or check internet connection or something...
        // ... then
        btnUpload.setVisibility(View.GONE);
        uploadProgress.setVisibility(View.VISIBLE);
        String token = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getToken();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.uploadProfile, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Response",response.toString());

                try {

                    JSONObject result = new JSONObject(resultResponse);
                    int status = result.getInt("success");

//Constant.REQUEST_SUCCESS
                    if (status==1) {
                        // tell everybody you have succed upload image and post strings
                        //Move to Dashboard
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        ActivationFragment activationFragment = new ActivationFragment();
                        fragmentTransaction.replace(R.id.fragment_container, activationFragment, null);
                        fragmentTransaction.commit();
                    } else {
                        btnUpload.setVisibility(View.VISIBLE);
                        uploadProgress.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                        Log.i("Unexpected", "upload Failed");
                    }
                } catch (JSONException e) {
                    btnUpload.setVisibility(View.VISIBLE);
                    uploadProgress.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnUpload.setVisibility(View.VISIBLE);
                uploadProgress.setVisibility(View.GONE);
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                //Progress Bar hide
                //Upload Button Show
                //Display Message
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message+" Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message+ " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message+" Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getContext(), "Upload Fail! Please try again"+errorMessage, Toast.LENGTH_SHORT).show();
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("file", new DataPart("file_avatar.jpg", getFileDataFromDrawable( pPhoto.getDrawable()), "image/jpeg"));
                // params.put("cover", new DataPart("file_cover.jpg", getFileDataFromDrawable(mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        RequestSingletonVolley.getInstance(getContext()).addToRequestQueue(multipartRequest);
    }

    public static byte[] getFileDataFromDrawable( Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}