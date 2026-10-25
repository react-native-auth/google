const path = require('path');
const { getDefaultConfig } = require('@react-native/metro-config');
const { withMetroConfig } = require('react-native-monorepo-config');
const { withNativeWind } = require('nativewind/metro');

const defaultConfig = getDefaultConfig(__dirname);

const monorepoConfig = withMetroConfig(defaultConfig, {
  root: path.resolve(__dirname, '..'),
  dirname: __dirname,
  conditions: ['react-native-auth-google-source'],
});

module.exports = withNativeWind(monorepoConfig, { input: './global.css' });