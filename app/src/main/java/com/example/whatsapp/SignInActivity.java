package com.example.whatsapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.media3.common.util.Log;

import com.example.whatsapp.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    ActivitySignInBinding binding;
    // Credential Manager
    private CredentialManager credentialManager;
    private GetGoogleIdOption googleIdOption;
    // ActivityResultLauncher for Google Sign-In
//    private final ActivityResultLauncher<IntentSenderRequest> googleSignInLauncher =
//            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
//                if (result.getResultCode() == RESULT_OK) {
//                    try {
//                        // Extract the Google ID token from the result
//                        GoogleIdTokenCredential credential = GoogleIdTokenCredential.createFromIntent(result.getData());
//                        String idToken = credential.getIdToken();
//                        firebaseAuthWithGoogle(idToken);
//                    } catch (
//                            GoogleIdTokenParsingException e) {
//                        androidx.media3.common.util.Log.e(TAG, "Google ID token parsing failed", e);
//                        progressDialog.dismiss();
//                        Toast.makeText(SignInActivity.this, "Google Sign In Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    androidx.media3.common.util.Log.e(TAG, "Google Sign In failed. Result code: " + result.getResultCode());
//                    progressDialog.dismiss();
//                    Toast.makeText(SignInActivity.this, "Google Sign In Cancelled or Failed", Toast.LENGTH_LONG).show();
//                }
//            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("SignIn");
        progressDialog.setMessage("We're logging\nPlease wait...");


//        // Initialize Credential Manager
//        credentialManager = CredentialManager.create(this);
//        // Instantiate a Google sign-in request
//        googleIdOption = new GetGoogleIdOption.Builder()
//                .setFilterByAuthorizedAccounts(false)
//                .setServerClientId(getString(R.string.default_web_client_id))
//                .build();


//        binding.googleBtnLog.setOnClickListener(v -> signInWithGoogle());
//        if (binding.googleBtnLog != null) { // Make sure this ID exists in your layout
//            binding.googleBtnLog.setOnClickListener(v -> {
//                progressDialog.setTitle("Google Sign-In");
//                progressDialog.setMessage("Initializing Google Sign-In...");
//                progressDialog.show();
//                signInWithGoogle();
//            });
//        } else {
//            androidx.media3.common.util.Log.w(TAG, "signInWithGoogleBtn is not defined in the layout or binding is incorrect.");
//            // You might want to add a placeholder or ensure the button exists
//            // For now, let's add a Toast to indicate it's not set up.
//            // Toast.makeText(this, "Google Sign-In button not found!", Toast.LENGTH_LONG).show();
//        }
        binding.signInBtnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                signInWithEmailAndPassword(binding.tvEmailLog.getText().toString(),binding.tvPassLog.getText().toString());
            }
        });
        binding.tvGoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });
    }
    private void signInWithEmailAndPassword(String email,String password){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(SignInActivity.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(SignInActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


//    private void signInWithGoogle() {
//        GetCredentialRequest request = new GetCredentialRequest.Builder()
//                .addCredentialOption(googleIdOption)
//                .build();
//
//        // Use the MainThreadExecutor for UI-related tasks
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            credentialManager.getCredentialAsync(
//                    request,
//                    this, // Activity
//                    null, // CancellationSignal
//                    getMainExecutor(), // Executor
//                    new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
//                        @Override
//                        public void onResult(GetCredentialResponse result) {
//                            try {
//                                GoogleIdTokenCredential credential = GoogleIdTokenCredential.createFrom(result.getCredential().getData());
//                                String idToken = credential.getIdToken();
//                                firebaseAuthWithGoogle(idToken);
//                            } catch (GoogleIdTokenParsingException e) {
//                                Log.e(TAG, "Google ID token parsing failed onResult", e);
//                                progressDialog.dismiss();
//                                Toast.makeText(SignInActivity.this, "Google Sign In Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onError(@NonNull GetCredentialException e) {
//                            Log.e(TAG, "GetCredentialException: ", e);
//                            progressDialog.dismiss();
//                            // Handle specific errors for better UX
//                            // For example, if the user cancels: e instanceof GetCredentialCancellationException
//                            // If no accounts are found: e instanceof NoCredentialException
//                            Toast.makeText(SignInActivity.this, "Google Sign In Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//
//                            // Fallback to traditional Google Sign-In if needed (or guide user)
//                            // This part is more complex and depends on how you want to handle failures.
//                            // For now, we're just showing an error. The old API's beginSignIn is different.
//                            // If you need to handle `PendingIntent` from `GetCredentialException`,
//                            // you would launch it using the `googleSignInLauncher`.
//                            // Example: if (e instanceof GetCredentialPendingIntentException) {
//                            //    googleSignInLauncher.launch(new IntentSenderRequest.Builder(((GetCredentialPendingIntentException) e).getPendingIntent().getIntentSender()).build());
//                            // }
//                        }
//                    }
//            );
//        }
//    }
//
//    private void firebaseAuthWithGoogle(String idToken) {
//        progressDialog.setTitle("Google Sign-In");
//        progressDialog.setMessage("Authenticating with Firebase...");
//        // progressDialog.show(); // Already shown or should be shown before calling this
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        progressDialog.dismiss();
//                        if (task.isSuccessful()) {
//                            androidx.media3.common.util.Log.d(TAG, "firebaseAuthWithGoogle:success");
//                            // Sign in success, update UI with the signed-in user's information
//                            // You might want to check if it's a new user and save additional info
//                            // boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
//                            navigateToMainActivity();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            androidx.media3.common.util.Log.w(TAG, "firebaseAuthWithGoogle:failure", task.getException());
//                            Toast.makeText(SignInActivity.this, "Firebase Authentication Failed: " + task.getException().getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
//
//    private void navigateToMainActivity() {
//        Toast.makeText(SignInActivity.this, "Sign In Successful!", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        if (auth.getCurrentUser() != null) {
//            // User is already signed in, navigate to MainActivity
//            // You might want to skip this if you want the user to always see the SignInActivity
//            // unless a specific auto-sign-in flow is triggered.
//            // For now, let's keep it to redirect if already logged in.
//            // navigateToMainActivity();
//        }
//    }


}