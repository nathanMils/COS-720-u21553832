import { ResponseCode } from "./ResponseCode"

export interface APIResponse<T> {
    status: ResponseCode,
    internalCode: string,
    data: T | null,
    errors: { [key: string]: string},
    warnings: { [key: string]: string}
}