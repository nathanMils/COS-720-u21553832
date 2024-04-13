import {
    required,
    minLength,
    maxLength
} from '@vuelidate/validators'

export const validPassword = {
    required,
    minLength: minLength(8),
    maxLength: maxLength(16),
    upperCase: (value: string) => /[A-Z]/.test(value),
    lowerCase: (value: string) => /[a-z]/.test(value),
    digit: (value: string) => /\d/.test(value),
    special: (value: string) => /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]+/.test(value),
    noWhitespace: (value: string) => !/\s/.test(value),
}