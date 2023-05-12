import {createRouter, createWebHistory} from 'vue-router'
import Home from "@/views/Home";
import HomePage from "@/views/HomePage";
import NumberOfAnswers from "@/views/NumberOfAnswers";
import AcceptedAnswers from "@/views/AcceptedAnswers";
import Tags from "@/views/Tags";
import Users from "@/views/Users";

const routes = [
    {
        path: '/',
        name: 'home',
        component: Home,
        children: [
            {
                path: '/homepage',
                name: 'homepage',
                component: HomePage,
            },
            {
                path: '/numberOfAnswers',
                name: 'numberOfAnswers',
                component: NumberOfAnswers,
            }, {
                path: '/acceptedAnswers',
                name: 'acceptedAnswers',
                component: AcceptedAnswers,
            }, {
                path: '/tags',
                name: 'tags',
                component: Tags,
            }, {
                path: '/users',
                name: 'users',
                component: Users,
            },
        ]
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
