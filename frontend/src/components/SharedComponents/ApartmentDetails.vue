<template>
<div style="background-color: white !important;padding: 50px; padding-bottom: 10px;" class="container">
  <div class="row" >
    <div class="col-sm-3">
        <div class="row">
          <label>Apartment type: </label>
        </div>
        <div class="row">
          <label>Number of rooms: </label>
        </div>
        <div class="row">
          <label>Number of guests: </label>
        </div>
        <div class="row">
          <label>Location: </label>
        </div>
        <div class="row">
          <label>Host username: </label>
        </div>
    </div>
    <div class="col-sm-3">
      <div class="row">
        <label>{{ apartmentDetails.apartmentType }}</label>
      </div>
      <div class="row">
        <label>{{ apartmentDetails.numberOfRooms }}</label>
      </div>
      <div class="row">
        <label>{{ apartmentDetails.numberOfGuests }}</label>
      </div>
      <div class="row">
        <label v-if="apartmentDetails.location">{{ apartmentDetails.location.address.country }}, {{apartmentDetails.location.address.city}}, {{apartmentDetails.location.address.street}} {{apartmentDetails.location.address.number}}</label>
      </div>
      <div class="row">
        <label>{{apartmentDetails.userName}}</label>
      </div>
    </div>


    <div class="col-sm-3">
      <div class="row">
        <label>Price per night: </label>
      </div>
      <div class="row">
        <label>Check in time: </label>
      </div>
      <div class="row">
        <label>Check out time: </label>
      </div>
      <div class="row">
        <label>Activity status: </label>
      </div>
    </div>
    <div class="col-sm-3">
      <div class="row">
        <label>{{apartmentDetails.pricePerNight}}</label>
      </div>
      <div class="row">
        <label>{{apartmentDetails.checkInTime}}</label>
      </div>
      <div class="row">
        <label>{{apartmentDetails.checkOutTime}}</label>
      </div>
      <div class="row">
        <label>{{apartmentDetails.activityStatus}}</label>
      </div>
    </div>
  </div>

  <div class="row">
    <div id="carouselExampleControls" class="carousel slide" data-ride="carousel" style="height: 50%; width: 50%;">
      <div class="carousel-inner">
        <div v-for="(photo, index) in apartmentDetails.photos" :key="index" :class="getClass(index)">
          <img class="d-block w-100" :src="photo" alt="Server error">
        </div>
      </div>
      <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
      </a>
      <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
      </a>
    </div>
  </div>
</div>
</template>

<script>

import http from '../../http-common'

export default {
name: "ApartmentDetails",
  data() {
    return {
      apartmentId: '',
      apartmentDetails: ''
    }
  },
  methods: {
    getClass(key) {
      if (key === 0) {
        return "carousel-item active"
      } else {
        return "carousel-item"
      }
    }
  },
  mounted() {
    this.apartmentId = this.$route.params.id;
    http.get(`apartments/apartmentDetails/${this.apartmentId}`)
    .then(response => {
      if (response.data) {
        this.apartmentDetails = response.data;
      }
    })
  }
}
</script>

<style scoped>
.carousel-control, .left{
  z-index: 999;
}
</style>