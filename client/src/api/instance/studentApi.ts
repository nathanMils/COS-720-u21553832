import axiosInstance from '@/api/instance/axios'
import type { CourseDTO, ModuleDTO, StudentApplicationDTO } from '@/types'
import type { APIResponse, FetchModuleContentResponse } from '@/api'
import type { FetchStudentCourseResponse } from '@/api/response/courseModerator/FetchStudentCourseResponse'

const base: string = '/student'

export const fetchStudentModules = () => {
    return axiosInstance.get<APIResponse<ModuleDTO[]>>(`${base}/fetchModules`)
}

export const fetchStudentCourses = () => {
    return axiosInstance.get<APIResponse<CourseDTO[]>>(`${base}/fetchCourses`)
}

export const fetchOtherCourses = () => {
    return axiosInstance.get<APIResponse<CourseDTO[]>>(`${base}/fetchOtherCourses`)
}

export const fetchStudentCourse = (courseID: string) => {
    return axiosInstance.get<APIResponse<FetchStudentCourseResponse>>(`${base}/fetch/${courseID}`)
}

export const registerModule = (courseId: string,moduleId: string) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/register/${courseId}/${moduleId}`)
}

export const deregisterModule = (courseId: string,moduleId: string) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/deRegister/${courseId}/${moduleId}`)
}

export const fetchCourseModules = (courseId: string) => {
    return axiosInstance.get<APIResponse<ModuleDTO>[]>(`${base}/fetchCourseModules/${courseId}`)
}

export const applyCourse = (courseId: string) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/apply/${courseId}`)
}

export const dropCourse = (courseId: string) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/drop/${courseId}`)
}

export const dropApplication = (applicationId: string) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/dropApplication/${applicationId}`)
}

export const fetchMyApplications = () => {
    return axiosInstance.get<APIResponse<StudentApplicationDTO[]>>(`${base}/fetchApplications`)
}

export const fetchStudentModuleContent = (moduleID: string) => {
    return axiosInstance.get<APIResponse<FetchModuleContentResponse>>(`${base}/fetchContent/${moduleID}`)
}

export const fetchLecture = (lectureID: string) => {
    return axiosInstance.get<Blob>(`${base}/fetchLecture/${lectureID}`, {responseType: 'blob'})
}