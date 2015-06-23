package de.schmitzekater

class Post {
    String content
    Date dateCreated

    static constraints = {
        content blank: false
        dateCreated nullable: true
    }

    static belongsTo = [user : User]
    static hasMany = [tags: Tag]
    static mapping = {
        dateCreated: "desc"
    }
}
