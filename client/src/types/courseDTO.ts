import type { ModuleDTO } from '@/types/moduleDTO'

export interface CourseDTO {
  id: string,
  name: string,
  description: string,
  modules: ModuleDTO[]
}