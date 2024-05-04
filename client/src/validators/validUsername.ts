import { helpers, maxLength, minLength, required } from '@vuelidate/validators'
import { hasNoWhitespace, isNotBlank } from '@/validators/customValidators'

const isValidFormat = (value: string) => /^[a-zA-Z0-9]*$/.test(value)

export const validUsername = {
    required: helpers.withMessage('username is required', required),
    minLength: helpers.withMessage('username must be at least 7 characters', minLength(7)),
    maxLength: helpers.withMessage('username must be at most 30 characters', maxLength(30)),
    isNotBlank: helpers.withMessage('username cannot be blank', isNotBlank),
    hasNoWhitespace: helpers.withMessage('username cannot contain spaces', hasNoWhitespace),
    isValidFormat: helpers.withMessage('username must be alphanumeric', isValidFormat),
}