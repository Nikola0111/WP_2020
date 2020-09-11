<template>
  <div id="app">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <a class="navbar-brand" href="#">Welcome Neighbors</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
          </li>
          <li class="nav-item">
            <router-link style="margin-top: 4px" v-if="loggedUserRole !== 'HOST'" to="/apartments" class="dropdown-item" href="#">Apartments</router-link>
          </li>
        </ul>
        <ul class="navbar-nav">
          <li v-if="loggedUserRole === ''" class="nav-item">
            <router-link to="/login" class="nav-link">Login</router-link>
          </li>
          <li v-if="loggedUserRole === ''" class="nav-item">
            <router-link to="/register" class="nav-link">Register</router-link>
          </li>
          <li v-if="loggedUserRole === 'GUEST'">
            <div style="margin-right: 50px" class="dropdown">
              <button class="btn btn-info dropdown-toggle" type="button" id="dropdownMenuButton1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Profile
              </button>
              <div class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                <a class="dropdown-item" href="#">My account</a>
                <a class="dropdown-item" href="#">My reservations</a>
                <a @click="logout" class="dropdown-item" href="#">Logout</a>
              </div>
            </div>
          </li>
          <li v-if="loggedUserRole === 'HOST'">
            <div style="margin-right: 50px" class="dropdown">
              <button class="btn btn-info dropdown-toggle" type="button" id="dropdownMenuButton2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Profile
              </button>
              <div class="dropdown-menu" aria-labelledby="dropdownMenuButton2">
                <a class="dropdown-item" href="#">My account</a>
                <router-link to="/apartments" class="dropdown-item" href="#">My apartments</router-link>
                <a @click="logout" class="dropdown-item" href="#">Logout</a>
              </div>
            </div>
          </li>
          <li v-if="loggedUserRole === 'ADMINISTRATOR'">
            <div style="margin-right: 50px" class="dropdown">
              <button class="btn btn-info dropdown-toggle" type="button" id="dropdownMenuButton3" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Profile
              </button>
              <div class="dropdown-menu" aria-labelledby="dropdownMenuButton2">
                <a class="dropdown-item" href="#">My account</a>
                <router-link to="/Users" class="dropdown-item" href="#">Users</router-link>
                <router-link to="/apartments" class="dropdown-item" href="#">Apartments</router-link>
                <router-link to="/AllReservations" class="dropdown-item" href="#">Reservations</router-link>
                <router-link to="/AllAmenities" class="dropdown-item" href="#">Amenities</router-link>
                <a @click="logout" class="dropdown-item" href="#">Logout</a>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </nav>
    <router-view></router-view>
  </div>

</template>

<script>
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap/dist/css/bootstrap.css'
import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/components/Login";
import Register from "@/components/Register";
import {MdIcon} from "vue-material/dist/components"
import 'material-design-icons/iconfont/material-icons.css'
import UsersTable from "@/components/AdministratorComponents/UsersTable";
import Home from "@/components/Home";
import ApartmentList from "@/components/ApartmentList";
import ReservationsForAdmin from "@/components/AdministratorComponents/ReservationsForAdmin";
import AmenitiesForAdmin from "@/components/AdministratorComponents/AmenitiesForAdmin";

Vue.use(VueRouter)
Vue.use(MdIcon)
const routes = [
  {path: '/', component: Home},
  {path: '/login', component: Login},
  {path: '/register', component: Register},
  {path: '/Users', component: UsersTable},
  {path: '/apartments', component: ApartmentList},
  {path: '/AllReservations', component: ReservationsForAdmin},
  {path: '/AllAmenities', component: AmenitiesForAdmin}
]

const router = new VueRouter({
  routes
})


export default {
  router,
  name: 'App',
  components: {

  },
  data() {
    return {
      loggedUserRole: "",
    }
  },
  mounted() {
    this.$root.$on('messageToParent', (data) => {
      this.loggedUserRole = data;
      localStorage.setItem("loggedUserRole", JSON.stringify(data));
    });
    this.$root.$on('loggedUser', (data) => {
      localStorage.setItem("loggedUser", JSON.stringify(data));
    })
  },
  methods: {
    logout() {
      this.loggedUserRole = ""
      localStorage.clear();
      this.$router.push('/')
    }
  }

}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
