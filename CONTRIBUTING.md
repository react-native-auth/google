# Contributing

Contributions are always welcome, no matter how large or small! 🎉

We want this community to be friendly and respectful to each other. Please follow it in all your interactions with the project. Before contributing, please read the [code of conduct](./CODE_OF_CONDUCT.md).

## 🚀 Development workflow

This project is a monorepo. It contains the following packages:

- The library package in the root directory.
- An example app in the `example/` directory.

### Prerequisites

- [Node.js](https://nodejs.org/) - See [`.nvmrc`](./.nvmrc) for the required version (v22.20.0)
- [Android Studio](https://developer.android.com/studio) for Android development
- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) or higher

### 📦 Installation

Run `npm install` in the root directory to install the required dependencies:

```sh
npm install
```

### 🏃 Running the Example App

The [example app](/example/) demonstrates usage of the library. You need to run it to test any changes you make.

It is configured to use the local version of the library, so any changes you make to the library's source code will be reflected in the example app. Changes to the library's JavaScript code will be reflected in the example app without a rebuild, but native code changes will require a rebuild of the example app.

**Start Metro bundler:**

```sh
npm run example start
```

**Run on Android:**

```sh
npm run example android
```

To confirm that the app is running with the new architecture, check the Metro logs for:

```sh
Running "GoogleExample" with {"fabric":true,"initialProps":{"concurrentRoot":true},"rootTag":1}
```

Note the `"fabric":true` and `"concurrentRoot":true` properties.

### 🛠️ Development Tools

**Type checking:**

```sh
npm run typecheck
```

**Linting:**

```sh
npm run lint
```

**Fix formatting:**

```sh
npm run lint -- --fix
```

**Run tests:**

```sh
npm test
```

**Build library:**

```sh
npm run prepare
```

### 📝 Editing Native Code

**Android:**

- Open `example/android` in Android Studio
- Find source files at `google` under Android view

**Native code location:**

- Java/Kotlin: `android/src/main/java/io/github/reactnativeauth/google/`

### 🎯 Commit message convention

We follow the [conventional commits specification](https://www.conventionalcommits.org/en) for our commit messages:

- `feat:` new features (e.g., `feat: add silent sign-in support`)
- `fix:` bug fixes (e.g., `fix: resolve crash on Android 14`)
- `docs:` documentation changes (e.g., `docs: update setup guide`)
- `refactor:` code refactoring (e.g., `refactor: simplify auth flow`)
- `test:` adding or updating tests (e.g., `test: add unit tests for oneTap`)
- `chore:` tooling changes (e.g., `chore: update CI config`)
- `perf:` performance improvements (e.g., `perf: optimize token caching`)

Our pre-commit hooks verify that your commit message matches this format when committing.

### 📤 Sending a pull request

> **Working on your first pull request?** You can learn how from this _free_ series: [How to Contribute to an Open Source Project on GitHub](https://app.egghead.io/playlists/how-to-contribute-to-an-open-source-project-on-github).

When you're sending a pull request:

- ✅ Prefer small pull requests focused on one change
- ✅ Verify that linters and tests are passing
- ✅ Review the documentation to make sure it looks good
- ✅ Follow the pull request template when opening a pull request
- ✅ For pull requests that change the API or implementation, discuss with maintainers first by opening an issue
- ✅ Add tests for your changes if possible
- ✅ Update the README.md if you're adding new features

### 🚢 Publishing to npm

We use [release-it](https://github.com/release-it/release-it) to make it easier to publish new versions. It handles common tasks like bumping version based on semver, creating tags and releases etc.

To publish new versions, run the following:

```sh
npm run release
```

### 📜 Scripts

The `package.json` file contains various scripts for common tasks:

| Script                    | Description                                |
| ------------------------- | ------------------------------------------ |
| `npm install`             | Setup project by installing dependencies   |
| `npm run typecheck`       | Type-check files with TypeScript           |
| `npm run lint`            | Lint files with ESLint                     |
| `npm test`                | Run unit tests with Jest                   |
| `npm run prepare`         | Build the library                          |
| `npm run example start`   | Start the Metro server for the example app |
| `npm run example android` | Run the example app on Android             |

### 💡 Tips

- Make sure to test on physical Android devices when possible
- Test with different Android versions (API 24+)
- Ensure the SHA-1 fingerprint is correctly configured for testing
- Check that both debug and release builds work correctly

---

## 🤝 Code of Conduct

Please note that this project is released with a [Contributor Code of Conduct](./CODE_OF_CONDUCT.md). By participating in this project you agree to abide by its terms.

## 📄 License

By contributing to @react-native-auth/google, you agree that your contributions will be licensed under its MIT license.
