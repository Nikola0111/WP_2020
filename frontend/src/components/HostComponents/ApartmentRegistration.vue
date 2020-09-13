<template>
  <div style="overflow-y: auto;background:white; width: 80%; margin-bottom:20px; margin-left: 10%;height: 500px;">
    <h3>Enter apartment information</h3>
    <div class="column">
      <div class="field">
        <label>Apartment type:</label><br>
        <input class="rb-male" style="alignment: left" type="radio" id="one" value="apartment" v-model="apartmentType">
        <label class="rb-label" for="one">Apartment</label>
        <input type="radio" id="two" value="room" v-model="apartmentType">
        <label class="rb-label" for="two">Room</label>
      </div>
      <div class="field">
        <label>Number of rooms:</label>
        <input class="form-control" type="number" min="0" style="width: 340px" v-model="numberOfRooms" />
      </div>
      <div class="field">
        <label>Number of guests:</label>
        <input class="form-control" type="number" min="0" style="width: 340px" v-model="numberOfGuests" />
      </div>
      <div class="field">
        <label>Country:</label><br>
        <input class="form-control" type="text" style="width: 340px" v-model="country" />
        <label>Street and number:</label>
        <input class="form-control" type="text" style="width: 340px" v-model="streetAndNumber" />
      </div>
    </div>
    <div class="column">
      <div class="field">
        <label>Select available dates for rent:</label><br>
        <date-picker mode="range" color="blue" v-model="dateRange" :disabled-dates="datesForRent"/>
        <button class="btn btn-info" style="margin-top: 5px" @click="saveDateForRent">Save date</button>
      </div>
      <div class="field">
        <label>Price per night:</label>
        <input class="form-control" type="number" min="0" style="width: 340px" v-model="pricePerNight" />
      </div>
      <div class="field">
        <div style="float:left;">
          <label>Check in time:</label><br>
          <time-picker v-model="checkInTime"></time-picker>
        </div>
        <div style="margin-left:20px;float:left;">
          <label>Check out time:</label><br>
          <time-picker v-model="checkOutTime"></time-picker>
        </div>
        <div style="padding-top: 10px">
          <label style="padding-top: 15px">Select amenities(hover amenities for amenity details)</label><br>
          <div v-for="amenity in amenities" style="display: inline; margin-right: 10px" v-bind:key="amenity.caption">
            <input @change="editAmenities(amenity)" type="checkbox" />
            <label v-tooltip="'This is a tooltip ffs show up'" style="margin-left: 3px" class="amenity-caption">{{amenity.caption}}</label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import VueLayers from 'vuelayers'
import 'vuelayers/lib/style.css'
import DatePicker from 'v-calendar/lib/components/date-picker.umd'
import VueTimepicker from 'vue2-timepicker'
import 'vue2-timepicker/dist/VueTimepicker.css'
import http from "@/http-common";
import VTooltip from 'v-tooltip'

Vue.use(VTooltip)
Vue.component('date-picker', DatePicker)
Vue.component('time-picker',VueTimepicker)
Vue.use(VueLayers)

export default {
name: "ApartmentRegistration",
  data () {
    return {
      apartmentType: "",
      numberOfRooms: 0,
      numberOfGuests: 0,
      country: "",
      streetAndNumber: "",
      datesForRent: [],
      dateRange: "",
      pricePerNight: 0,
      checkInTime: "",
      checkOutTime: "",
      amenities: [],
      checkedAmenities: [],
      msg: 'This is a button.'
    }
  },
  methods: {
    saveDateForRent(){
      let value = {
        start: this.dateRange.start,
        end: this.dateRange.end
      }
      this.attributes[0].dates.push(value)

      this.datesForRent.push(value)
      this.dateRange = ""
    },
    editAmenities(item){
      if(this.checkedAmenities.includes(item)){
        this.checkedAmenities.splice(item,1)
      }else{
        this.checkedAmenities.push(item)
      }
      console.log(this.checkedAmenities)
    }
  },
  mounted() {
    http.get('Amenity').then(response => {
      this.amenities = response.data
    })
  }
}
</script>

<style scoped>
.field, h3{
  margin: 15px;
  padding-top: 10px;
}

.rb-label {
  margin-right: 10px;
  margin-left: 3px;
}

.column {
  width: 50%;
  float: left;
}
</style>