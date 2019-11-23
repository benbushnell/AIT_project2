package hu.ait.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

internal class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val buyTwoAnim = AnimationUtils.loadAnimation(this, R.anim.anim_slide1)
        val buyOneAnim = AnimationUtils.loadAnimation(this, R.anim.anim_slide2)
        val fadeAnim = AnimationUtils.loadAnimation(this, R.anim.anim_fade)

        buyOneAnim.setAnimationListener(
            object: Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }
                override fun onAnimationEnd(animation: Animation?) {
                    ivBuy1.startAnimation(fadeAnim)
                    ivBuy2.startAnimation(fadeAnim)
                    launchShoppingActivity()
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            }
        )

        ivBuy1.startAnimation(buyOneAnim)
        ivBuy2.startAnimation(buyTwoAnim)



    }


    fun launchShoppingActivity() {
        var intentShoppingList = Intent()
        intentShoppingList.setClass(this@SplashActivity, ListActivity::class.java)

        startActivity(intentShoppingList)
    }

}
