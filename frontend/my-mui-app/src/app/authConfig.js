import { TAuthConfig } from "react-oauth2-code-pkce";



export const authConfig = {
  clientId: 'oauth2-pkce-client',
  authorizationEndpoint: 'http://localhost:8090/realms/fitness-oauth2/protocol/openid-connect/auth',
  tokenEndpoint: 'http://localhost:8090/realms/fitness-oauth2/protocol/openid-connect/token',
  redirectUri: 'http://localhost:5173',
  scope: 'openid profile email offline_access',
  
  // This tells the library to refresh the token in the background 
  // before the access token expires (default is 30-60 seconds before)
  autoLogin: false,

  // IMPORTANT: This library handles refresh internally as long as 
  // 'offline_access' is in the scope and the server provides a refresh token.
  
  onRefreshTokenExpire: (event) => {
    console.warn("Refresh token expired. The user must log in again.");
    event.logIn();
  },
}