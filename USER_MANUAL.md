# EasyCare User and Testing Manual

This manual explains how to install, operate, demonstrate, and manually test the EasyCare Android prototype.

> **Important:** EasyCare is a prototype. Food orders are not sent to a real restaurant. Messages and calls are prepared in the phone's normal apps, and the user must press the final **Send** or **Call** button. In an immediate or life-threatening emergency, call **112** directly.

## 1. What you need

- An Android phone with EasyCare installed.
- Internet access for the best voice-recognition experience.
- Microphone permission if you want to use voice commands.
- Location permission if you want to include your location in an emergency message.

## 2. Install and open EasyCare with Android Studio

1. Install a recent stable version of Android Studio on the computer.
2. Open Android Studio and select **Open**.
3. Choose the main EasyCare project folder named **Sahara**. Do not select only the `app` folder.
4. If Android Studio asks whether to trust the project, select **Trust Project**.
5. Wait for the project setup and Gradle sync to finish. This may take several minutes the first time.
6. If Android Studio asks to install missing Android tools, accept the recommended installation.
7. Connect an Android phone with USB debugging enabled, or start an Android emulator from **Device Manager**.
8. Select the connected phone or emulator from the device list at the top of Android Studio.
9. Select the **app** run configuration.
10. Click the green **Run** button.
11. Android Studio builds and installs EasyCare on the selected device.
12. EasyCare opens automatically. If it does not, find the **EasyCare** icon on the device and tap it.
13. The first-time caregiver setup screen will appear.

For the best test of voice commands, calls, messages, and location sharing, use a physical Android phone. An emulator is suitable for reviewing the screens and most button flows.

## 3. First-time caregiver setup

EasyCare shows the setup screen only on first launch.

1. Enter or review the user's name.
2. Enter the family member's name and phone number.
3. Enter the emergency contact phone number.
4. Enter the home address.
5. Tap **Save and Continue**.

The prototype starts with these example values:

- User: Test User
- Family member: Test Family Member
- Family phone: +491234567890

Use test phone numbers when demonstrating the app. The saved details remain available after the app is closed and reopened.

## 4. Home screen

The home screen shows the user's greeting, current date and time, and the main actions:

- **Tap and Speak** — use a voice command.
- **Food** — select a mock meal.
- **Call Family** — open the family member in the phone dialer.
- **Message Family** — type or dictate an SMS.
- **Medicine** — view and complete reminders.
- **EMERGENCY HELP** — open emergency actions.

The small settings icon is intended for a caregiver.

## 5. Use voice commands

1. From the home screen, tap **Tap and Speak**.
2. If prompted, allow microphone access.
3. Wait for **Listening...**.
4. Say one short command clearly.
5. EasyCare displays the detected sentence, speaks a response when voice output is enabled, and opens the matching action.

Example phrases:

| What you want | Example phrases |
|---|---|
| Food | “I want food”, “Order food”, “I am hungry” |
| Call family | “Call Dipesh”, “Call family”, “Call my son” |
| Message family | “Send message to Dipesh”, “Message my son”, “Tell my family” |
| Emergency | “Help me”, “Emergency”, “I need help” |
| Medicine | “Medicine”, “My medicine”, “Medication” |

For a call command, EasyCare opens the system dialer directly. It does not place the call automatically. Press the phone app's **Call** button only when you intend to make the call.

If voice recognition is unavailable or permission is denied, use the large buttons on the home screen. All important functions still work without voice commands.

## 6. Order food

1. Tap **Food**, or say a food voice command.
2. Review the four local meal choices.
3. Tap **Select** on a meal.
4. EasyCare immediately shows **Order Request Sent** with a **Preparing** status.
5. Return home when finished.

Available mock meals:

- Gujarati Thali — €10
- Khichdi Kadhi — €8
- Punjabi Thali — €12
- Simple Home Meal — €7

No money is charged and no restaurant receives the request.

## 7. Call a family member

1. Tap **Call Family** on the home screen, or open **Family** and tap **Call Dipesh**.
2. EasyCare opens the system phone app with the saved number.
3. Review the number.
4. Press **Call** in the phone app only if you want to place the call.

There is no extra confirmation page inside EasyCare. The phone app is the final review step.

## 8. Send a family message

1. Tap **Message Family**.
2. Select **Family** or **Emergency Contact** as the recipient.
3. Type a message in the large text box, or tap **Speak Message** and dictate it.
4. Tap **Send Message**.
5. Review the prepared message in the system SMS app.
6. Press **Send** in the SMS app when ready.

If microphone access is denied, typing remains available.

## 9. Use Emergency Help

Tap the large red **EMERGENCY HELP** button on the home screen. Choose the family member or emergency contact before preparing an alert.

### Call family

Tap **Call Dipesh**. The system dialer opens with the saved number. Press **Call** in the dialer to place the call.

### Send a quick emergency alert

1. Select the intended recipient.
2. Tap **Send Quick Alert**.
3. Allow location access if you want to include the current location.
4. EasyCare opens the SMS app with a preset help message.
5. If a location is available, the text includes a Google Maps link.
6. Review the recipient and message, then press **Send** in the SMS app.

The message can still be prepared if location permission is denied or the device cannot obtain a location.

### Speak an emergency message

