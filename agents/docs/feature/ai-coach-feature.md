# SmartStep — Milestone #3: AI Feature Integration Guide

> **For Junie AI**
> This document describes **what to build, why, and the rules to follow**.
> It contains no code. All implementation decisions must be derived from your
> own scan of the existing codebase. Always understand the project first, then build.

---

## Step 0 — Understand the Project Before Writing Anything

Before touching a single file, scan the entire project and answer these for yourself:

- What is the package namespace?
- What UI framework is in use? (Expected: Jetpack Compose)
- What is the navigation approach? (NavHost, destinations, route naming conventions)
- What is the dependency injection setup? (Hilt, Koin, or manual — identify which)
- Where does step count and daily goal data live? (Room, DataStore, StateFlow, or other)
- What is the existing ViewModel pattern? (How are they created, scoped, injected?)
- What is the existing Repository pattern? (Interface + impl, or plain class?)
- What theme tokens are in use? (Color scheme, typography, shape — from MaterialTheme)
- What icons are already present in the project?
- Is there already any connectivity observation utility?
- Is there already any AI or network-related code?

Only after understanding all of the above should you proceed with implementation.
Every decision you make — naming, structure, patterns — must be consistent with what already exists.

---

## Step 1 — Gradle & API Key Setup

The project needs the Google Gemini SDK added as a dependency.
Use the official `google-generativeai` Kotlin SDK (latest stable version).

The API key must be stored in `local.properties` as `GEMINI_API_KEY` property and can be accessed via `BuildConfig`.
It must never be hardcoded in any source file.
Enable `buildConfig` generation in the app module's build configuration if it is not already enabled.

Verify that `local.properties` is already listed in `.gitignore`.
If it is not, add it.

Also ensure the following permissions are declared in `AndroidManifest.xml` if not already present:
- `android.permission.INTERNET`
- `android.permission.ACCESS_NETWORK_STATE`

---

## Step 2 — Connectivity Observation

The app needs a way to observe the device's internet connectivity as a reactive stream,
so that the UI can respond automatically when the connection is lost or restored —
without requiring any screen refresh or user action.

If a connectivity observer already exists in the project, use and extend it.
If not, create one that fits the existing architecture.

The output of this utility should be a stream (Flow or similar reactive type)
that emits a boolean representing whether the device is currently online.
It must emit the current state immediately upon subscription,
and continue to emit whenever the state changes.

This utility should be available for injection wherever it is needed,
following the same DI pattern used elsewhere in the project.

---

## Step 3 — Gemini Repository

Create a repository (or service — match the existing naming convention) that wraps
all communication with the Gemini API. No other part of the app should call the SDK directly.

This repository is responsible for two distinct types of AI interaction:

### 3a. One-shot Insight Generation

Used by the AI Insights block on the Main Screen.
Takes structured activity context as input and returns a single short text message.
Has no memory of previous calls. Each call is fully independent.

The input context that must be passed to the AI:
- Current step count for the day
- Daily step goal
- Goal completion as a percentage
- Time of day context (morning / day / evening — derive this from the current hour)

The AI must return one short message (max 2 sentences) that:
- Interprets the user's current activity state
- Has a motivational or analytical tone
- Does not contain medical advice
- Does not repeat raw numeric values
- Does not ask questions

Use a system prompt to enforce these rules on the AI's output.
Every call to this function creates a fresh, stateless request with no conversation history.

### 3b. Session-based Chat

Used by the AI Coach Chat screen.
A new chat session must be created each time the Chat screen is opened.
The session receives the same structured activity context as the insight (step count, goal, percentage, time of day),
so the AI is aware of the user's current state throughout the conversation.

The repository must expose:
- A function to create a new chat session with activity context pre-loaded
- A function to generate the initial greeting message for a new session
- A function to send a user message and receive the AI's reply

The initial greeting must:
- Welcome the user as their AI fitness coach
- Briefly acknowledge their current activity level
- Ask how the AI can help
- Be concise (no more than 4 sentences)
- Contain no medical advice and no raw numbers

All functions must handle errors gracefully and return a sensible fallback string
if the API call fails, so the UI never crashes or shows a blank message.

---

## Step 4 — AI Insights Block (Main Screen)

### What it is
A new UI block added to the Main Screen, placed directly below the existing Daily Average section.
It displays a short AI-generated message about the user's current activity.

### When the insight refreshes
The insight must be regenerated **only** when the Main Screen becomes active under these specific conditions:
1. The first time the app is launched in the current session
2. When the app returns from the background (ON_RESUME lifecycle event)
3. When the user's step count reaches or crosses the daily step goal
4. When the user changes their daily step goal

The insight must **not** regenerate for minor step count increments.
The insight must **not** update on a timer.
If none of the trigger conditions are met, the previously generated insight is reused as-is.

Add this trigger logic to the existing Main Screen ViewModel.
Do not create a separate ViewModel for this block.

### States the block must handle

**Loading state**
Shown while the API call is in progress.
Display a visual placeholder (e.g. a shimmer or muted bar) where the message will appear.

**Success state**
Display the AI-generated message.
Display a "More" button that navigates to the AI Coach Chat screen.

**Error state**
If the API call fails, display a short fallback message.
Still show the "More" button so the user can open the chat.

**Offline state**
If there is no internet connection when a refresh is triggered, do not call the API.
Display a static message informing the user to connect to the internet.
Replace the "More" button with a "Try Again" action accompanied by a refresh icon.
When "Try Again" is tapped, check connectivity again:
- If online, perform the API call and restore normal state.
- If still offline, do nothing and keep the offline state as-is. Show no error.

