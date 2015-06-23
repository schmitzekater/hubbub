package de.schmitzekater

class User {

    String loginId
    String password
    Date dateCreated
    static hasOne = [profile : Profile]
    static hasMany = [posts : Post, tags: Tag, following: User]
    static mapping = {
        posts sort: 'dateCreated'
    }

    static constraints = {
        loginId size: 3..20, unique: true, nullable: false
        password size: 6..8, nullable: false , validator: {passwd, user -> passwd != user.loginId}
        profile nullable: true
        dateCreated nullable: true
    }
    String getDisplayString() {return loginId}
}
