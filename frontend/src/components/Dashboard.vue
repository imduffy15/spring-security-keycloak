<template>
  <div class="container">
    <h1 class="mt-5">{{'jumbo.header.title' | translate}}</h1>
    <section>
      <h2>Your JWT:</h2>
      <pre>
{{oidcUser}}
      </pre>
    </section>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
    computed: {
        ...mapGetters(["oidcUser"])
    }
};
</script>

<style scoped>
</style>
