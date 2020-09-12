<template>
  <div style="width: 80%; margin-top: 5%; margin-left: 10%;">
    <md-table v-model="searched" md-sort="name" md-sort-order="asc" md-card md-fixed-header>
      <md-table-toolbar>
        <div class="md-toolbar-section-start">
          <h2 class="md-title">Apartments</h2>
        </div>

        <md-field md-clearable class="md-toolbar-section-end">
          <md-input placeholder="Search by location..." v-model="search" @input="searchOnTable" />
        </md-field>
      </md-table-toolbar>

      <md-table-empty-state
          md-label="No apartments found"
          :md-description="`No apartment found for this '${search}' query. Try a different search term.`">
      </md-table-empty-state>

      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="Apartment type" md-sort-by="apartmentType">{{ item.apartmentType }}</md-table-cell>
        <md-table-cell md-label="Location" md-sort-by="location">{{ item.location.address.country }}, {{item.location.address.city}}</md-table-cell>
        <md-table-cell md-label="Host username" md-sort-by="hostUserName">{{ item.hostUserName }}</md-table-cell>
        <md-table-cell md-label="Price per night" md-sort-by="pricePerNight">{{ item.pricePerNight }}</md-table-cell>
      </md-table-row>
    </md-table>
  </div>
</template>

<script>

const toLower = text => {
  return text.toString().toLowerCase()
}

const searchByLocation = (items, term) => {
  if (term) {
    return items.filter(item => toLower(item.location.address.country + ' ' + item.location.address.city).includes(toLower(term)))
  }
  return items
}

import Vue from 'vue'
import http from '../../http-common'
import {MdTable, MdCard, MdContent, MdField, MdEmptyState, MdRipple} from 'vue-material/dist/components'


Vue.use(MdTable)
Vue.use(MdCard)
Vue.use(MdField)
Vue.use(MdContent)
Vue.use(MdEmptyState)
Vue.use(MdRipple)

export default {
name: "AllActiveApartments",
  data() {
    return {
      search: null,
      searched: [],
      apartments: []
    }
  },
  mounted() {
    http.get('/apartments/ActiveApartments')
    .then(response => {
      this.apartments = response.data;
      this.searched = this.apartments;
    })
  },
  methods: {
    searchOnTable () {
      this.searched = searchByLocation(this.apartments, this.search)
    }
  },
  created () {
    this.searched = this.apartments
  },
}
</script>

<style scoped>

</style>