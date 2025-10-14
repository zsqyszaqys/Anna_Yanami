import $ from "jquery"

export default {
    state: {
        id: "",
        name: "",
        email: "",
        photo: "",
        token: "",
        is_login: false,
        error_message: "",
    },
    getters: {},
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.name = user.name;
            state.email = user.email;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },

        updateToken(state, token) {
            state.token = token;
        },

        setErrorMessage(state, msg) {
            if (state._errTimer) {
                clearTimeout(state._errTimer);
                state._errTimer = null;
            }

            // 直接设置错误消息，不进行相同判断
            state.error_message = msg;

            if (msg) {
                state._errTimer = setTimeout(() => {
                    state.error_message = "";
                    state._errTimer = null;
                }, 3000);
            }
        },

        logout(state) {
            state.id = "";
            state.name = "";
            state.email = "";
            state.photo = "";
            state.is_login = false;
            state.token = "";
        },

    },
    actions: {
        login(context, data) {
            context.commit("setErrorMessage", "");
            $.ajax({
                url: "http://localhost:3000/user/account/token",
                method: "POST",
                data: {
                    loginId: data.loginId,
                    password: data.password,
                },
                success(resp) {
                    if (resp.code == 200) {
                        localStorage.setItem("jwt_token", resp.data);
                        context.commit("updateToken", resp.data);
                        data.success(resp);
                    } else {
                        context.commit("setErrorMessage", "用户名或密码错误");
                        data.error(resp);
                    }
                },
                error() {
                    context.commit("setErrorMessage", "服务器连接失败");
                }
            })
        },

        getInfo(context, data) {
            $.ajax({
                url: "http://localhost:3000/user/account/info",
                method: "GET",
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    if (resp.code == 200) {
                        context.commit("updateUser", {
                            id: resp.id,
                            name: resp.name,
                            email: resp.email,
                            photo: resp.photo,
                            is_login: true,
                        });
                        data.success(resp);
                    } else {
                        context.commit("setErrorMessage", "获取用户信息失败");
                        data.error(resp);
                    }
                },
                error(resp) {
                    context.commit("setErrorMessage", "服务器连接失败");
                    data.error(resp);
                },
            })
        },

        // 在注册 action 中
        register(context, data) {
            context.commit("setErrorMessage", "");
            $.ajax({
                url: "http://localhost:3000/user/account/register",
                method: "POST",
                data: {
                    name: data.name,
                    email: data.loginId,
                    password: data.password,
                },
                success(resp) {
                    if(resp.code == 200){
                        data.success(resp);
                    } else {
                        const errorMsg = resp.data?.error_message || "注册失败";
                        context.commit("setErrorMessage", errorMsg);
                        data.error(resp);
                    }
                },
                error(resp) {
                    context.commit("setErrorMessage", "网络连接失败");
                    data.error(resp);
                }
            })
        },

        logout(context){
            localStorage.removeItem("jwt_token");
            context.commit("logout");
        }
    },
    modules: {}
}
