import axiosInstance from '@/api/instance/axios'

const base: string = '/student'

export const fetchStudentModules = () => {
    return axiosInstance.get(`${base}/fetchModules`)
}

export const registerModule = (moduleId: string) => {
    return axiosInstance.post(`${base}/register/${moduleId}`)
}

export const deregisterModule = (moduleId: string) => {
    return axiosInstance.post(`${base}/deregister/${moduleId}`)
}

export const fetchCourseModules = (courseId: string) => {
    return axiosInstance.get(`${base}/fetchCourseModules/${courseId}`)
}