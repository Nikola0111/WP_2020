<template>
  <div>
    <gmap-map
        :center="center"
        :zoom="12"
        style="width:100%;  height: 400px;"
    >
      <gmap-marker
          :key="index"
          v-for="(m, index) in markers"
          :position="m.position"
          @click="center=m.position"
      ></gmap-marker>
    </gmap-map>
  </div>
</template>

<script>
import http from "@/http-common";

export default {
  name: "GoogleMap.vue",
  props: ['location'],
  data() {
    return {
      // default to Montreal to keep it simple
      // change this to whatever makes sense
      center: { lat: 45.508, lng: -73.587 },
      markers: [],
      places: [],
      currentPlace: null,
    };
  },
  mounted() {
    setTimeout(() => {
      this.setOtherLocation(this.location);
    }, 1000)
  },
  methods: {
    addMarker() {
      if (this.currentPlace) {
        const marker = {
          lat: this.currentPlace.geometry.location.lat,
          lng: this.currentPlace.geometry.location.lng
        };
        this.markers.push({ position: marker });
        this.places.push(this.currentPlace);
        this.center = marker;
        this.currentPlace = null;
      }
    },
    setOtherLocation() {
      if(this.location){
        http.get('https://maps.googleapis.com/maps/api/geocode/json?address='+ this.location.address.number + this.location.address.street + ',' + this.location.address.city + ',' + this.location.address.country + '&key=AIzaSyDL4anuQdZ6zzjSGiJKbhZZaEAYKgME72k').then(response => {
          this.currentPlace = response.data.results[0];
          this.addMarker();
        })
      }
    },
  }
};
</script>

<style scoped>

</style>