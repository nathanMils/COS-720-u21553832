import { required, helpers, email } from '@vuelidate/validators'
import { hasNoWhitespace, isNotBlank } from '@/validators/customValidators'

export const validEmail = {
  required: helpers.withMessage('is required', required),
  isNotBlank: helpers.withMessage('cannot be blank', isNotBlank),
  hasNoWhitespace: helpers.withMessage('cannot contain spaces', hasNoWhitespace),
  validFormat: helpers.withMessage('must be a valid email address', email)
}