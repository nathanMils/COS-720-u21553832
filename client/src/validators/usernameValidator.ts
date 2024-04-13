import { required } from '@vuelidate/validators'

export const validUsername = {
    required,
    isNotBlank: (value: string) => !!value.trim(),
    isValidFormat: (value: string) => /^[A-Za-z][A-Za-z0-9_]{7,29}$/.test(value),
    hasNoWhitespace: (value: string) => !/\s/.test(value)
}