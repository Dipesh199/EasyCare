# EasyCare Android MVP

EasyCare is a functional Android prototype for elderly people who live alone or find standard smartphone interfaces difficult to use. It emphasizes one-tap actions, large high-contrast controls, spoken feedback, and full button-based fallbacks when permissions or voice recognition are unavailable.

For step-by-step operating instructions, a five-minute demo flow, manual test cases, reset steps, and troubleshooting, see [USER_MANUAL.md](USER_MANUAL.md).

## Technology stack

- Kotlin and Jetpack Compose
- Material 3 design system
- MVVM with `ViewModel`, immutable UI state, `StateFlow`, and lifecycle-aware collection
- Navigation Compose
- Preferences DataStore for first-launch setup and settings
- Android `SpeechRecognizer` and `TextToSpeech`
- Google Play services Fused Location Provider
- Android `ACTION_DIAL` and `ACTION_SENDTO` intents
- Local mock meals and medicine reminders; no backend, login, Firebase, or Room
- Minimum SDK 26, target SDK 36, compile SDK 37

API 36 is the latest stable target platform used by this project. Compile SDK 37 is required by the current stable AndroidX libraries, but does not opt users into Android 17 runtime behavior.

## Open and run

1. Open this repository folder in a recent stable Android Studio version.
2. Allow Android Studio to finish Gradle sync.
3. Ensure Android SDK Platform 37 and an emulator/device with API 26 or newer are installed.
4. Select the `app` run configuration and press **Run**.
5. On first launch, review the prefilled caregiver setup and select **Save and Continue**.

To build from the repository root on Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

Useful verification commands:

```powershell
.\gradlew.bat test
.\gradlew.bat :app:lintDebug
```

## Required permissions

- `RECORD_AUDIO`: requested when voice recognition or message dictation is used. Every flow remains available through buttons or typing if denied.
- `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION`: requested only when preparing an emergency SMS. If denied or unavailable, EasyCare opens the SMS without a location link.

Phone and SMS actions use external system apps and therefore do not request direct-call or direct-send permissions. The user always reviews the dialer or SMS before taking the final action.

## Implemented MVP features

- First-launch caregiver setup persisted with DataStore
- Dynamic greeting, current date/time, large microphone action, four primary actions, and prominent emergency button
- Speech recognition with extensible keyword-based intent parsing and detected-sentence feedback
- Spoken feedback for screens and important actions, with a settings toggle
- Four local meal choices with direct simulated request submission and `Preparing` status
- Family contact card, direct dialer handoff, and typed or dictated SMS to either the family member or emergency contact
- Emergency hub with direct family/112 dialer handoff, a one-tap preset alert, and a dictated emergency alert
- Emergency SMS location sharing through an optional Google Maps link when permission and a location fix are available
- Family alerts through the device SMS application, with the message prepared for immediate review and sending
- Local morning, afternoon, and evening medicine reminders with visible `DONE ✓` state
- Caregiver settings for names, phone numbers, voice output, and three text sizes
- Accessible labels, large touch targets, high contrast, and text-plus-icon actions

## Prototype limitations

- Food orders are simulated and are not sent to a restaurant.
- Medicine completion is held in memory for the current process and resets after the app is fully restarted.
- Speech recognition availability and quality depend on the device's installed recognition service and language support.
- Location can be unavailable indoors, while services are disabled, or on devices without Google Play services; the emergency SMS still works without it.
- Calls and messages intentionally open system apps and require the user to press the final call/send action.
- Contact phone validation is intentionally lightweight for the MVP.
- Community support is a future integration because this offline MVP has no verified volunteer/service directory or dispatch backend.

## Future features

- Real restaurant API integration
- WhatsApp integration and video calls
- Companion family caregiver app
- Remote medicine management
- Fall detection and wearable integration
- AI conversational assistant
- Automatic check-ins
- Home automation
