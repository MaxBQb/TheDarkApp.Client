package lab.maxb.dark.presentation.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import lab.maxb.dark.ui.theme.spacing

data class ExpandAnimationDuration(
    val fadeIn: Int = 400,
    val fadeOut: Int = 400,
    val expand: Int = 400,
    val collapse: Int = 400,
)

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    onExpandToggleClick: () -> Unit = {},
    expanded: Boolean,
    title: @Composable RowScope.() -> Unit,
    body: @Composable ColumnScope.() -> Unit,
    animationDuration: ExpandAnimationDuration = ExpandAnimationDuration(),
    hideOnExpanded: Boolean = false,
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "transition")
    val cardElevation by transition.animateDp({
        tween(durationMillis = animationDuration.expand)
    }, label = "elevationTransition") {
        if (expanded) 24.dp else 4.dp
    }
    val cardBgColor by transition.animateColor({
        tween(durationMillis = animationDuration.expand)
    }, label = "bgColorTransition") {
        MaterialTheme.colorScheme.surfaceColorAtElevation(cardElevation)
    }
    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = animationDuration.expand,
            easing = FastOutSlowInEasing
        )
    }, label = "cornersTransition") {
        if (expanded) 4.dp else 16.dp
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(cardElevation),
        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.spacing.normal,
                vertical = MaterialTheme.spacing.small
            )
            .clickable {
                if (!expanded)
                    onExpandToggleClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Min),
        ) {
            CardArrow(
                expanded = expanded,
                onClick = onExpandToggleClick,
                animationDuration = animationDuration,
                hideOnExpanded = hideOnExpanded,
            )
            title()
        }
        ExpandableContent(
            visible = expanded,
            content = body,
            animationDuration = animationDuration,
        )
    }
}

@Composable
fun CardArrow(
    expanded: Boolean,
    onClick: () -> Unit,
    animationDuration: ExpandAnimationDuration = ExpandAnimationDuration(),
    hideOnExpanded: Boolean = false,
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "transition")

    val rotation by transition.animateFloat({
        tween(durationMillis = if (expanded) animationDuration.collapse else animationDuration.expand)
    }, label = "rotationDegreeTransition") {
        if (it) 0f else -180f
    }

    val alpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0.35f,
        animationSpec = tween(
            durationMillis = if (expanded) animationDuration.collapse else animationDuration.expand,
            easing = LinearEasing,
        ),
    )

    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = animationDuration.fadeIn,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(animationDuration.expand))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = animationDuration.fadeOut,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(animationDuration.collapse))
    }
    AnimatedVisibility(
        visible = !(hideOnExpanded && expanded),
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        IconButton(
            modifier = Modifier.alpha(alpha),
            onClick = onClick,
        ) {
            Icon(
                Icons.Filled.KeyboardArrowDown,
                "",
                Modifier.rotate(rotation),
            )
        }
    }
}


@Composable
fun ExpandableContent(
    visible: Boolean = true,
    animationDuration: ExpandAnimationDuration = ExpandAnimationDuration(),
    content: @Composable ColumnScope.() -> Unit,
) {
    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = animationDuration.fadeIn,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(animationDuration.expand))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = animationDuration.fadeOut,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(animationDuration.collapse))
    }
    AnimatedVisibility(
        visible = visible,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.small)) {
            content()
        }
    }
}