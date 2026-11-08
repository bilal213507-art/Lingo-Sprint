# Lingo Sprint — Android app project

A native Android starter app for Lingo Sprint, built with Java and Android SDK (no third-party libraries).

## Included
- Home dashboard with XP, streak and hearts
- Spanish beginner lesson with multiple-choice questions
- XP scoring, heart loss and saved progress
- Course selection screen (Spanish, French, Arabic, Urdu, German, Italian)
- Vocabulary flashcards
- Text-to-speech pronunciation practice
- Profile and achievement screen
- GitHub Actions workflow that builds an APK

## Build an APK without installing Android Studio
1. Create a GitHub repository and upload the contents of this ZIP.
2. Open the repository's **Actions** tab.
3. Select **Build Lingo Sprint APK** and press **Run workflow** (or push to `main`).
4. When it finishes, open the workflow run and download the `lingo-sprint-debug-apk` artifact.
5. Extract it to get `app-debug.apk`.

## Build locally
Open this folder in Android Studio, allow Gradle sync, then choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.

## APKPure publishing note
The workflow produces a **debug APK for testing**. For public distribution, create a signed **release APK** using your own private upload key in Android Studio (Build > Generate Signed Bundle / APK > APK). Keep the keystore and passwords private and backed up. Never upload a debug APK as your final public release.

## Important scope
This is an offline first version. It does not include cloud accounts, live multiplayer leaderboards, subscription payments, server-backed course content, or a production speech-recognition grading service. The displayed leaderboard/achievement concepts are starter UI, not connected to a backend. Add privacy policy, support contact, app screenshots, and store metadata before submitting to APKPure.
