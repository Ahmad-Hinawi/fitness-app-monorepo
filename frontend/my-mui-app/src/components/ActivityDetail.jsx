import React, { useState, useEffect } from "react"; // 1. Added useEffect
import { getActivityRecommendation } from "../api/aiApi";
import { useParams } from "react-router-dom"
import { Card, CardContent, Typography, List, ListItem, ListItemText, Divider, Chip, CircularProgress, Box } from '@mui/material';

const ActivityDetail = () => {
  // 2. Create a state to hold the recommendation
  const [recommendation, setRecommendation] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { savedId} = useParams();


  useEffect(() => {
    let timer;
  
  const fetchData = async () => {
    try {
      setLoading(true);
      const data = await getActivityRecommendation(savedId);
      
      if (data) {
        setRecommendation(data);
        setLoading(false); // We got it! Stop loading.
      } else {
        // Data not in DB yet, wait 3 seconds and try again
        console.log("Not ready yet, retrying in 3 seconds...");
        timer = setTimeout(fetchData, 3000);
      }
    } catch (err) {
      // Even if there is a 500 error, let's try again in a few seconds
      timer = setTimeout(fetchData, 3000);
    }
  };

    if (savedId) {
      fetchData();
    }
  }, [savedId]); // 4. This runs whenever saveId changes

  // 5. Handling different states
  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
        <Typography sx={{ ml: 2 }}>Analyzing with AI...</Typography>
      </Box>
    );
  }

  if (error) return <Typography color="error">{error}</Typography>;
  if (!recommendation) return <Typography>No data found for this activity.</Typography>;

  return (
    <Card sx={{ mt: 3, boxShadow: 3 }}>
      <CardContent>
        <Typography variant="h5" color="primary" gutterBottom>
          AI Performance Analysis
        </Typography>
        
        <Typography variant="body1" sx={{ whiteSpace: 'pre-line', mb: 2 }}>
          {recommendation.recommendation}
        </Typography>

        <Divider sx={{ my: 2 }} />

        <Typography variant="h6" color="secondary">Key Improvements</Typography>
        <List dense>
          {recommendation.improvements?.map((text, index) => (
            <ListItem key={index}>
              <ListItemText primary={text} />
            </ListItem>
          ))}
        </List>

        <Typography variant="h6" sx={{ mt: 2 }}>Workout Suggestions</Typography>
        <List dense>
          {recommendation.suggestions?.map((text, index) => (
            <ListItem key={index}>
              <ListItemText primary={text} />
            </ListItem>
          ))}
        </List>

        <Typography variant="h6" color="error" sx={{ mt: 2 }}>Safety Warnings</Typography>
        <Box sx={{ display: 'flex', gap: '10px', flexWrap: 'wrap', mt: 1 }}>
          {recommendation.safety?.map((text, index) => (
            <Chip key={index} label={text} color="error" variant="outlined" />
          ))}
        </Box>
      </CardContent>
    </Card>
  );
};

export default ActivityDetail;