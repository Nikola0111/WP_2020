<template>
  <div style="width: 80%; margin-top: 5%; margin-left: 10%;">
    <md-table v-model="searched" md-sort="name" md-sort-order="asc" md-card md-fixed-header>
      <md-table-toolbar>
        <div class="md-toolbar-section-start">
          <h1 class="md-title">Reservations</h1>
        </div>

        <md-field md-clearable class="md-toolbar-section-end">
          <md-input placeholder="Search by host username..." v-model="search1" @input="searchOnTable1" />
        </md-field>
        <md-field md-clearable class="md-toolbar-section-end">
          <md-input placeholder="Search by guest username..." v-model="search2" @input="searchOnTable2" />
        </md-field>
      </md-table-toolbar>

      <md-table-empty-state
          md-label="No reservations found"
          :md-description="`No reservations found for this query. Try a different search term.`">
      </md-table-empty-state>

      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="Username" md-sort-by="userName">{{ item.userName }}</md-table-cell>
        <md-table-cell md-label="Name" md-sort-by="name">{{ item.name }} {{ item.surname }}</md-table-cell>
        <md-table-cell md-label="User role" md-sort-by="userRole">{{ item.userRole }}</md-table-cell>
        <md-table-cell md-label="Gender" md-sort-by="userGender">{{ item.userGender }}</md-table-cell>
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

const searchByHostUsername = (items, term) => {
  if (term) {
    return items.filter(item => toLower(item.hostUserName).includes(toLower(term)))
  }
  return items
}

const searchByGuestUserName = (items, term) => {
  if (term) {
    return items.filter(item => toLower(item.guestUserName).includes(toLower(term)))
  }
  return items
}

export default {
  name: "ReservationsForAdmin",
  data() {
    return {
      search1: null,
      search2: null,
      searched: [],
      reservations: []
    }
  },
  methods: {
    searchOnTable1 () {
      this.searched = searchByHostUsername(this.reservations, this.search1)
    },
    searchOnTable2 () {
      this.searched = searchByGuestUserName(this.reservations, this.search2)
    }
  },
  created () {
    this.searched = this.reservations
  },
  mounted() {
    http.get('Reservation/')
        .then(response => {
          this.reservations = response.data;
          this.searched = this.reservations;
        })
  }
}
</script>

<style scoped>
.md-field {
  max-width: 300px;
}
</style>