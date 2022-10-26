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
fun CarouselPagerScreen() {
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
            LottieAnimationContent(currentPage = pagerState.currentPage, speed = speed)

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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalPagerScreen(
    items: List<HorizontalPagerContent>,
    pagerState: PagerState
) {
    HorizontalPager(
        count = items.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .background(Color.Transparent)
    ) { page ->
        /**
         * One possible solution was to use the targetPage (deprecated) parameter
         * of the PagerState to figure out which direction the pages are moving towards.
         * However, once the half of the current page is dragged away from the screen,
         * the currentPage automatically updates to the targetPage value which makes it
         * very hard to capture by using (targetPage - currentPage).toFloat()
         */
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopRowContent() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
    )
    {
        val context = LocalContext.current
        // Removes extra 48dp touch target padding around the Icon Button
        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
            IconButton(
                onClick = {
                    Toast.makeText(context, "Pressed Up Button.", Toast.LENGTH_SHORT).show()
                }) {
                Icon(
                    Icons.Filled.KeyboardArrowLeft,
                    "Back Arrow",
                    tint = Color.White
                )
            }
        }
        Text(
            text = "Sign In",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.clickable {
                Toast.makeText(context, "Pressed Sign-In Button.", Toast.LENGTH_SHORT).show()
            })
    }
}

val firstClipSpec = LottieClipSpec.Progress(min = 0f, max = 0.19f)
val secondClipSpec = LottieClipSpec.Progress(min = 0.19f, max = 0.38f)
val thirdClipSpec = LottieClipSpec.Progress(min = 0.38f, max = 0.57f)
val fourthClipSpec = LottieClipSpec.Progress(min = 0.57f, max = 0.76f)
val fifthClipSpec = LottieClipSpec.Progress(min = 0.76f, max = 1.0f)

@Composable
fun LottieAnimationContent(currentPage: Int, speed: Float) {

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

    /**
     * Following is the experimental code to keep track of currentPage and targetPage
     * to figure out the direction of swipe.
     */
//    val direction = (targetPage - currentPage).toFloat()
//    Log.d("CarouselPager", "targetPage: $targetPage, currentPage: $currentPage")
//    Log.d("CarouselPager", "direction: $direction")


//    var speed = prevSpeed
//
//    if (currentPage != prevPage) {
//        Log.d("ZZZ", "There is a CHANGE")
//
//        if (currentPage > prevPage) {
//            Log.d("ZZZ", "Setting speed to 1F")
//            speed = 1F
//        } else {
//            // speed = if (currentPage == 0) 0F else -1F
//            Log.d("ZZZ", "Setting speed to -1F")
//            speed = -1F
//        }
//        prevPage = currentPage
//        prevSpeed = speed
//
//
//    } else {
//        Log.d("ZZZ", "There is NO CHANGE ---------------------")
//
//    }

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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerIndicator(pagerState: PagerState) {
    HorizontalPagerIndicator(
        pagerState = pagerState,
        activeColor = Color.White,
        inactiveColor = Color.Gray,
        modifier = Modifier
            .padding(16.dp)
    )
}

@Composable
fun TextContent(items: List<HorizontalPagerContent>, currentPage: Int) {
    Column() {
        Text(
            text = items[currentPage].title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = items[currentPage].description,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
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

data class HorizontalPagerContent(
    val title: String,
    val description: String
)
