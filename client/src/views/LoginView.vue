<template>
  <div>
    <section class="bg-appPrimary-light dark:bg-appPrimary-dark">
      <div class="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
        <div class="flex flex-row items-center justify-evenly text-4xl mb-8 font-bold text-appText-light dark:text-appText-dark">
            <LogoImg :size="3"/>
            <h1>Portal</h1>
          </div>
        <div
          class="w-full bg-white rounded-lg shadow-xl border border-appBorder-light dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-appSecondary-dark dark:border-appBorder-dark dark:shadow-none"
        >
          <div class="p-6 space-y-4 md:space-y-6 sm:p-8">
            <h1
              class="text-xl font-bold leading-tight tracking-tight text-center text-appText-light md:text-2xl dark:text-appText-dark"
            >
              Log in to Portal
            </h1>
            <form class="space-y-4 md:space-y-6" action="#">
              <div>
                <label
                  for="username"
                  class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
                  >Username</label
                >
                <input
                  type="username"
                  name="username"
                  id="username"
                  class="bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg focus:ring-primaryButton-600 focus:border-primaryButton-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  placeholder="username"
                  required="true"
                />
              </div>
              <div>
                <label
                  for="password"
                  class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
                  >Password</label
                >
                <input
                  type="password"
                  name="password"
                  id="password"
                  class="bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg focus:ring-primaryButton-600 focus:border-primaryButton-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  placeholder="password"
                  required="true"
                />
              </div>
              <div class="flex items-center justify-between">
                <a
                  href="#"
                  class="text-sm font-medium text-primaryButton-600 hover:underline dark:text-primaryButton-500"
                  >Forgot password?</a
                >
              </div>
              <button type="submit" class="w-full text-white bg-primaryButton-600 hover:bg-primaryButton-700 focus:ring-4 focus:outline-none focus:ring-primaryButton-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primaryButton-600 dark:hover:bg-primaryButton-700 dark:focus:ring-primaryButton-800">Sign in</button>
              <p class="text-sm font-light text-gray-500 dark:text-gray-400">
                Not a student?
                <RouterLink :to="{name: 'apply'}">
                  <a
                    href="#"
                    class="font-medium text-primaryButton-600 hover:underline dark:text-primaryButton-500"
                    >Apply</a
                  >
                </RouterLink>
              </p>
            </form>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { LogoImg } from '@/components';
import { RouterLink } from 'vue-router';
</script>
