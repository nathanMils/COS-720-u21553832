import axiosInstance from '@/api/instance/axios'
import type { APIResponse, GetProfileResponse } from '@/api'
import { Role } from '@/types'

const base: string = '/user'

export const getProfile = () => {
    return axiosInstance.get<APIResponse<GetProfileResponse>>(`${base}/profile`)
}

export const getRole = () => {
    return axiosInstance.get<APIResponse<Role>>(`${base}/role`)
}