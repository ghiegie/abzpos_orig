//package com.abztrakinc.ABZPOS.ADMIN;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.drive.Drive;
//import com.google.android.gms.drive.DriveResourceClient;
//
//public class GoogleDriveManager {
//
//    private static final String TAG = "GoogleDriveManager";
//    private DriveResourceClient mDriveResourceClient;
//    private Context mContext;
//
//    public GoogleDriveManager(Context context) {
//        mContext = context;
//        GoogleSignInOptions signInOptions =
//                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestScopes(Drive.SCOPE_FILE)
//                        .requestEmail()
//                        .build();
//
//        GoogleSignInClient signInClient = GoogleSignIn.getClient(context, signInOptions);
//        mDriveResourceClient = Drive.getDriveResourceClient(context, signInClient);
//    }
//
//    public void signIn() {
//        // Implement the sign-in process for Google Drive here
//        // For example:
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    // Method to create new contents for a file
//    public void createFileContents() {
//        // Ensure mDriveResourceClient is not null
//        if (mDriveResourceClient == null) {
//            Log.e(TAG, "DriveResourceClient is null");
//            return;
//        }
//
//
//        // Create new contents
//        mDriveResourceClient.createContents()
//                .addOnSuccessListener(contents -> {
//                    // Handle successful creation of contents
//                    Log.d(TAG, "New contents created successfully");
//                    // You can use the contents here to write data to a file
//                })
//                .addOnFailureListener(e -> {
//                    // Handle failure
//                    Log.e(TAG, "Failed to create new contents", e);
//                });
//    }
//}
