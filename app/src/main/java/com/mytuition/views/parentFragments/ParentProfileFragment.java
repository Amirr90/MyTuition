package com.mytuition.views.parentFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mytuition.databinding.FragmentParentProfileBinding;
import com.mytuition.models.ParentModel;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.mytuition.utility.AppConstant.USERS;
import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.AppUtils.isEmailValid;
import static com.mytuition.utility.Utils.getParentModel;
import static com.mytuition.utility.Utils.setParentModel;

public class ParentProfileFragment extends Fragment {
    private static final String TAG = "ParentProfileFragment";


    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String EMAIL = "email";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    FragmentParentProfileBinding profileBinding;
    NavController navController;

    ParentModel parentModel;


    boolean isPicChange = false;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileBinding = FragmentParentProfileBinding.inflate(getLayoutInflater());
        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        progressDialog = new ProgressDialog(requireActivity());

        parentModel = getParentModel(requireActivity());

        profileBinding.setUser(parentModel);

        profileBinding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAllFieldCheck() && null != getUid()) {
                    AppUtils.showRequestDialog(requireActivity());
                    getFirestoreReference().collection(USERS).document(getUid())
                            .update(getUserMap(parentModel))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    AppUtils.hideDialog();
                                    Toast.makeText(requireActivity(), "Profile Updated !!", Toast.LENGTH_SHORT).show();
                                    setParentModel(requireActivity(), parentModel);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AppUtils.hideDialog();
                            Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        profileBinding.tvChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) profileBinding.tvChangeProfile.getTag();
                selectImage(tag);
            }
        });
    }

    private void selectImage(int tag) {
        ImagePicker.Companion.with(this)
                .crop(4f, 4f)                    //Crop image(Optional), Check Customization for more option
                .compress(512)//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (null != data) {
                Uri uri = data.getData();
                profileBinding.profileImage.setImageURI(uri);
                isPicChange = true;
                Log.d(TAG, "onActivityResult: Uri" + data.getData());
                try {
                    uploadImageToFirebase(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else Log.d(TAG, "onActivityResult: No Data ");
        } else Log.d(TAG, "onActivityResult: resultCode not matched");


    }

    private void uploadImageToFirebase(Uri uri) throws FileNotFoundException {


        progressDialog.show();
        progressDialog.setMessage("Updating profile image,please wait...");
        final DocumentReference uploadImageUriRef = firestore.collection("Users").document(getUid());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();


        final String STORAGE_PATH = "profile_image/" + getUid() + "/" + System.currentTimeMillis() + ".jpg";
        StorageReference spaceRef = storageRef.child(STORAGE_PATH);

        Bitmap bitmap2 = ((BitmapDrawable) profileBinding.profileImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] compressData = baos.toByteArray();
        UploadTask uploadTask = spaceRef.putBytes(compressData);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setProgress((int) progress);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.child(STORAGE_PATH).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, Object> imageMap = new HashMap<>();
                        imageMap.put("image", uri.toString());

                        uploadImageUriRef.update(imageMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(requireActivity(), " Image Uploaded", Toast.LENGTH_SHORT).show();
                                setProfile();
                            }
                        });

                    }
                });

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                progressDialog.dismiss();
                Toast.makeText(requireActivity(), "Upload cancelled, try again", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(requireActivity(), "failed to upload Image " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setProfile() {
        if (null != getUid())
            AppUtils.getFirestoreReference().collection("Users").document(getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ParentModel parentModel = documentSnapshot.toObject(ParentModel.class);
                            setParentModel(requireActivity(), parentModel);
                        }
                    });
        else Toast.makeText(requireActivity(), "User not found !!", Toast.LENGTH_SHORT).show();
    }

    private Map<String, Object> getUserMap(ParentModel parentModel) {
        Map<String, Object> map = new HashMap<>();
        map.put(NAME, parentModel.getName());
        map.put(ADDRESS, parentModel.getAddress());
        map.put(EMAIL, parentModel.getEmail());
        return map;
    }

    private boolean isAllFieldCheck() {

        if (null == parentModel) {
            Log.d(TAG, "isAllFieldCheck: null");
            return false;
        } else if (profileBinding.editTextTextPersonName.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (profileBinding.editTextTextPersonEmail.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Enter email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isEmailValid(profileBinding.editTextTextPersonEmail.getText().toString())) {
            Toast.makeText(requireActivity(), "Enter valid email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (profileBinding.editTextTextPersonNumber.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Enter mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (profileBinding.editTextTextPersonAddress.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Enter address", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            parentModel.setName(profileBinding.editTextTextPersonName.getText().toString());
            parentModel.setAddress(profileBinding.editTextTextPersonAddress.getText().toString());
            parentModel.setEmail(profileBinding.editTextTextPersonEmail.getText().toString());
            return true;
        }
    }
}