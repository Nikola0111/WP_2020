<template>
  <div style="width: 90%; margin-top: 5%; margin-left: 5%;">
    <md-table v-model="searched" md-sort="name" md-sort-order="asc" class="md-card md-fixed-header" style="padding:20px ">
      <md-table-toolbar>

        <div class="md-toolbar-section-start">
          <h2 class="md-title">Amenities</h2>
        </div>
        <router-link style="margin-right: 50px" to="/createAmenity" tag="button" class="btn btn-info">Create new amenity</router-link>
        <md-field md-clearable class="md-toolbar-section-end">
          <md-input placeholder="Search by caption..." v-model="search" @input="searchOnTable" />
        </md-field>
      </md-table-toolbar>

      <md-table-empty-state
          md-label="No amenities found"
          :md-description="`No amenity found for this '${search}' query. Try a different search term.`">
      </md-table-empty-state>

      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="ID" md-sort-by="id">{{ item.id }}</md-table-cell>
        <md-table-cell md-label="Caption" md-sort-by="caption">{{ item.caption }}</md-table-cell>
        <md-table-cell md-label="Description" md-sort-by="description">{{ item.description }}</md-table-cell>
        <md-table-cell md-label="Type" md-sort-by="type">{{ item.type }}</md-table-cell>
        <md-table-cell><button @click="getLink(item.id)" class="btn btn-info">Change amenity</button></md-table-cell>
        <md-table-cell><button @click="deleteAmenity(item, item.id)" class="btn btn-danger">Delete</button></md-table-cell>
      </md-table-row>
    </md-table>
    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="showSnackbar1" md-persistent>
      <span>Amenity deleted.</span>
      <md-button class="md-primary" @click="showSnackbar1 = false">Retry</md-button>
    </md-snackbar>
    <md-snackbar :md-position="position" :md-duration="duration" :md-active.sync="showSnackbar2" md-persistent>
      <span>There are apartments that have this amenity, it's not possible to delete it.</span>
      <md-button class="md-primary" @click="showSnackbar2 = false">Retry</md-button>
    </md-snackbar>
  </div>
</template>

<script>

const toLower = text => {
  return text.toString().toLowerCase()
}

const searchByCaption = (items, term) => {
  if (term) {
    return items.filter(item => toLower(item.caption).includes(toLower(term)))
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
  name: "AmenitiesForAdmin",
  data() {
    return {
      search: null,
      searched: [],
      amenities: [],
      showSnackbar1: false,
      showSnackbar2: false,
      position: 'center',
      duration: 3000
    }
  },
  methods: {
    searchOnTable () {
      this.searched = searchByCaption(this.amenities, this.search)
    },
    getLink(id) {
      this.$router.push(`/changeAmenityDetails/${id}`);
    },
    deleteAmenity(amenity, id) {
      http
      .get(`Amenity/delete/${id}`)
      .then(response => {
        if(response.data) {
          this.showSnackbar1 = true;
          this.amenities.splice(amenity, 1);
        } else {
          this.showSnackbar2 = true;
        }
      })
    }
  },
  created () {
    this.searched = this.amenities
  },
  mounted() {
    http.get('Amenity')
        .then(response => {
          this.amenities = response.data;
          this.searched = this.amenities;
        })
  }
}
</script>

<style scoped>

</style>