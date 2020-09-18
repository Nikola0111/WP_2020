<template>
<div style="overflow-y: auto;height: 600px;background-color: white !important;padding: 50px; padding-bottom: 10px;" class="container">
  <div v-if="changingDetails" class="row">
    <div class="col-sm-3">
      <div class="row">
        <label>Apartment type: </label>
      </div>
      <div class="row">
        <label style="margin-top: 5px">Number of rooms: </label>
      </div>
      <div class="row">
        <label style="margin-top: 5px">Number of guests: </label>
      </div>
      <div class="row">
        <label>Location: </label>
      </div>
    </div>
    <div class="col-sm-3">
      <div class="row">
        <input class="rb-male" style="alignment: left" type="radio" id="one" value="Apartment" v-model="newApartmentDetails.apartmentType">
        <label style="margin-left: 5px" class="rb-label" for="one">Apartment</label>
        <input style="margin-left: 8px" type="radio" id="two" value="Room" v-model="newApartmentDetails.apartmentType">
        <label style="margin-left: 5px" class="rb-label" for="two">Room</label>
      </div>
      <div class="row">
        <input type="number" min="1" v-model="newApartmentDetails.numberOfRooms"/>
      </div>
      <div class="row">
        <input style="margin-top: 5px" type="number" min="1" v-model="newApartmentDetails.numberOfGuests"/>
      </div>
      <div class="row">
        <input style="margin-top: 5px;" type="text" v-model="newApartmentDetails.location.address.country"/><br>
        <input style="margin-top: 5px;" type="text" v-model="newApartmentDetails.location.address.city"/><br>
        <input style="margin-top: 5px;" type="text" v-model="newApartmentDetails.location.address.street"/><br>
        <input style="margin-top: 5px;" type="text" v-model="newApartmentDetails.location.address.number"/><br>
      </div>
    </div>

    <div class="col-sm-2">
      <div class="row">
        <label>Price per night: </label>
      </div>
      <div class="row">
        <label style="margin-top: 5px;">Check in time: </label>
      </div>
      <div class="row">
        <label style="margin-top: 5px;">Check out time: </label>
      </div>
      <div class="row">
        <label style="margin-top: 5px;">Amenities: </label>
      </div>
    </div>
    <div class="col-sm-2">
      <div class="row">
        <input type="number" min="0" style="width: 150px" v-model="newApartmentDetails.pricePerNight">
      </div>
      <div class="row">
        <time-picker style="margin-top: 5px;" v-model="newApartmentDetails.checkInTime"></time-picker>
      </div>
      <div class="row">
        <time-picker style="margin-top: 5px;" v-model="newApartmentDetails.checkOutTime"></time-picker>
      </div>
      <div class="row">
        <md-field>
          <md-select multiple v-model="newAmenities">
            <md-option v-for="(amenity) in amenities" :key="amenity.caption" :value="amenity.id">{{amenity.caption}}</md-option>
          </md-select>
        </md-field>
      </div>
    </div>
    <div>
      <button v-if="changingDetails" @click="saveDetails(apartmentDetails)" style="width: 76px" class="btn btn-success">Save  </button><br><br>
      <button v-if="changingDetails" @click="changingDetails = false" class="btn btn-secondary">Cancel</button>
    </div>
  </div>


  <div v-if="!changingDetails" class="row" >
    <div class="col-sm-3">
        <div class="row">
          <label>Apartment type: </label>
        </div>
        <div class="row">
          <label>Number of rooms: </label>
        </div>
        <div class="row">
          <label>Number of guests: </label>
        </div>
        <div class="row">
          <label>Location: </label>
        </div>
        <div class="row">
          <label>Host username: </label>
        </div>
    </div>
    <div class="col-sm-3">
      <div class="row">
        <label>{{ apartmentDetails.apartmentType }}</label>
      </div>
      <div class="row">
        <label>{{ apartmentDetails.numberOfRooms }}</label>
      </div>
      <div class="row">
        <label>{{ apartmentDetails.numberOfGuests }}</label>
      </div>
      <div class="row">
        <label v-if="apartmentDetails.location">{{ apartmentDetails.location.address.country }}, {{apartmentDetails.location.address.city}}, {{apartmentDetails.location.address.street}} {{apartmentDetails.location.address.number}}</label>
      </div>
      <div class="row">
        <label>{{apartmentDetails.userName}}</label>
      </div>
    </div>


    <div class="col-sm-2">
      <div class="row">
        <label>Price per night: </label>
      </div>
      <div class="row">
        <label>Check in time: </label>
      </div>
      <div class="row">
        <label>Check out time: </label>
      </div>
      <div class="row">
        <label>Activity status: </label>
      </div>
      <div class="row">
        <label>Amenities: </label>
      </div>
    </div>
    <div class="col-sm-2">
      <div class="row">
        <label>{{apartmentDetails.pricePerNight}}</label>
      </div>
      <div class="row">
        <label>{{apartmentDetails.checkInTime}}</label>
      </div>
      <div class="row">
        <label>{{apartmentDetails.checkOutTime}}</label>
      </div>
      <div class="row">
        <label>{{apartmentDetails.activityStatus}}</label>
      </div>
      <div class="row">
        <label v-for="(amenity, index) in apartmentDetails.amenities" :key="index">{{amenity.caption}}; </label>
      </div>
    </div>
    <div>
      <button v-if="!changingDetails" @click="changeDetails" class="btn btn-info">Change details</button>
    </div>
  </div>

  <div v-if="!changingDetails" class="row">
    <div id="carouselExampleControls" class="carousel slide" data-ride="carousel" style="height: 50%; width: 50%;">
      <div class="carousel-inner">
        <div v-for="(photo, index) in apartmentDetails.photos" :key="index" :class="getClass(index)">
          <img class="d-block w-100" :src="photo" alt="Server error">
        </div>
      </div>
      <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
      </a>
      <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
      </a>
    </div>

    <div style="margin-left: 10px">
      <vc-calendar :available-dates="availableDates" :attributes="attributes"></vc-calendar>
    </div>
  </div>

  <hr>
  <h2>Comment section</h2>
  <hr>
  <div>
    <div style="margin-bottom: 20px" v-if="isGuest && eligibleForCommenting">
      <label>Rate this apartment below: </label>
      <star-rating :rating="rating"></star-rating>
      <br>
      <label>Enter a caption:</label>
      <input type="text" v-model="commentCaption"/>
      <textarea v-model="commentContent" style="font-size: 18px" rows="3" cols="110" :placeholder="'Write a comment, ' + guest.userName" ></textarea>
      <button @click="postComment" style="margin-top:10px;margin-left: 860px" class="btn btn-success">Post comment</button>
      <hr>
    </div>

    <div v-for="comment in comments" :key="comment.id">
      <div v-if="isGuest && comment.showed">
        <label style="font-size: 24px">Posted by: {{comment.guestUserName}}</label><br>
        <label style="font-size: 20px">Caption: {{comment.caption}}</label><br>
        <label style="font-size: 20px">Rated with: {{comment.rating}}</label><br>
        <textarea style="margin-top: 20px;font-size: 18px" v-model="comment.content" disabled cols="110" rows = 3></textarea><br>
        <hr>
      </div>
      <div v-if="isAdmin || isHost">
        <label style="font-size: 24px">Posted by: {{comment.guestUserName}}</label>
        <label style="margin-left: 500px" v-if="comment.showed"><b><i>Comment visible</i></b></label><br>
        <label style="margin-left: 500px" v-if="!comment.showed"><b><i>Comment hidden</i></b></label><br>
        <label style="font-size: 20px">Caption: {{comment.caption}}</label><br>
        <label style="font-size: 20px">Rated with: {{comment.rating}}</label><br>
        <textarea style="margin-top: 20px;font-size: 18px" v-model="comment.content" disabled cols="110" rows = 3></textarea><br>
        <hr>
      </div>
    </div>
  </div>

  <md-snackbar :md-position="'center'" :md-duration="3000" :md-active.sync="validationChange" md-persistent>
    <span>Changing of apartment details failed</span>
    <md-button class="md-primary" @click="validationChange = false">Close</md-button>
  </md-snackbar>
