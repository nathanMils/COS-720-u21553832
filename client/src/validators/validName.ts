import { helpers , minLength, maxLength} from '@vuelidate/validators'
import { hasNoWhitespace, isNotBlank } from '@/validators/customValidators'

export const validName = {
  minLength: helpers.withMessage('must be at least 2 characters', minLength(2)),
  maxLength: helpers.withMessage('must be at most 30 characters', maxLength(30)),
  isNotBlank: helpers.withMessage('cannot be blank', isNotBlank),
  hasNoWhitespace: helpers.withMessage('cannot contain spaces', hasNoWhitespace)
}