import React, { useState } from "react";
// 1. Added Button to the imports
import { Box, Typography, Divider, Button } from "@mui/material"; 
import ActivityForm from './ActivityForm.jsx';
import ActivityList from './ActivityList.jsx';
// 2. Changed import name to match the usage below
import ActivityDetail from "./ActivityDetail.jsx"; 

const ActivitiesPage = () => {
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [selectedId, setSelectedId] = useState(null);

  // 3. Updated to accept the 'id' passed from the Form
  const handleRefresh = (id) => {
    console.log("Received ID in Parent:", id);
    setRefreshTrigger(prev => prev + 1);
   /* if (id) {
        setSelectedId(id); // This triggers the switch to the Analysis view
    }*/
  };

  return (
    <Box sx={{ p: 2 }}>
        {!selectedId ? (
            /* --- DASHBOARD VIEW --- */
            <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
                <Typography variant="h4" sx={{ mb: 3 }}>
                    My Fitness Dashboard
                </Typography>
                <ActivityForm onActivityAdded={handleRefresh} />
                <Divider sx={{ my: 4 }} />
                <ActivityList refreshTrigger={refreshTrigger}  />
            </Box>
        ) : (
            /* --- ANALYSIS VIEW --- */
            <Box sx={{ mb: 4 }}>
                <Button 
                    variant="outlined" 
                    onClick={() => setSelectedId(null)} 
                    sx={{ mb: 2 }}
                >
                    ← Back to Dashboard
                </Button>
                
                <Typography variant="h5" color="secondary" gutterBottom>
                    AI Performance Analysis
                </Typography>
                
                {/* Now the names match */}
                <ActivityDetail activityId={selectedId} />
                <Divider sx={{ my: 4 }} />
            </Box>
        )}
    </Box>
  );
};

export default ActivitiesPage;