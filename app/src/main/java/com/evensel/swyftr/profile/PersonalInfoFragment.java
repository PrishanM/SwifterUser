package com.evensel.swyftr.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppController;
import com.evensel.swyftr.util.AppURL;
import com.evensel.swyftr.util.Constants;
import com.evensel.swyftr.util.JsonRequestManager;
import com.evensel.swyftr.util.Notifications;
import com.evensel.swyftr.util.ResponseModel;
import com.evensel.swyftr.util.ValidatorUtil;
import com.evensel.swyftr.util.VolleyMultipartRequest;
import com.evensel.swyftr.util.VolleySingleton;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class PersonalInfoFragment extends Fragment {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 120;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private CircularImageView circularImageView;
    private Uri fileUri;
    private View layout;
    private LayoutInflater inflate;
    private SharedPreferences profilePref,sharedPref;
    private EditText txtFirstName,mail,mobileNumber,fixedAddress,swyftrName,officeAddress;
    private TextView name;
    private Button btnSave;

    private ProgressDialog progress;

    private String token;



    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.personal_info_fragment, container, false);
        profilePref = getActivity().getSharedPreferences(Constants.PROFILE_PREF, Context.MODE_PRIVATE);
        sharedPref = getActivity().getSharedPreferences(Constants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
        token = sharedPref.getString(Constants.LOGIN_ACCESS_TOKEN, "");

        inflate = inflater;
        layout = inflate.inflate(R.layout.custom_toast_layout,(ViewGroup) rootView.findViewById(R.id.toast_layout_root));
        circularImageView = (CircularImageView)rootView.findViewById(R.id.imgProfilePic);

        txtFirstName = (EditText)rootView.findViewById(R.id.txtFullName);
        mail = (EditText)rootView.findViewById(R.id.txtEmailAddress);
        mobileNumber = (EditText)rootView.findViewById(R.id.txtMobileNumber);
        fixedAddress = (EditText)rootView.findViewById(R.id.txtAddress);
        swyftrName = (EditText)rootView.findViewById(R.id.txtSwyftName);
        officeAddress = (EditText)rootView.findViewById(R.id.txtSwyftAddress);
        name = (TextView) rootView.findViewById(R.id.name);

        txtFirstName.setText(profilePref.getString(Constants.PROFILE_NAME,""));
        mail.setText(profilePref.getString(Constants.PROFILE_EMAIL,""));
        mobileNumber.setText(profilePref.getString(Constants.PROFILE_PHONE,""));
        fixedAddress.setText(profilePref.getString(Constants.PROFILE_ADDRESS,""));
        swyftrName.setText(profilePref.getString(Constants.PROFILE_SWYFTR_NAME,""));
        officeAddress.setText(profilePref.getString(Constants.PROFILE_OFFICE,""));
        name.setText(profilePref.getString(Constants.PROFILE_NAME,""));



        btnSave = (Button)rootView.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtFirstName.getText().toString().isEmpty()){
                    Notifications.showToastMessage(layout,getActivity(),"Sorry!!! Full Name cannot be empty.").show();
                }else if(mail.getText().toString().isEmpty()){
                    Notifications.showToastMessage(layout,getActivity(),"Sorry!!! Email cannot be empty.").show();
                }else if(!ValidatorUtil.isValidEmailAddress(mail.getText().toString())){
                    Notifications.showToastMessage(layout,getActivity(),"Sorry!!! Invalid email address.").show();
                }else if(mobileNumber.getText().toString().isEmpty()){
                    Notifications.showToastMessage(layout,getActivity(),"Sorry!!! Mobile number cannot be empty.").show();
                }else if(fixedAddress.getText().toString().isEmpty()){
                    Notifications.showToastMessage(layout,getActivity(),"Sorry!!! Fixed address cannot be empty.").show();
                }else if(officeAddress.getText().toString().isEmpty()){
                    Notifications.showToastMessage(layout,getActivity(),"Sorry!!! Office address cannot be empty.").show();
                }else{
                    progress = ProgressDialog.show(getActivity(), null,
                            "Creating user...", true);
                    JsonRequestManager.getInstance(getActivity()).updateUserRequest(AppURL.APPLICATION_BASE_URL+AppURL.USER_UPDATE_URL, token,txtFirstName.getText().toString(),mail.getText().toString(),mobileNumber.getText().toString(),fixedAddress.getText().toString(),officeAddress.getText().toString(), requestCallback);
                }
            }
        });

        String imageUrl = profilePref.getString(Constants.PROFILE_PIC_URL, "");
        String imageUri = profilePref.getString(Constants.PROFILE_PIC,"");

        if(!imageUrl.isEmpty()){
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageLoader.get(imageUrl, new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERR", "Image Load Error: " + error.getMessage());
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        // load image into imageview
                        circularImageView.setImageBitmap(response.getBitmap());
                    }
                }
            });
        }else if(!imageUri.isEmpty()){
            circularImageView.setImageBitmap(profileImage(imageUri));
        }

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUploadDialog();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void showUploadDialog(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Change profile image");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        //arrayAdapter.add("View Image");
        arrayAdapter.add("Capture Now");
        arrayAdapter.add("Select from Gallery");


        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                if(strName.equalsIgnoreCase("Capture Now")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                }else if(strName.equalsIgnoreCase("Select from Gallery")){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            }
        });
        builderSingle.show();
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == -1) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setMessage("Are you sure want to upload image?");
                builderSingle.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveProfileAccount(true);
                    }
                });
                builderSingle.show();
            }
        }else if (requestCode == PICK_IMAGE_REQUEST){
            if (resultCode == -1) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setMessage("Are you sure want to upload image?");
                builderSingle.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File newFile = new File(AppController.getPath(getActivity(),data.getData()));
                        fileUri = Uri.fromFile(newFile);
                        saveProfileAccount(false);
                    }
                });
                builderSingle.show();
            }
        }
    }

    private Bitmap profileImage(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap realPhoto = BitmapFactory.decodeFile(path, options);
        return realPhoto;
    }

    private void saveProfileAccount(final boolean isFromCamera) {

        progress = ProgressDialog.show(getActivity(), null,
                "Uploading image...", true);

        String url = AppURL.APPLICATION_BASE_URL+AppURL.FILE_UPLOAD_URL+"?token="+token;

        final Bitmap photo = profileImage(fileUri.getPath());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        final byte[] byteArray = byteArrayOutputStream.toByteArray();

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                if(progress!=null)
                    progress.dismiss();
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    ObjectMapper mapper = new ObjectMapper();

                    if(result!=null){
                        ResponseModel responseModel = mapper.readValue(result.toString(), ResponseModel.class);
                        Notifications.showToastMessage(layout,getActivity(),responseModel.getMessage()).show();
                        if(isFromCamera){
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            Bitmap resizedBitmap = Bitmap.createBitmap(photo, 0, 0,
                                    photo.getWidth(), photo.getHeight(), matrix, true);
                            circularImageView.setImageBitmap(resizedBitmap);
                        }else{
                            circularImageView.setImageBitmap(photo);
                        }
                        SharedPreferences.Editor editor = profilePref.edit();
                        editor.putString(Constants.PROFILE_PIC, fileUri.getPath().toString());
                        editor.putString(Constants.PROFILE_PIC_URL, "");
                        editor.commit();

                        Intent intent = getActivity().getIntent();
                        intent.putExtra("FRAGMENT",4);
                        getActivity().finish();
                        startActivity(intent);

                    }else{
                        Notifications.showToastMessage(layout,getActivity(),"Error uploading image").show();
                    }
                } catch (Exception e) {
                    Notifications.showToastMessage(layout,getActivity(),"Error uploading image").show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progress!=null)
                    progress.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
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
                Log.i("Error", errorMessage);
                Notifications.showToastMessage(layout,getActivity(),errorMessage).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                params.put("profile_image", new DataPart("profile_pic.jpg", byteArray, "image/jpg"));

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest);
    }

    //Response callback for "User Profile Update"
    private final JsonRequestManager.updateUser requestCallback = new JsonRequestManager.updateUser() {

        @Override
        public void onSuccess(ResponseModel model) {

            if(progress!=null)
                progress.dismiss();

            if(model.getStatus().equalsIgnoreCase("success")){
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                // Setting Dialog Message
                alertDialog.setMessage("User information updated successfully");
                // Setting OK Button
                alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setSharedPrefData();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }else{
                Notifications.showToastMessage(layout,getActivity(),model.getMessage()).show();
            }


        }

        @Override
        public void onError(String status) {
            if(progress!=null)
                progress.dismiss();
            Notifications.showToastMessage(layout,getActivity(),status).show();
        }

        @Override
        public void onError(ResponseModel model) {
            if(progress!=null)
                progress.dismiss();
            String message = "";
            for (int i=0;i<model.getDetails().size();i++){
                message = message + "\n" + model.getDetails().get(i);
            }

            Notifications.showToastMessage(layout,getActivity(),message).show();
        }
    };

    private void setSharedPrefData(){
        SharedPreferences.Editor editor = profilePref.edit();
        editor.putString(Constants.PROFILE_NAME, txtFirstName.getText().toString());
        editor.putString(Constants.PROFILE_EMAIL, mail.getText().toString());
        editor.putString(Constants.PROFILE_ADDRESS, fixedAddress.getText().toString());
        editor.putString(Constants.PROFILE_PHONE, mobileNumber.getText().toString());
        editor.putString(Constants.PROFILE_SWYFTR_NAME, swyftrName.getText().toString());
        editor.putString(Constants.PROFILE_OFFICE, officeAddress.getText().toString());
        editor.commit();

        name.setText(txtFirstName.getText().toString());
    }
}
