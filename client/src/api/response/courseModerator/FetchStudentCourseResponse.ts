import type { StudentModuleDTO } from '@/types/studentModuleDTO'

export interface FetchStudentCourseResponse {
  courseName: string,
  courseDescription: string,
  courseModerator: string,
  modules: StudentModuleDTO[]
}