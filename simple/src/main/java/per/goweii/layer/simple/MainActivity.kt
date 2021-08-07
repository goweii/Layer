package per.goweii.layer.simple

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onBtnLayers(view: View) {
        startActivity(Intent(this@MainActivity, LayersSimpleActivity::class.java))
    }

    fun onBtnMaterial(view: View) {
        startActivity(Intent(this@MainActivity, MaterialSimpleActivity::class.java))
    }

    fun onBtnCupertino(view: View) {
        startActivity(Intent(this@MainActivity, CupertinoSimpleActivity::class.java))
    }
}