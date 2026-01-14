# 🔒 Security Policy

## 🛡️ Supported Versions

We release patches for security vulnerabilities for the following versions:

| Version | Supported |
| ------- | --------- |
| x.x.x   | ✅ Yes    |
| < 0.0   | ❌ No     |

## 🚨 Reporting a Vulnerability

We take the security of @react-native-auth/google seriously. If you believe you have found a security vulnerability, please report it to us as described below.

### ⚠️ Please do NOT:

- ❌ Open a public GitHub issue
- ❌ Disclose the vulnerability publicly before it has been addressed
- ❌ Test the vulnerability on production systems without permission

### ✅ Please DO:

1. **Email us privately** at: **[react.native.auth@outlook.com)**

2. **Include the following information:**
   - Type of vulnerability
   - Full paths of source file(s) related to the vulnerability
   - Location of the affected source code (tag/branch/commit or direct URL)
   - Step-by-step instructions to reproduce the issue
   - Proof-of-concept or exploit code (if possible)
   - Impact of the issue, including how an attacker might exploit it

3. **Allow us time to respond:**
   - We will acknowledge receipt of your report within **48 hours**
   - We will provide a more detailed response within **7 days**
   - We will work on a fix and coordinate the disclosure timeline with you

## 🔐 Security Best Practices

When using this library, please follow these security best practices:

### 1. **Protect Your Client IDs**

```typescript
// ❌ Bad: Hardcoding Client ID in source code exposed in version control
const CLIENT_ID = 'xxxxx.apps.googleusercontent.com';

// ✅ Good: Use environment variables or secure configuration
import Config from 'react-native-config';
const CLIENT_ID = Config.GOOGLE_CLIENT_ID;
```

### 2. **Validate ID Tokens on Backend**

```typescript
// ⚠️ Client-side only (for UI purposes)
const result = await oneTap({ clientId: CLIENT_ID });

// ✅ Always verify on your backend server
fetch('https://your-api.com/auth/google', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ idToken: result.idToken }),
});
```

### 3. **Secure Your Keystores**

- ✅ Never commit your release keystore to version control
- ✅ Use secure keystore passwords (16+ characters)
- ✅ Store keystore credentials in secure CI/CD secrets
- ✅ Rotate keystores if compromised

### 4. **Configure ProGuard for Release Builds**

```proguard
# Keep authentication classes
-keep class io.github.reactnativeauth.google.** { *; }
-keep class com.google.android.gms.auth.** { *; }
```

### 5. **Use HTTPS for All API Calls**

```typescript
// ❌ Bad: HTTP endpoints
fetch('http://your-api.com/auth/verify', ...)

// ✅ Good: HTTPS only
fetch('https://your-api.com/auth/verify', ...)
```

### 6. **Handle Tokens Securely**

```typescript
// ✅ Store tokens securely (use react-native-keychain or similar)
import * as Keychain from 'react-native-keychain';

const result = await oneTap({ clientId: CLIENT_ID });
await Keychain.setGenericPassword('idToken', result.idToken);

// ❌ Don't store in AsyncStorage (not encrypted)
// await AsyncStorage.setItem('idToken', result.idToken);
```

### 7. **Implement Token Refresh**

- ✅ ID tokens expire after 1 hour
- ✅ Implement proper token refresh logic
- ✅ Handle authentication errors gracefully

### 8. **Keep Dependencies Updated**

```bash
# Check for security vulnerabilities
npm audit

# Update dependencies
npm update
```

## 🔍 Security Considerations

### OAuth Scopes

Only request the minimum scopes necessary for your application:

```typescript
// ❌ Bad: Requesting unnecessary permissions
const result = await legacySignIn({
  clientId: CLIENT_ID,
  scopes: [
    'https://www.googleapis.com/auth/drive',
    'https://www.googleapis.com/auth/gmail.modify',
    'https://www.googleapis.com/auth/calendar',
  ],
});

// ✅ Good: Request only what you need
const result = await legacySignIn({
  clientId: CLIENT_ID,
  scopes: ['https://www.googleapis.com/auth/drive.readonly'],
});
```

### Android Security

1. **Use `newArchEnabled=true`** for better security and performance
2. **Enable code obfuscation** with ProGuard/R8 in release builds
3. **Verify app signatures** with SHA-1 fingerprints in Google Cloud Console
4. **Test on multiple Android versions** (API 24+)

## 📚 Resources

- [Google Identity Best Practices](https://developers.google.com/identity/protocols/oauth2/production-readiness)
- [OWASP Mobile Security](https://owasp.org/www-project-mobile-security/)
- [React Native Security Guide](https://reactnative.dev/docs/security)

## 🤝 Responsible Disclosure

We believe in responsible disclosure and appreciate the security community's efforts to improve the security of this project. We will:

- ✅ Acknowledge your contribution in release notes (if you wish)
- ✅ Keep you informed throughout the fix process
- ✅ Credit you appropriately once the vulnerability is disclosed

## 📝 Security Updates

Security updates will be released as patch versions (e.g., 0.1.1 → 0.1.2) and announced via:

- GitHub Security Advisories
- Release notes on GitHub
- NPM package updates

---

**Thank you for helping keep @react-native-auth/google secure!** 🙏
