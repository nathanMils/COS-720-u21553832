import axiosInstance from '@/api/instance/axios'
import type { APIResponse } from '@/api'
import type { CourseDTO } from '@/types'
import type { FetchStudentApplications } from '@/api/response/admin/FetchStudentApplications'

const base: string = '/admin'

export const fetchApplications = () => {
    return axiosInstance.get<APIResponse<FetchStudentApplications>>(`${base}/fetchStudentApplicants`)
}

export const acceptStudent = (applicationId: number) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/acceptStudent`, { applicationId })
}

export const denyStudent = (applicationId: number) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/denyStudent`, { applicationId })
}

export const createCourse = (courseName: string, courseDescription: string) => {
    return axiosInstance.post<APIResponse<CourseDTO>>(`${base}/createCourse`, { courseName, courseDescription })
}

export const deleteCourse = (courseID: string) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/${courseID}/deleteCourse`)
}