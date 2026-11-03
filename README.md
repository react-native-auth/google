<div align="center">

<img src="https://github.com/react-native-auth.png" alt="react-native-auth logo" height="120">

# `@react-native-auth/google`

**Modern Google Authentication for React Native**

[![NPM Version](https://img.shields.io/npm/v/%40react-native-auth%2Fgoogle?style=for-the-badge&logo=npm&logoColor=white&color=greeen)](https://www.npmjs.com/package/@react-native-auth/google)
[![License: MIT](https://img.shields.io/github/license/react-native-auth/google?style=for-the-badge&logo=opensourceinitiative&logoColor=white)](https://opensource.org/licenses/MIT)
[![Platform: Android](https://img.shields.io/badge/Platform-Android-green.svg?style=for-the-badge&logo=android)](https://developer.android.com/)

[![GitHub Issues](https://img.shields.io/github/issues/react-native-auth/google?style=for-the-badge&logo=github)](https://github.com/react-native-auth/google/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/react-native-auth/google?style=for-the-badge&logo=github)](https://github.com/react-native-auth/google/pulls)

</div>

---

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [API Reference](#api-reference)
- [Troubleshooting](#troubleshooting)
- [License](#license)

---

## Features

<table>
<thead>
<tr>
<th>Modern Authentication</th>
</tr>
</thead>
<tr>
<td>

- **One Tap Sign-In** (Credential Manager)
- **Account Chooser UI**
- **Legacy OAuth Support**
- **Scope Request**

</td>
</tr>
</table>

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

## Usage

See (src/GoogleAuth.tsx)[https://github.com/react-native-auth/google/blob/main/src/GoogleAuth.tsx] for more information.

#### `prompt` Parameter

Controls the authentication flow behavior:

| Value              | Behavior                                          |
| ------------------ | ------------------------------------------------- |
| `undefined`        | Default behavior - no forced interaction          |
| `'consent'`        | Always show consent screen, even for existing app |
| `'login'`          | Always show login screen, force account selection |
| `'select_account'` | Always show account selection screen              |
| `'none'`           | Silent authentication (may fail if no session)    |

---

## API Reference

### Types

See (src/NativeGoogleAuth.ts)[https://github.com/react-native-auth/google/blob/main/src/NativeGoogleAuth.ts] for more information.

---

## Troubleshooting

- Verify you're using the **Web Client ID**, not Android Client ID
- Check SHA-1 fingerprint is correctly added for Android Client ID

---

## License

[![License: MIT](https://img.shields.io/github/license/react-native-auth/google?style=for-the-badge&logo=opensourceinitiative&logoColor=white)](https://github.com/react-native-auth/google#-license)

---

<p align="center">Made with <a href="https://github.com/callstack/react-native-builder-bob">create-react-native-library</a></p>
