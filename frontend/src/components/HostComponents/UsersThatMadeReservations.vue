<template>
  <div style="width: 90%; margin-top: 5%; margin-left: 5%;">
    <md-table v-model="searched" md-sort="name" md-sort-order="asc" class="md-card md-fixed-header" style="padding:20px ">
      <md-table-toolbar>
        <div class="md-toolbar-section-start">
          <h2 class="md-title">Users</h2>
        </div>

        <md-field md-clearable class="md-toolbar-section-end">
          <md-input placeholder="Search by name..." v-model="search" @input="searchOnTable" />
        </md-field>
      </md-table-toolbar>

      <md-table-empty-state
          md-label="No users found"
          :md-description="`No user found for this '${search}' query. Try a different search term.`">
      </md-table-empty-state>

      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="Username" md-sort-by="userName">{{ item.userName }}</md-table-cell>
        <md-table-cell md-label="Name" md-sort-by="name">{{ item.name }} {{ item.surname }}</md-table-cell>
        <md-table-cell md-label="Gender" md-sort-by="userGender">{{ item.userGender }}</md-table-cell>
        <md-table-cell><button @click="showDetails(item.userName)" class="btn btn-primary">Account details</button></md-table-cell>
      </md-table-row>
    </md-table>
  </div>
</template>

<script>

const toLower = text => {
  return text.toString().toLowerCase()
}

const searchByUserName = (items, term) => {
  if (term) {
    return items.filter(item => toLower(item.userName).includes(toLower(term)))
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
  name: "UsersThatMadeReservations",
  data() {
    return {
      search: null,
      searched: [],
      users: [],
      user: null
    }
  },
  methods: {
  showDetails(userName){
    this.$router.push('/userProfile/' + userName);
  },
  searchOnTable () {
    this.searched = searchByUserName(this.users, this.search)
  }
  },
  created () {
    this.searched = this.users
  },
  mounted() {
    if (localStorage.getItem('loggedUser') !== null) {
      this.user = JSON.parse(localStorage.getItem('loggedUser'))
    }
    http.get(`Reservation/UsersMadeReservations/${this.user.id}`)
        .then(response => {
          this.users = response.data;
          this.searched = this.users;
        })
  }
}
</script>

<style scoped>

</style>