import {createRouter, createWebHistory} from 'vue-router'
import UserAccountOperateView from "@/views/user/account/UserAccountOperateView.vue";
import navBar from "@/components/Me/NavBar.vue";
import store from "@/store";
import GroupLinksView from "@/components/tools/Groups/GroupLinksView.vue"
import omltwoD from "@/components/tools/live/OmltwoD.vue";
import NotFound from "@/views/ME/NotFound.vue";

const routes = [
    {
        path: '/user/account',
        name: 'AccountOperate',
        component: UserAccountOperateView,
        meta: {
            requestAuth: false,
        }
    },
    {
        path: "/",
        redirect: "/me",
        meta: {
            requestAuth: false,
        }
    },
    {
        path: "/me",
        name: "me",
        component: navBar,
        meta: {
            requestAuth: false,
        }
    },
    {
        path: "/me/groups/:groupId",
        name: "GroupLinks",
        component: GroupLinksView,
        meta: {
            requestAuth: false,
        },
    },

    {
        path: "/live",
        name: "live2d",
        component: omltwoD,
        meta: {
            requestAuth: false,
            hideGlobalWidget: true,
        },
    },

    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: NotFound,
        meta: {
            requestAuth: false,
        },
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})


export default router
