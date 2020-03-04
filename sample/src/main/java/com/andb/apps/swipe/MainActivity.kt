package com.andb.apps.swipe

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.andb.apps.swiper.SwipeStep
import com.andb.apps.swiper.swipeWith
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private val itemsList = listOf("Apple", "Banana", "Cranberry", "Fig", "Grape", "Honeydew", "Kiwi", "Lemon", "Mango", "Nectarine", "Orange", "Peach", "Raspberry", "Strawberry", "Tangerine", "Watermelon")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.app_name)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(itemsList)
        recyclerView.swipeWith {
            rightToLeft {
                threshold = 36
                step(124){
                    colorFun = { vh, dX ->
                        Color.BLUE.withAlpha(dX/endX)
                    }
                    icon(resources.getDrawable(R.drawable.ic_edit))
                    side = SwipeStep.SIDE_EDGE
                    action { vh->
                        Snackbar.make(recyclerView, "Step 1 of left swipe at position ${vh.adapterPosition}", Snackbar.LENGTH_SHORT).show()
                    }
                }
                endStep {
                    color(Color.RED)
                    icon(resources.getDrawable(R.drawable.ic_delete))
                    side = SwipeStep.SIDE_VIEW
                    action { vh->
                        Snackbar.make(recyclerView, "Last step of left swipe at position ${vh.adapterPosition}", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            leftToRight {
                threshold = 36
                endStep {
                    colorFun = {viewHolder, dX ->
                        val saturation: Float = (viewHolder.adapterPosition * .1f) % 1
                        Color.BLUE.withSaturation(saturation)
                    }
                }
            }
        }
    }

}
