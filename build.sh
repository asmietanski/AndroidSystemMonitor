#!/bin/bash

# Android System Monitor - Build Script
# This script helps build the APK file

set -e

echo "=========================================="
echo "Android System Monitor - Build Script"
echo "=========================================="
echo ""

# Check if Android SDK is installed
if [ -z "$ANDROID_HOME" ]; then
    echo "ERROR: ANDROID_HOME is not set!"
    echo "Please install Android SDK and set ANDROID_HOME environment variable."
    echo ""
    echo "Example:"
    echo "  export ANDROID_HOME=\$HOME/Android/Sdk"
    echo "  export PATH=\$PATH:\$ANDROID_HOME/tools:\$ANDROID_HOME/platform-tools"
    echo ""
    exit 1
fi

echo "✓ Android SDK found at: $ANDROID_HOME"
echo ""

# Check if Gradle wrapper exists
if [ ! -f "gradlew" ]; then
    echo "Gradle wrapper not found. Checking for system Gradle..."
    
    if ! command -v gradle &> /dev/null; then
        echo "ERROR: Gradle is not installed!"
        echo "Please install Gradle or use Android Studio to build the project."
        echo ""
        echo "To install Gradle:"
        echo "  1. Install SDKMAN: curl -s \"https://get.sdkman.io\" | bash"
        echo "  2. Install Gradle: sdk install gradle"
        echo ""
        exit 1
    fi
    
    echo "✓ System Gradle found: $(gradle --version | head -n 1)"
    GRADLE_CMD="gradle"
else
    echo "✓ Gradle wrapper found"
    chmod +x gradlew
    GRADLE_CMD="./gradlew"
fi

echo ""
echo "Building APK..."
echo "----------------------------------------"

# Clean previous builds
echo "Cleaning previous builds..."
$GRADLE_CMD clean

# Build debug APK
echo ""
echo "Building debug APK..."
$GRADLE_CMD assembleDebug

echo ""
echo "=========================================="
echo "Build completed successfully!"
echo "=========================================="
echo ""
echo "APK Location:"
echo "  app/build/outputs/apk/debug/app-debug.apk"
echo ""
echo "To install on device:"
echo "  adb install app/build/outputs/apk/debug/app-debug.apk"
echo ""
echo "Or transfer the APK to your Android device and install manually."
echo ""