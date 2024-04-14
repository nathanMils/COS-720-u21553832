import axiosInstance from '@/api/instance/axios'

const moduleModeratorPrefix: string = '/moduleModerator'

export const addPost = (content: string, moduleId: string) => {
  return axiosInstance.post(`${moduleModeratorPrefix}/${moduleId}/addPost`, content)
}

export const removePost = (postId: string, moduleId: string) => {
  return axiosInstance.delete(`${moduleModeratorPrefix}/${moduleId}/deletePost/${postId}`)
}