# Building the Android APK

## Prerequisites

To build this Android application into an APK file, you need one of the following:

### Option 1: Using Android Studio (Recommended)

1. **Install Android Studio**
   - Download from: https://developer.android.com/studio
   - Install Android SDK (API 34)
   - Install Android Build Tools

2. **Open the Project**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `AndroidSystemMonitor` folder
   - Wait for Gradle sync to complete

3. **Build the APK**
   - Click `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
   - Or use the menu: `Build` → `Generate Signed Bundle / APK`
   - The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

### Option 2: Using Command Line (Requires Android SDK)

1. **Install Android SDK and Gradle**
   ```bash
   # Install SDKMAN (if not installed)
   curl -s "https://get.sdkman.io" | bash
   source "$HOME/.sdkman/bin/sdkman-init.sh"
   
   # Install Gradle
   sdk install gradle 8.2
   ```

2. **Set Environment Variables**
   ```bash
   export ANDROID_HOME=$HOME/Android/Sdk
   export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
   ```

3. **Build the APK**
   ```bash
   cd AndroidSystemMonitor
   gradle assembleDebug
   ```
   
   The APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

### Option 3: Using Gradle Wrapper (After Initial Setup)

If you have the Gradle wrapper files:

```bash
cd AndroidSystemMonitor
chmod +x gradlew
./gradlew assembleDebug
```

## Build Variants

### Debug APK (for testing)
```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK (for distribution)
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release-unsigned.apk`

**Note:** Release APKs need to be signed before installation.

## Signing the APK (for Release)

1. **Generate a Keystore**
   ```bash
   keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
   ```

2. **Sign the APK**
   ```bash
   jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 -keystore my-release-key.jks app-release-unsigned.apk my-key-alias
   ```

3. **Align the APK**
   ```bash
   zipalign -v 4 app-release-unsigned.apk SystemMonitor.apk
   ```

## Installing the APK

### On Physical Device
1. Enable "Unknown Sources" in device settings
2. Transfer the APK to your device
3. Open the APK file and install

### Using ADB
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Troubleshooting

### Common Issues

1. **Gradle sync failed**
   - Check internet connection
   - Update Android Studio
   - Invalidate caches: `File` → `Invalidate Caches / Restart`

2. **SDK not found**
   - Install Android SDK through Android Studio
   - Set ANDROID_HOME environment variable

3. **Build tools version mismatch**
   - Update build tools in SDK Manager
   - Modify `compileSdk` in `app/build.gradle` if needed

4. **Out of memory during build**
   - Increase heap size in `gradle.properties`:
     ```
     org.gradle.jvmargs=-Xmx4096m
     ```

## Quick Start for Users Without Build Tools

If you don't have Android development tools installed, you have two options:

1. **Use Android Studio** (easiest)
   - Download and install Android Studio
   - Open the project and click "Build APK"

2. **Request a Pre-built APK**
   - Ask someone with Android Studio to build it for you
   - Or use a CI/CD service like GitHub Actions

## File Locations After Build

- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release-unsigned.apk`
- **Build logs**: `app/build/outputs/logs/`

## Minimum Requirements

- **JDK**: Version 8 or higher
- **Gradle**: Version 8.0 or higher
- **Android SDK**: API 24 (minimum) to API 34 (target)
- **Build Tools**: Version 34.0.0
- **RAM**: At least 4GB (8GB recommended)
- **Disk Space**: At least 2GB free

## Next Steps After Building

1. Install the APK on your Android device
2. Grant "Usage Access" permission when prompted
3. The app will display all installed applications with their resource usage

For more information, see the main [README.md](README.md)