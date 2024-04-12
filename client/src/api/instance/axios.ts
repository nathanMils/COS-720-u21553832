import axios from 'axios';

const baseURL = process.env.VITE_API_BASE_URL;

const axiosInstance = axios.create({
    baseURL: baseURL,
    timeout: 10000
})

axiosInstance.interceptors.request.use(
    (config) => {
        const isAuthEndpoint = config.url?.includes('/auth/');
        if (!isAuthEndpoint) {
            const authToken = localStorage.getItem('authToken');
            if (authToken) {
                config.headers.Authorization = `Bearer ${authToken}`;
            }
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      const originalRequest = error.config;
      if (error.response && error.response.status === 403 && !originalRequest._retry) {
        originalRequest._retry = true;
        try {
          const response = await axiosInstance.post('/refresh-token', {
            refreshToken: localStorage.getItem('refreshToken')
          });

          localStorage.setItem('authToken', response.data.authToken);
          originalRequest.headers['Authorization'] = `Bearer ${response.data.authToken}`;
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