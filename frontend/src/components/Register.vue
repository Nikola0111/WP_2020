<template>
  <div>
    <form class="form-style-9" @submit.prevent="showData">
      <ul>
        <li>
          <input name="name" class="field-style field-full align-none" v-model="name"
                 placeholder="Name"/>
        </li>
        <li>
          <input  name="surname" class="field-style field-full align-none" v-model="surname"
                  placeholder="Surname" />
        </li>
        <li>
          <input name="username" class="field-style field-full align-none" v-model="username"
                 placeholder="Username" />
        </li>
        <li>
          <input type="password" name="password" class="field-style field-full align-none" v-model="password"
                 placeholder="Password" />
        </li>
        <li>
          <div class="rb-group">
            <label>Gender:</label>
            <input class="rb-male" style="alignment: left" type="radio" id="one" value="MALE" v-model="gender">
            <label class="rb-label" for="one">Male</label>
            <input type="radio" id="two" value="FEMALE" v-model="gender">
            <label class="rb-label" for="two">Female</label>
          </div>
        </li>
        <li v-if="showValidation1">
          <span style="color: red">None of the fields can remain empty.</span>
        </li>
        <li v-if="showValidation2">
          <span style="color: red">Passwords needs to be at least 6 characters long.</span>
        </li>
        <li>
          <input type="submit" value="Register" />
        </li>
      </ul>
    </form>
    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="showSnackbar1" md-persistent>
      <span>You registered successfully!</span>
      <md-button class="md-primary" @click="showSnackbar1 = false">Ok</md-button>
    </md-snackbar>
    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="showSnackbar2" md-persistent>
      <span>Username: {{this.username}} is already taken!</span>
      <md-button class="md-primary" @click="showSnackbar2 = false">Ok</md-button>
    </md-snackbar>
  </div>
</template>

<script>
import Vue from 'vue'
import { MdSnackbar, MdButton } from "vue-material/dist/components"
Vue.use(MdSnackbar)
Vue.use(MdButton)
import http from '../http-common';
export default {
  name: "Register.vue",
  data() {
    return {
      showValidation1: "",
      showValidation2: "",
      username: "",
      password: "",
      name: "",
      surname: "",
      gender: "",
      showSnackbar1: false,
      showSnackbar2: false,
      position: 'center',
      duration: 3000
    }
  },
  methods: {
    showData(){
      if(this.username === "" || this.password === "" || this.name === "" || this.surname === "" || this.gender === ""){
        this.showValidation1 = true;
        return
      } else {
        this.showValidation1 = false;
      }

      if(this.password.length <= 5){
        this.showValidation2 = true;
        return
      } else {
        this.showValidation2 = false;
      }
      this.showValidation1 = false;
      this.showValidation2 = false;
      http.post('/Register',
        JSON.stringify({
          userName: this.username,
          name: this.name,
          surname: this.surname,
          password: this.password,
          userGender: this.gender
        })
      ).then(response => {
        if(!response.data){
          this.showSnackbar2 = true;
        } else {
          this.showSnackbar1 = true;
        }
      })
    }
  }
}
</script>

<style scoped>
.form-style-9{
  max-width: 450px;
  background: #FAFAFA;
  padding: 30px;
  margin: 50px auto;
  box-shadow: 1px 1px 25px rgba(0, 0, 0, 0.35);
  border-radius: 10px;
  border: 6px solid #305A72;
}
.form-style-9 ul{
  padding:0;
  margin:0;
  list-style:none;
}
.form-style-9 ul li{
  display: block;
  margin-bottom: 10px;
  min-height: 35px;
}
.form-style-9 ul li  .field-style{
  box-sizing: border-box;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  padding: 8px;
  outline: none;
  border: 1px solid #B0CFE0;
  -webkit-transition: all 0.30s ease-in-out;
  -moz-transition: all 0.30s ease-in-out;
  -ms-transition: all 0.30s ease-in-out;
  -o-transition: all 0.30s ease-in-out;

}.form-style-9 ul li  .field-style:focus{
   box-shadow: 0 0 5px #B0CFE0;
   border:1px solid #B0CFE0;
 }
.form-style-9 ul li .field-split{
  width: 49%;
}
.form-style-9 ul li .field-full{
  width: 100%;
}
.form-style-9 ul li input.align-left{
  float:left;
}
.form-style-9 ul li input.align-right{
  float:right;
}
.form-style-9 ul li textarea{
  width: 100%;
  height: 100px;
}
.form-style-9 ul li input[type="button"],
.form-style-9 ul li input[type="submit"] {
  -moz-box-shadow: inset 0px 1px 0px 0px #3985B1;
  -webkit-box-shadow: inset 0px 1px 0px 0px #3985B1;
  box-shadow: inset 0px 1px 0px 0px #3985B1;
  background-color: #216288;
  border: 1px solid #17445E;
  display: inline-block;
  cursor: pointer;
  color: #FFFFFF;
  padding: 8px 18px;
  text-decoration: none;
  font: 12px Arial, Helvetica, sans-serif;
}
.form-style-9 ul li input[type="button"]:hover,
.form-style-9 ul li input[type="submit"]:hover {
  background: linear-gradient(to bottom, #2D77A2 5%, #337DA8 100%);
  background-color: #28739E;
}

.form-style-9 ul li input[type="radio"]{
  margin-left: 20px;
}
.form-style-9 ul li .rb-label{
  margin-left: 5px
}
</style>