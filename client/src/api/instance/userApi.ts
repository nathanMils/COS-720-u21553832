import axiosInstance from '@/api/instance/axios'
import type { APIResponse, GetProfileResponse } from '@/api'
import { Role } from '@/types'

const base: string = '/user'

export const getProfile = () => {
    return axiosInstance.get<APIResponse<GetProfileResponse>>(`${base}/profile`)
}

export const logout = () => {
  axiosInstance.post(`${base}/logout`);
}

export const getUserRole = () => {
    return axiosInstance.get<APIResponse<Role>>(`${base}/role`)
}

export const loggedIn = () => {
  return axiosInstance.get<APIResponse<void>>(`${base}/loggedIn`);
}