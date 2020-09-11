<template>
  <div style="width: 80%; margin-top: 5%; margin-left: 10%;">
    <md-table v-model="searched" md-sort="hostUserName" md-sort-order="asc" md-card md-fixed-header>
      <md-table-toolbar>
        <div class="md-toolbar-section-start">
          <h1 class="md-title">Apartments</h1>
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
      </md-table-row>
    </md-table>

    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="failedDelete" md-persistent>
      <span>Problem while deleting apartment, try reloading the page</span>
      <md-button class="md-primary" @click="failedDelete = false">Close</md-button>
    </md-snackbar>
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

Vue.use(MdTable)
Vue.use(MdCard)
Vue.use(MdField)
Vue.use(MdContent)
Vue.use(MdEmptyState)
Vue.use(MdRipple)

export default {
  name: "ApartmentList",

  data() {
    return {
      search: null,
      searched: [],
      apartments: [],
      isAdmin: false,
      isHost: false,
      failedDelete: false,
      duration: 3000,
      position: 'center'
    }
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
    }
  },
  created () {
    this.searched = this.apartments
    this.isAdmin = JSON.parse(localStorage.getItem("loggedUserRole")) === 'ADMINISTRATOR';
    this.isHost = JSON.parse(localStorage.getItem("loggedUserRole")) === 'HOST';
  },
  mounted() {
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
  }
}
</script>


<style scoped>

</style>