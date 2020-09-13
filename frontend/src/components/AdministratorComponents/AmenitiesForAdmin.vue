<template>
  <div style="width: 80%; margin-top: 5%; margin-left: 10%;">
    <md-table v-model="searched" md-sort="name" md-sort-order="asc" md-card md-fixed-header>
      <md-table-toolbar>
        <div class="md-toolbar-section-start">
          <h2 class="md-title">Amenities</h2>
        </div>

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
      </md-table-row>
    </md-table>
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
      amenities: []
    }
  },
  methods: {
    searchOnTable () {
      this.searched = searchByCaption(this.amenities, this.search)
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