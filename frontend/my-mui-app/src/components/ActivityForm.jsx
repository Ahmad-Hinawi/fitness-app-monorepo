import React, { useState } from "react";
import Box from '@mui/material/Box';
import { Button, FormControl, InputLabel, MenuItem, TextField, Select } from "@mui/material";
import { addActivity } from "../api/activityApi"; 
import { useSelector } from "react-redux"; 
import { useNavigate } from 'react-router-dom';

const ActivityForm = ({ onActivityAdded }) => {
    const navigate = useNavigate();
    const token = useSelector((state) => state.auth.token);

    // 1. This stores ONLY the ID of the activity after the API call succeeds
    const [savedId, setSavedId] = useState(null);
    const userId = useSelector((state) => state.auth.user?.id || state.auth.userId);
    console.log("Current User ID from Redux:", userId);
    // 2. This stores the current data in the form fields
    const [activity, setActivity] = useState({
        
        activityType: "RUNNING",
        duration: '',
        caloriesBurned: '',
        startTime: '',
        additionalMetrics: { notes: '' } 
    });

    const handleSubmit = async (e) => {
        e.preventDefault();
        const payload = {
        userId: userId,                // NEW: Send the userId
        activityType: activity.activityType,   // FIX: Rename 'type' to 'activityType'
        duration: activity.duration,
        caloriesBurned: activity.caloriesBurned,
        startTime: activity.startTime,
        additionalMetrics: activity.additionalMetrics
    };
        try {
            // We pass the 'activity' state object to the API
            const response = await addActivity(payload, token); 
            
            // Extract the ID from the response and store it
            const newId = response.id || response._id;
            setSavedId(newId);

            if (onActivityAdded) {
                 onActivityAdded(newId);
            }

            // Reset the form fields
            setActivity({ 
                activityType: "RUNNING", 
                duration: '', 
                caloriesBurned: '', 
                startTime: '', 
                additionalMetrics: { notes: '' } 
            });
            
            alert("Activity added successfully!");
        } catch (error) {
            console.error("Error adding activity:", error);
        }
    };


    const handleGoToDetails = () => {
        // Use the savedId we got from the server
        if (savedId) {
            console.log(`activityId :${savedId}`);
            navigate(`/activities/${savedId}`);
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ mb: 4 }}>
            <FormControl fullWidth sx={{ mb: 2 }}>
                <InputLabel id="activity-type-label">Activity Type</InputLabel>
                <Select
                    labelId="activity-type-label"
                    label="Activity Type"
                    value={activity.activityType}
                    onChange={(e) => setActivity({ ...activity, activityType: e.target.value })}
                >
                    <MenuItem value="RUNNING">RUNNING</MenuItem>
                    <MenuItem value="WALKING">WALKING</MenuItem>
                    <MenuItem value="CYCLING">CYCLING</MenuItem>
                    <MenuItem value='SWIMMING'>SWIMMING</MenuItem>
                    <MenuItem value='YOGA'>YOGA</MenuItem>
                    <MenuItem value='WEIGHT_TRAINING'>WEIGHT_TRAINING</MenuItem>
                    <MenuItem value='CARDIO'>CARDIO</MenuItem>
                    <MenuItem value='STRETCHING'>STRETCHING</MenuItem>
                </Select>
            </FormControl>

            <TextField
                fullWidth
                label="Duration (Minutes)"
                type='number'
                sx={{ mb: 2 }}
                value={activity.duration}
                onChange={(e) => setActivity({ ...activity, duration: Number(e.target.value) })}
            />

            <TextField
                fullWidth
                label="Calories Burned"
                type='number'
                sx={{ mb: 2 }}
                value={activity.caloriesBurned}
                onChange={(e) => setActivity({ ...activity, caloriesBurned:Number( e.target.value) })}
            />

            <TextField 
                fullWidth 
                label="Start Time" 
                type="datetime-local"
                sx={{ mb: 2 }} 
                InputLabelProps={{ shrink: true }} 
                value={activity.startTime}
                onChange={(e) => setActivity({ ...activity, startTime: e.target.value })}
            />

            <TextField 
                fullWidth
                label="Extra Notes" 
                multiline
                rows={2}
                sx={{ mb: 2 }}
                value={activity.additionalMetrics?.notes || ''}
                onChange={(e) => setActivity({ 
                    ...activity,
                    additionalMetrics: { ...activity.additionalMetrics, notes: e.target.value } 
                })}
            />

            <Box sx={{ display: 'flex', gap: 2, mt: 3 }}>
                <Button type='submit' variant='contained' color="primary" fullWidth>
                    Add Activity
                </Button>
                <Button 
                    type="button" 
                    variant="outlined" 
                    color="secondary"
                    onClick={handleGoToDetails} 
                    disabled={!savedId} 
                >
                    View AI Analysis
                </Button>
            </Box>
        </Box>
    );
}

export default ActivityForm;