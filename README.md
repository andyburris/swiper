# Swiper
Kotlin DSL for RecyclerView swipe actions

Works with any RecyclerView implementation that supports ItemTouchHelper (should be all)

## Setup

#### Example
```kotlin
recyclerView.swipeWith{
    left{
        step(dp = 124){
            color = Color.BLUE
            icon = ContextCompat.getDrawable(context, R.drawable.ic_edit)
            side = SwipeStep.SIDE_VIEW
            action = { pos ->
                Log.d("swiper", "swiped first step of position $pos")
            }
        }
        endStep{
            color = Color.RED
            icon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
            side = SwipeStep.SIDE_VIEW
            action = { pos ->
                Log.d("swiper", "swiped to end of position $pos")
            }
        }
    }
}
```