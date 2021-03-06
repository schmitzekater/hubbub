package de.schmitzekater

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
@Mock([User, Profile])
class UserControllerSpec extends Specification {
    def "Registering a user with known good Parameters"(){

        given: "A set of user parameters"
        params.with {
            loginId = "glen_a_smith"
            password = "winning"
            homepage = "http://blogs.bytecode.com.au/glen"
        }

        and: "a set of profile parameters"
        params['profile.fullName'] = "Glen Smith"
        params['profile.email'] = "glen@bytecode.au"
        params['profile.homepage'] = "http://blogs.bytecode.com.au/glen"

        when: "The user is registered"
        request.method ="POST"
        controller.register()

        then: "The user is created, and browser redirected"
        response.redirectedUrl == '/'
        User.count() ==1
        Profile.count() ==1
    }

    @Unroll
    def "Registration command object for #loginId validate correctly"(){

        given: "a mocked command object"
        def urc = mockCommandObject(UserRegistrationCommand)

        and: "a set of initial values from the spock test"
        urc.loginId = loginId
        urc.password = password
        urc.passwordRepeat = passwordRepeat
        urc.fullName = "Your Name here"
        urc.email = "someone@nowhere.net"

        when: "The validator is invoked"
        def isValidRegistration = urc.validate()

        then: "The appropriate fields are flagged as errors"
        isValidRegistration == anticipatedValid
        urc.errors.getFieldError(fieldInError)?.code == errorCode

        where:

        loginId     |   password    |   passwordRepeat  |   anticipatedValid    | fieldInError       |   errorCode
        "glen"      |   "password"  |   "no-match"      |   false               |   "passwordRepeat" |   "validator.invalid"
        "peter"     |   "password"  |   "password"      |   true                |     null          | null
        "a"         |   "password"  |   "password"      |   false               |   "loginId"       |   "size.toosmall"
    }

    def "Invoking the new register action via a command object"(){

        given: "A configured command object"
        def urc = mockCommandObject(UserRegistrationCommand)
        urc.with{
            loginId = "glen_a_smith"
            fullName = "Glen Smith"
            email = "glen@bytecode.com.au"
            password = "password"
            passwordRepeat = "password"
        }

        and: "which has been validated"
        urc.validate()

        when: "The register action is invoked"
        controller.register2(urc)

        then: "the user is registered and browser redirected"
        !urc.hasErrors()
        response.redirectedUrl == '/'
        User.count() == 1
        Profile.count() == 1
    }

}
