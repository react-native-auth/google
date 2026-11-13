<div align="center">
<img src="https://github.com/react-native-auth.png" alt="react-native-auth logo" height="120">

# `@react-native-auth/google`

**Modern Google Authentication for React Native**

[![NPM Version](https://img.shields.io/npm/v/%40react-native-auth%2Fgoogle?style=for-the-badge&logo=npm&logoColor=white&color=green)](https://www.npmjs.com/package/@react-native-auth/google) [![License: MIT](https://img.shields.io/github/license/react-native-auth/google?style=for-the-badge&logo=opensourceinitiative&logoColor=white)](https://opensource.org/licenses/MIT) [![Platform: Android](https://img.shields.io/badge/Platform-Android-green.svg?style=for-the-badge&logo=android)](https://developer.android.com/)

[![GitHub Issues](https://img.shields.io/github/issues/react-native-auth/google?style=for-the-badge&logo=github)](https://github.com/react-native-auth/google/issues) [![GitHub Pull Requests](https://img.shields.io/github/issues-pr/react-native-auth/google?style=for-the-badge&logo=github)](https://github.com/react-native-auth/google/pulls)

</div>

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Features](#features)
- [Installation](#installation)
- [Example App](#download-the-example-app)
- [Usage](#usage)
- [API Reference](#api-reference)
- [License](#license)

---

## Prerequisites

Before implementing this library, you must configure your Google Cloud Console credentials correctly:

- **Web Client ID:** This is the *only* Client ID you will use inside your actual application code (such as your React components or configuration files).
- **Android Client ID:** You must create an Android Client ID in the Google Cloud Console and register your app's SHA-1 fingerprint to it. 

> **Important:** Do not use the Android Client ID in your React Native source code. The Android Client ID exists strictly for Google's servers to verify your application's SHA-1 signature behind the scenes. Your app code should only ever reference the Web Client ID.

---

## Features

- **One Tap Sign-In** (via Credential Manager)
- **Account Chooser UI**
- **Legacy OAuth Support**
- **Scope Request**

---

## Installation

### Using npm

```bash
npm install @react-native-auth/google
```

### Using yarn

```bash
yarn add @react-native-auth/google
```
---

## Download the Example App

You can download the example app on our github action's [artifacts](https://github.com/react-native-auth/google/actions/workflows/ci.yml)

The source of the example app can be found [here](https://github.com/react-native-auth/google/tree/main/example)

The `fingerprints` of the example app are:

> SHA1:
> ```
> 5E:8F:16:06:2E:A3:CD:2C:4A:0D:54:78:76:BA:A6:F3:8C:AB:F6:25
> ```
> 
> SHA-256:
> ```
> FA:C6:17:45:DC:09:03:78:6F:B9:ED:E6:2A:96:2B:39:9F:73:48:F0:BB:6F:89:9B:83:32:66:75:91:03:3B:9C
> ```
> The example app uses the default [`debug.keystore`](https://github.com/react-native-auth/google/blob/main/example/android/app/debug.keystore) file provided by android.

## Usage

For detailed implementation examples, please refer to [src/GoogleAuth.tsx](https://github.com/react-native-auth/google/blob/main/src/GoogleAuth.tsx).

### The `prompt` Parameter

This parameter controls the behavior of the authentication flow:

| Value | Behavior |
| --- | --- |
| `undefined` | Default behavior - no forced interaction |
| `'consent'` | Always show consent screen, even for an existing app |
| `'login'` | Always show login screen, force account selection |
| `'select_account'` | Always show account selection screen |
| `'none'` | Silent authentication (may fail if no session exists) |

---

## API Reference

### Types

For full type definitions, please refer to [src/NativeGoogleAuth.ts](https://github.com/react-native-auth/google/blob/main/src/NativeGoogleAuth.ts).

---

## License

[![License: MIT](https://img.shields.io/github/license/react-native-auth/google?style=for-the-badge&logo=opensourceinitiative&logoColor=white)](https://github.com/react-native-auth/google/blob/main/LICENSE)