### Wiring
The block itself is purely informational — it has no interactive elements except
the "More" button and the "Try Again" action in offline state.
The connectivity observer must be collected in the UI so the offline state
updates automatically when connectivity changes.

---

## Step 5 — AI Coach Chat Screen

### What it is
A dedicated screen for conversational interaction with the AI fitness coach.
It is a lightweight, session-based chat — not a persistent conversation.
Every time the screen is opened, a completely fresh session begins.
Previous messages are never restored.

### Navigation
- Follow the existing route naming convention in the project
- Navigated to from the "More" button in the AI Insights block
- The Back button navigates the user back to the Main Screen
- This screen must be **removed from the back stack** when navigated away from,
  so that pressing back from the Main Screen does not return to a stale chat session
- When the screen is opened again, a brand new session starts from scratch

### Top Bar
- Centered title: "AI Coach"
- Back button (arrow icon) on the left

### Session Lifecycle
When the screen opens:
1. The message history is empty
2. A new chat session is created with the user's current activity context
3. The AI automatically sends an initial greeting message (no user action required)
4. The greeting appears as the first message in the chat

When the user navigates away:
1. The screen is removed from the back stack
2. The entire conversation state is discarded

### Message History Area
Displays all messages in chronological order.
New messages are appended immediately after being sent or received.

**AI messages:**
- Appear on the left side
- On mobile: occupy the full available width of the message area (respecting horizontal padding)
- On wide screen: can expand to the full width of the message container (up to 600dp)
- Include the AI avatar/icon to the left of the bubble

**User messages:**
- Appear on the right side
- On mobile: maximum width of 75% of the screen width; adapt tightly to shorter content
- On wide screen: maximum width of 400dp
- No avatar

While an AI response is being generated, show a loading indicator inside a bubble on the left side.

### Quick Suggestions Section
Displayed below the message history, above the input field.
Contains exactly three fixed suggestion buttons:
- "Recommend workout"
- "Explain today's trend"
- "How to reach today's goal?"

The section can be expanded or collapsed by the user.

Tapping a suggestion:
- Sends it immediately as a user message
- Triggers an AI response
- Adds it to the message history like any other user message

Suggestion buttons are disabled when the device is offline.

### Message Input Field
- Single-line by default, expands vertically as the user types (up to 5 lines maximum)
- After 5 lines, the field becomes internally scrollable
- A message can only be sent if the trimmed input contains at least one non-whitespace character

**When offline:**
- The field is disabled and does not accept focus or open the keyboard
- The placeholder text changes to "Online connection required"
- An inline icon (e.g. a crossed-out connection icon) appears inside the field
- The send button is disabled and visually muted

**When connectivity is restored:**
- The field automatically becomes active again
- Placeholder returns to default
- Send button is re-enabled
- No screen refresh or user action is required

The UI must respond to connectivity changes automatically via the connectivity observer.

---

## Step 6 — Adaptive Layout

The app supports two breakpoints:
- **Mobile:** screen width below 840dp
- **Wide screen:** screen width 840dp and above

Apply the breakpoint by reading the current screen width at runtime.
Use whatever approach is already established in the project for responsive layouts.

**AI Insights Block:**
No layout difference between breakpoints — it fills the screen width as part of the
Main Screen layout in both cases.

**AI Coach Chat Screen — Message History Area:**
- Mobile: messages fill the full screen width with standard horizontal padding
- Wide screen: messages are centered in a container with a maximum width of 600dp

**AI Coach Chat Screen — Input & Suggestions Area:**
- Mobile: fills the full screen width with standard horizontal padding
- Wide screen: centered horizontally with a fixed width of 400dp

---

## Step 7 — Dependency Injection

Register the Gemini repository and the connectivity observer using the **exact same DI pattern**
already present in the project.

Do not introduce a new DI framework.
Do not create a new DI module style if one already exists — add to it instead.

The AI Coach Chat ViewModel should receive both the repository and the connectivity observer
through whatever injection mechanism is already in use.

---

## Step 8 — Edge Cases & Rules Checklist

Before considering the implementation complete, verify every item below:

- [ ] Insight does not regenerate on minor step count changes — only on the 4 defined triggers
- [ ] Chat conversation state is fully discarded when navigating away — never persisted
- [ ] Input field cannot be focused and keyboard does not open when offline
- [ ] Send button is non-interactive and visually distinct when offline or when input is empty/whitespace
- [ ] Quick suggestion buttons are disabled when offline
- [ ] "More" button in the Insights block is not shown when offline
- [ ] "Try Again" in offline state checks connectivity before calling the API
- [ ] All Gemini API calls have error handling with fallback strings — no crashes, no blank messages
- [ ] Time-of-day context helper (morning / day / evening) is not duplicated — shared utility if used in multiple places
- [ ] API key is only ever read from `BuildConfig` — never hardcoded
- [ ] `local.properties` is in `.gitignore`
- [ ] Connectivity UI updates happen automatically — no screen refresh required

---

## Reference

**Requirements document:** SmartStep Milestone #3 Requirements (provided separately)
**Figma mockups:** https://www.figma.com/design/aTiGCCavPLtgZNGXcV23H6/Smart-Step?node-id=57-1002

> Before building any UI composable, fetch the Figma design context for node `57-1002`
> and match colors, spacing, corner radii, and typography exactly to what is defined there.
> Do not invent or assume any design values.