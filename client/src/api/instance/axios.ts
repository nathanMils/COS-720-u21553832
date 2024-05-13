import axios from 'axios';
import type { APIResponse, AuthResponse } from '@/api';

const baseURL = import.meta.env.VITE_API_BASE_URL;

const axiosInstance = axios.create({
    baseURL: baseURL,
    timeout: 10000,
    withCredentials: true
})

axiosInstance.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      const originalRequest = error.config;
      if (
        error.response.status === 401 &&
        error.response.data.internalCode === 'JWT_EXPIRED' &&
        !originalRequest._retry
      ) {
        originalRequest._retry = true;
        try {
          await axiosInstance.post<APIResponse<AuthResponse>>('/auth/refresh');
          return axiosInstance(originalRequest);
        } catch (refreshError) {
          window.location.href = '/login';
          return Promise.reject(refreshError);
        }
      }
      return Promise.reject(error);
    }
);

export default axiosInstance;