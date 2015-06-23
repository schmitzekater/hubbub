package de.schmitzekater

class PostController {
    static scaffold = true

    def postService

    def index(){
        if(!params.id){
            params.id = "chuck_norris"
        }
        redirect(action: 'timeline', params: params)
    }
    def timeline(String id){
        def user = User.findByLoginId(id)
        if(!user) {
            response.sendError(404)
        }
        else {
            [ user : user ]
        }
    }

    def personal(){
     /**   if(!session.user){
            redirect(controller: 'login', action: 'form')
            return
        } else {
            render view: "timeline", model: [ user : session.user.refresh() ] **/
        render view: "timeline", model: [ user : user.refresh() ]
       // }

    }

    def addPost(String id, String content){
        try {
            def newPost = postService.createPost(id, content)
            flash.message = "Added new post: ${newPost.content}"
        } catch(PostException pe){
            flash.message = pe.message
        }
        redirect(action: 'timeline', id: id)
    }
}
