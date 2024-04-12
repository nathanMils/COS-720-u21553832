import { defineStore } from 'pinia'

import router from '@/router'
import {
  login, 
  logout, 
  apply
} from '@/api'

export const AuthStore = defineStore({
  id: 'auth',
  state: () => ({
    loggedIn: JSON.parse(localStorage.getItem('loggedIn')?? 'false') as boolean,
    returnUrl: null as string | null,
    role: JSON.parse(localStorage.getItem('refresh') ?? 'null') as string | null
  }),
  actions: {
    async login(username: string, password: string) {
      const response = await login(username,password);
      if (response.status == 200) {
        localStorage.setItem('loggedIn', JSON.stringify(true))
        router.push('/home')
      } else if (response.status == 409){
        return "USERNAME_EXISTS"
      }
      console.log('Unkown Server Error')
      return "SERVER_ERROR"
    },
    logout() {
      this.loggedIn = false;
      localStorage.removeItem('/loggedIn')
      router.push('/welcome')
    }
  }
})
