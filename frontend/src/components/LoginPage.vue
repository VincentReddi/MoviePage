<template>
  <div class="login-container">
    <h2 class="title">Willkommen bei MoviePage</h2>
    <div class="login-box">
      <input v-model="username" placeholder="Benutzername" class="input" />
      <input v-model="password" placeholder="Passwort" type="password" class="input" />
      <button @click="login" class="btn">Anmelden</button>
      <p v-if="error" class="error">Benutzername oder Passwort falsch</p>
    </div>
  </div>
</template>

<script>
const USER = import.meta.env.VITE_LOGIN_USER
const PASS = import.meta.env.VITE_LOGIN_PASS

export default {
  data() {
    return {
      username: '',
      password: '',
      error: false
    }
  },
  methods: {
    login() {
      if (this.username === USER && this.password === PASS) {
        this.$emit('login-success')
      } else {
        this.error = true
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #1e1e1e;
  color: #ffffff;
}
.title {
  font-size: 2rem;
  margin-bottom: 1rem;
}
.login-box {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 2rem;
  border-radius: 1rem;
  background-color: #1e1e1e;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
.input {
  padding: 0.5rem;
  border-radius: 0.5rem;
  border: 1px solid #ccc;
}
.btn {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 0.5rem;
  border-radius: 0.5rem;
  cursor: pointer;
}
.btn:hover {
  background-color: #369f6b;
}
.error {
  color: red;
  font-size: 0.9rem;
}
</style>
