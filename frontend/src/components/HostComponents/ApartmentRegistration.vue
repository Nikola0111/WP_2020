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
        <input class="form-control" type="number" min="1" style="width: 340px" v-model="numberOfRooms" />
      </div>
      <div class="field">
        <label>Number of guests:</label>
        <input class="form-control" type="number" min="1" style="width: 340px" v-model="numberOfGuests" />
      </div>
      <div class="field">
        <label>Country:</label><br>
        <input class="form-control" type="text" style="width: 340px" v-model="country" />
        <label>City:</label>
        <input class="form-control" type="text" style="width: 340px" v-model="city" />
        <label>Street:</label>
        <input class="form-control" type="text" style="width: 340px" v-model="street" />
        <label>Number:</label>
        <input class="form-control" type="number" style="width: 340px" v-model="number" />
      </div>
    </div>
    <div class="column">
      <div class="field">
        <label>Select available dates for rent:</label><br>
        <date-picker mode="range" color="blue" v-model="dateRange" :disabled-dates="datesForRent"/>
        <button class="btn btn-info" style="margin-top: 5px" @click="saveDateForRent">Save date</button>
      </div>
      <div class="field">
        <label style="padding: 5px" >Upload pictures</label>
        <input style="padding: 5px" type="file" ref="file" multiple="multiple">
      </div>
      <div class="field">
        <label>Price per night:</label>
        <input class="form-control" type="number" min="1" style="width: 340px" v-model="pricePerNight" />
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
          <input @change="filterAmenities" placeholder="Search amenities" type="text" v-model="searchCriteria"/><br><br>
          <div v-for="item in amenities" style="display: inline; margin-right: 10px" v-bind:key="item.item.id">
            <input :checked="item.checked" @change="editAmenities(item)" type="checkbox" />
            <label v-tooltip="'This is a tooltip ffs show up'" style="margin-left: 3px" class="amenity-caption">{{item.item.caption}}</label>
          </div>
        </div>

        <button class="btn btn-success" @click="registerAmenity" style="margin-left: 320px;">Register</button>
      </div>
    </div>

    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="saved" md-persistent>
      <span>Apartment successfully registered!</span>
      <md-button class="md-primary" @click="saved = false">Close</md-button>
    </md-snackbar>

    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="notSaved" md-persistent>
      <span>There was an error saving the apartment, please re-enter information</span>
      <md-button class="md-primary" @click="notSaved = false">Close</md-button>
    </md-snackbar>

    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="apartmentTypeCheck" md-persistent>
      <span>You must select an apartment type</span>
      <md-button class="md-primary" @click="apartmentTypeCheck = false">Close</md-button>
    </md-snackbar>

    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="adressCheck" md-persistent>
      <span>You must enter the full adress of the apartment</span>
      <md-button class="md-primary" @click="adressCheck = false">Close</md-button>
    </md-snackbar>

    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="datesForRentCheck" md-persistent>
      <span>Please enter the dates when the apartment/room is available for rent</span>
      <md-button class="md-primary" @click="datesForRentCheck = false">Close</md-button>
    </md-snackbar>
    <div id="imgTest"></div>
    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="checkInOutTimeCheck" md-persistent>
      <span>Check in time must be after 14:00(2:00PM) and check out time must be after 10:00(10:00AM)</span>
      <md-button class="md-primary" @click="checkInOutTimeCheck = false">Close</md-button>
    </md-snackbar>
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
Vue.component('time-picker', VueTimepicker)
Vue.use(VueLayers)

