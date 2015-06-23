package de.schmitzekater

import grails.test.spock.IntegrationSpec

class PostIntegrationIntegrationSpec extends IntegrationSpec {

    def "Adding Posts to user links post to user"() {
        given: "A brand new user"
        def user = new User(loginId: 'Joe', password: 'secret')
        user.save(failOnError: true)

        when: "Several posts are added to the user"
        user.addToPosts(new Post(content: "First post .... w00t!!"))
        user.addToPosts(new Post(content: "Second post....."))
        user.addToPosts(new Post(content: "Third post!"))

        then: "The user has a list of posts attached"
        3== User.get(user.id).posts.size()
    }

    def "Ensure posts linked to a user can be retrieved"(){
        given: "A user with several posts"
        def user = new User(loginId: 'Joe', password: 'secret')
        user.addToPosts(new Post(content: "First"))
        user.addToPosts(new Post(content: "Second"))
        user.addToPosts(new Post(content: "Third"))
        user.save(failOnError: true)

        when: "The user is retrieved by their ID"
        def foundUser = User.get(user.id)
        def sortedPostContent = foundUser.posts.collect{it.content}.sort()

        then: "The posts appear on the retrieved user"
        sortedPostContent == ['First', 'Second', 'Third']
    }

    def "Exercise tagging several posts with various tags"(){
        given: "A user with a set of tags"
        def user = new User(loginId: 'Joe', password: 'secret')
        def tagGroovy = new Tag(name: 'Groovy')
        def tagGrails = new Tag(name: 'Grails')
        user.addToTags(tagGroovy)
        user.addToTags(tagGrails)
        user.save(failOnError: true)

        when: "The user tags two fresh posts"
        def groovyPost = new Post(content: 'A groovy Post')
        user.addToPosts(groovyPost)
        groovyPost.addToTags(tagGroovy)

        def bothPosts = new Post(content: "A groovy and grails post")
        user.addToPosts(bothPosts)
        bothPosts.addToTags(tagGroovy)
        bothPosts.addToTags(tagGrails)

        then:
        user.tags*.name.sort() == ['Grails', 'Groovy']
        1 == groovyPost.tags.size()
        2 == bothPosts.tags.size()
    }
    def cleanup() {
    }

    void "test something"() {
    }
}
