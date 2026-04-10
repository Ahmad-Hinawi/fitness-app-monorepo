import API from "./axiosInstance";


export const getActivityRecommendation=async(activityId)=>{
    const response=await API.get(`/ai/activity/${activityId}` );
    return response.data;
};