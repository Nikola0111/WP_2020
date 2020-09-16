<template>
  <div>
    <md-table v-model="comments" md-sort="id" md-sort-order="asc" class="md-card md-fixed-header" style="padding:20px ">
      <md-table-toolbar>
        <div v-if="showEnabledComments" class="md-toolbar-section-start">
          <h2 class="md-title">Enabled comments</h2>
        </div>

        <div v-if="showDisabledComments" class="md-toolbar-section-start">
          <h2 class="md-title">Disabled comments</h2>
        </div>

        <md-button @click="showDisabled" v-if="showEnabledComments">
          Show disabled
        </md-button>

        <md-button @click="showEnabled" v-if="showDisabledComments">
          Show enabled
        </md-button>
      </md-table-toolbar>

      <md-table-empty-state
          md-label="No comments found"
          :md-description="`No comments here.`">
      </md-table-empty-state>

      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="Guest username" md-sort-by="hostUserName">{{ item.guestUserName }}</md-table-cell>
        <md-table-cell md-label="Caption" md-sort-by="pricePerNight">{{ item.caption }}</md-table-cell>
        <md-table-cell md-label="Rating" md-sort-by="apartmentType">{{ item.rating }}</md-table-cell>
        <md-table-cell><button class="btn btn-info" @click="showDialog(item)">Details</button></md-table-cell>
        <md-table-cell v-if="item.showed === true && isHost"><button class="btn btn-warning" @click="hideComment(item)">Hide comment</button></md-table-cell>
        <md-table-cell v-if="item.showed === false && isHost"><button class="btn btn-warning" @click="showComment(item)">Show comment</button></md-table-cell>
      </md-table-row>
    </md-table>

    <modal :height="410" name="commentDetailed">
      <div style="margin: 20px">
        <h2>{{commentToShow.caption}}</h2>
        <div>
          <label>Guest username: <b>{{commentToShow.guestUserName}}</b></label>
        </div>
        <div>
          <label>Given rating: <b>{{commentToShow.rating}}</b></label>
        </div>
        <div>
          <label>Comment:</label>
          <textarea style="margin-left: 0; margin-right: 0" v-model="commentToShow.content" disabled cols="75" rows="10"></textarea>
        </div>
        <div style="margin-top: 10px;">
          <button style="margin-left: 485px" class="btn btn-info" @click="hideDialog">Close</button>
        </div>
      </div>
    </modal>
  </div>
</template>

<script>
import http from "@/http-common";
import VModel from 'vue-js-modal'
import Vue from "vue"
Vue.use(VModel)
export default {

name: "CommentList",
  data(){
    return {
      comments: [],
      fallbackComments: [],
      position: 'center',
      duration: 3000,
      showDisabledComments: true,
      showEnabledComments: false,
      commentToShow: {},

      isAdmin: false,
      isHost: false
    }
  },
  methods: {
    showDisabled(){
      this.showEnabledComments = false
      this.showDisabledComments = true
      this.comments = []
      this.fallbackComments.forEach(comment => {
        if(comment.showed === false) {
          this.comments.push(comment)
        }
      })
    },
    showEnabled(){
      this.showEnabledComments = true
      this.showDisabledComments = false
      this.comments = []
      this.fallbackComments.forEach(comment => {
        if(comment.showed === true) {
          this.comments.push(comment)
        }
      })
    },
    hideComment(comment){
      console.log(comment)
      http.get(`comment/disableComment/${comment.id}`).then(response => {
        if(response.data) {
          this.comments.splice(comment, 1)

          this.comments.forEach(item => {
            if(comment.id === item.id){
              item.showed = true
            }
          })
          this.fallbackComments.forEach(temp => {
            if(comment.id === temp.id){
              temp.showed = false
            }
          })

          comment.showed = false
        }
      })
    },
    showComment(comment){
      console.log(comment)
      http.get(`comment/enableComment/${comment.id}`).then(response => {
        if(response.data) {
          this.comments.splice(comment, 1)

          this.comments.forEach(item => {
            if(comment.id === item.id){
              item.showed = false
            }
          })
          this.fallbackComments.forEach(temp => {
            if(comment.id === temp.id){
              temp.showed = true
            }
          })

          comment.showed = true
        }
      })
    },
    showDialog(item){
      this.commentToShow = item
      this.$modal.show('commentDetailed')
    },
    hideDialog(){
      this.$modal.hide('commentDetailed')
    }
  },
  mounted() {
    let loggedUser = JSON.parse(localStorage.getItem("loggedUser"))

    this.isAdmin = JSON.parse(localStorage.getItem("loggedUserRole")) === 'ADMINISTRATOR';
    this.isHost = JSON.parse(localStorage.getItem("loggedUserRole")) === 'HOST';

    if(this.isHost){
      http.get(`comment/getHostsApartmentsComments/${loggedUser.id}`).then(response => {
        this.comments = response.data
        this.fallbackComments = response.data
        console.log(this.fallbackComments)
        this.showEnabled()
      })
    } else if(this.isAdmin){
      http.get('comment/getComments').then(response => {
        this.comments = response.data
        this.fallbackComments = response.data
        console.log(this.fallbackComments)
        this.showEnabled()
      })
    }


  }
}
</script>

<style scoped>

</style>