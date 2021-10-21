package com.thiha.android4k.testfirebasevideostreaming.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.thiha.android4k.testfirebasevideostreaming.R
import com.thiha.android4k.testfirebasevideostreaming.view.activity.LoginActivity
import com.thiha.android4k.testfirebasevideostreaming.view.activity.MainActivity
import java.text.SimpleDateFormat


class ProfileFragment : Fragment() {

    private lateinit var backBtn: ImageButton
    private lateinit var logoutBtn: Button
    private lateinit var ivProfile: ImageView
    private lateinit var tvUserNameInput: TextView
    private lateinit var tvUserPhoneInput: TextView
    private lateinit var tvExpiryInput: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        (activity as MainActivity).supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn = view.findViewById(R.id.btnBack)
        logoutBtn = view.findViewById(R.id.btnLogout)
        tvExpiryInput = view.findViewById(R.id.tvExpiryDateInput)
        ivProfile = view.findViewById(R.id.ivProfile)
        tvUserPhoneInput = view.findViewById(R.id.tvUserPhoneInput)
        tvUserNameInput = view.findViewById(R.id.tvUserNameInput)

        val uidString = FirebaseAuth.getInstance().currentUser?.uid.toString()

        Firebase.firestore.collection("subscribed_users").document(uidString).get()
            .addOnSuccessListener {
                it.data?.also { data1 ->
                    tvUserNameInput.text = data1["name"]?.toString()
                    tvUserPhoneInput.text = data1["phone"]?.toString() ?: "No Phone"
                    val sfd = SimpleDateFormat("dd-MM-yyyy")
                    tvExpiryInput.text =
                        sfd.format((data1["expiry"] as Timestamp).toDate()).toString()
                }

            }

        logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            activity?.startActivity(
                Intent(
                    this.activity?.applicationContext,
                    LoginActivity::class.java
                )
            )
            activity?.finishAffinity()
        }

        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}