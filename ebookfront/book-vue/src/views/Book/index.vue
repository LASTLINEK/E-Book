<template>
  <div>
    <label v-text="'ID: '+ ID"></label>
    <br>
    <label v-text="'Name: ' + Name"></label>
    <br>
    <label v-text="'Author: ' + Author"></label>
    <br>
    <label v-text="'Notes:  ' + Notes"></label>
  </div>
</template>

<script>


import service from '@/service/index'


export default {
  name: "index",
  data() {
    return {
      ID: '',
      Name: '',
      Author: '',
      Notes: ''
    }
  },
  methods: {
    created() {
    }
  },
  created() {
    let ii = this.$route.params.id;
    service.book(ii).then(res => {
      this.ID = res['id']
      this.Name = res['name']
      this.Author = res['author']
      this.Notes = res['note']
    }).catch(err => {

    });
  }
}
</script>

<style scoped>

</style>
