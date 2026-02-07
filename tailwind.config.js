/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'pro-blue': '#003366',
        'pro-orange': '#F26522',
        'pro-gray': '#F4F7F9',
      }
    },
  },
  plugins: [],
}
