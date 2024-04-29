import axiosInstance from '@/api/instance/axios'
import type { APIResponse } from '@/api'
import type { CourseDTO, StudentApplicationDTO } from '@/types'

const base: string = '/admin'

export const fetchCourses = () => {
    return axiosInstance.get<APIResponse<CourseDTO[]>>(`${base}/fetchCourses`)
}

export const createModerator = (username: string, password: string, email: string, firstName: string, lastName: string) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/createModerator`, { username, password, email, firstName, lastName })
}

export const fetchApplications = () => {
    return axiosInstance.get<APIResponse<StudentApplicationDTO[]>>(`${base}/fetchApplications`)
}

export const acceptStudent = (applicationId: number) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/acceptStudent`, { applicationId })
}

export const denyStudent = (applicationId: number) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/denyStudent`, { applicationId })
}