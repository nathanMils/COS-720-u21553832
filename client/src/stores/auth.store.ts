import { defineStore } from 'pinia'

import router from '@/router'
import {
  login, 
  logout, 
  apply
} from '@/api'
import type { UserDTO } from '@/types';

export const AuthStore = defineStore({
  id: 'auth',
  state: () => ({
    user: JSON.parse(localStorage.getItem('user')?? 'null') as UserDTO | null,
    returnUrl: null as string | null
  }),
  actions: {
    async Login(username: string, password: string) {
      try {
        const response = await login(username, password);
        console.log('Response:', response);
        console.log('Response status:', response.status);
    
        if (response.status === 200) {
          console.log('Login successful');
          const userData = response.data.data;
          console.log('User Data:', userData);
          
          localStorage.setItem('user', JSON.stringify(userData));
          router.push({name: 'home'})
        } else if (response.data.internalCode === "EMAIL_UNVERIFIED") {
          console.log('Email unverified');
          return "EMAIL_UNVERIFIED";
        } else {
          console.log('Unknown Server Error');
          return "SERVER_ERROR";
        }
      } catch (error) {
        console.error('Error:', error);
        return "REQUEST_ERROR";
      }
    },
    Logout() {
      if (this.user != null) logout();
      this.user = null;
      localStorage.removeItem('user')
      router.push('/login')
    },
    async Apply(
      username: string,
      password: string,
      email: string,
      firstName: string,
      lastName: string,
      courseId: string
    ) {
      const response = await apply(
        username,
        password,
        email,
        firstName,
        lastName,
        courseId
      )
      if (response.status === 200) {
        router.push("/confirm")
        return "APPLICATION_SUCCESSFUL"
      } else if (response.status === 409) {
        return "USERNAME_EXISTS"
      } else if (response.status === 404) {
        return "COURSE_NOT_FOUND"
      }
    }
  }
})
