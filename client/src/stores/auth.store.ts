import { defineStore } from 'pinia'

import router from '@/router'
import {
  login, 
  logout, 
  apply,
  verifyEmail
} from '@/api'
import { type UserDTO } from '@/types'
import { Role } from '@/types/role'

export const AuthStore = defineStore({
  id: 'auth',
  state: () => ({
    user: JSON.parse(localStorage.getItem('user')?? 'null') as UserDTO | null,
    role: JSON.parse(localStorage.getItem('role')?? 'null') as Role | null,
    returnUrl: null as string | null,
    loginStatus: null as string | null
  }),
  actions: {
    async Login(username: string, password: string): Promise<{ status: number, message: string }>{
      try {
        const response = await login(username, password);
        if (response.status === 200 && response.data != null) {
          this.user = response.data.data;
          this.role = response.data.data!.role;
          localStorage.setItem('user', JSON.stringify(this.user));
          localStorage.setItem('role', JSON.stringify(this.role));
          router.push(this.returnUrl ?? '/dashboard');
          this.returnUrl = null;
          return { status: response.status, message: 'Success' };
        } else {
          return { status: 500, message: 'UNKNOWN_SERVER_ERROR' };
        }
      } catch (error: any) {
        if (error.response) {
          return { status: error.response.status, message: error.response.data.internalCode};
        } else {
          return { status: 500, message: 'UNKNOWN_SERVER_ERROR' };
        }
      }
    },
    Logout() {
      logout();
      this.user = null;
      this.role = null;
      localStorage.removeItem('user');
      localStorage.removeItem('role');
      router.push('/login');
    },
    async Apply(
      username: string,
      password: string,
      email: string,
      firstName: string,
      lastName: string
    ): Promise<{ status: number, message: string }>{
      try {
        const response = await apply(
          username,
          password,
          email,
          firstName,
          lastName
        )
        if (response.status === 200) {
          router.push('/confirm');
          return { status: response.status, message: 'Success' };
        } else {
          return { status: 500, message: 'UNKNOWN_SERVER_ERROR' };
        }
      } catch (error: any) {
        if (error.response) {
          if (error.response.status === 409 && error.response.data.internalCode === 'USERNAME_EXISTS') {
            return { status: error.response.status, message: 'USERNAME_EXISTS' };
          } else {
            return { status: error.response.status, message: 'UNKNOWN_SERVER_ERROR' };
          }
        } else {
          return { status: 500, message: 'UNKNOWN_SERVER_ERROR' };
        }
      }
    },
    async VerifyEmail(token: string) {
      try {
        const response = await verifyEmail(token);
        if (response.status === 200) {
          return { status: response.status, message: 'Success' };
        } else {
          return { status: 500, message: 'UNKNOWN_SERVER_ERROR' };
        }
      } catch (error: any) {
        console.log(error.response)
        if (error.response) {
          if (error.response.status === 404 && error.response.data.internalCode === 'INVALID_TOKEN') {
            return { status: error.response.status, message: 'INVALID_TOKEN' };
          } else {
            return { status: error.response.status, message: 'UNKNOWN_SERVER_ERROR' };
          }
        } else {
          return { status: 500, message: 'UNKNOWN_SERVER_ERROR' };
        }
      }
    },
    isAdmin(): boolean {
      return this.role === "ROLE_ADMIN"
    },
    isStudent(): boolean {
      return this.role === "ROLE_STUDENT"
    },
    isModuleModerator(): boolean {
      return this.role === "ROLE_MODULE_MODERATOR"
    },
    isCourseModerator(): boolean {
      return this.role === "ROLE_COURSE_MODERATOR"
    }
  }
})
