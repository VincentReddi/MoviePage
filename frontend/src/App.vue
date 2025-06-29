<script setup>
</script>

<template>
  <div>
    <h1>PopcornPilot</h1>
    <nav>
      <router-link to="/">Start</router-link> |
      <router-link to="/liste">Meine Liste</router-link>
    </nav>
    <router-view />
  </div>
</template>

<style scoped>
h1 {
  text-align: center;
  margin: 20px 0;
}
nav {
  text-align: center;
  margin-bottom: 20px;
}
nav a {
  margin: 0 10px;
  text-decoration: none;
  color: #333;
}
</style>
