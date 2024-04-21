import type { Status } from '@/types/status'

export interface StudentApplicationDTO {
  applicationId: number,
  courseID: string,
  username: string,
  userFirstName: string,
  userLastName: string,
  courseName: string,
  description: string,
  status: Status
}