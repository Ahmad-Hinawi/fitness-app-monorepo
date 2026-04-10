import React,{useState,useEffect} from 'react'
import { Box,TextField,Button,Typography,Paper,Grid
    ,MenuItem,Avatar,Divider,Container} from '@mui/material'
import { useSelector } from 'react-redux'
import { getUser,updateUser } from "../../api/userApi";
const ProfilePage=()=>
{
    const{userId,token}=useSelector((state)=>state.auth);
    const[isEditing,setIsEditing]=useState(false);
    const [loading, setLoading] = useState(true);
    const[profile,setProfile]=useState({
        firstName:'',
        lastName:'',
        email:'',
        height:'',
        weight:'',
        gender:'',
        birthDate:''
    })
    const handleChange=(e)=>{
        const {name,value}=e.target;
        setProfile(prev=>({...prev,[name]:value}));
    };
    useEffect(() => {
     const fetchFullProfile = async () => {
            if (!userId || !token) return;
            
            setLoading(true);
            try {
                // 2. Call the API function
              const data = await getUser(userId, token);
              setProfile(data);
            } catch (error) {
                console.error("Error fetching profile:", error);
            } finally {
                setLoading(false);
            }
        };

    if (userId) {
        fetchFullProfile();
    }
    }, [userId, token]);
    const handleSave = async () => {
     try {
        setLoading(true); // Show spinner while saving
    
        // Call the API function we just created
        const updatedData = await updateUser(profile.keycloakId, profile);
    
        setProfile(updatedData); // Update state with the fresh data from DB
        setIsEditing(false);     // Switch back to "View" mode
        alert("Profile updated successfully!"); 
  } catch (error) {
        console.error("Save failed:", error);
        alert("Failed to update profile. Please try again.");
  } finally {
       setLoading(false);
  }
};
    return (
        <Container maxWidth="md" sx={{mt:4,mb:4}}>
            <Paper elevation={3} sx={{p:4,borderRadius:2}}>
                <Box sx={{display:'flex',alignItems:'center',mb:3}}>
                    <Avatar sx={{width:100,height:100,mr:3,bgcolor:'primary'}}>
                        {profile.firstName? profile.firstName[0]:'U'}
                    </Avatar>
                    <Box>
                        <Typography variant='h4' fontWeight='bold'>
                            {profile.firstName} {profile.lastName}
                        </Typography>
                        <Typography color='textSecondary'>Fitness Profile</Typography>
                    </Box>
                </Box>
                <Divider sx={{mb:4}}/>
                <Grid container spacing={3}>
               
                    <Grid size={{xs:12, sm:6}}>
                        <TextField fullWidth label="Gender" name="gender"
                            value={profile.gender||''} onChange={handleChange}
                            disabled={!isEditing} select>
                            <MenuItem value='Male'>Male</MenuItem>
                            <MenuItem value='Female'>Female</MenuItem>
                            <MenuItem value='Other'>Other</MenuItem>
                        </TextField>    
                    </Grid>
                     <Grid size={{xs:12, sm:6}}>
                        <TextField fullWidth   type="number"
                            label="Weight (kg)" name="weight"  value={profile.weight||''}
                            onChange={handleChange}  disabled={!isEditing}
                        />
                    </Grid>
                     <Grid size={{xs:12, sm:6}}>
                        <TextField fullWidth   type="number"
                            label="Height (cm)" name="height"  value={profile.height||''}
                            onChange={handleChange}  disabled={!isEditing}
                        />
                    </Grid>
                     <Grid size={{xs:12, sm:6}}>
                        <TextField fullWidth   type="text" 
                            label="Fitness Goal" name="fitnessGoal"  value={profile.fitnessGoal||''}
                            onChange={handleChange}  disabled={!isEditing} select>
                            <MenuItem value='Weight Loss'>Weight Loss</MenuItem>                       
                            <MenuItem value='Muscle Gain'>Muscle Gain</MenuItem>
                            <MenuItem value='Maintenance'>Maintenance</MenuItem>
                         </TextField>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6 }}>
                        <TextField fullWidth label="Birth Date" name="birthDate"
                            type="date" // This enables the calendar picker
                            value={profile.birthDate || ''} // Ensure it's never undefined
                            onChange={handleChange}     disabled={!isEditing}
                            
                        />
                    </Grid>

                </Grid>
                <Box sx={{mt:4, display:'flex',justifyContent:'flex-end' ,gap:2}}>
                    {isEditing?(
                        <>
                        <Button variant='outlined' color='error' onClick={()=>setIsEditing(false)}>Cancel</Button>
                        <Button variant='contained' color='success' onClick={handleSave } >{loading ? "Saving..." : "Save Profile"} </Button>

                        </>
                    ): (
                        <Button variant="contained" onClick={() => setIsEditing(true)} >
                            Edit Profile
                        </Button>
                    )}
                </Box>
            </Paper>
        </Container>
    )
}
export default ProfilePage;
