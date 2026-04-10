import { useContext, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import { AuthContext } from 'react-oauth2-code-pkce';
import { useDispatch } from 'react-redux';
import { setCredentials } from './features/authSlice';
import { Button, Typography, Box, Container, Paper ,Link} from '@mui/material';

// Components
import ActivitiesPage from './components/ActivitiesPage';
import ActivityDetail from './components/ActivityDetail'

import ProfilePage from './components/ProfilePage/Profile';

function App() {
    const { token, tokenData, logIn, logOut, isAuthenticated } = useContext(AuthContext);
    const dispatch = useDispatch();

    // Sync Redux whenever the token is received
    useEffect(() => {
        if (token && tokenData) {
            dispatch(setCredentials({ token, user: tokenData }));
        }
    }, [token, tokenData, dispatch]);

    // 1. THE WELCOME / LANDING PAGE COMPONENT
    const WelcomePage = () => (
        <Box 
            display="flex" 
            flexDirection="column" 
            alignItems="center" 
            justifyContent="center" 
            minHeight="100vh"
            sx={{ backgroundColor: '#f5f5f5' }}
        >
            <Paper elevation={3} sx={{ p: 6, textAlign: 'center', borderRadius: 4 }}>
                <Typography variant="h2" gutterBottom sx={{ fontWeight: 'bold', color: '#1976d2' }}>
                    VITALITY TRACKER
                </Typography>
                <Typography variant="h5" color="textSecondary" sx={{ mb: 4 }}>
                    Your journey to a healthier lifestyle starts here.
                </Typography>
                <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center' }}>
                    <Button 
                        variant="contained" 
                        size="large" 
                        sx={{ px: 4, py: 1.5, fontSize: '1.1rem' }}
                        onClick={() => logIn()}
                    >
                        Login / Register
                    </Button>
                </Box>
            </Paper>
            <Box sx={{ mt: 6, opacity: 0.8 }}>
            <Typography variant="body1">
                Programmed by <strong>Ahmad Hinawi</strong>
            </Typography>
            <Typography variant="body2" sx={{ mt: 1 }}>
                © 2026 Vitality Tracker Project
            </Typography>
           <Typography variant='body2' sx={{ mt: 1 }}>
                For support, email: {' '}
                 <Link href="mailto:ahmad.hinawi@gmail.com" underline="hover">
                   ahmad.hinawi@gmail.com
                </Link>
            </Typography>
        </Box>
        </Box>
    );

    return (
        <Router>
            {/* If NOT authenticated, ONLY show the Welcome Page */}
            {!token ? (
                <Routes>
                    <Route path="*" element={<WelcomePage />} />
                </Routes>
            ) : (
                /* 2. THE AUTHENTICATED APP LAYOUT */
                <Box sx={{ flexGrow: 1 }}>
                    {/* Minimal Navbar for authenticated users */}
                    <Box sx={{ p: 2, display: 'flex', justifyContent: 'flex-end', gap: 2, bgcolor: '#fff', borderBottom: '1px solid #ddd' }}>
                        <Button onClick={() => window.location.href = '/activities'}>Dashboard</Button>
                        <Button onClick={() => window.location.href = '/profile'}>My Profile</Button>
                        <Button variant="outlined" color="error" onClick={logOut}>Logout</Button>
                    </Box>

                    <Container maxWidth="lg" sx={{ mt: 4 }}>
                        <Routes>
                            <Route path="/activities" element={<ActivitiesPage />} />
                            <Route path="/activities/:savedId" element={<ActivityDetail />} />
                            <Route path="/profile" element={<ProfilePage/> }/>
                            {/* Redirect root to activities if logged in */}
                            <Route path="/" element={<Navigate to="/activities" replace />} />
                            <Route path="*" element={<Navigate to="/activities" replace />} />
                        </Routes>
                    </Container>
                </Box>
            )}
        </Router>
    );
}

export default App;