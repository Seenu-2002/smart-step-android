package com.seenu.dev.android.smartstep.ai_coach.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.core.design_system.R
import com.seenu.dev.android.smartstep.ai_coach.presentation.models.ChatMessage
import com.seenu.dev.android.smartstep.design_system.utils.AdaptiveLayoutType
import com.seenu.dev.android.smartstep.design_system.theme.buttonSecondary
import com.seenu.dev.android.smartstep.design_system.theme.Inter
import com.seenu.dev.android.smartstep.design_system.theme.SmartStepTheme
import com.seenu.dev.android.smartstep.design_system.theme.backgroundSecondary
import com.seenu.dev.android.smartstep.design_system.theme.bodyLargeMedium
import com.seenu.dev.android.smartstep.design_system.theme.strokeMain
import com.seenu.dev.android.smartstep.design_system.theme.textWhite
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AiCoachScreen(
    adaptiveLayoutType: AdaptiveLayoutType,
    currentSteps: Int,
    stepGoal: Int,
    onNavigateBack: () -> Unit
) {
    val viewModel: AiCoachViewModel = koinViewModel(
        parameters = { parametersOf(currentSteps, stepGoal) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AiCoachScreenRoot(
        adaptiveLayoutType = adaptiveLayoutType,
        state = uiState,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}

// Previews
@Preview(name = "AI Coach - Mobile", widthDp = 600)
@Composable
private fun AiCoachScreenMobilePreview() {
    SmartStepTheme {
        AiCoachScreenRoot(
            adaptiveLayoutType = AdaptiveLayoutType.Mobile,
            state = AiCoachState(
                messages = listOf(
                    ChatMessage(
                        text = "Hi! I’m your AI coach. How can I help today?",
                        isFromUser = false
                    ),
                    ChatMessage(text = "Suggest a quick cardio routine", isFromUser = true)
                ),
                isOnline = true,
                isSuggestionsExpanded = true,
                isAiResponding = false,
                inputText = ""
            ),
            onAction = {},
            onNavigateBack = {}
        )
    }
}

@Preview(name = "AI Coach - Wide", widthDp = 1000)
@Composable
private fun AiCoachScreenWidePreview() {
    SmartStepTheme {
        AiCoachScreenRoot(
            adaptiveLayoutType = AdaptiveLayoutType.Tablet,
            state = AiCoachState(
                messages = listOf(
                    ChatMessage(
                        text = "Good afternoon! You’re 60% towards your goal.",
                        isFromUser = false
                    )
                ),
                isOnline = true,
                isSuggestionsExpanded = false,
                isAiResponding = false,
                inputText = "Type here..."
            ),
            onAction = {},
            onNavigateBack = {}
        )
    }
}

@Composable
private fun AiCoachScreenRoot(
    adaptiveLayoutType: AdaptiveLayoutType,
    state: AiCoachState,
    onAction: (AiCoachAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    val listState = rememberLazyListState()

    // Scroll to bottom when messages change
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            AiCoachTopBar(onNavigateBack = onNavigateBack)
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    color = MaterialTheme.colorScheme.strokeMain,
                    thickness = 1.dp
                )
                BottomInputArea(
                    inputText = state.inputText,
                    onInputChange = { onAction(AiCoachAction.UpdateInput(it)) },
                    isOnline = state.isOnline,
                    isSuggestionsExpanded = state.isSuggestionsExpanded,
                    isAiResponding = state.isAiResponding,
                    onSendClick = {
                        // Extra UI-level guard to prevent sending when offline or while AI is responding
                        if (!state.isOnline || state.isAiResponding) return@BottomInputArea

                        val text = state.inputText
                        if (text.isNotBlank()) {
                            onAction(AiCoachAction.SendMessage(text))
                            onAction(AiCoachAction.UpdateInput(""))
                        }
                    },
                    onToggleSuggestions = { onAction(AiCoachAction.ToggleSuggestions) },
                    onSuggestionClick = { suggestion ->
                        // Prevent suggestion taps when offline or busy
                        if (!state.isOnline || state.isAiResponding) return@BottomInputArea
                        onAction(AiCoachAction.SendMessage(suggestion))
                    },
                    modifier = if (adaptiveLayoutType.isWide) Modifier.width(400.dp) else Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.backgroundSecondary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.strokeMain
            )

            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .weight(1F),
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = if (adaptiveLayoutType.isWide) Alignment.CenterHorizontally else Alignment.Start
            ) {
                itemsIndexed(state.messages, key = { index, _ -> index }) { _, message ->
                    ChatBubble(
                        message = message,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AiCoachTopBar(onNavigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.ai_coach_title),
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    lineHeight = 24.sp
                ),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.cd_back),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    if (message.isFromUser) {
        // User message — right aligned
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 48.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 6.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp)
            ) {
                Text(
                    text = message.text,
                    style = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.textWhite
                )
            }
        }
    } else {
        // AI message — left aligned with avatar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            // AI Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(com.seenu.dev.android.smartstep.ai_coach.R.drawable.ic_robot),
                    contentDescription = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.cd_ai),
                    tint = MaterialTheme.colorScheme.textWhite,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 6.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.strokeMain,
                        shape = RoundedCornerShape(
                            topStart = 6.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .padding(16.dp)
            ) {
                if (message.isLoading) {
                    TypingIndicator()
                } else {
                    Text(
                        text = message.text,
                        style = TextStyle(
                            fontFamily = Inter,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun TypingIndicator() {
    val transition = rememberInfiniteTransition(label = "typing")
    val dot1Alpha by transition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "dot1"
    )
    val dot2Alpha by transition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "dot2"
    )
    val dot3Alpha by transition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "dot3"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(dot1Alpha, dot2Alpha, dot3Alpha).forEach { alpha ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha))
            )
        }
    }
}

