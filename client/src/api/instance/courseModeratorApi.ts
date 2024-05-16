import axiosInstance from '@/api/instance/axios'
import type { APIResponse, FetchCourseResponse, FetchModuleContentResponse } from '@/api'
import type { CourseDTO, LectureDTO, PostDTO } from '@/types'

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

export const fetchCourse = (courseID: string) => {
    return axiosInstance.get<APIResponse<FetchCourseResponse>>(`${base}/fetch/${courseID}`)
}


export const createCourse = (name: string, description: string) => {
    return axiosInstance.post<APIResponse<CourseDTO>>(`${base}/createCourse`, { name, description })
}

export const deleteCourse = (courseID: string) => {
    return axiosInstance.delete<APIResponse<void>>(`${base}/deleteCourse/${courseID}`)
}

export const fetchModuleContent = (moduleID: string) => {
    return axiosInstance.get<APIResponse<FetchModuleContentResponse>>(`${base}/fetchModule/${moduleID}`)
}

export const createPost = (moduleID: string, content: string) => {
    return axiosInstance.post<APIResponse<PostDTO>>(`${base}/post/${moduleID}`, { content })
}

export const deletePost = (moduleID: string, postID: string) => {
    return axiosInstance.delete<APIResponse<void>>(`${base}/deletePost/${moduleID}/${postID}`)
}

export const uploadLecture = (moduleID: string, file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    return axiosInstance.post<APIResponse<LectureDTO>>(`${base}/uploadLecture/${moduleID}`, formData)
}

export const deleteLecture = (lectureID: string) => {
    return axiosInstance.delete<APIResponse<void>>(`${base}/deleteLecture/${lectureID}`)
}