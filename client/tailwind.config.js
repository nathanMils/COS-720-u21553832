/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        appPrimary: {
          light: '#f9fafb',
          dark: '#111827',
        },
        appSecondary: {
          light: '#ffffff',
          dark: '#1f2937',
        },
        appText: {
          light: '#111827',
          dark: '#ffffff',
        },
        appBorder: {
          light: '#d1d5db',
          dark: '#374151',
        },
        primaryButton: {
          50: "#eafdf6",
          100: "#c9f8e2",
          200: "#96f3c0",
          300: "#52eaa0",
          400: "#1ed985",
          500: "#1ccf5d",
          600: "#1abe42",
          700: "#179531",
          800: "#147c28",
          900: "#10621e",
          950: "#0d5019"
        }
      }
    },
    fontFamily: {
      Roboto: ['Roboto, sans-serif']
    },
    container: {
      padding: '2rem',
      center: true
    },
    screens: {
      sm: '640px',
      md: '768px'
    }
  },
  plugins: []
}