@Composable
private fun BottomInputArea(
    inputText: String,
    onInputChange: (String) -> Unit,
    isOnline: Boolean,
    isSuggestionsExpanded: Boolean,
    isAiResponding: Boolean,
    onSendClick: () -> Unit,
    onToggleSuggestions: () -> Unit,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        // Quick Suggestions section (only when online)
        if (isOnline) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = onToggleSuggestions
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.quick_suggestions),
                    style = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        lineHeight = 24.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_down),
                    contentDescription = if (isSuggestionsExpanded) stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.collapse) else stringResource(
                        com.seenu.dev.android.smartstep.ai_coach.R.string.expand
                    ),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(if (isSuggestionsExpanded) 0f else 180f)
                )
            }

            AnimatedVisibility(
                visible = isSuggestionsExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val recommendWorkout =
                        stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.suggest_recommend_workout)
                    val explainTrend =
                        stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.suggest_explain_todays_trend)
                    val reachGoal =
                        stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.suggest_how_to_reach_todays_goal)
                    SuggestionChip(
                        text = recommendWorkout,
                        onClick = { onSuggestionClick(recommendWorkout) }
                    )
                    SuggestionChip(
                        text = explainTrend,
                        onClick = { onSuggestionClick(explainTrend) }
                    )
                    SuggestionChip(
                        text = reachGoal,
                        onClick = { onSuggestionClick(reachGoal) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Input field row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Text input
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        color = if (isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.strokeMain,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (!isOnline) {
                    // Offline state
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.offline_required),
                            style = TextStyle(
                                fontFamily = Inter,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 18.sp
                            ),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                } else {
                    BasicTextField(
                        value = inputText,
                        onValueChange = onInputChange,
                        textStyle = TextStyle(
                            fontFamily = Inter,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        singleLine = false,
                        maxLines = 5,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (inputText.isEmpty()) {
                                Text(
                                    text = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.input_placeholder),
                                    style = TextStyle(
                                        fontFamily = Inter,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        lineHeight = 18.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            // Send button
            val canSend = isOnline && inputText.isNotBlank() && !isAiResponding
            IconButton(
                onClick = onSendClick,
                enabled = canSend,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (canSend) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.buttonSecondary)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_send),
                    contentDescription = stringResource(com.seenu.dev.android.smartstep.ai_coach.R.string.cd_send),
                    tint = if (canSend) MaterialTheme.colorScheme.textWhite else MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(20.dp)
                        .offset(x = 1.dp)
                )
            }
        }
    }
}

@Composable
private fun SuggestionChip(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colorScheme.backgroundSecondary)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.strokeMain,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLargeMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
