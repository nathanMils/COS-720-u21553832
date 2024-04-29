import type { ModuleDTO } from '@/types'

export interface FetchCourseResponse {
  courseName: string,
  courseDescription: string,
  courseModerator: string,
  modules: ModuleDTO[]
}