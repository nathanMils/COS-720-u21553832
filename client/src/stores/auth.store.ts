import { defineStore } from 'pinia'

import router from '@/router'

import type { User } from '@/types'

export const AuthStore = defineStore({
  id: 'auth',
  state: () => ({
    user: JSON.parse(localStorage.getItem('user') ?? 'null') as User | null,
    returnUrl: null as string | null
  }),
  actions: {
    async login(username: string, password: string) {
      const user: User = { username: 'username', firstName: 'firstname', lastName: 'lastName' }
      this.user = user
      localStorage.setItem('user', JSON.stringify(user))
      router.push(this.returnUrl ?? '/')
    },
    logout() {
      this.user = null
      localStorage.removeItem('user')
      router.push('/login')
    }
  }
})
