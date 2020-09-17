<template>
  <div style="width: 90%; margin-top: 5%; margin-left: 5%;">
    <div style="background-color: white !important;padding: 10px">
    <div style="padding: 20px" class="row">
      <div class="form-group col-sm-3">
        <label for="exampleFormControlSelect1">Number of rooms</label>
        <select v-model="selectedNumberOfRooms" class="form-control" id="exampleFormControlSelect1">
          <option value="all">All</option>
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5 and more</option>
        </select>
      </div>
      <div class="form-group col-sm-3">
        <label for="exampleFormControlSelect2">Apartment type</label>
        <select v-model="selectedApartmentType" class="form-control" id="exampleFormControlSelect2">
          <option value="all">All</option>
          <option value="apartment">Apartment</option>
          <option value="room">Room</option>
        </select>
      </div>
      <div class="form-group col-sm-3">
        <label for="exampleFormControlSelect3">Number of guests</label>
        <select v-model="selectedNumberOfGuests" class="form-control" id="exampleFormControlSelect3">
          <option value="all">All</option>
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5 and more</option>
        </select>
      </div>
      <div class="form-group col-sm-3">
        <button @click="filterApartments()" style="margin-top: 30px" class="btn btn-info">Filter</button>
      </div>
    </div>
    <div class="row">
      <div class="form-group col-sm-3">
        <label>Price from: </label>
        <input type="number" v-model="priceFrom">
      </div>
      <div class="form-group col-sm-3">
        <label>Price to: </label>
        <input type="number" v-model="priceTo">
      </div>
    </div>
    </div>
    <md-table v-model="searched" md-sort="name" md-sort-order="asc" class="md-card md-fixed-header" style="padding:20px ">
      <md-table-toolbar>
        <div v-if="!isHost" class="md-toolbar-section-start">
          <h2 class="md-title">Apartments</h2>
        </div>

        <div v-if="isHost && showInactiveBool" class="md-toolbar-section-start">
          <h2 class="md-title">Active apartments</h2>
        </div>

        <div v-if="isHost && showActiveBool" class="md-toolbar-section-start">
          <h2 class="md-title">Deactivated apartments</h2>
        </div>

        <md-button @click="showInactive" v-if="showInactiveBool && isHost">
          Show inactive
        </md-button>

        <md-button @click="showActive" v-if="showActiveBool && isHost">
          Show active
        </md-button>

        <md-field v-if="!isHost" md-clearable class="md-toolbar-section-end">
          <md-input placeholder="Search by host username" v-model="search" @input="searchOnTable" />
        </md-field>
      </md-table-toolbar>

      <md-table-empty-state
          md-label="No apartments found"
          :md-description="`No apartments found for this '${search}' query. Try a different search term.`">
      </md-table-empty-state>

      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="Host username" md-sort-by="hostUserName">{{ item.hostUserName }}</md-table-cell>
        <md-table-cell md-label="Price per night" md-sort-by="pricePerNight">{{ item.pricePerNight }}</md-table-cell>
        <md-table-cell md-label="Apartment type" md-sort-by="apartmentType">{{ item.apartmentType }}</md-table-cell>
        <md-table-cell v-if="isAdmin || isHost" md-label="Activity status" md-sort-by="activityStatus">{{ item.activityStatus }}</md-table-cell>
        <md-table-cell><router-link :to="'/apartmentDetails/' + item.id" tag="button" class="btn btn-info">Details</router-link></md-table-cell>
        <md-table-cell v-if="isAdmin || isHost"><button class="btn btn-danger" @click="deleteApartment(item.id, item)">Delete</button></md-table-cell>
        <md-table-cell v-if="isGuest"><button class="btn btn-success" @click="showDialog(item, item.startDates, item.endDates)">Reserve</button></md-table-cell>
        <md-table-cell v-if="isHost && item.activityStatus === 'Active'"><button class="btn btn-warning" @click="deactivateApartment(item)">Deactivate</button></md-table-cell>
        <md-table-cell v-if="isHost && item.activityStatus !== 'Active'"><button class="btn btn-warning" @click="activateApartment(item)">Activate</button></md-table-cell>
      </md-table-row>
    </md-table>

    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="failedDelete" md-persistent>
      <span>Problem while deleting apartment, try reloading the page</span>
      <md-button class="md-primary" @click="failedDelete = false">Close</md-button>
    </md-snackbar>

    <modal :height="550" :width="400" name="reservationDialog">
      <h2 style="margin: 30px">Fill in the information</h2>
      <div style="margin: 30px">
        <div>
          <label>Select starting date</label>
          <date-picker color="blue" v-model="date" :attributes="attributes" :available-dates="availableDates"/>
        </div>
        <div style="margin-top: 20px">
          <label>How many days will you stay?</label><br>
          <input type="number" min="0" style="width: 340px" v-model="numberOfDays" @change="calculateBill"/>
        </div>
        <div>
          <label>Additional comments:</label><br>
          <textarea v-model="additionalComment" rows="5" cols="44"></textarea>
        </div>
        <hr>
        <div style="margin-top: 20px">
          <label style="font-size: 20px">Your total is: <b>{{bill}}</b></label>
        </div>
        <div style="margin-top: 30px; text-align: right;">
          <button style="margin-right: 10px" class="btn btn-dark" @click="cancelReservationCreation">Cancel</button>
          <button class="btn btn-success" @click="registerReservation">Reserve</button>
        </div>

      </div>
    </modal>
  </div>
