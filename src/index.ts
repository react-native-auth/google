import Google from './NativeGoogle';
import type { GoogleAuthOptions, GoogleAuthResult } from './types';

/* -------------------------------------------------------------------------- */
/* Public API                                                                 */
/* -------------------------------------------------------------------------- */

export function oneTap(options: GoogleAuthOptions): Promise<GoogleAuthResult> {
  return Google.oneTap(options);
}

export function signIn(options: GoogleAuthOptions): Promise<GoogleAuthResult> {
  return Google.signIn(options);
}

export function legacySignIn(
  options: GoogleAuthOptions
): Promise<GoogleAuthResult> {
  return Google.legacySignIn(options);
}

export function signOut(): Promise<void> {
  return Google.signOut();
}

export type { GoogleAuthOptions, GoogleAuthResult };
