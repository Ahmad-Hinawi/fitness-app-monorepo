import API from "./axiosInstance";

export const addActivity=async (activityData)=>{
    const response=await API.post('/activities',activityData,{
      headers: {
                'Content-Type': 'application/json' // Double-check this!
            }  
    });

    return response.data;
};
export const getUserActivities=async(userId)=>{
    const response=await API.get(`/activities/${userId}` );
    return response.data;
};