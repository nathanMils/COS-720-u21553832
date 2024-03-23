<template>
  <div class="flex flex-col items-center justify-center h-screen">
    <h1 class="text-4xl font-bold mb-8">Welcome to Our Application</h1>
    <div class="space-x-4">
      <RouterLink :to="{ name: 'login' }">
        <button class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded">
          Log In
        </button>
      </RouterLink>
      <button class="bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded">
        Apply
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>
