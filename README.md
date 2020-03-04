# Swiper
Kotlin DSL for RecyclerView swipe actions

Works with any RecyclerView implementation that supports ItemTouchHelper (should be all)

## Setup

#### Example
```kotlin
recyclerView.swipeWith{
    rightToLeft {
        threshold = 36
        step(124){
            colorFun = { vh, dX ->
                Color.BLUE.withAlpha(dX/endX)
            }
            icon(resources.getDrawable(R.drawable.ic_edit))
            side = SwipeStep.SIDE_EDGE
            action { vh->
                Log.d("swiper", "swiped first step of position ${vh.adapterPosition}")
            }
        }
        endStep {
            color(Color.RED)
            icon(resources.getDrawable(R.drawable.ic_delete))
            side = SwipeStep.SIDE_VIEW
            action { vh->
                Log.d("swiper", "swiped end step of position ${vh.adapterPosition}")
            }
        }
    }
}
```