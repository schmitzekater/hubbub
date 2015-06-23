package de.schmitzekater

class LoginController {

    def form(String id) {
        [loginId : id]
    }

    def signIn(String id, String pwd) {
        def user = User.findByLoginId(id)
        if (user && pwd == user.password) {
            session.user = user
            redirect uri: '/'
        } else {
            flash.error = ("User or password unknown.")
            redirect action: "form"
        }

    }
}
