import {
    required,
    minLength,
    maxLength, helpers
} from '@vuelidate/validators'
import { hasNoWhitespace } from '@/validators/customValidators'

const digits = (value: string) => /\d/.test(value)
const special = (value: string) => /[!@#$%^&*(),.?":{}|<>]/.test(value)
const upperCase = (value: string) => /[A-Z]/.test(value)
const lowerCase = (value: string) => /[a-z]/.test(value)


export const validPassword = {
    required: helpers.withMessage('password is required', required),
    minLength: helpers.withMessage('must be at least 8 characters', minLength(8)),
    maxLength: helpers.withMessage('must be at most 14 characters', maxLength(16)),
    upperCase: helpers.withMessage('must contain at least one uppercase letter', upperCase),
    lowerCase: helpers.withMessage('must contain at least one lowercase letter', lowerCase),
    digits: helpers.withMessage('must contain at least one digit', digits),
    special: helpers.withMessage('must contain at least one special character', special),
    noWhitespace: helpers.withMessage('cannot contain spaces', hasNoWhitespace)
}