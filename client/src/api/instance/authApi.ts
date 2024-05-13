import type { APIResponse } from '@/api';
import axiosInstance from './axios';

const base: string = '/auth'

export const login = (username: string, password: string) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/login`,{username, password})
}


export const apply  = (
    username: string,
    password: string,
    email: string,
    firstName: string,
    lastName: string

) => {
    return axiosInstance.post<APIResponse<void>>(
        `${base}/apply`,
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
    return axiosInstance.post<APIResponse<void>>(`${base}/refresh`,{refreshToken})
}

export const forgotPassword = (
    username: string,
    email: string
) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/forgot`,{username,email})
}

export const isValidateToken = (
    token: string
) => {
    return axiosInstance.get<APIResponse<void>>(`${base}/isValid/${token}`)
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

