import type { PostDTO } from '@/types'

export interface ModuleDTO {
  id: string,
  name: string,
  description: string,
  posts: PostDTO[]
}