export default {
  name: "ApartmentRegistration",
  data () {
    return {
      apartmentType: "",
      numberOfRooms: 1,
      numberOfGuests: 1,
      country: "",
      city: '',
      street: '',
      number: '',
      datesForRent: [],
      dateRange: "",
      pricePerNight: 1,
      checkInTime: "",
      checkOutTime: "",
      amenities: [],
      tempAmenities: [],
      checkedAmenities: [],
      searchCriteria: '',
      duration: 3000,
      position: 'center',
      photos: [],
      saved: false,
      notSaved: false,
      apartmentTypeCheck: false,
      adressCheck: false,
      datesForRentCheck: false,
      checkInOutTimeCheck: false
    }
  },
  methods: {
    saveDateForRent(){
      let value = {
        start: this.dateRange.start,
        end: this.dateRange.end
      }

      this.datesForRent.push(value)
      this.dateRange = ""
    },
    editAmenities(item){
      this.tempAmenities.forEach(amenity => {
        if(amenity.item.id === item.item.id){
          amenity.checked = !amenity.checked
        }
      })

      if(this.checkedAmenities.length === 0) {
        this.checkedAmenities.push({checked: true, item: item.item})

        console.log(this.checkedAmenities)
        return
      }

      var exists = false
      var temp = {}

      console.log(item.item)
      this.checkedAmenities.forEach(checkedAmenity => {
        console.log('Comparing: ' + checkedAmenity.item.id + ':' + item.item.id)
        if(checkedAmenity.item.id === item.item.id){
          console.log('Indices match!')
          exists = true
          temp = checkedAmenity
        }
      })

      console.log(temp)
      if(exists){
        this.checkedAmenities = []
        this.tempAmenities.forEach(amenity => {
          if(amenity.checked) {
            this.checkedAmenities.push(amenity)
          }
        })
      }else {
        this.checkedAmenities.push({checked: true, item: item.item})
      }

      item.checked = false

      console.log(this.checkedAmenities)
    },
    filterAmenities(){
      if(this.searchCriteria === ''){
        this.amenities = this.tempAmenities
      }else {
        this.amenities = this.tempAmenities.filter(current => {
          let item = current.item.caption.toLowerCase()
          let criteriaLowerCase = this.searchCriteria.toLowerCase()
          if(item.includes(criteriaLowerCase)){
            console.log(current)
            return current
          }
        })
      }
    },
    getBase64(vueInstance, files, callback) {
      for (let i = 0; i < files.length; i++) {
        const reader = new FileReader();
        reader.addEventListener('load', () => callback(reader.result));
        reader.readAsDataURL(files[i]);
      }
    },
    registerAmenity(){
      if(this.apartmentType === '') {
        this.apartmentTypeCheck = true
        return
      }

      if(this.city === '' || this.country === '' || this.street === '' || this.number === '' || this.number <= 0){
        this.adressCheck = true
        return
      }

      if(this.datesForRent.length === 0) {
        this.datesForRentCheck = true;
        return
      }

      if(this.checkInTime !== '' && this.checkOutTime !== '') {
        let checkInTimeElements = this.checkInTime.split(':')
        let cIHour = parseInt(checkInTimeElements[0])

        let checkOutTimeElements = this.checkOutTime.split(':')
        let cOHours = parseInt(checkOutTimeElements[0])

        if(cIHour < 14 || cOHours < 10){
          this.checkInOutTimeCheck = true
          return
        }
      } else {
        this.checkInOutTimeCheck = true
        return
      }

      let street = this.street;
      let number = this.number;
      let loggedUser = JSON.parse(localStorage.getItem("loggedUser"))

      console.log(this.datesForRent)

      let startDates = []
      let endDates = []
      this.datesForRent.forEach(item =>{
        startDates.push(item.start)
        endDates.push(item.end)
      })

      let chosenAmenities = []

      this.checkedAmenities.forEach(amenity => {
        chosenAmenities.push(amenity.item)
      })

      //PICTURES
      let files = this.$refs.file.files;
      let vueInstance = this;
      this.getBase64(vueInstance, files, function (base64Data) {
        let photoString = base64Data.toString();
        vueInstance.photos.push(photoString);

        if(files.length == vueInstance.photos.length){
          http.post('apartments/registerApartment',{
            apartmentType: vueInstance.apartmentType,
            numberOfRooms: vueInstance.numberOfRooms,
            numberOfGuests: vueInstance.numberOfGuests,
            location: {
              address: {
                street: street,
                number: number,
                city: vueInstance.city,
                postalCode: '22330',
                country: vueInstance.country
              }
            },
            startDates: startDates,
            endDates: endDates,
            hostId: loggedUser.id,
            photos: vueInstance.photos,
            pricePerNight: vueInstance.pricePerNight,
            checkInTime: vueInstance.checkInTime,
            checkOutTime: vueInstance.checkOutTime,
            activityStatus: false,
            amenities: chosenAmenities
          }).then(response => {
            if(response.data === true){
              vueInstance.saved = true
            }else {
              vueInstance.notSaved = true
            }
          })

          return;
        }
      });

    }
  },
  mounted() {
    http.get('Amenity').then(response => {
      response.data.forEach(amenity =>{
        this.amenities.push({checked: false, item: amenity})
        this.tempAmenities.push({checked: false, item: amenity})
      })
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