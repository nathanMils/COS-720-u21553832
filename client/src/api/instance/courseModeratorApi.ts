import axiosInstance from '@/api/instance/axios'
import type { APIResponse } from '@/api'
import type { FetchModulesResponse } from '@/api/response/courseModerator/fetchModulesResponse'

const base: string = '/courseModerator'

export const createModule = (courseID: string, moduleName: string, moduleDescription: string) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/createModule/${courseID}`, { moduleName, moduleDescription })
}

export const deleteModule = (courseID: string, moduleID: string) => {
    return axiosInstance.post<APIResponse<void>>(`${base}/deleteModule/${courseID}/${moduleID}`)
}

export const fetchModules = (courseID: string) => {
    return axiosInstance.get<APIResponse<FetchModulesResponse>>(`${base}/fetchModules/${courseID}`)
}