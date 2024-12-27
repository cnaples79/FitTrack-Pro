
# Integrating the Stonly SDK into a KMM App

## What You’ll Learn
- How to add the Stonly SDK to the Android and iOS modules of a Kotlin Multiplatform Mobile (KMM) project.
- How to configure your application to display and manage Stonly widgets.

---

## Step 1: Introduction
**Title:** Integrating the Stonly SDK into a KMM App  
**Subtitle:** A Step-by-Step Guide for Android & iOS

---

## Step 2: KMM Architecture Overview
**Key Components:**
- **Shared Module:** Common Kotlin code (business logic).
- **Android Module:** Standard Android app; integrate Android SDKs here.
- **iOS Module:** Standard iOS app; integrate iOS SDKs here.

**Goal:**  
Use Stonly’s native Android and iOS SDKs within a KMM project to enhance your app’s user experience.

---

## Step 3: Prerequisites
- A working KMM project with Android and iOS targets.
- Admin permission in Stonly to access the Widget ID.
- Installed development tools: Android Studio, Xcode, etc.

---

## Step 4: Stonly Widget ID
**Steps to Find the Widget ID:**
1. Log into Stonly.
2. Go to Widget tab > Settings.
3. Open Installation.
4. Copy the Stonly widget ID.

**Note:** You’ll need this for both Android and iOS configurations.

---

## Step 5: Integrating the Stonly SDK on Android
**File:** `androidApp/build.gradle`  
Add Repositories & Dependency:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.StonlyHQ:stonly-android-sdk:<VERSION_TAG>'
}
```
Replace `<VERSION_TAG>` with the latest release.

---

## Step 6: Initializing the Android Widget
**Create/Update Application Class:**

```kotlin
class YourApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stonly.setWidgetId("YOUR_WIDGET_ID", this)
    }
}
```

**Manifest Update:**

```xml
<application
    android:name=".YourApplication"
    ... >
</application>
```

---

## Step 7: Android URL Scheme & Web Builder
**In AndroidManifest.xml**, within your Launcher Activity:

```xml
<intent-filter android:autoVerify="true">
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.BROWSABLE" />
    <category android:name="android.intent.category.DEFAULT" />
    <data android:scheme="stonly-APP_KEY"/>
</intent-filter>
```

**Enable Web Builder in `onResume()`:**

```kotlin
override fun onResume() {
    super.onResume()
    Stonly.register(intent)
}
```

---

## Step 8: iOS Integration Options
**Installation Methods:**  
- **CocoaPods:** Add `pod 'Stonly'` to your Podfile.  
- **Swift Package Manager:** Add the Stonly repo URL.  
- **Direct Framework:** Download and integrate the framework directly.  

**Note:** Use the method that best fits your existing setup.

---

## Step 9: Setting the Widget ID on iOS
**In AppDelegate.swift:**

```swift
import Stonly

func application(_ application: UIApplication,
                 didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
    // If you use SwiftUI, set to true
    Stonly.Widget.hasSwiftUI = false
    Stonly.Widget.widgetId = "YOUR_WIDGET_ID"
    return true
}
```
**Note:** If your app uses SwiftUI views, set `Stonly.Widget.hasSwiftUI = true`.

---

## Step 10: Configuring URL Handling on iOS
**Add URL Scheme in Info.plist:**

```xml
<key>CFBundleURLTypes</key>
<array>
  <dict>
    <key>CFBundleURLSchemes</key>
    <array>
      <string>stonly-APP_KEY</string>
    </array>
  </dict>
</array>
```

**In AppDelegate (Without Scenes):**

```swift
func application(_ app: UIApplication, open url: URL,
                 options: [UIApplication.OpenURLOptionsKey: Any] = [:]) -> Bool {
    if Stonly.Widget.handleURL(url) { return true }
    return false
}
```

**With Scenes (in SceneDelegate):**

```swift
func scene(_ scene: UIScene, openURLContexts URLContexts: Set<UIOpenURLContext>) {
    URLContexts.forEach { _ = Stonly.Widget.handleURL($0.url, scene: scene) }
}
```

---

## Step 11: Permissions (Optional)
**If using contact forms with file attachments:**  
Add `NSCameraUsageDescription`, `NSMicrophoneUsageDescription`, and `NSPhotoLibraryUsageDescription` to your Info.plist.  

**If not, no additional permissions required.**

---

## Step 12: Finishing Up
**Android & iOS Integration Complete!**  
1. Run your Android app: The Stonly widget should be accessible.  
2. Run your iOS app: Confirm widget access and URL handling works.

**Final Step:**  
Go back to Stonly and confirm the app’s registration.

---

## Step 13: Tips & Best Practices
1. **Version Control:** Pin Stonly SDK versions for reproducible builds.  
2. **Shared Code Access:** Use `expect/actual` in KMM if calling Stonly logic from common code.  
3. **Testing:** Test on real devices to ensure permissions and camera/microphone integrations work seamlessly.

---

## Step 14: Conclusion
By following these steps, you’ve successfully integrated the Stonly SDK into both the Android and iOS modules of your KMM project. Now your app can leverage Stonly’s features for a richer user experience on both platforms—all while maintaining a single, shared Kotlin codebase.

**Thank You!**
