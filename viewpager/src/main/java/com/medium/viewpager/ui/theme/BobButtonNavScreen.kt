package com.medium.viewpager.ui.theme

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.google.accompanist.pager.*
import com.medium.viewpager.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BobButtonNavScreen() {
//    var isSwipeRight: Boolean  by remember { mutableStateOf(true) }

    /**
     * Modifier.pointerInput() is used to register drag events.
     * x value captures the horizontal drag amount.
     * It registers log events only when added to UI Content's(see comment below)
     * Column parameter but doing so causes the HorizontalPagerScreen not to swipe anymore.
     * Could be used to set the isSwipeRight state to figure out which direction the user
     * swiped.
     *
    Surface(modifier = Modifier
    .fillMaxSize()
    .pointerInput(Unit) {
    detectDragGestures { change, dragAmount ->
    change.consume()
    val (x,y) = dragAmount
    when {
    x > 0 -> { isSwipeRight = true
    Log.d("CarouselPager", "Swiped Right")}
    x < 0 -> { isSwipeRight = false
    Log.d("CarouselPager", "Swiped Left")}
    }
    }
    }) {}
     */


    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .background(color = Color.Black)
    )
    {
        val items = createItems()
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()

        var speed by remember {
            mutableStateOf(1F)
        }

        fun nextPage(): Int {
            return if (pagerState.currentPage < 4) pagerState.currentPage + 1 else 4
        }
        fun prevPage(): Int {
            return if (pagerState.currentPage > 0) pagerState.currentPage - 1 else 0
        }

        fun speedChange(newSpeed: Float): Unit {
            coroutineScope.launch {
                speed = newSpeed
                if (speed < 0) {
                    pagerState.scrollToPage(prevPage())
                } else if (speed > 0) {
                    pagerState.scrollToPage(nextPage())
                }
            }
        }

        Box(
            modifier = Modifier
                .background(Color.Black)
                .padding(16.dp)
        )
        {
            // Lottie Animation
            LottieAnimationContentButtonNav(currentPage = pagerState.currentPage, speed = speed)

            // Full Screen Horizontal Pager
            HorizontalPagerScreen(items, pagerState)

            // UI Content
            Column {
                // Top Row
                Box(
                    modifier = Modifier
                        .weight(0.1f)
                        .background(Color.Black)
                ) {
                    TopRowContent()
                }
                // Adds additional space
                Box(modifier = Modifier.weight(0.5f)) {
                }
                // Pager Indicator
                Box(
                    modifier = Modifier
                        .weight(0.1f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Column {
                        PagerIndicator(pagerState = pagerState)
                        Text(
                            modifier = Modifier
                                .background(Color.LightGray),
                            color = Color.Black,
                            text = "PAGE: ${pagerState.currentPage}"
                        )
                    }
                }
                // Text Content
                Crossfade(
                    targetState = pagerState.currentPage,
                    animationSpec = tween(2000),
                    modifier = Modifier.weight(0.2f)
                ) { it ->
                    Box {
                        TextContent(items = items, currentPage = it)
                    }
                }
                // Sign Up Button
                Box(
                    modifier = Modifier
                        .weight(0.1f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        SignUpButton("back", speedChange = ::speedChange)
                        SignUpButton("next", speedChange = ::speedChange)
                    }
                }
            }
        }
    }
}





@Composable
fun LottieAnimationContentButtonNav(currentPage: Int, speed: Float) {

    Log.d("ZZZ", "ENTER LottieAnimationContent()")

    val clipSpec = remember {
        mutableStateOf<LottieClipSpec?>(firstClipSpec)
    }

    val lottieComposition = remember {
        mutableStateOf<LottieCompositionSpec>(LottieCompositionSpec.RawRes(R.raw.onboarding_carousel_test))
    }

    lottieComposition.value = LottieCompositionSpec.RawRes(R.raw.onboarding_carousel_test)

    when (currentPage) {
        0 -> {
            clipSpec.value = firstClipSpec
        }
        1 -> {
            clipSpec.value = secondClipSpec
        }
        2 -> {
            clipSpec.value = thirdClipSpec
        }
        3 -> {
            clipSpec.value = fourthClipSpec
        }
        4 -> {
            clipSpec.value = fifthClipSpec
        }
    }
    // TODO: Figure out the direction of swipe to play the partial animations in normal or reverse

//    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.onboarding_carousel_test))
    val composition by rememberLottieComposition(spec = lottieComposition.value)

    Log.d("ZZZ", "currentPage: $currentPage  speed: $speed")

    val progress by animateLottieCompositionAsState(
        composition = composition,
        clipSpec = clipSpec.value,
        iterations = 1,

        /**
         * speed is the parameter that controls the animation will be played in normal or reverse
         * 1.0f for normal and -1.0f is for reverse
         */
//        speed = 1.0f
//        speed = (targetPage - currentPage).toFloat()
//        speed = newSpeed
        speed = speed
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
//        modifier = Modifier
//            .background(Color.Black)
    )
}





@Composable
fun SignUpButton(label: String, speedChange: (sp: Float) -> Unit) {
    Button(
        onClick = {
            if (label == "back") speedChange(-1F) else speedChange(1F)
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(Color.Blue),
        modifier = Modifier.width(150.dp)
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.White, textAlign = TextAlign.Center)
    }
}

private fun createItems() = listOf(
    HorizontalPagerContent(
        title = "Share your ideas whenever and where ever.",
        description = "Ideas worth spreading. No boundaries are good enough. Let's go infinity and beyond!",
    ),
    HorizontalPagerContent(
        title = "Connect with others like you.",
        description = "Connect with other people and increase your network and net worth to new horizons.",
    ),
    HorizontalPagerContent(
        title = "Keep climbing to new heights.",
        description = "Never look behind you. Keep moving forward. And one day, you might see the top.",
    ),
    HorizontalPagerContent(
        title = "Juggle files with a single tap.",
        description = "Make sure to align yourself between folders and practice levitation doing so.",
    ),
    HorizontalPagerContent(
        title = "A Big Step For Humanity",
        description = "We love exploring new places and collect rocks from other planets!",
    )
)


