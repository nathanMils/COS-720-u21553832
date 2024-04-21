<script setup lang="ts">
import { ref } from 'vue'

const items = ref([
  { title: 'Dashboard', icon: 'home', link: '/' },
  { title: 'Student Applications', icon: 'user', link: '/student-applications' },
  { title: 'Access Denied', icon: 'lock', link: '/access-denied' },
  { title: 'Dashboard', icon: 'home', link: '/' },
  { title: 'Student Applications', icon: 'user', link: '/student-applications' },
  { title: 'Access Denied', icon: 'lock', link: '/access-denied' },
  { title: 'Dashboard', icon: 'home', link: '/' },
  { title: 'Student Applications', icon: 'user', link: '/student-applications' },
  { title: 'Access Denied', icon: 'lock', link: '/access-denied' },
  { title: 'Dashboard', icon: 'home', link: '/' },
  { title: 'Student Applications', icon: 'user', link: '/student-applications' },
  { title: 'Access Denied', icon: 'lock', link: '/access-denied' },
  { title: 'Dashboard', icon: 'home', link: '/' },
  { title: 'Student Applications', icon: 'user', link: '/student-applications' },
  { title: 'Access Denied', icon: 'lock', link: '/access-denied' },
])
</script>

<template>
  <div class="flex-grow overflow-auto px-10 py-10">
    <h1 class="text-2xl font-bold">Welcome to my dashboard!</h1>
    <p class="mt-2 text-gray-600">This is an example dashboard using Tailwind CSS.</p>
    <div class="grid grid-cols-1 gap-4 mt-4 sm:grid-cols-2 lg:grid-cols-3">
      <div v-for="(item, index) in items"  :key="index" class="p-4 bg-white rounded-lg shadow-md dark:bg-gray-800">
        <h2 class="text-lg font-semibold">Card 1</h2>
        <p class="mt-2 text-gray-600">This is a card component.</p>
      </div>
    </div>
  </div>
</template>
