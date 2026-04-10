import API from "./axiosInstance";

export const getUser=async (userId)=>{
    const response=API.get(`/users/${userId}`);
    return (await response).data;
};
export const updateUser=async (userId,profileData)=>{
    const response=await API.put(`/users/${userId}`,profileData);
    return (await response).data;
};