<template>
  <div style="margin-top: 2%; background-color: white !important; width: fit-content; padding: 50px" class="container">
    <h2>Create new amenity</h2>
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
        <button @click="createAmenity" style="float:left;" type="button" class="btn btn-info">Create amenity</button>
        <router-link style="float:right;" tag="button" to="/AllAmenities" type="button" class="btn btn-dark">Cancel</router-link>
      </div>
    </div>
    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="showSnackbar" md-persistent>
      <span>Amenity successfully created.</span>
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
name: "CreateAmenity",
  data() {
    return {
      caption: "",
      description: "",
      type: "",
      showSnackbar: false,
      showSnackbar1: false,
      position: 'center',
      duration: 3000
    }
  },
  methods: {
    createAmenity() {
      if (this.caption === "" || this.description === "" || this.type === "") {
        this.showSnackbar1 = true;
        return;
      }
      http
      .post('Amenity/createAmenity', JSON.stringify({
        caption: this.caption,
        description: this.description,
        type: this.type
      }))
      .then(response => {
        if(response.data) {
          this.showSnackbar = true;
        }
      })
    }
  }
}
</script>

<style scoped>

</style>