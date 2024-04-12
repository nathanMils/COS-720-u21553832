export interface AuthResponse {
    authToken: string | null,
    refreshToken: string | null,
    verified: boolean
}