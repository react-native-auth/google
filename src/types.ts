export type GoogleAuthOptions = {
  clientId: string;
  scopes?: string[];
  prompt?: string;
};

export type GoogleAuthResult = {
  idToken: string;
  email?: string;
};
