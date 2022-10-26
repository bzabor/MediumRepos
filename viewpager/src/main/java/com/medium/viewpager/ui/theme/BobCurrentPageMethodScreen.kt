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
fun BobCurrentPageMethodScreen() {

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

        Box(
            modifier = Modifier
                .background(Color.Black)
                .padding(16.dp)
        )
        {
            // Lottie Animation
            LottieAnimationContent(currentPage = pagerState.currentPage)

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
                        SignUpButton()
                    }
                }
            }
        }
    }
}




val firstClipSpec = LottieClipSpec.Progress(min = 0f, max = 0.19f)
val secondClipSpec = LottieClipSpec.Progress(min = 0.19f, max = 0.38f)
val thirdClipSpec = LottieClipSpec.Progress(min = 0.38f, max = 0.57f)
val fourthClipSpec = LottieClipSpec.Progress(min = 0.57f, max = 0.76f)
val fifthClipSpec = LottieClipSpec.Progress(min = 0.76f, max = 1.0f)

@Composable
fun LottieAnimationContent(currentPage: Int) {

    Log.d("ZZZ", "ENTER LottieAnimationContent()")

    var prevPage by remember {
        mutableStateOf(-1)
    }

    var prevSpeed by remember {
        mutableStateOf(1F)
    }

    val clipSpec = remember {
        mutableStateOf<LottieClipSpec?>(firstClipSpec)
    }

    val lottieComposition = remember {
        mutableStateOf<LottieCompositionSpec>(LottieCompositionSpec.RawRes(R.raw.onboarding_carousel_test))
    }

    lottieComposition.value = LottieCompositionSpec.RawRes(R.raw.onboarding_carousel_test)

    var speed = prevSpeed
    if (currentPage != prevPage) {
        Log.d("ZZZ", "There is a CHANGE")

        if (currentPage > prevPage) {
            Log.d("ZZZ", "Setting speed to 1F")
            speed = 1F
        } else {
            // speed = if (currentPage == 0) 0F else -1F
            Log.d("ZZZ", "Setting speed to -1F")
            speed = -1F
        }
        prevPage = currentPage
        prevSpeed = speed


    } else {
        Log.d("ZZZ", "There is NO CHANGE ---------------------")

    }

    Log.d("ZZZ", "currentPage: $currentPage  prevPage: $prevPage speed: $speed")

    when (currentPage) {
        0 -> {
            clipSpec.value = if (speed > 0) firstClipSpec else secondClipSpec
        }
        1 -> {
            clipSpec.value = if (speed > 0) secondClipSpec else thirdClipSpec
        }
        2 -> {
            clipSpec.value = if (speed > 0) thirdClipSpec else fourthClipSpec
        }
        3 -> {
            clipSpec.value = if (speed > 0) fourthClipSpec else fifthClipSpec
        }
        4 -> {
            clipSpec.value = fifthClipSpec
        }
    }
    // TODO: Figure out the direction of swipe to play the partial animations in normal or reverse

//    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.onboarding_carousel_test))
    val composition by rememberLottieComposition(spec = lottieComposition.value)




    val progress by animateLottieCompositionAsState(
        composition = composition,
        clipSpec = clipSpec.value,
        iterations = 1,
        speed = speed
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
//        modifier = Modifier
//            .background(Color.Black)
    )
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


