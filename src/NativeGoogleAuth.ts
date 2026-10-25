import { TurboModuleRegistry, type TurboModule } from "react-native";

export interface GoogleAuthOptions {
  clientId: string;
  scopes?: string[];
  prompt?: string;
}

export interface Spec extends TurboModule {
  oneTap(options: GoogleAuthOptions): Promise<Object>;
  signIn(options: GoogleAuthOptions): Promise<Object>;
  legacySignIn(options: GoogleAuthOptions): Promise<Object>;
  requestScopes(scopes: string[]): Promise<Object>;
  signOut(): Promise<string>;
}

export default TurboModuleRegistry.getEnforcing<Spec>("GoogleAuth");
