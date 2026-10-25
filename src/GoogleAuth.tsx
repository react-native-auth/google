import GoogleAuth from "./NativeGoogleAuth";

export interface GoogleAuthOptions {
  clientId: string;
  scopes?: string[];
  prompt?: string;
}

export interface GoogleAuthResult {
  idToken?: string;
  email?: string;
  displayName?: string;
  photoUrl?: string;
  serverAuthCode?: string;
  [key: string]: any;
}

export async function legacySignIn(
  options: GoogleAuthOptions,
): Promise<GoogleAuthResult> {
  const res = (await GoogleAuth.legacySignIn(options)) as {
    account: GoogleAuthResult;
  };
  return res.account;
}

export async function oneTap(
  options: GoogleAuthOptions,
): Promise<GoogleAuthResult> {
  const res = (await GoogleAuth.oneTap(options)) as {
    account: GoogleAuthResult;
  };
  return res.account;
}

export async function signIn(
  options: GoogleAuthOptions,
): Promise<GoogleAuthResult> {
  const res = (await GoogleAuth.signIn(options)) as {
    account: GoogleAuthResult;
  };
  return res.account;
}

export async function requestScopes(scopes: string[]): Promise<any> {
  const res = (await GoogleAuth.requestScopes(scopes)) as { result: any };
  return res.result;
}

export async function signOut(): Promise<string> {
  const res = await GoogleAuth.signOut();
  return res;
}
