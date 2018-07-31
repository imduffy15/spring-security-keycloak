<template>
  <div class="container">
    <ball-pulse-loader color="#343a40" size="20px"></ball-pulse-loader>
  </div>
</template>

<script>
import { mapActions } from "vuex";

import router from "@/router";

export default {
    name: "OidcCallback",
    methods: {
        ...mapActions(["oidcSignInCallback"])
    },
    mounted() {
        this.oidcSignInCallback()
            .then(redirectPath => {
                router.push(redirectPath);
            })
            .catch(err => {
                console.error(err);
            });
    }
};
</script>

<style scoped>
.container {
    margin: 30px auto;
    text-align: center;
}
</style>
