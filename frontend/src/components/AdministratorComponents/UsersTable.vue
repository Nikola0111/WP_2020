<template>
  <div style="background-color: white !important;width: 90%; margin-top: 5%; margin-left: 5%;">
    <div style="padding: 20px" class="row">
      <div class="form-group col-sm-4">
        <label for="exampleFormControlSelect1">User role</label>
        <select v-model="selectedUserRole" class="form-control" id="exampleFormControlSelect1">
          <option value="all">All</option>
          <option value="administrator">Administrator</option>
          <option value="host">Host</option>
          <option value="guest">Guest</option>
        </select>
      </div>
        <div class="form-group col-sm-4">
          <label for="exampleFormControlSelect2">User gender</label>
          <select v-model="selectedGender" class="form-control" id="exampleFormControlSelect2">
            <option value="all">All</option>
            <option value="male">Male</option>
            <option value="female">Female</option>
          </select>
        </div>
      <div class="form-group col-sm-4">
          <button @click="filterUsers()" style="margin-top: 30px" class="btn btn-info">Filter</button>
      </div>
    </div>
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
        <md-table-cell md-label="User role" md-sort-by="userRole">{{ item.userRole }}</md-table-cell>
        <md-table-cell md-label="Gender" md-sort-by="userGender">{{ item.userGender }}</md-table-cell>
      </md-table-row>
    </md-table>
  </div>
</template>

<script>

const toLower = text => {
  return text.toString().toLowerCase()
}

const searchByName = (items, term) => {
  if (term) {
    return items.filter(item => toLower(item.name).includes(toLower(term)))
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
  name: "UsersTable",
  data() {
    return {
      search: null,
      searched: [],
      users: [],
      selectedUserRole: 'all',
      selectedGender: 'all'
    }
  },
  methods: {
    searchOnTable() {
      this.searched = searchByName(this.users, this.search)
    },
    filterUsers() {
      if (this.selectedUserRole === 'all' && this.selectedGender === 'all') {
        http.get('User/getUsers')
            .then(response => {
              this.users = response.data;
              this.searched = this.users;
            })
      } else {
        http.post('User/FilterUsers', JSON.stringify({
          userRole: this.selectedUserRole,
          userGender: this.selectedGender
        }))
            .then(response => {
              this.users = response.data;
              this.searched = this.users;
            })
      }
    }
    },
    created() {
      this.searched = this.users
    },
    mounted() {
      http.get('User/getUsers')
          .then(response => {
            this.users = response.data;
            this.searched = this.users;
          })
    }
}
</script>

<style scoped>
.md-field {
  max-width: 300px;
}
</style>