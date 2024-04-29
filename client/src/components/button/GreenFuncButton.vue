<script setup lang="ts">
const emit = defineEmits(['click'])
const click = () => {
  emit('click')
}
</script>

<template>
  <button @click="click" class="w-full text-white bg-primaryButton-600 hover:bg-primaryButton-700 focus:ring-4 focus:outline-none focus:ring-primaryButton-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primaryButton-600 dark:hover:bg-primaryButton-700 dark:focus:ring-primaryButton-800">
    <slot/>
  </button>
</template>