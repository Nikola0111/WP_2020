<template>
  <div style="width: 90%; margin-top: 5%; margin-left: 5%;">
    <md-table v-model="searched" md-sort="name" md-sort-order="asc" class="md-card md-fixed-header" style="padding:20px ">
      <md-table-toolbar>
        <div class="md-toolbar-section-start">
          <h2 class="md-title">Reservations you made:</h2>
        </div>

        <md-field md-clearable class="md-toolbar-section-end">
          <md-input placeholder="Search by host username..." v-model="search2" @input="searchOnTable2" />
        </md-field>
      </md-table-toolbar>

      <md-table-empty-state
          md-label="No reservations found"
          :md-description="`No reservations found for this query. Try a different search term.`">
      </md-table-empty-state>

      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="Apartment type" md-sort-by="apartmentType">{{ item.apartmentType }}</md-table-cell>
        <md-table-cell md-label="Location" md-sort-by="location">{{ item.location.address.country }}, {{item.location.address.city}}</md-table-cell>
        <md-table-cell md-label="Guest username" md-sort-by="guestUserName">{{ item.hostUserName }}</md-table-cell>
        <md-table-cell md-label="Starting date" md-sort-by="startingDate">{{ item.date }}</md-table-cell>
        <md-table-cell md-label="Rental duration" md-sort-by="rentalDuration">{{ item.rentalDuration }}</md-table-cell>
        <md-table-cell md-label="Status" md-sort-by="status">{{ item.reservationStatus }}</md-table-cell>
        <md-table-cell v-if="item.reservationStatus === 'Created' || item.reservationStatus === 'Accepted'"><button @click="cancelReservation(item.id, item)" class="btn btn-danger">Cancel</button></md-table-cell>

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

const searchByHostUserName = (items, term) => {
  if (term) {
    return items.filter(item => toLower(item.hostUserName).includes(toLower(term)))
  }
  return items
}

export default {
  name: "ReservationsByGuest",
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
      this.searched = searchByHostUserName(this.reservations, this.search2)
    },
    cancelReservation(reservationId, item) {
      http.put('Reservation/cancelReservation/' + reservationId)
      .then(response => {
        if (response.data) {
          item.reservationStatus = 'Canceled';
        }
      })
    },
    reload() {
      this.$forceUpdate();
    }
  },
  created () {
    this.searched = this.reservations
  },
  mounted() {
    if (localStorage.getItem('loggedUser') !== null) {
      this.user = JSON.parse(localStorage.getItem('loggedUser'))
    }

    http.get(`Reservation/GuestReservations/${this.user.id}`)
        .then(response => {
          this.reservations = response.data;
          this.searched = this.reservations;
        })
  }
}
</script>

<style scoped>

</style>