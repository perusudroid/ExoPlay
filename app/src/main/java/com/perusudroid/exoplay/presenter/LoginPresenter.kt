package com.perusudroid.exoplay.presenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ServerValue
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.perusudroid.exoplay.R
import com.perusudroid.exoplay.common.Constants
import com.perusudroid.exoplay.model.FirebaseModel
import com.perusudroid.exoplay.presenter.ipresenter.ILoginPresenter
import com.perusudroid.exoplay.view.iview.ILoginView
import java.util.HashMap

class LoginPresenter(val iLoginView: ILoginView) : BasePresenter(iLoginView), ILoginPresenter{



    private lateinit var auth: FirebaseAuth
    private var idToken : String? = null
    private var name : String? = null
    private var email : String? = null
    private var photo : String? = null

    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreatePresenter(bundle: Bundle?) {
        super.onCreatePresenter(bundle)
        Firebase.setAndroidContext(iLoginView.getActivity())

        if(iLoginView.hasNetworkConnection()){

        }else{
            iLoginView.showMessage("No internet connection found!")
        }
    }

    override fun doLogin() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(iLoginView.getActivity().getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(iLoginView.getActivity(), gso)

        auth = FirebaseAuth.getInstance()

        signIn()
    }


    override fun onActivityResultPresenter(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == Constants.RC.GOOGLE_REQUEST) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)
                idToken = account?.idToken
                name = account?.displayName
                email = account?.email
                photo = account?.photoUrl.toString()
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.e(TAG, "Google sign in failed", e)
            }
        }
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)


        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(iLoginView.getActivity()) { task ->
                    if (task.isSuccessful) {

                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        createUserInFirebaseHelper()

                        iLoginView.startLaunchActivity()

                    } else {
                        iLoginView.showMessage(task.exception.toString())
                        Log.w(TAG, "signInWithCredential:failure", task.exception)

                    }

                }
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        iLoginView.getActivity().startActivityForResult(signInIntent, Constants.RC.GOOGLE_REQUEST)
    }


    private fun createUserInFirebaseHelper() {

        //Since Firebase does not allow "." in the key name, we'll have to encode and change the "." to ","
        // using the encodeEmail method in class Utils

        val encodedEmail = email?.toLowerCase()?.replace(".", ",")

        //create an object of Firebase database and pass the the Firebase URL
        val userLocation = Firebase(Constants.FireBase.FIREBASE_URL_USERS).child(encodedEmail)

        //Add a Listerner to that above location
        userLocation.addListenerForSingleValueEvent(object : com.firebase.client.ValueEventListener {
            override fun onDataChange(dataSnapshot: com.firebase.client.DataSnapshot) {
                if (dataSnapshot.value == null) {
                    /* Set raw version of date to the ServerValue.TIMESTAMP value and save into dateCreatedMap */
                    val timestampJoined = HashMap<String, Any>()
                    timestampJoined[Constants.FireBase.FIREBASE_PROPERTY_TIMESTAMP] = ServerValue.TIMESTAMP

                    // Insert into Firebase database
                    val newUser = FirebaseModel(name, photo, encodedEmail, timestampJoined)
                    userLocation.setValue(newUser)

                    iLoginView.showMessage("Account created!")

                   iLoginView.startLaunchActivity()
                }
            }

            override fun onCancelled(firebaseError: FirebaseError) {

                Log.d(TAG, iLoginView.getActivity().getString(R.string.log_error_occurred) + firebaseError.message)
                //hideProgressDialog();
                if (firebaseError.code == FirebaseError.EMAIL_TAKEN) {
                } else {
                    Toast.makeText(iLoginView.getActivity(), firebaseError.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


}