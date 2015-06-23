package de.schmitzekater

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PostController)
@Mock([User, Post, LameSecurityFilters])
class PostControllerSpec extends Specification {

    def "Get a users timeline given in their id"(){
        given: "A user with posts in the db"
        User chuck = new User(loginId: "chuck_norris", password:"password")
        chuck.addToPosts(new Post(content: "A first one"))
        chuck.addToPosts(new Post(content: "Second one"))
        chuck.save(failOnError: true)

        and: "A loginId parameter"
        params.id = chuck.loginId

        when: "the timeline is invoked"
        def model = controller.timeline()

        then: "The user is the returned model"
        model.user.loginId == "chuck_norris"
        model.user.posts.size() == 2
    }

    def "Check that non.existent users are handled with an error"() {
        given: "The id of a non-existent user"
        params.id = "this-user-id-does-not-exists"

        when: "the timeline is invoked"
        controller.timeline()

        then: "a 404 is sent to the browser"
        response.status == 404
    }

    def "Adding a valid new post to the timeline"(){
        given: "a mock post service"
        def mockPostService = Mock(PostService)
        1 * mockPostService.createPost(String  , String)>> new Post(content: "Mock Post")
        controller.postService = mockPostService

        when: "controller is invoked"
        def result = controller.addPost("joe_cool", "Posting up a storm")

        then: "redirected to timeline, flash message tells us all is well"
        flash.message ==~ /Added new post: Mock.*/
        response.redirectedUrl == '/post/timeline/joe_cool'
    }

    def "Adding an invalid new post to the timeline"(){
        given: "A user with posts in the db"
        User chuck = new User(
                loginId: "chuck_norris",
                password: "password").save(failOnError: true)

        and: "A loginId parameter"
        params.id = chuck.loginId

        and: "Some invalid content for the post"
        params.content = ""

        when: "Add post is invoked"
        def model = controller.addPost()

        then: "our flash message and redirect sends an error"
        flash.message == "Invalid or empty post"
        response.redirectedUrl =="/post/timeline/${chuck.loginId}"
        Post.countByUser(chuck) == 0
    }

    @spock.lang.Unroll
    def "Testing id of #suppliedId redirects to #expectedUrl"(){
        given:
        params.id = suppliedId

        when: "Controller is invoked"
        controller.index()

        then:
        response.redirectedUrl == expectedUrl

        where:
        suppliedId  |   expectedUrl
        'joe_cool'  |   '/users/joe_cool'
        null        |   '/users/chuck_norris'
    }


    def "Exercising security filter for unauthenticated user"() {

        when:
        withFilters(action: "addPost") {
            controller.addPost("glen_a_smith", "A first post")
        }
        then:
        response.redirectedUrl == '/login/form'

    }
}
