export const isNotBlank = (value: string) => !!value.trim()
export const hasNoWhitespace = (value: string) => /^\S*$/.test(value)