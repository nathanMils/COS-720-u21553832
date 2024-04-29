import axiosInstance from '@/api/instance/axios'
import type { APIResponse, FetchCourseResponse } from '@/api'
import type { CourseDTO } from '@/types'

const base: string = '/courseModerator'

export const createModule = (courseID: string, name: string, description: string) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/createModule/${courseID}`, { name, description })
}

export const deleteModule = (courseID: string, moduleID: string) => {
    return axiosInstance.delete<APIResponse<void>>(`${base}/deleteModule/${courseID}/${moduleID}`)
}

export const fetchModules = (courseID: string) => {
    return axiosInstance.get<APIResponse<CourseDTO[]>>(`${base}/fetch/${courseID}`)
}

export const addPost = (content: string, courseId: string, moduleId: string) => {
    return axiosInstance.post(`${base}/post/${courseId}/${moduleId}`, content)
}

export const fetchCourse = (courseID: string) => {
    return axiosInstance.get<APIResponse<FetchCourseResponse>>(`${base}/fetch/${courseID}`)
}


export const createCourse = (name: string, description: string) => {
    return axiosInstance.post<APIResponse<CourseDTO>>(`${base}/createCourse`, { name, description })
}

export const deleteCourse = (courseID: string) => {
    return axiosInstance.delete<APIResponse<void>>(`${base}/deleteCourse/${courseID}`)
}