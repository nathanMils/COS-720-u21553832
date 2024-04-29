import type { APIResponse } from '@/api';
import axiosInstance from './axios';
import type { CourseDTO } from '@/types'

const base = '/public';

export const fetchAllCourses = () => {
    return axiosInstance.get<APIResponse<CourseDTO[]>>(`${base}/fetchCourses`);
}