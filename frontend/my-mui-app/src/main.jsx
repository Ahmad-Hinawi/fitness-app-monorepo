// main.jsx
import React from 'react'
import { createRoot } from 'react-dom/client' // <--- Add this line!
import App from './App.jsx'
import { AuthProvider } from "react-oauth2-code-pkce";
import { Provider } from 'react-redux';
import { store } from './app/store';
import {authConfig} from './app/authConfig.js'

const container = document.getElementById('root');
const root = createRoot(container);

root.render(
  <AuthProvider 
    authConfig={authConfig}
    loadingComponent={<div>Authenticating with Keycloak...</div>}
    errorComponent={(error) => (
        <div>
            <p>Auth Error: {error.message}</p>
            <button onClick={() => window.location.href = '/'}>Try Again</button>
        </div>
    )}
  >
    <Provider store={store}>
      <App />
    </Provider>
  </AuthProvider>
);

