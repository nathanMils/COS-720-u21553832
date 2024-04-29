import { defineStore } from 'pinia'

import router from '@/router'
import {
  login,
  logout,
  apply,
  verifyEmail,
  loggedIn,
  getRole, forgotPassword, resetPassword
} from '@/api'
import { type UserDTO } from '@/types'
import { Role } from '@/types/role'

export const AuthStore = defineStore({
  id: 'auth',
  state: () => ({
    isLoggedIn: null as boolean | null,
    role: null as Role | null,
    returnUrl: null as string | null
  }),
  actions: {
    async Login(username: string, password: string): Promise<{ status: number, message: string }>{
      try {
        const response = await login(username, password);
        if (response.status === 200) {
          this.isLoggedIn = true;
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
      this.isLoggedIn = false;
      this.role = null;
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
        console.log(error.response)
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
    async forgotPassword(username: string,email: string): Promise<{ status: number, message: string }>{
      try {
        const response = await forgotPassword(username,email);
        if (response.status === 200) {
          return { status: response.status, message: 'Success' };
        } else {
          return { status: 500, message: 'UNKNOWN_SERVER_ERROR' };
        }
      } catch (error: any) {
        console.log(error.response)
        return { status: 500, message: 'UNKNOWN_SERVER_ERROR' };
      }
    },
    async resetPassword(token: string, password: string): Promise<{ status: number, message: string }>{
      try {
        const response = await resetPassword(token,password);
        if (response.status === 200) {
          return { status: response.status, message: 'Success' };
        } else {
          return { status: 500, message: 'UNKNOWN_SERVER_ERROR' };
        }
      } catch (error: any) {
        console.log(error.response)
        if (error.response.data.internalCode === 'PASSWORD_SAME') {
          return { status: 400, message: 'PASSWORD_SAME' };
        }
        return { status: 500, message: 'UNKNOWN_SERVER_ERROR' };
      }
    },
    async loggedIn(): Promise<boolean> {
      try {
        if (this.isLoggedIn === null) {
          const response = await loggedIn();
          this.isLoggedIn = (response.status === 200);
        }
        return this.isLoggedIn;
      } catch (error: any) {
        this.isLoggedIn = false;
        return this.isLoggedIn;
      }
    },
    async getRole(): Promise<Role|null> {
      try {
        if (this.role === null) {
          const response = await getRole();
          this.role = response.data.data;
        }
        return this.role;
      } catch (error: any) {
        this.role = null;
        return this.role;
      }
    },
    async isRole(role: Role): Promise<boolean> {
      try {
        if (this.role === null) {
          const response = await getRole();
          this.role = response.data.data;
        }
        return this.role === role;
      } catch (error: any) {
        this.role = null;
        return false;
      }
    },
    async isRoleIncluded(roles: Role[]): Promise<boolean> {
      try {
        if (this.role === null) {
          const response = await getRole();
          this.role = response.data.data;
        }
        return roles.includes(this.role!);
      } catch (error: any) {
        this.role = null;
        return false;
      }
    }
  }
})