</div>
</template>

<script>
import Vue from "vue"
import http from '../../http-common'
import VCalendar from 'v-calendar';
import StarRating from 'vue-star-rating'
import VueTimepicker from 'vue2-timepicker'

Vue.use(VCalendar, {
  componentPrefix: 'vc'
})
Vue.component('star-rating', StarRating);
Vue.component('time-picker', VueTimepicker)


export default {
name: "ApartmentDetails",
  data() {
    return {
      apartmentId: '',
      amenities: [],
      apartmentDetails: '',
      availableDates: [],
      occupiedDates: [],
      attributes: [],
      comments: [],
      eligibleForCommenting: false,
      guest: {},
      rating: 3,
      commentCaption: '',
      commentContent: '',
      reservationId: '',
      minusOne: '-1',
      changingDetails: false,
      newApartmentDetails: {},
      validationChange: false,
      newAmenities: [],
      amenityObjects: [],

      isGuest: false,
      isAdmin: false,
      isHost: false
    }
  },
  methods: {
    getClass(key) {
      if (key === 0) {
        return "carousel-item active"
      } else {
        return "carousel-item"
      }
    },
    postComment() {
      let userId = this.guest.id
      console.log('This is guests id: ' + userId)
      let comment = {
        caption: this.commentCaption,
        content: this.commentContent,
        userId: userId,
        reservationId: this.reservationId,
        apartmentId: this.$route.params.id,
        rating: this.rating
      }

      http.post('comment/saveCommentAndGrade', JSON.stringify(comment)).then(response => {
        if(response.data){
          this.eligibleForCommenting = false;
        }
      })
    },
    changeDetails(){
      http.get('Amenity').then(response => {
        this.amenities = response.data
        this.newApartmentDetails = this.apartmentDetails
        this.changingDetails = true
      })
    },
    saveDetails(apartmentDetails){
      for(let i = 0; i < this.newAmenities.length; i++){
        this.amenityObjects.push(
            this.amenities.find(amenity => amenity.id === this.newAmenities[i])
        )
      }
      this.apartmentDetails.amenities = this.amenityObjects
      console.log(this.apartmentDetails.amenities)
      http.post('apartments/editDetails', JSON.stringify(apartmentDetails)).then(response => {
        if(response.data){
          this.apartmentDetails = this.newApartmentDetails
          this.changingDetails = false
        } else {
          this.validationChange = true
          this.changingDetails = false
        }
      })
    }
  },
  mounted() {
    this.isGuest = JSON.parse(localStorage.getItem("loggedUserRole")) === 'GUEST';
    this.isAdmin = JSON.parse(localStorage.getItem("loggedUserRole")) === 'ADMINISTRATOR';
    this.isHost = JSON.parse(localStorage.getItem("loggedUserRole")) === 'HOST';

    if(this.isGuest) {
      this.guest = JSON.parse(localStorage.getItem("loggedUser"))

      http.post('comment/checkRatingEligibility', {
        userId: this.guest.id,
        apartmentId: this.$route.params.id
      }).then(response => {
        console.log(response.data)
        if(response.data === -1){
          this.eligibleForCommenting = false
          console.log(this.eligibleForCommenting)
        } else {
          this.eligibleForCommenting = true
          this.reservationId = response.data

          console.log(this.eligibleForCommenting)
          console.log(this.reservationId)
        }
      })
    }

    this.apartmentId = this.$route.params.id;
    http.get(`apartments/apartmentDetails/${this.apartmentId}`)
    .then(response => {
      if (response.data) {
        this.apartmentDetails = response.data;

        for(let i = 0;i < this.apartmentDetails.startDates.length; i++){
          this.availableDates.push({start: this.apartmentDetails.startDates[i], end: this.apartmentDetails.endDates[i]})
        }

        this.apartmentDetails.reservations.forEach(reservation => {
          this.occupiedDates.push({start: reservation.startingDate, span: reservation.rentalDuration})
        })

        this.attributes.push({
          highlight: true,
          dates: this.occupiedDates
        })

        this.comments = this.apartmentDetails.comments
        console.log(this.comments)
      }
    })
  }
}
</script>

<style scoped>
.carousel-control, .left{
  z-index: 999;
}
</style>