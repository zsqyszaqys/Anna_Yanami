import $ from "jquery"

export default {
    state: {
        id:"",
        name:"",
        email:"",
        photo:"",
        token:"",
        is_login:false,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user){
            state.id = user.id;
            state.name = user.name;
            state.email = user.email;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },

        updateToken(state, token){
            state.token = token;
        }
    },
    actions: {
        login(context, data){
            $.ajax({
                url:"http://localhost:3000/user/account/token",
                method:"POST",
                data:{
                    email:data.email,
                    password: data.password,
                },
                success(resp){
                    console.log(resp.code);
                    if(resp.code == 200) {
                        context.commit("updateToken", resp.data);
                        data.success(resp);
                    }else{
                        data.error(resp);
                    }
                },
                error(resp){
                    data.error(resp);
                    console.log(resp);
                }
            })
        }
    },
    modules: {
    }
}