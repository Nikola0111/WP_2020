<template>
  <div style="width: 80%; margin-top: 5%; margin-left: 10%;">
    <md-table v-model="searched" md-sort="hostUserName" md-sort-order="asc" md-card md-fixed-header>
      <md-table-toolbar>
        <div class="md-toolbar-section-start">
          <h2 class="md-title">Apartments</h2>
        </div>

        <md-field md-clearable class="md-toolbar-section-end">
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
        <md-table-cell><button>Details</button></md-table-cell>
        <md-table-cell v-if="isAdmin || isHost"><button @click="deleteApartment(item.id, item)">Delete</button></md-table-cell>
        <md-table-cell v-if="isGuest"><button @click="showDialog(item)">Reserve</button></md-table-cell>
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
          <date-picker color="blue" v-model="date" :attributes="attributes" :disabled-dates="occupiedDates"/>
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
        <div style="margin-top: 30px; text-align: right">
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
      attributes: []
    }
  },
  components: {
  },
  methods: {
    searchOnTable () {
      this.searched = searchByHostUsername(this.apartments, this.search)
    },
    deleteApartment(apartmentId, objectInList) {
      http.get(`apartments/deleteApartment/${apartmentId}`).then(response => {
        if(response.data === true) {
          this.apartments.splice(objectInList, 1)
        }else {
          this.failedDelete = true
        }
      })
    },
    showDialog(item){
      http.get(`Reservation/getActiveReservationsByApartment/${item.id}`).then(response =>{
        this.selectedApartment = item
        this.apartmentReservations = response.data

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
        this.hideDialog()
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
              console.log(response.data)
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
.vl {
  border-left: 2px solid gray;
  height: 290px;
  position: absolute;
  left: 50%;
  margin-left: -3px;
  margin-top: 22%;
  top: 0;
}
</style>