1. Select the intended recipient.
2. Tap **Speak Emergency Message**.
3. Allow microphone access if prompted.
4. Speak a short message, such as “I fell down and need help.”
5. Check the detected text shown by EasyCare.
6. EasyCare opens the SMS app with the dictated text and an optional location link.
7. Review it and press **Send** in the SMS app.

### Call emergency services

Tap **Call Emergency Services**. EasyCare opens the system dialer with **112**. The app never silently places the call; press **Call** in the dialer to continue.

## 10. Mark medicine as taken

1. Tap **Medicine**.
2. Find the reminder you completed.
3. Tap its large **TAKEN** button.
4. Confirm that the button changes to **DONE ✓**.

EasyCare says “Medicine marked as taken” when voice output is enabled. This is only a reminder prototype and does not provide medical advice. Completion state resets when the app process is fully restarted.

## 11. Caregiver settings

Tap the small settings icon on the home screen. A caregiver can change:

- My Name
- Family Contact
- Emergency Contact
- Voice Assistant ON or OFF
- Text Size: Normal, Large, or Extra Large

Turn off **Voice Assistant** to stop spoken Text-to-Speech feedback. This does not remove the large navigation buttons.

## 12. Five-minute demonstration script

Use this sequence for a client or caregiver demonstration:

1. Complete first-launch setup and arrive at the home screen.
2. Tap **Tap and Speak** and say “I want food.”
3. Select **Gujarati Thali** and show **Order Request Sent** and **Preparing**.
4. Return home, tap **Tap and Speak**, and say “Call Dipesh.”
5. Show the number in the system dialer, then return without placing a real call.
6. Open **EMERGENCY HELP**, select a contact, and tap **Send Quick Alert**.
7. Allow location access and show the prepared SMS and Google Maps link. Return without sending it to a real contact.
8. Open **Medicine**, tap **TAKEN**, and show **DONE ✓**.
9. Open settings, change the text size, and turn voice output off and back on.

## 13. Manual test checklist

Use test contacts and do not press the final system **Call** or **Send** button unless the recipient has agreed to participate.

| Test | Steps | Expected result |
|---|---|---|
| First launch | Clear app data and open EasyCare | Setup screen appears with example values |
| Save setup | Change a name, tap **Save and Continue**, close and reopen | Home screen appears and the changed name is retained |
| Home content | Open home | Greeting, date, time, voice button, four cards, and emergency button are visible |
| Food voice command | Say “I want food” | Detected sentence appears and Food opens |
| Call voice command | Say “Call Dipesh” | System dialer opens with the saved family number |
| Message voice command | Say “Message my son” | Message screen opens |
| Emergency voice command | Say “I need help” | Emergency screen opens |
| Medicine voice command | Say “Medication” | Medicine screen opens |
| Unknown voice command | Say an unrelated sentence | App explains that it did not understand and remains usable |
| Microphone denied | Deny microphone permission, then try voice | Clear explanation appears; buttons and typing still work; no crash |
| Food order | Select every meal one at a time | Success screen shows the selected meal and **Preparing** |
| Family call | Tap **Call Family** | Dialer opens with the saved number; EasyCare does not call silently |
| Typed message | Type text and tap **Send Message** | SMS app opens with recipient and body filled in |
| Dictated message | Tap **Speak Message** and speak | Recognized text fills the message before SMS handoff |
| Quick emergency, location allowed | Allow location and send quick alert | SMS app opens; Maps link is included when a location fix is available |
| Quick emergency, location denied | Deny location and send quick alert | SMS app still opens with emergency text and no location; no crash |
| Spoken emergency | Dictate a custom emergency message | Detected text is used in the prepared SMS |
| Emergency call | Tap **Call Emergency Services** | Dialer opens with 112; call is not placed automatically |
| Medicine | Tap **TAKEN** | Item displays **DONE ✓** and spoken feedback plays if enabled |
| Voice setting | Turn voice output off and visit screens | No Text-to-Speech feedback plays; controls still work |
| Text size | Select all three sizes | App text changes and important controls remain readable and usable |
| Missing external app | Test on a device without an SMS or dialer handler | EasyCare shows an error rather than crashing |

## 14. Reset the app for another first-launch test

Resetting removes all saved EasyCare names, phone numbers, address, and settings.

On the device:

1. Open Android **Settings**.
2. Open **Apps** > **EasyCare**.
3. Open **Storage**.
4. Tap **Clear storage** or **Clear data**.
5. Open EasyCare again.

## 15. Troubleshooting

### Voice recognition could not start

- Confirm microphone permission is enabled in Android **Settings** > **Apps** > **EasyCare** > **Permissions**.
- Check the internet connection.
- Confirm that voice recognition works in other apps on the phone.
- Wait for the current listening session to finish before tapping the microphone again.
- Continue with the large on-screen buttons if voice remains unavailable.

### No location appears in an emergency message

- Turn on device Location.
- Allow precise or approximate location permission for EasyCare.
- Try outdoors or wait briefly for a location fix on a physical device.

An emergency SMS without a location is valid prototype behavior when no location is available.

### The dialer or SMS app does not open

- Confirm a phone/dialer or SMS app is installed and enabled.
- Check that the saved phone number is not empty.

### Setup appears again

Tap **Save and Continue** and allow the save to complete. Clearing app data or reinstalling the app intentionally returns EasyCare to first-launch setup.

### Text or buttons do not fit on the screen

Ask the caregiver to select a smaller text size in EasyCare settings. Scroll vertically where the screen allows scrolling.
