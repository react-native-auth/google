export type GoogleAuthOptions = {
  clientId: string;
  scopes?: string[];
};

export type GoogleAuthResult = {
  idToken: string;
  email?: string;
};
