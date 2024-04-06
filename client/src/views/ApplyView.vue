<template>
    <div>
      <section class="bg-white-50 dark:bg-gray-900">
        <div class="flex flex-row items-center justify-evenly px-6 py-8 mx-auto md:h-screen lg:py-0">
            
          <div class="flex flex-row items-center justify-evenly text-8xl font-bold text-gray-900 dark:text-white">
            <img src="/src/assets/education-svgrepo-com (2).svg" alt="Missing" class="w-24 h-24 mx-4">
            <h1>Portal</h1>
          </div>
          <div
            class="w-full bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700"
          >
            <div class="p-6 space-y-4 md:space-y-6 sm:p-8">
              <h1
                class="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white"
              >
                Apply for Portal
              </h1>
              <form class="space-y-4 md:space-y-6" action="#">
                <div>
                  <label
                    for="firstName"
                    class="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                    >your name</label
                  >
                  <input
                    name="firstName"
                    id="firstName"
                    class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="Nathan"
                    required="true"
                  />
                </div>
                <div>
                  <label
                    for="lastName"
                    class="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                    >Your last name</label
                  >
                  <input
                    name="lastName"
                    id="lastName"
                    class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="lastName"
                    required="true"
                  />
                </div>
                <div>
                  <label
                    for="username"
                    class="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                    >Your username</label
                  >
                  <input
                    type="username"
                    name="username"
                    id="username"
                    class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="username"
                    required="true"
                  />
                </div>
                <div>
                  <label
                    for="email"
                    class="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                    >Your email</label
                  >
                  <input
                    type="email"
                    name="email"
                    id="email"
                    class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="your@email.com"
                    required="true"
                  />
                </div>
                <div>
                  <label
                    for="password"
                    class="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                    >Password</label
                  >
                  <input
                    type="password"
                    name="password"
                    id="password"
                    class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="password"
                    required="true"
                  />
                </div>
                <button
                  type="submit"
                  class="w-full text-white bg-primary-600 hover:bg-primary-700 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800"
                >
                  Apply
                </button>
                <p class="text-sm font-light text-gray-500 dark:text-gray-400">
                  Already a student?
                  <RouterLink :to="{name: 'login'}">
                    <a
                      href="#"
                      class="font-medium text-primary-600 hover:underline dark:text-primary-500"
                      >Login</a
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
  import { AuthStore } from '@/stores'
  import { RouterLink } from 'vue-router';
  </script>
  