<template>
  <div style="margin-top: 2%;background-color: white !important; padding: 50px" class="container">
    <h2>Details</h2>
    <div class="row">
    <div class="col-sm-3">
      <div class="row">
        <label>Username:   </label>
      </div>
      <div class="row">
        <label>Name:   </label>
      </div>
      <div class="row">
        <label>Surname:   </label>
      </div>
      <div class="row">
        <label>Gender:   </label>
      </div>
      <div v-if="loggedUserRole == 'HOST'" class="row">
        <label>Available apartments:   </label>
      </div>
      <div  v-if="loggedUserRole == 'HOST'" class="row">
        <label>Rented apartments:   </label>
      </div>
      <div  v-if="loggedUserRole == 'GUEST'" class="row">
        <label>Number of made reservations:   </label>
      </div>
    </div>
    <div class="col-sm-3">
      <div class="row">
        <label>{{userDetails.userName}}</label>
      </div>
      <div class="row">
        <label>{{userDetails.name}}</label>
      </div>
      <div class="row">
        <label>{{userDetails.surname}}</label>
      </div>
      <div class="row">
        <label>{{userDetails.userGender}}</label>
      </div>
      <div  v-if="loggedUserRole == 'HOST'" class="row">
        <label>{{userDetails.availableApartments}}</label>
      </div>
      <div  v-if="loggedUserRole == 'HOST'" class="row">
        <label>{{userDetails.rentedApartments}}</label>
      </div>
      <div  v-if="loggedUserRole == 'GUEST'" class="row">
        <label>{{userDetails.numberOfReservationsMade}}</label>
      </div>
    </div>
      <div class = "col-sm-3">
        <div class="row">
          <router-link tag="button" to="/ChangeDetails" type="button" class="btn btn-info">Change details</router-link>
        </div>
        <div class="row">
          <router-link tag="button" to="/ChangePassword" type="button" class="btn btn-info">Change password</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import http from '../../http-common'

export default {
  name: "MyProfile",
  mounted() {
    if (localStorage.getItem("loggedUserRole") !== null) {
     this.loggedUserRole = JSON.parse(localStorage.getItem("loggedUserRole"))
    }
    if (localStorage.getItem("loggedUserRole") !== null) {
      let user = JSON.parse(localStorage.getItem("loggedUser"))
      this.loggedUserName = user.userName;
    }

    http.get('/User/UserDetails/' + this.loggedUserName)
      .then(response => {
        if (response.data) {
          this.userDetails = response.data;
        }
      })
},
  data() {
    return {
      loggedUserRole: '',
      userDetails: '',
      loggedUserName: ''
    }
  },
  methods: {

  }
}

</script>

<style scoped>
.row {
  margin-top: 10px
}
</style>