import type { APIResponse, AuthResponse } from '../response';
import axiosInstance from './axios';

export const login = (username: string, password: string) => {
    return axiosInstance.post<APIResponse<AuthResponse>>('/auth/login',{username, password})
}

export const logout = () => {
    axiosInstance.post('/user/logout');
}

export const apply  = (
    username: string,
    password: string,
    email: string,
    firstName: string,
    lastName: string,
    courseId: string

) => {
    return axiosInstance.post<APIResponse<void>>(
        '/auth/apply',
        {
            username,
            password,
            email,
            firstName,
            lastName,
            courseId
        }
    );
}

export const refresh = (
    refreshToken: string
) => {
    return axiosInstance.post<APIResponse<AuthResponse>>('/auth/refresh',{refreshToken})
}