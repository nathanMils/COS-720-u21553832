import type { PostDTO } from '@/types'

export interface FetchModuleContentResponse {
  name: string,
  description: string,
  postDTOS: PostDTO[]
}