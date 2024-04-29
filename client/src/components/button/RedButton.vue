<script setup lang="ts">
const emit = defineEmits(['click'])
const click = () => {
  emit('click')
}
</script>

<template>
  <button @click="click" class="w-full text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-800">
    <slot/>
  </button>
</template>