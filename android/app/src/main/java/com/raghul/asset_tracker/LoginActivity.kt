package com.raghul.asset_tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.raghul.asset_tracker.config.RetrofitServiceBuilder
import com.raghul.asset_tracker.config.SharedPreferenceUtils
import com.raghul.asset_tracker.model.Authenticate
import com.raghul.asset_tracker.model.AuthenticationRequest
import com.raghul.asset_tracker.model.AuthenticationResponse
import com.raghul.asset_tracker.repository.LoginRepository
import com.raghul.asset_tracker.service.UserService
import com.raghul.asset_tracker.utils.CommonConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var submitButton: MaterialButton
    private lateinit var usernameText: EditText
    private lateinit var passwordText: EditText
    private lateinit var loginRepository: LoginRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userFromPref = SharedPreferenceUtils.getString(CommonConstants.USERNAME)
        if(userFromPref != null && userFromPref.trim() != ""){
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            finish()
        }
        setContentView(R.layout.activity_login)
        loginRepository = LoginRepository(this)

        submitButton = findViewById(R.id.submit)
        submitButton.setOnClickListener(submitListener)
        usernameText = findViewById(R.id.username_edit_text)
        passwordText = findViewById(R.id.password_edit_text)

    }

    private val submitListener: View.OnClickListener = View.OnClickListener {
        val username = usernameText.text.toString()
         val password = passwordText.text.toString()
        println(username)
        println(password)

        loginRepository.authenticateUser(username,password){
            SharedPreferenceUtils.putString(CommonConstants.USERNAME,username)
            SharedPreferenceUtils.putString(CommonConstants.PASSWORD,password)
            SharedPreferenceUtils.putString(CommonConstants.TOKEN,it.jsonToken!!)
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            finish()
        }





    }
}