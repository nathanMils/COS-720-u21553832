import type { UserDTO } from "@/types"

export interface AuthResponse {
    userDTO: UserDTO | null
    verified: boolean | null
}