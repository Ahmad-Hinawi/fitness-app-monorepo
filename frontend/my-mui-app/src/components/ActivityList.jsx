import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom"; // Added for navigation
import { getUserActivities } from "../api/activityApi";
import { 
    List, 
    ListItem, 
    ListItemText, 
    Paper, 
    Typography, 
    Divider, 
    Box, 
    CircularProgress,
    Button // Added Button import
} from "@mui/material";

const ActivityList = ({ refreshTrigger }) => {
    const [activities, setActivities] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate(); // Initialize navigate
    
    const token = useSelector((state) => state.auth.token);
    const userId = useSelector((state) => state.auth.userId);

    const fetchActivities = async () => {
        if (!userId || !token) return;
        
        setLoading(true);
        try {
            const data = await getUserActivities(userId, token);
            setActivities(data);
        } catch (error) {
            console.error("Error fetching activities:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchActivities();
    }, [userId, token, refreshTrigger]);

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
                <CircularProgress />
            </Box>
        );
    }

    return (
        <Paper elevation={3} sx={{ p: 2, mt: 2 }}>
            <Typography variant="h6" gutterBottom>
                Your Recent Activities
            </Typography>
            <Divider />
            <List>
                {activities.length === 0 ? (
                    <Typography sx={{ p: 2, color: 'gray' }}>
                        No activities found. Start moving!
                    </Typography>
                ) : (
                    activities.map((activity) => (
                        <ListItem 
                            key={activity.id} 
                            divider 
                            secondaryAction={
                                <Button 
                                    variant="contained" 
                                    size="small" 
                                    color="primary" 
                                    onClick={() => navigate(`/activities/${activity.id}`)}
                                >
                                    View Details
                                </Button>
                            }
                        >
                            <ListItemText
                                primary={`${activity.activityType} - ${activity.duration} Minutes`}
                                secondary={
                                    <>
                                        <Typography component="span" variant="body2" color="text.primary">
                                            {activity.caloriesBurned} kcal burned — 
                                            {/* Formatting the date to look nicer */}
                                            {` Start Time: ${new Date(activity.createdAt).toLocaleString()}`}
                                        </Typography>
                                        {activity.additionalMetrics?.notes && (
                                            <Typography component="block" variant="caption" sx={{ display: 'block', mt: 0.5 }}>
                                                Note: {activity.additionalMetrics.notes}
                                            </Typography>
                                        )}
                                    </>
                                }
                            />
                        </ListItem>
                    ))
                )}
            </List>
        </Paper>
    );
};

export default ActivityList;