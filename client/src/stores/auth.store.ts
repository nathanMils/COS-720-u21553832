import { defineStore } from 'pinia'

import router from '@/router'

import type { User } from '@/types'

export const AuthStore = defineStore({
  id: 'auth',
  state: () => ({
    // initialize state from local storage to enable user to stay logged in
    user: JSON.parse(localStorage.getItem('user') || 'null') as User | null,
    returnUrl: null as string | null
  }),
  actions: {
    async login(username: string, password: string) {
      const user: User = { username: 'username', firstName: 'firstname', lastName: 'lastName' }

      // update pinia state
      this.user = user

      // store user details and jwt in local storage to keep user logged in between page refreshes
      localStorage.setItem('user', JSON.stringify(user))

      // redirect to previous url or default to home page
      router.push(this.returnUrl || '/')
    },
    logout() {
      this.user = null
      localStorage.removeItem('user')
      router.push('/login')
    }
  }
})
