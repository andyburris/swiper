# Swiper
Kotlin DSL for RecyclerView swipe actions

Works with any RecyclerView implementation that supports ItemTouchHelper (should be all)

## Setup

#### Example
```kotlin
recyclerView.swipeWith {
    rightToLeft {
        friction = .6f
        step(124){
            color(Color.BLUE)
            icon(resources.getDrawable(R.drawable.ic_edit))
            side = SwipeStep.SIDE_EDGE
            action { viewHolder ->
                Log.d("swiper", "left swipe at position ${vh.adapterPosition}")
            }
        }
    }
    leftToRight {
        threshold = 36
        endStep {
            colorFun = { viewHolder, dX ->
                val saturation: Float = (viewHolder.adapterPosition * .1f) % 1
                Color.BLUE.withSaturation(saturation)
            }
        }
    }
}
```

### Multiple Item Types
```kotlin
recyclerView.swipeWith {
    endStep(VIEW_TYPE_1) {
        color(Color.RED)
        icon(resources.getDrawable(R.drawable.ic_delete))
        side = SwipeStep.SIDE_VIEW
        action { vh ->
            Log.d("swiper", "left swipe for type 1 at position ${vh.adapterPosition}")
        }
    }
    endStep(VIEW_TYPE_2){
        color(Color.GREEN)
        icon(resources.getDrawable(R.drawable.ic_more_horiz))
        side = SwipeStep.SIDE_VIEW
        action { vh ->
            Log.d("swiper", "left swipe for type 2 at position ${vh.adapterPosition}")
        }
    }
}
```