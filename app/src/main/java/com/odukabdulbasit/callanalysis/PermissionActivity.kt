package com.odukabdulbasit.callanalysis

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_permission.*

class PermissionActivity : AppCompatActivity() {

    private val requestReadLog = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        val styledString = SpannableString("Bu uygulama size hizmet sunabilmek için arama kayıtlarınıza erişmektedir bilgilerinizin nasıl kullanıldığını öğrenmek için TIKLAYINIZ."
        )

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                //You could do anything you want here.
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://bit.ly/2qEui28")
                startActivity(openURL)
            }
        }

        styledString.setSpan(clickableSpan, 123, 134, 0)

        // make text italic
        styledString.setSpan(StyleSpan(Typeface.ITALIC), 123, 134, 0)
        styledString.setSpan(StyleSpan(Typeface.BOLD), 123, 134, 0)

        // change text color
        styledString.setSpan(ForegroundColorSpan(Color.BLUE), 123, 134, 0)

        // highlight text
        styledString.setSpan(BackgroundColorSpan(Color.WHITE), 123, 134, 0)

        // this step is mandated for the url and clickable styles.
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.textSize = 20f

        textView.text = styledString

    }

    fun kabul_et_Clicked(view : View){

        val intent = Intent(this, MainActivity ::class.java)
        intent.putExtra("kabul", "kabul")
        startActivity(intent)

    }

}
