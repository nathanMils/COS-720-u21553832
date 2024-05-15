import type { LectureDTO, PostDTO } from '@/types'

export interface FetchModuleContentResponse {
  name: string,
  description: string,
  postDTOS: PostDTO[],
  lectures: LectureDTO[],
}