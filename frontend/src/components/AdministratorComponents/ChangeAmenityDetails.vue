<template>
  <div style="margin-top: 2%; background-color: white !important; width: fit-content; padding: 50px" class="container">
    <h2>Change amenity details</h2>
    <div class="row">
      <div style="flex: 100%; max-width: 100%" class="col-sm-4">
        <label>Caption</label>
        <div class="form-group pass_show">
          <input v-model="caption" class="form-control" placeholder="Caption">
        </div>
        <label>Description</label>
        <div class="form-group pass_show">
          <input v-model="description" class="form-control" placeholder="Description">
        </div>
        <label>Type</label>
        <div class="form-group pass_show">
          <input v-model="type" class="form-control" placeholder="Type">
        </div>
        <button @click="changeDetails" style="float:left;" type="button" class="btn btn-info">Change details</button>
        <router-link style="float:right;" tag="button" to="/AllAmenities" type="button" class="btn btn-dark">Cancel</router-link>
      </div>
    </div>
    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="showSnackbar" md-persistent>
      <span>Amenity successfully changed.</span>
      <md-button class="md-primary" @click="showSnackbar = false">Retry</md-button>
    </md-snackbar>
    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="showSnackbar1" md-persistent>
      <span>All fields are required.</span>
      <md-button class="md-primary" @click="showSnackbar1 = false">Retry</md-button>
    </md-snackbar>
  </div>
</template>

<script>

import http from '../../http-common'

export default {
name: "ChangeAmenityDetails",
  data() {
    return {
      amenity: null,
      caption: null,
      description: null,
      type: null,
      showSnackbar: false,
      showSnackbar1: false,
      position: 'center',
      duration: 3000
    }
  },
  methods: {
    changeDetails() {
      if (this.caption === "" || this.description === "" || this.type === "") {
        this.showSnackbar1 = true;
        return;
      }

      http
      .put('Amenity/changeAmenity', JSON.stringify({
        id: this.amenity.id,
        caption: this.caption,
        description: this.description,
        type: this.type
      }))
      .then(response => {
        if (response.data) {
          this.showSnackbar = true;
        }
      })
    }
  },
  mounted() {
    let userRole = JSON.parse(localStorage.getItem("loggedUserRole"))
    if (userRole !== "ADMINISTRATOR") {
      this.$router.push("/forbidden");
    }

    let id = this.$route.params.id

    http
    .get(`Amenity/getAmenity/${id}`)
    .then(response => {
      if (response.data) {
        this.amenity = response.data;
        this.caption = this.amenity.caption
        this.description = this.amenity.description
        this.type = this.amenity.type
      }
    })
  }
}
</script>

<style scoped>

</style>