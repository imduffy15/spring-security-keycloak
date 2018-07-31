<template>
    <div>
        <app-header></app-header>
        <div>
          <transition name="fade" mode="out-in">
            <router-view></router-view>
          </transition>
        </div>
    </div>
</template>

<script>
import Header from "@/components/Header.vue";

export default {
    components: {
        appHeader: Header
    }
};
</script>