</template>

<script>
const toLower = text => {
  return text.toString().toLowerCase()
}

const searchByHostUsername = (items, term) => {
  if (term) {
    return items.filter(item => toLower(item.hostUserName).includes(toLower(term)))
  }
  return items
}

import Vue from "vue"
import {MdTable, MdCard, MdContent, MdField, MdEmptyState, MdRipple} from 'vue-material/dist/components'
import VueMaterial from 'vue-material'
import http from "@/http-common";
import VModel from 'vue-js-modal'
import DatePicker from 'v-calendar/lib/components/date-picker.umd'
import VCalendar from 'v-calendar';

Vue.use(MdTable)
Vue.use(MdCard)
Vue.use(MdField)
Vue.use(MdContent)
Vue.use(MdEmptyState)
Vue.use(MdRipple)
Vue.use(VModel)
Vue.use(VueMaterial)
import 'vue-material/dist/vue-material.css'
Vue.use(VCalendar, {
  componentPrefix: 'vc'
})
Vue.component('date-picker', DatePicker)
export default {
  name: "ApartmentList",
  data() {
    return {
      search: null,
      searched: [],
      apartments: [],
      isAdmin: false,
      isHost: false,
      isGuest: false,
      failedDelete: false,
      duration: 3000,
      position: 'center',
      showReservationDialog: false,
      selectedApartment: "",
      date: "",
      numberOfDays: 0,
      bill: 0.0,
      additionalComment: "",
      apartmentReservations: [],
      occupiedDates: [],
      attributes: [],
      fallbackApartments: [],
      availableDates: [],

      showActiveBool: false,
      showInactiveBool: true,

      selectedNumberOfRooms: "all",
      selectedApartmentType: "all",
      selectedNumberOfGuests: "all",
      priceFrom: 0,
      priceTo: 200
    }
  },
  components: {
  },
  methods: {
    filterApartments() {
      if (this.priceFrom < 0 || this.priceTo <= 0 || this.priceFrom > this.priceTo) {
        alert("Invalid filter arguments, check price filter")
        return;
      }

      http
      .post('apartments/filterApartments', JSON.stringify({
        rooms: this.selectedNumberOfRooms,
        guests: this.selectedNumberOfGuests,
        type: this.selectedApartmentType,
        priceFrom: this.priceFrom,
        priceTo: this.priceTo
      }))
      .then(response => {
        if (response.data) {
          this.apartments = response.data
          this.searched = this.apartments;
        }
      })
    },
    cancelReservationCreation(){
      this.$modal.hide('reservationDialog')
      this.clearLists()
    },
    clearLists(){
      this.attributes = []
      this.occupiedDates = []
      this.availableDates = []
    },
    searchOnTable () {
      this.searched = searchByHostUsername(this.apartments, this.search)
    },
    deleteApartment(apartmentId, objectInList) {
      http.get(`apartments/deleteApartment/${apartmentId}`).then(response => {
        if(response.data === true) {
          this.apartments.splice(objectInList, 1)

          this.searched.forEach(apartment => {
            if(apartment.id === apartmentId){
              this.searched.splice(apartment, 1)
            }
          })

          this.fallbackApartments.forEach(apartment => {
            if(apartment.id === apartmentId){
              this.fallbackApartments.splice(apartment, 1)
            }
          })
        }else {
          this.failedDelete = true
        }
      })
    },
    showDialog(item, startDates, endDates){
      console.log(item.id)
      http.get(`Reservation/getActiveReservationsByApartment/${item.id}`).then(response =>{
        this.selectedApartment = item
        this.apartmentReservations = response.data

        console.log(this.attributes)

        this.apartmentReservations.forEach(reservation => {
          console.log(reservation.startingDate)
          this.occupiedDates.push({
            start: reservation.startingDate,
            span: reservation.rentalDuration
          })
        })

        this.attributes.push({
          highlight: true,
          dates: this.occupiedDates
        })

        for(let i = 0; i  < startDates.length; i++){
          this.availableDates.push({start: startDates[i], end: endDates[i]})
        }

        console.log(this.attributes)

        this.$modal.show('reservationDialog')
      })

    },
    hideDialog(){
      this.date = ""
      this.bill = 0
      this.additionalComment = ""
      this.numberOfDays = 0
      this.occupiedDates = []
      this.attributes = []
      this.$modal.hide('reservationDialog')
    },
    showActive(){
      console.log('showing active')
      this.showActiveBool = false
      this.showInactiveBool = true
      this.searched = []
      this.fallbackApartments.forEach(apartment => {
        if(apartment.activityStatus === 'Active') {
          this.searched.push(apartment)
        }
      })
    },
    showInactive(){
      console.log('showing inactive')
      this.showActiveBool = true
      this.showInactiveBool = false
      this.searched = []
      this.fallbackApartments.forEach(apartment => {
        if(apartment.activityStatus !== 'Active') {
          this.searched.push(apartment)
        }
      })
    },
    date_function: function () {

      var currentDate = new Date();
      console.log(currentDate);

      var formatted_date = new Date().toJSON().slice(0, 10).replace(/-/g, '/');
      console.log(formatted_date);
    },
    calculateBill(){
      this.bill = this.selectedApartment.pricePerNight * this.numberOfDays
      console.log("This is the bill:" + this.bill)
    },
    registerReservation(){
      let loggedUser = JSON.parse(localStorage.getItem("loggedUser"))

      console.log('Selected date: ' + this.date)

      http.post('Reservation/registerReservation',
      JSON.stringify({
        id: '',
        apartmentId: this.selectedApartment.id,
        startingDate: this.date,
        rentalDuration: this.numberOfDays,
        fullPrice: this.bill,
        optionalMessage: this.additionalComment,
        guestId: loggedUser.id,
        deleted: false
      })).then(response => {
        console.log(response.data)
        this.datesForRent = []
        this.attributes = []
        this.clearLists()
        this.hideDialog()
      })
    },
    activateApartment(item) {
      http.get(`apartments/activateApartment/${item.id}`).then(response => {
        if(response.data) {
          this.searched.splice(item, 1)

          this.apartments.forEach(apartment => {
            if(item.id === apartment.id){
              apartment.activityStatus = 'Active'
            }
          })

          this.fallbackApartments.forEach(apartment => {
            if(item.id === apartment.id){
              apartment.activityStatus = 'Active'
            }
          })

          item.activityStatus = 'Active'
        }
      })
    },
    deactivateApartment(item) {
      http.get(`apartments/deactivateApartment/${item.id}`).then(response => {
        if(response.data) {
          this.searched.splice(item, 1)

          this.apartments.forEach(apartment => {
            if(item.id === apartment.id){
              apartment.activityStatus = 'Not active'
            }
          })

          this.fallbackApartments.forEach(apartment => {
            if(item.id === apartment.id){
              apartment.activityStatus = 'Not active'
            }
          })

          item.activityStatus = 'Not active'
        }
      })
    }
  },
  created () {
    this.searched = this.apartments
    this.isAdmin = JSON.parse(localStorage.getItem("loggedUserRole")) === 'ADMINISTRATOR';
    this.isHost = JSON.parse(localStorage.getItem("loggedUserRole")) === 'HOST';
    this.isGuest = JSON.parse(localStorage.getItem("loggedUserRole")) === 'GUEST';
  },
  mounted() {
    this.date_function()
    if(localStorage.getItem("loggedUserRole") === null || JSON.parse(localStorage.getItem("loggedUserRole")) === "GUEST") {
      http.get('apartments/ActiveApartments')
          .then(response => {
            this.apartments = response.data;
            this.searched = this.apartments;
            console.log(response.data)
          })
    } else {
      console.log(localStorage.getItem("loggedUserRole"))
      let role = JSON.parse(localStorage.getItem("loggedUserRole"))
      if(role === "HOST"){
        let user = JSON.parse(localStorage.getItem("loggedUser"))
        http.get(`apartments/HostApartments/${user.id}`)
            .then(response => {
              this.apartments = response.data;
              this.searched = this.apartments;
              this.fallbackApartments = this.apartments
              this.showActive()
            })
      } else if(role === "ADMINISTRATOR"){
        http.get('apartments/Apartments')
            .then(response => {
              this.apartments = response.data;
              this.searched = this.apartments;
              console.log(response.data)
            })
      }
    }
  },

}
</script>


<style scoped>

</style>