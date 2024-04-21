import type { APIResponse } from '@/api';
import axiosInstance from './axios';
import type { UserDTO } from '@/types'

export const login = (username: string, password: string) => {
    return axiosInstance.post<APIResponse<UserDTO>>('/auth/login',{username, password})
}

export const logout = () => {
    axiosInstance.post('/user/logout');
}

export const apply  = (
    username: string,
    password: string,
    email: string,
    firstName: string,
    lastName: string

) => {
    return axiosInstance.post<APIResponse<void>>(
        '/auth/apply',
        {
            username,
            password,
            email,
            firstName,
            lastName
        }
    );
}

export const refresh = (
    refreshToken: string
) => {
    return axiosInstance.post<APIResponse<UserDTO>>('/auth/refresh',{refreshToken})
}

export const resetPassword = (
    username: string,
) => {
    return axiosInstance.post<APIResponse<void>>('/auth/reset',{username})
}

export const verifyEmail = (
  token: string
) => {
  return axiosInstance.post<APIResponse<void>>(`/auth/verifyEmail?token=${token}`);
}