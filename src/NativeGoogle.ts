import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export type GoogleAuthOptions = {
  clientId: string;
  scopes?: string[];
};

export type GoogleAuthResult = {
  idToken: string;
  email?: string;
};

export interface Spec extends TurboModule {
  oneTap(options: GoogleAuthOptions): Promise<GoogleAuthResult>;

  signIn(options: GoogleAuthOptions): Promise<GoogleAuthResult>;

  legacySignIn(options: GoogleAuthOptions): Promise<GoogleAuthResult>;

  signOut(): Promise<void>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('Google');
