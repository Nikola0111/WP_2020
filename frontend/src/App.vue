<template>
  <div id="app">
    <nav class="navbar navbar-expand-lg navbar-light bg-light" id="mainNav">
      <a class="navbar-brand js-scroll-trigger" href=""><img src="./assets/img/logo.png" alt="" style="width:200px;height:50px;"></a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <router-link to="/" class="nav-link" href="#">Home <span class="sr-only">(current)</span></router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" v-if="loggedUserRole !== 'HOST'" to="/apartments" href="#">Apartments</router-link>
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
                <router-link to="/Profile" tag="a" class="dropdown-item" href="#">My account</router-link>
                <router-link to="/myReservations" class="dropdown-item" href="#">My reservations</router-link>
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
                <router-link to="/Profile" tag="a" class="dropdown-item" href="#">My account</router-link>
                <router-link to="/apartments" class="dropdown-item" href="#">My apartments</router-link>
                <router-link to="/reservationsFromGuests" class="dropdown-item" href="#"> Reservations</router-link>
                <router-link to="/apartmentRegistration" class="dropdown-item" href="#">Register apartment</router-link>
                <router-link to="/guests" class="dropdown-item" href="#">My guests</router-link>
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
    <!-- Masthead-->
    <header class="masthead">
      <div class="container h-100">
        <router-view></router-view>
      </div>
    </header>
  </div>

</template>

<script>
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap/dist/css/bootstrap.css'
import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/components/Login";
import Register from "@/components/Register";
import 'material-design-icons/iconfont/material-icons.css'
import UsersTable from "@/components/AdministratorComponents/UsersTable";
import Home from "@/components/Home";
import ApartmentList from "@/components/SharedComponents/ApartmentList";
import ReservationsForAdmin from "@/components/AdministratorComponents/ReservationsForAdmin";
import AmenitiesForAdmin from "@/components/AdministratorComponents/AmenitiesForAdmin";
import ChangePassword from "@/components/SharedComponents/ChangePassword";
import MyProfile from "@/components/SharedComponents/MyProfile";
import ChangeUserDetails from "@/components/SharedComponents/ChangeUserDetails";
import ApartmentRegistration from "@/components/HostComponents/ApartmentRegistration";
import ReservationsFromGuests from "@/components/HostComponents/ReservationsFromGuests";
import ReservationsByGuest from "@/components/GuestComponents/ReservationsByGuest";
import UsersThatMadeReservations from "@/components/HostComponents/UsersThatMadeReservations";

Vue.use(VueRouter)
const routes = [
  {path: '/', component: Home},
  {path: '/login', component: Login},
  {path: '/register', component: Register},
  {path: '/Users', component: UsersTable},
  {path: '/apartments', component: ApartmentList},
  {path: '/AllReservations', component: ReservationsForAdmin},
  {path: '/AllAmenities', component: AmenitiesForAdmin},
  {path: '/Profile', component: MyProfile },
  {path: '/ChangePassword', component: ChangePassword},
  {path: '/ChangeDetails', component: ChangeUserDetails},
  {path: '/apartmentRegistration', component: ApartmentRegistration},
  {path: '/reservationsFromGuests', component: ReservationsFromGuests},
  {path: '/myReservations', component: ReservationsByGuest},
  {path: '/guests', component: UsersThatMadeReservations}
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
    if (localStorage.getItem("loggedUserRole") !== null) {
      this.loggedUserRole = JSON.parse(localStorage.getItem("loggedUserRole"))
    }
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

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;

  header.masthead {
    padding-top: 10rem;
    padding-bottom: calc(10rem - 4.5rem);
    background: transparent;
  }

  header.masthead h1 {
    font-size: 2.25rem;
  }
  @media (min-width: 992px) {
    header.masthead {
      height: 100vh;
      min-height: 40rem;
      padding-top: 4.5rem;
      padding-bottom: 0;
    }
    header.masthead p {
      font-size: 1.15rem;
    }
    header.masthead h1 {
      font-size: 3rem;
    }
  }
  @media (min-width: 1200px) {
    header.masthead h1 {
      font-size: 3.5rem;
    }
  }

  .text-white-75 {
    color: rgba(255, 255, 255, 0.75);
  }

  .font-weight-light {
    font-weight: 300 !important;
  }

  .mb-5,
  .my-5 {
    margin-bottom: 10rem !important;
  }

  #mainNav {
    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
    background-color: #fff;
    transition: background-color 0.2s ease;
  }

  #mainNav .navbar-brand {
    font-family: "Merriweather Sans", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
    font-weight: 700;
    color: #212529;
  }

  #mainNav .navbar-nav .nav-item .nav-link {
    color: #6c757d;
    font-family: "Merriweather Sans", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
    font-weight: 700;
    font-size: 1rem;
  }

  #mainNav .navbar-nav .nav-item .nav-link:hover, #mainNav .navbar-nav .nav-item .nav-link:active {
    color: #d9534f;
    text-decoration-line: none;
  }

  #mainNav .navbar-nav .nav-item .nav-link.active {
    color: #d9534f !important;
  }

  @media (min-width: 992px) {
    #mainNav {
      box-shadow: none;
      background-color: transparent;
    }

    #mainNav.navbar-nav {
      box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
      background-color: #fff;
    }
    #mainNav.navbar-nav .navbar-brand {
      color: #212529;
    }
    #mainNav.navbar-nav .navbar-brand:hover {
      color: #f4623a;
    }
    #mainNav.navbar-nav .nav-item .nav-link {
      color: #212529;
    }
    #mainNav.navbar-nav .nav-item .nav-link:hover {
      color: #f4623a;
    }
  }
}
</style>
