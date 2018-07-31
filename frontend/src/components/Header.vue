<template>
    <b-navbar toggleable="md" type="dark" variant="mad">
      <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>
      <b-navbar-brand to="/">{{ 'navbar.title' | translate }}</b-navbar-brand>
      <b-collapse is-nav id="nav_collapse">
          <b-navbar-nav class="ml-auto">
              <b-button size="sm" class="btn-danger" v-if="oidcIsAuthenticated" @click="signOutOidc">
                  {{ 'navbar.sign.out' | translate }}
              </b-button>
              <b-button size="sm" class="btn-success" v-else @click="authenticateOidc">
                  {{ 'navbar.sign.in' | translate }}
              </b-button>
          </b-navbar-nav>
      </b-collapse>
    </b-navbar>
</template>

<script>
import { mapActions, mapGetters } from "vuex";

export default {
    computed: {
        ...mapGetters(["oidcIsAuthenticated"])
    },
    methods: {
        ...mapActions(["signOutOidc"]),
        ...mapActions(["authenticateOidc"])
    }
};
</script>

<style scoped>
.bg-mad {
    background-color: #1ea9f0 !important;
}

.logo {
    height: 1em;
}
</style>
