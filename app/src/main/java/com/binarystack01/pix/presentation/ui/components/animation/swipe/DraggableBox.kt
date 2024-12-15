package com.binarystack01.pix.presentation.ui.components.animation.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//fun Modifier.draggableBox(
//    coroutine: CoroutineScope,
//    state: DraggableState,
//    visibleContent: MutableState<Boolean>,
//    screenWidth: Float,
//    decay: DecayAnimationSpec<Float>,
//    translationX: Animatable<Float, AnimationVector1D>,
//): Modifier {
//    return this then Modifier.draggable(
//        state = state,
//        orientation = Orientation.Horizontal,
//        onDragStopped = { velocity ->
//            val decayX = decay.calculateTargetValue(
//                translationX.value,
//                velocity
//            )
//            coroutine.launch {
//
//                val targetX = if (decayX > screenWidth * 0.5) {
//                    visibleContent.value = false
//                    screenWidth
//                } else {
//                    visibleContent.value = true
//                    0f
//                }
//
//                val reachTargetWithDecay =
//                    (decayX > targetX && targetX == screenWidth) ||
//                            (decayX < targetX && targetX == 0f)
//
//                if (reachTargetWithDecay) {
//                    translationX.animateDecay(
//                        initialVelocity = velocity,
//                        animationSpec = decay
//                    )
//                } else {
//                    translationX.animateTo(
//                        targetX,
//                        initialVelocity = velocity
//                    )
//                }
//            }
//        }
//    )
//}

@Composable
fun DraggableBox(
    modifier: Modifier = Modifier,
    coroutine: CoroutineScope,
    translationX: Animatable<Float, AnimationVector1D>,
    draggableState: DraggableState,
    screenWidth: Float,
    decay: DecayAnimationSpec<Float>,
    visibleContent: MutableState<Boolean>,
    onDragThreshold: Float,
    content: @Composable () -> Unit,
) {

    Box(
        modifier = modifier
            .draggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
                onDragStopped = { velocity ->
                    val decayX = decay.calculateTargetValue(
                        translationX.value,
                        velocity
                    )
                    coroutine.launch {

                        val targetX = if (decayX > screenWidth * onDragThreshold) {
                            visibleContent.value = false
                            screenWidth
                        } else {
                            visibleContent.value = true
                            0f
                        }

                        val reachTargetWithDecay =
                            (decayX > targetX && targetX == screenWidth) ||
                                    (decayX < targetX && targetX == 0f)

                        if (reachTargetWithDecay) {
                            translationX.animateDecay(
                                initialVelocity = velocity,
                                animationSpec = decay
                            )
                        } else {
                            translationX.animateTo(
                                targetX,
                                initialVelocity = velocity
                            )
                        }
                    }
                }
            )
    ) {
        content()
    }
}