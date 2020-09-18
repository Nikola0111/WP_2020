<template>
  <div style="margin-top: 2%;background-color: white !important; padding: 50px" class="container">
    <h2>Change your password</h2>
    <div class="row">
      <div class="col-sm-4">

        <label>Current Password</label>
        <div class="form-group pass_show">
          <span @click="showPassword1" class="ptxt">{{this.passwordElements[0].text}}</span>
          <input :type="this.passwordElements[0].type" v-model="password" class="form-control" placeholder="Current Password">
        </div>
        <label>New Password</label>
        <div class="form-group pass_show">
          <span @click="showPassword2" class="ptxt">{{this.passwordElements[1].text}}</span>
          <input :type="this.passwordElements[1].type" v-model="newPassword" class="form-control" placeholder="New Password">
        </div>
        <label>Confirm Password</label>
        <div class="form-group pass_show">
          <span @click="showPassword3" class="ptxt">{{this.passwordElements[2].text}}</span>
          <input :type="this.passwordElements[2].type" v-model="passwordConfirm" class="form-control" placeholder="Confirm Password">
        </div>
        <button @click="checkPassword" style="float:right;  " type="button" class="btn btn-info">Change password</button>
      </div>
    </div>

    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="showSnackbar1" md-persistent>
      <span>Passwords do not match!</span>
      <md-button class="md-primary" @click="showSnackbar1 = false">Retry</md-button>
    </md-snackbar>
    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="showSnackbar2" md-persistent>
      <span>You successfully changed your password!</span>
      <md-button class="md-primary" @click="showSnackbar2 = false">Close</md-button>
    </md-snackbar>
    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="showSnackbar3" md-persistent>
      <span>You did not enter the correct password!</span>
      <md-button class="md-primary" @click="showSnackbar3 = false">Close</md-button>
    </md-snackbar>
  </div>
</template>

<script>

import http from '../../http-common'
import Vue from "vue"
import { MdSnackbar, MdButton } from "vue-material/dist/components"
Vue.use(MdSnackbar)
Vue.use(MdButton)

export default {
  name: "ChangePassword",
  data() {
    return {
      userName: '',
      password: '',
      newPassword: '',
      passwordConfirm: '',
      showSnackbar1: false,
      showSnackbar2: false,
      showSnackbar3: false,
      position: 'center',
      duration: 3000,
      passwordElements: [{type:'password', text:'Show'},{type:'password', text:'Show'},{type:'password', text:'Show'}],
    }
  },
  methods: {
    checkPassword() {
      if (this.newPassword !== this.passwordConfirm) {
        this.showSnackbar1 = true;
      } else {
        let user = JSON.parse(localStorage.getItem('loggedUser'))
        this.userName = user.userName;
        http.put('/User/changePassword',
        JSON.stringify({
          userName: this.userName,
          password: this.password,
          newPassword: this.newPassword
        }))
        .then(response => {
          if (response.data) {
            this.showSnackbar2 = true;
          } else {
            this.showSnackbar3 = true;
          }
        })
      }
    },
    showPassword1() {
      if (this.passwordElements[0].text === 'Show') {
        this.passwordElements[0].type = 'text'
        this.passwordElements[0].text = 'Hide'
      } else {
        this.passwordElements[0].type = 'password'
        this.passwordElements[0].text = 'Show'
      }
    },
    showPassword2() {
      if (this.passwordElements[1].text === 'Show') {
        this.passwordElements[1].type = 'text'
        this.passwordElements[1].text = 'Hide'
      } else {
        this.passwordElements[1].type = 'password'
        this.passwordElements[1].text = 'Show'
      }
    },
    showPassword3() {
      if (this.passwordElements[2].text === 'Show') {
        this.passwordElements[2].type = 'text'
        this.passwordElements[2].text = 'Hide'
      } else {
        this.passwordElements[2].type = 'password'
        this.passwordElements[2].text = 'Show'
      }
    }
  }
}
</script>

<style scoped>
.pass_show{position: relative}

.pass_show .ptxt {

  position: absolute;

  top: 50%;

  right: 10px;

  z-index: 1;

  color: #f36c01;

  margin-top: -10px;

  cursor: pointer;

  transition: .3s ease all;

}

.pass_show .ptxt:hover{color: #333333;}
</style>