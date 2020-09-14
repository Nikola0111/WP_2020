<template>
  <div style="width: 100%; margin-top: 5%; margin-left: 5%;">
    <md-table v-model="searched" md-sort="name" md-sort-order="asc" class="md-card md-fixed-header" style="padding:20px ">
      <md-table-toolbar>
        <div class="md-toolbar-section-start">
          <h2 class="md-title">Reservations</h2>
        </div>

        <md-field md-clearable class="md-toolbar-section-end">
          <md-input placeholder="Search by guest username..." v-model="search2" @input="searchOnTable2" />
        </md-field>
      </md-table-toolbar>

      <md-table-empty-state
          md-label="No reservations found"
          :md-description="`No reservations found for this query. Try a different search term.`">
      </md-table-empty-state>

      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="Apartment type" md-sort-by="apartmentType">{{ item.apartmentType }}</md-table-cell>
        <md-table-cell md-label="Location" md-sort-by="location">{{ item.location.address.country }}, {{item.location.address.city}}</md-table-cell>
        <md-table-cell md-label="Guest username" md-sort-by="guestUserName">{{ item.guestUserName }}</md-table-cell>
        <md-table-cell md-label="Starting date" md-sort-by="startingDate">{{ item.startingDate }}</md-table-cell>
        <md-table-cell md-label="Rental duration" md-sort-by="rentalDuration">{{ item.rentalDuration }}</md-table-cell>
        <md-table-cell md-label="Status" md-sort-by="status">{{ item.reservationStatus }}</md-table-cell>
        <md-table-cell v-if="item.reservationStatus === 'Created'"><button @click="acceptReservation(item.id, item)" class="btn btn-success">Accept</button></md-table-cell>
        <md-table-cell v-if="item.reservationStatus === 'Created' || item.reservationStatus === 'Accepted'"><button @click="declineReservation(item.id, item)" class="btn btn-danger">Decline</button></md-table-cell>
        <md-table-cell v-if="item.reservationStatus === 'Accepted'"><button @click="finishReservation(item.id, item)" class="btn btn-info">Finish</button></md-table-cell>

      </md-table-row>
    </md-table>
  </div>
</template>

<script>

import Vue from 'vue'
import http from '../../http-common'
import {MdTable, MdCard, MdContent, MdField, MdEmptyState, MdRipple} from 'vue-material/dist/components'


Vue.use(MdTable)
Vue.use(MdCard)
Vue.use(MdField)
Vue.use(MdContent)
Vue.use(MdEmptyState)
Vue.use(MdRipple)

const toLower = text => {
  return text.toString().toLowerCase()
}

const searchByGuestUserName = (items, term) => {
  if (term) {
    return items.filter(item => toLower(item.guestUserName).includes(toLower(term)))
  }
  return items
}


export default {
  name: "ReservationsFromGuests",
  data() {
    return {
      search2: null,
      searched: [],
      reservations: [],
      user: null
    }
  },
  methods: {
    searchOnTable2 () {
      this.searched = searchByGuestUserName(this.reservations, this.search2)
    },
    declineReservation(reservationId, item) {
      http.put('Reservation/declineReservation/' + reservationId)
          .then(response => {
            if (response.data) {
              item.reservationStatus = 'Declined';
            }
          })
    },
    acceptReservation(reservationId, item) {
      http.put('Reservation/acceptReservation/' + reservationId)
          .then(response => {
            if (response.data) {
              item.reservationStatus = 'Accepted';
            }
          })
    },
    finishReservation(reservationId, item) {
      http.put('Reservation/finishReservation/' + reservationId)
          .then(response => {
            if (response.data) {
              item.reservationStatus = 'Finished';
            }
          })
    },
  },
  created () {
    this.searched = this.reservations
  },
  mounted() {
    if (localStorage.getItem('loggedUser') !== null) {
      this.user = JSON.parse(localStorage.getItem('loggedUser'))
    }

    http.get(`Reservation/HostReservations/${this.user.id}`)
        .then(response => {
          this.reservations = response.data;
          this.searched = this.reservations;
        })
  }
}
</script>

<style scoped>

</style>