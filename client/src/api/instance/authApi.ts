import type { APIResponse } from '@/api';
import axiosInstance from './axios';

export const login = (username: string, password: string) => {
    return axiosInstance.post<APIResponse<void>>('/auth/login',{username, password})
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
    return axiosInstance.post<APIResponse<void>>('/auth/refresh',{refreshToken})
}

export const forgotPassword = (
    username: string,
    email: string
) => {
    return axiosInstance.post<APIResponse<void>>('/auth/forgot',{username,email})
}

export const isValidateToken = (
    token: string
) => {
    return axiosInstance.get<APIResponse<void>>(`/auth/isValid/${token}`)
}

export const resetPassword = (
    token: string,
    password: string
) => {
    return axiosInstance.post<APIResponse<void>>('/auth/reset',{token,password})
}

export const verifyEmail = (
  token: string
) => {
  return axiosInstance.post<APIResponse<void>>(`/auth/verifyEmail?token=${token}`);
}

export const loggedIn = () => {
  return axiosInstance.get<APIResponse<void>>('/auth/loggedIn');
}