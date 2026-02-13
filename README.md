# Android System Monitor

An Android application that displays all operational applications in the system along with their CPU and memory usage in real-time.

## Features

- **App List Display**: Shows all installed applications on the device
- **Real-time Monitoring**: Displays CPU and memory usage for each app
- **Running Status**: Indicates which apps are currently running
- **App Icons**: Shows the icon for each application
- **Refresh Functionality**: Manual refresh button to update the app list
- **Sorted Display**: Apps are sorted by running status first, then by memory usage
- **Material Design**: Modern UI with Material Design components

## Screenshots

The app displays:
- App icon
- App name
- Package name
- Running status (Running/Stopped)
- Memory usage in MB
- CPU usage percentage

## Technical Details

### Architecture
- **Language**: Kotlin
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Architecture Pattern**: MVVM-like with coroutines

### Key Components

1. **MainActivity.kt**: Main activity that handles app logic and data fetching
2. **AppInfo.kt**: Data class representing application information
3. **AppListAdapter.kt**: RecyclerView adapter for displaying apps
4. **Layout Files**: 
   - `activity_main.xml`: Main screen layout
   - `item_app.xml`: Individual app item layout

### Permissions Required

- `PACKAGE_USAGE_STATS`: Required to access app usage statistics
- `QUERY_ALL_PACKAGES`: Required to query all installed packages

**Note**: The app will prompt users to grant Usage Access permission on first launch.

## How It Works

1. **App Discovery**: Uses `PackageManager` to get all installed applications
2. **Running Process Detection**: Uses `ActivityManager.runningAppProcesses` to identify running apps
3. **Memory Usage**: Retrieves memory information using `ActivityManager.getProcessMemoryInfo()`
4. **CPU Usage**: Reads CPU statistics from `/proc/[pid]/stat` (simplified calculation)

## Building the Project

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 8 or higher
- Android SDK with API 34

### Steps
1. Open the project in Android Studio
2. Sync Gradle files
3. Build and run on an emulator or physical device

```bash
# Using Gradle command line
./gradlew assembleDebug
```

## Installation

1. Clone or download the project
2. Open in Android Studio
3. Connect an Android device or start an emulator
4. Click "Run" or use `Shift + F10`
5. Grant Usage Access permission when prompted

## Usage

1. Launch the app
2. Grant Usage Access permission in system settings (app will redirect you)
3. Return to the app to see the list of all applications
4. View CPU and memory usage for each app
5. Use the "Refresh" button to update the information

## Limitations

- CPU usage calculation is simplified and may not be 100% accurate
- Some system apps may not report accurate memory usage
- Requires Android 7.0 (API 24) or higher
- Usage stats permission must be granted manually by the user

## Dependencies

- AndroidX Core KTX
- AndroidX AppCompat
- Material Components
- ConstraintLayout
- RecyclerView
- CardView
- Lifecycle Runtime KTX
- Kotlinx Coroutines

## Future Enhancements

- [ ] Add real-time CPU usage tracking with delta calculations
- [ ] Add battery usage information
- [ ] Add network usage statistics
- [ ] Add app sorting and filtering options
- [ ] Add detailed app information screen
- [ ] Add app management features (force stop, clear cache)
- [ ] Add charts and graphs for resource usage
- [ ] Add background monitoring service
- [ ] Add notifications for high resource usage

## License

This project is created for educational purposes.

## Author

Created as a system monitoring tool for Android devices.