<div align="center">

<img src="https://avatars.githubusercontent.com/u/254466332" alt="react-native-auth logo" height="120">

# 🔐 @react-native-auth/google

**Modern Google Authentication for React Native**

[![NPM Version](https://img.shields.io/npm/v/%40react-native-auth%2Fgoogle?style=for-the-badge&logo=npm&logoColor=white&color=greeen)](https://www.npmjs.com/package/@react-native-auth/google)
[![License: MIT](https://img.shields.io/github/license/react-native-auth/google?style=for-the-badge&logo=opensourceinitiative&logoColor=white)](https://opensource.org/licenses/MIT)
[![Platform: Android](https://img.shields.io/badge/Platform-Android-green.svg?style=for-the-badge&logo=android)](https://developer.android.com/)

[![GitHub Issues or Pull Requests](https://img.shields.io/github/issues/react-native-auth/google?style=for-the-badge&logo=github)](../../issues)
![GitHub Issues or Pull Requests](https://img.shields.io/github/issues-pr/react-native-auth/google?style=for-the-badge&logo=github)

</div>

---

## 📋 Table of Contents

- [✨ Features](#-features)
- [📦 Installation](#-installation)
- [⚙️ Setup](#️-setup)
  - [📱 Step 1: Enable New Architecture](#-step-1-enable-new-architecture)
  - [☁️ Step 2: Google Cloud Console Setup](#️-step-2-google-cloud-console-setup)
- [🚀 Usage](#-usage)
  - [🎯 One Tap Sign-In](#-one-tap-sign-in)
  - [👤 Sign-In with Account Chooser](#-sign-in-with-account-chooser)
  - [🔐 Legacy OAuth Sign-In](#-legacy-oauth-sign-in)
- [📝 API Reference](#-api-reference)
- [🛠️ Troubleshooting](#️-troubleshooting)
- [📄 License](#-license)

---

## ✨ Features

<table>
<tr>
<td width="50%">

### 🎯 Modern Authentication

- ✅ **One Tap Sign-In** (Credential Manager)
- ✅ **Account Chooser UI**
- ✅ **Legacy OAuth Support**

</td>
<td width="50%">

### ⚡ Built with Latest Tech

- ✅ **TurboModule** (New Architecture)
- ✅ **Codegen** Type Safety
- ✅ **Android Only** (for now)

</td>
</tr>
</table>

### 📋 Available Methods

| Method           | Description                  | Use Case                                  |
| ---------------- | ---------------------------- | ----------------------------------------- |
| `oneTap()`       | One Tap / Credential Manager | Quick sign-in for returning users         |
| `signIn()`       | Account Chooser UI           | First-time sign-in, account selection     |
| `legacySignIn()` | Legacy OAuth with scopes     | Advanced features (Drive, Calendar, etc.) |
| `signOut()`      | Sign out current user        | Logout, clear session                     |

---

## 📦 Installation

```bash
# Using npm
npm install @react-native-auth/google

# Using yarn
yarn add @react-native-auth/google
```

---

## ⚙️ Setup

### 📱 Step 1: Enable New Architecture

<details>
<summary>Show details</summary>

<br>

Add to your `android/gradle.properties`:

```properties
newArchEnabled=true
kotlin.version=2.1.20
```

</details>

### ☁️ Step 2: Google Cloud Console Setup

<details>
<summary>Show details</summary>

<br>

### 🌐 Access Console

Go to [**Google Cloud Console**](https://console.cloud.google.com/) and create/select your project.

### 🔌 Enable API

1. Navigate to **APIs & Services** → **Library**
2. Search for **"Google Sign-In API"** or **"Google Identity"**
3. Click **Enable**

### 🔑 Create OAuth 2.0 Credentials

You need **TWO** client IDs:

#### a) 📱 Android Client ID

For app verification (SHA-1 fingerprint required)

1. Go to **APIs & Services** → **Credentials**
2. Click **Create Credentials** → **OAuth 2.0 Client ID**
3. Select **Android**
4. Fill in:
   - **Name**: `Your App (Android)`
   - **Package name**: `com.yourapp` (from `android/app/build.gradle`)
   - **SHA-1 fingerprint**: Get it by running:

     ```bash
     # Debug
     keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

     # Release
     keytool -list -v -keystore /path/to/release.keystore -alias your-alias
     ```

5. Click **Create**

#### b) 🌐 Web Client ID

For authentication (**USE THIS IN YOUR CODE**)

1. Click **Create Credentials** → **OAuth 2.0 Client ID** again
2. Select **Web application**
3. Fill in:
   - **Name**: `Your App (Web)`
   - **Authorized redirect URIs**: Leave empty
4. Click **Create**
5. **Copy the Web Client ID** → `xxxxx.apps.googleusercontent.com`

> **💡 Pro Tip**: The Android Client ID verifies your app. The Web Client ID is used for authentication.

</details>

---

## 🚀 Usage

### 📥 Import

```typescript
import { oneTap, signIn, legacySignIn, signOut } from '@react-native-auth/google';
````

### 🎯 One Tap Sign-In

Quick authentication for returning users with saved credentials.

```typescript
const CLIENT_ID = 'YOUR_WEB_CLIENT_ID.apps.googleusercontent.com';

try {
  const result = await oneTap({ clientId: CLIENT_ID });
  console.log('✅ Signed in:', result.idToken);
  console.log('📧 Email:', result.email);
} catch (error) {
  console.error('❌ Sign-in failed:', error);
}
```

### 👤 Sign-In with Account Chooser

Display account picker UI for first-time users or account switching.

```typescript
const result = await signIn({
  clientId: CLIENT_ID,
});
```

### 🔐 Legacy OAuth Sign-In

Advanced authentication with custom OAuth scopes (Drive, Calendar, etc.)

```typescript
const result = await legacySignIn({
  clientId: CLIENT_ID,
  scopes: [
    'https://www.googleapis.com/auth/drive.readonly',
    'https://www.googleapis.com/auth/calendar.readonly',
  ],
});
```

### 🚪 Sign Out

Sign out the current user and clear the session.

```typescript
try {
  await signOut();
  console.log('✅ Signed out successfully');
} catch (error) {
  console.error('❌ Sign-out failed:', error);
}
```

---

## 📝 API Reference

### Types

```typescript
type GoogleAuthOptions = {
  clientId: string; // Your Web Client ID
  scopes?: string[]; // OAuth scopes (legacySignIn only)
};

type GoogleAuthResult = {
  idToken: string; // JWT ID token
  email?: string; // User email (if available)
};
```

---

## 🛠️ Troubleshooting

<details>
<summary><b>Common Issues</b></summary>

### ❌ "Sign-in failed" or "Invalid client ID"

- ✅ Verify you're using the **Web Client ID**, not Android Client ID
- ✅ Check SHA-1 fingerprint is correctly added for Android Client ID
- ✅ Ensure Google Sign-In API is enabled in Console

### ❌ "Credential Manager not available"

- ✅ Test on Android 9+ (API level 28+)
- ✅ Ensure Google Play Services is updated
- ✅ Test on a physical device (emulators may have issues)

</details>

---

## 📄 License

[![License: MIT](https://img.shields.io/github/license/react-native-auth/google?style=for-the-badge&logo=opensourceinitiative&logoColor=white)](../..)

---

<div align="center">

**Made with ❤️ for React Native**

[Report Bug](../../issues) · [Request Feature](../../issues)

</div>
